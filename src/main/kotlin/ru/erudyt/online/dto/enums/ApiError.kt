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
    NOT_FOUND("Объект не найден"),
    SEARCH_EMPTY_EMAIL("Необходимо указать email для поиска"),
    NOT_ALL_ANSWERS("Чтобы принять участие в конкурсе, необходимо дать ответы на все вопросы"),
    TEST_NOT_SUPPORTED("В данный момент этот вид тестов не поддерживается"),
    INCORRECT_PERIOD("Некорректное значение периода"),
}

fun ApiError.getException() = InternalApiException(httpStatus, name, message)
fun ApiError.getException(e: Exception) = InternalApiException(httpStatus, name, message, e)