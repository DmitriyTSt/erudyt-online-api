package ru.erudyt.online.config.jwt

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerExceptionResolver
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthenticationEntryPoint(
    @Qualifier("handlerExceptionResolver") private val resolver: HandlerExceptionResolver
) : AuthenticationEntryPoint {

    override fun commence(request: HttpServletRequest, response: HttpServletResponse, e: AuthenticationException) {
        resolver.resolveException(request, response, null, e)
    }
}