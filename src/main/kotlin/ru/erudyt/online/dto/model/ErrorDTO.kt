package ru.erudyt.online.dto.model

import ru.erudyt.online.exception.InternalApiException

class ErrorDTO(
    val code: String,
    val message: String,
    val tail: String? = null,
) {
    constructor(e: InternalApiException) : this(e.code, e.message, e.cause?.message)
}