package ru.erudyt.online.exception

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.HandlerExceptionResolver
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import ru.erudyt.online.controller.base.ErrorResponse
import ru.erudyt.online.dto.model.ErrorDTO

@RestControllerAdvice
class GlobalExceptionHandler @Autowired constructor(
    private val handlerExceptionResolver: HandlerExceptionResolver
) : ResponseEntityExceptionHandler() {

    @ExceptionHandler(Exception::class)
    fun handleCommonException(e: Exception): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            ErrorResponse(ErrorDTO("INTERNAL_EXCEPTION", "Извините, что-то пошло не так", e.message))
        )
    }

    @ExceptionHandler(InternalApiException::class)
    fun apiInternalException(e: InternalApiException): ResponseEntity<ErrorResponse?>? {
        return ResponseEntity.status(e.httpStatus).body(
            ErrorResponse(ErrorDTO(e))
        )
    }

    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthenticationException(e: AuthenticationException?): ResponseEntity<ErrorResponse?>? {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            ErrorResponse(
                ErrorDTO(
                    "UNAUTHORIZED",
                    "Для доступа к этому ресурсу требуется полная аутентификация"
                )
            )
        )
    }

    override fun handleExceptionInternal(
        ex: Exception, body: Any?, headers: HttpHeaders, status: HttpStatus, request: WebRequest
    ): ResponseEntity<Any?> {
        ex.printStackTrace()
        return ResponseEntity.status(status).body(
            ErrorResponse(ErrorDTO("INTERNAL_EXCEPTION", "Извините, что-то пошло не так", ex.message))
        )
    }
}