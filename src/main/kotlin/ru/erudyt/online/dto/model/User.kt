package ru.erudyt.online.dto.model

class User(
    val id: Long,
    val email: String,
    val name: String,
    val surname: String,
    val patronymic: String?,
    val avatar: String?,
)