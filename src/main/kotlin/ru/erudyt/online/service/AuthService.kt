package ru.erudyt.online.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.erudyt.online.dto.enums.ApiError
import ru.erudyt.online.dto.enums.Os
import ru.erudyt.online.dto.enums.getException
import ru.erudyt.online.dto.model.Device
import ru.erudyt.online.dto.model.Token
import ru.erudyt.online.dto.request.RegistrationRequest
import ru.erudyt.online.dto.response.AnonymTokenResponse
import ru.erudyt.online.dto.response.LoginResponse
import ru.erudyt.online.entity.api.AnonymousProfileEntity
import ru.erudyt.online.entity.resource.UserEntity
import ru.erudyt.online.repository.api.AnonymousProfileRepository
import java.security.MessageDigest
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID

private const val DEFAULT_LOGIN_COUNT = 3
private const val LOCK_PERIOD_SECONDS = 60

@Service
class AuthService @Autowired constructor(
    private val tokenService: TokenService,
    private val userService: UserService,
    private val anonymousProfileRepository: AnonymousProfileRepository,
) {
    fun createAnonym(device: Device): AnonymTokenResponse {
        val profile = anonymousProfileRepository
            .findByDeviceId(device.id)
            ?.let {
                updateAnonymousUser(it, device.os)
            }
            ?: createAnonymousUser(device)
        return AnonymTokenResponse(tokenService.createToken(profile.id, device.id, true, device.os))
    }

    @Transactional
    fun logout(): AnonymTokenResponse {
        val currentToken = tokenService.getCurrentTokenPair()
        if (currentToken.isAnonym) {
            throw ApiError.TOKEN_BELONGS_ANONYM.getException()
        }
        if (!currentToken.isActive) {
            throw ApiError.WRONG_TOKEN.getException()
        }
        val anonProfile = anonymousProfileRepository.findByDeviceId(currentToken.deviceId)
            ?: throw ApiError.PROFILE_NOT_EXISTS.getException()
        currentToken.isActive = false
        tokenService.save(currentToken)
        anonProfile.isActive = true
        anonymousProfileRepository.save(anonProfile)

        return AnonymTokenResponse(
            tokenService.createToken(
                userId = anonProfile.id,
                deviceId = anonProfile.deviceId,
                isAnonym = true,
                os = anonProfile.os
            )
        )
    }

    fun login(login: String, password: String): LoginResponse {
        val currentTokenPair = tokenService.getCurrentTokenPair()
        val anonymousProfile = anonymousProfileRepository.findByDeviceIdAndIsActiveIsTrue(currentTokenPair.deviceId)
            ?: throw ApiError.ANONYM_NOT_EXISTS.getException()
        val userEntity = userService.findByLogin(login)
            ?: throw ApiError.AUTH_ERROR.getException()
        val time = LocalDateTime.now().atOffset(ZoneOffset.UTC).toEpochSecond()

        if (userEntity.loginCount < 1) {
            userEntity.locked = time
            userEntity.loginCount = DEFAULT_LOGIN_COUNT
            userService.save(userEntity)
            // TODO log login count limit
            throw ApiError.AUTH_ERROR.getException()
        }

        checkAccess(userEntity)

        val userProfile = userService.findByLoginAndPassword(login, password)
        if (userProfile == null) {
            userEntity.loginCount--
            userService.save(userEntity)
            throw ApiError.AUTH_ERROR.getException()
        }
        userProfile.apply {
            lastLogin = currentLogin
            currentLogin = time
            loginCount = DEFAULT_LOGIN_COUNT
        }
        userService.save(userProfile)
        anonymousProfile.isActive = false
        anonymousProfileRepository.save(anonymousProfile)
        return LoginResponse(
            tokenService.createToken(
                userId = userProfile.id,
                deviceId = anonymousProfile.deviceId,
                isAnonym = false,
                os = anonymousProfile.os
            )
        )
    }

    private fun checkAccess(userEntity: UserEntity) {
        val time = LocalDateTime.now().atOffset(ZoneOffset.UTC).toEpochSecond()
        if (userEntity.locked + LOCK_PERIOD_SECONDS > time) {
            throw ApiError.AUTH_LOCKED_BY_LOGIN_COUNT.getException()
        }
        if (userEntity.disable == "1") {
            throw ApiError.AUTH_ERROR.getException()
        }
        if (userEntity.login != "1") {
            throw ApiError.AUTH_ERROR.getException()
        }
        val timeFloorToMinute = floorToMinute(time)
        userEntity.start.toLongOrNull()?.let { start ->
            if (start > timeFloorToMinute) {
                throw ApiError.AUTH_ERROR.getException()
            }
        }
        userEntity.stop.toLongOrNull()?.let { stop ->
            if (stop <= (timeFloorToMinute + 60)) {
                throw ApiError.AUTH_ERROR.getException()
            }
        }
    }

    // TODO refactor
    fun registration(request: RegistrationRequest): Token {
        val currentTokenPair = tokenService.getCurrentTokenPair()
        val anonymousProfile = anonymousProfileRepository.findByDeviceIdAndIsActiveIsTrue(currentTokenPair.deviceId)
            ?: throw ApiError.ANONYM_NOT_EXISTS.getException()
        val activation = UUID.randomUUID().toString().let { uniqId ->
            MessageDigest.getInstance("MD5").digest(uniqId.toByteArray()).toHexString().lowercase()
        }
        val userProfile = userService.create(
            UserEntity(
                username = request.email,
                password = request.password,
                firstName = request.name,
                lastName = request.surname,
                email = request.email,
                middleName = request.patronymic.orEmpty(),
                activation = activation,
                disable = "1",
                city = request.city,
                country = request.country,
                emailAgreement = if (request.emailAgreement) "1" else "",
                loginCount = 3,
            )
        )
        return tokenService.createToken(userProfile.id, anonymousProfile.deviceId, false, anonymousProfile.os)
    }

    fun refreshToken(deviceId: String, refreshToken: String): Token {
        return tokenService.refreshToken(deviceId, refreshToken)
    }

    fun getAnonymousProfile(id: Long): AnonymousProfileEntity {
        return anonymousProfileRepository.findByIdOrNull(id) ?: throw ApiError.NOT_FOUND.getException()
    }

    fun updateAnonymousUserLastEmail(id: Long, email: String): AnonymousProfileEntity {
        val user = getAnonymousProfile(id)
        user.lastEmail = email
        return anonymousProfileRepository.save(user)
    }

    private fun updateAnonymousUser(user: AnonymousProfileEntity, os: Os): AnonymousProfileEntity {
        user.os = os
        user.isActive = true
        return anonymousProfileRepository.save(user)
    }

    private fun createAnonymousUser(device: Device): AnonymousProfileEntity {
        val user = AnonymousProfileEntity(
            deviceId = device.id,
            os = device.os,
        )
        return anonymousProfileRepository.save(user)
    }

    private fun ByteArray.toHexString(): String {
        val sb = StringBuilder()
        for (b in this) sb.append(String.format("%02x", b.toInt() and 0xFF))
        return sb.toString()
    }

    private fun floorToMinute(time: Long): Long {
        return time - (time % 60)
    }
}