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
    AUTH_LOCKED_BY_LOGIN_COUNT("Эта учетная запись заблокирована! Вы сможете зайти через 1 мин"),
    AUTH_NOT_CONFIRMED("Эта учетная запись не подтверждена. Подтвердите учетную запись через email"),
    NOT_FOUND("Объект не найден"),
    SEARCH_EMPTY_EMAIL("Необходимо указать email для поиска"),
    NOT_ALL_ANSWERS("Чтобы принять участие в конкурсе, необходимо дать ответы на все вопросы"),
    TEST_NOT_SUPPORTED("В данный момент этот вид тестов не поддерживается"),
    INCORRECT_PERIOD("Некорректное значение периода"),
    PROFILE_NOT_EXISTS("Профиль не существует"),
    COUNTRY_NOT_FOUND("Страна не найдена"),
    WRONG_EMAIL("Некорректный email"),
    WRONG_PHONE("Некорректный телефон"),
    PASSWORD_INCORRECT_MIN("Минимальная длина пароля 8 символов"),
    ERROR_SEND_CONFIRM_MESSAGE("Ошибка отправки письма для подтверждения email адреса. Пожалуйста, напишите в поддержку."),
    NOT_AVAILABLE_BY_ENV("Недоступно в текущей среде"),
}

fun ApiError.getException() = InternalApiException(httpStatus, name, message)
fun ApiError.getException(e: Exception) = InternalApiException(httpStatus, name, message, e)