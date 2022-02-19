package ru.erudyt.online.exception

import org.springframework.http.HttpStatus

class InternalApiException(
    val httpStatus: HttpStatus,
    val code: String,
    override val message: String,
    override val cause: Throwable? = null,
) : RuntimeException(message, cause)