package ru.erudyt.online.config.jwt

import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import ru.erudyt.online.config.property.JwtSettings
import ru.erudyt.online.entity.api.TokenPairEntity
import ru.erudyt.online.repository.api.TokenPairRepository
import java.time.LocalDateTime
import java.util.Date
import java.util.concurrent.atomic.AtomicReference
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
@EnableConfigurationProperties(JwtSettings::class)
class JwtAuthorizationFilter @Autowired constructor(
	private val jwtSettings: JwtSettings,
	private val tokenPairRepository: TokenPairRepository,
) : OncePerRequestFilter() {

    companion object {
        const val AUTHORIZATION = "Authorization"
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authentication = getAuthentication(request)
        if (authentication != null) {
            SecurityContextHolder.getContext().authentication = authentication
        }
        filterChain.doFilter(request, response)
    }

    private fun getAuthentication(request: HttpServletRequest): UsernamePasswordAuthenticationToken? {
        val token = request.getHeader(AUTHORIZATION)
        val auth = AtomicReference<UsernamePasswordAuthenticationToken?>()
        if (token != null) {
            tokenPairRepository.findByAccessTokenAndIsActiveIsTrue(token)?.takeIf { tokenPair ->
                try {
                    getExpiration(token).after(Date()) || tokenPair.isNotExpired()
                } catch (e: Exception) {
                    tokenPair.isNotExpired()
                }
            }?.let { tokenPair ->
                auth.set(UsernamePasswordAuthenticationToken(tokenPair, null, arrayListOf()))
            }
        }
        return auth.get()
    }

    private fun getExpiration(token: String): Date {
        return Jwts.parser().setSigningKey(jwtSettings.secret).parseClaimsJws(token).body.expiration
    }

    private fun TokenPairEntity.isNotExpired(): Boolean {
        return updatedAt
            .plusSeconds(jwtSettings.accessTokenExpirationTime)
            .isAfter(LocalDateTime.now())
    }
}