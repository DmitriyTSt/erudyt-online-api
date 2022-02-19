package ru.erudyt.online.service

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.erudyt.online.config.property.JwtSettings
import ru.erudyt.online.dto.enums.ApiError
import ru.erudyt.online.dto.enums.Os
import ru.erudyt.online.dto.enums.getException
import ru.erudyt.online.dto.model.Token
import ru.erudyt.online.entity.api.TokenPairEntity
import ru.erudyt.online.repository.TokenPairRepository
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Date

@Service
@EnableConfigurationProperties(JwtSettings::class)
class TokenService @Autowired constructor(
    private val repository: TokenPairRepository,
    private val jwtSettings: JwtSettings,
) {
    @Transactional
    fun createToken(userId: Long, deviceId: String, isAnonym: Boolean, os: Os): Token {
        repository.findAllByDeviceId(deviceId)
            .filter { it.isActive }
            .map { entity ->
                entity.isActive = false
                repository.save(entity)
            }
        val token = buildToken(userId, deviceId, isAnonym, os)
        repository.save(token)
        return Token.fromEntity(token, jwtSettings.accessTokenExpirationTime)
    }

    fun refreshToken(deviceId: String, refreshToken: String): Token {
        val tokenPair = repository.findByDeviceIdAndRefreshToken(deviceId, refreshToken)
            ?: throw ApiError.WRONG_TOKEN.getException()
        if (!tokenPair.isActive || tokenPair.isExpired()) {
            throw ApiError.WRONG_TOKEN.getException()
        }
        tokenPair.updatedAt = LocalDateTime.now()
        tokenPair.accessToken = createAccessToken(deviceId)
        tokenPair.refreshToken = createRefreshToken(deviceId)
        return Token.fromEntity(repository.save(tokenPair), jwtSettings.accessTokenExpirationTime)
    }

    fun getCurrentTokenPair(): TokenPairEntity {
        try {
            val token = SecurityContextHolder.getContext().authentication.principal as TokenPairEntity

            return repository.findByAccessTokenAndIsActiveIsTrue(token.accessToken)
                ?: throw ApiError.ACCESS_TOKEN_NOT_FOUND.getException()
        } catch (e: Exception) {
            throw ApiError.ACCESS_TOKEN_NOT_FOUND.getException(e)
        }
    }

    private fun buildToken(userId: Long, deviceId: String, isAnonym: Boolean, os: Os): TokenPairEntity {
        return TokenPairEntity(
            accessToken = createAccessToken(deviceId),
            refreshToken = createRefreshToken(deviceId),
            isAnonym = isAnonym,
            deviceId = deviceId,
            os = os,
            userId = userId,
        )
    }

    private fun createAccessToken(deviceId: String): String {
        val expireDate = LocalDateTime.now()
            .plusSeconds(jwtSettings.accessTokenExpirationTime)
            .toInstant(ZoneOffset.UTC)
            .let { Date.from(it) }
        return Jwts.builder()
            .setSubject(deviceId)
            .setExpiration(expireDate)
            .signWith(SignatureAlgorithm.HS512, jwtSettings.secret)
            .compact()
    }

    private fun createRefreshToken(deviceId: String): String {
        val expireDate = LocalDateTime.now()
            .plusDays(jwtSettings.refreshTokenExpirationTime)
            .toInstant(ZoneOffset.UTC)
            .let { Date.from(it) }
        return Jwts.builder()
            .setSubject(deviceId)
            .setExpiration(expireDate)
            .signWith(SignatureAlgorithm.HS512, jwtSettings.secret)
            .compact()
    }

    private fun getDeviceIdFromAccessToken(accessToken: String): String {
        return Jwts.parser().setSigningKey(jwtSettings.secret).parseClaimsJws(accessToken).body.subject
    }

    private fun TokenPairEntity.isExpired(): Boolean {
        return updatedAt.plusDays(jwtSettings.refreshTokenExpirationTime).isBefore(LocalDateTime.now())
    }
}