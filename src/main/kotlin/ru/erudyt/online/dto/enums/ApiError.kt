package ru.erudyt.online.dto.enums

import org.springframework.http.HttpStatus
import ru.erudyt.online.exception.InternalApiException

enum class ApiError(
    val message: String,
    val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST,
) {
    UNKNOWN_ERROR("Unknown error", HttpStatus.INTERNAL_SERVER_ERROR),
    WRONG_TOKEN("Неправильный токен, необходимо повторно авторизоваться"),
    ACCESS_TOKEN_NOT_FOUND("Access token не найден"),
    TOKEN_BELONGS_ANONYM("Access token принадлежит анонимному пользователю"),
    ANONYM_NOT_EXISTS("Анонимный профиль не существует или не активный"),
    AUTH_ERROR("Неверная пара логин/пароль"),

}

fun ApiError.getException() = InternalApiException(httpStatus, name, message)
fun ApiError.getException(e: Exception) = InternalApiException(httpStatus, name, message, e)