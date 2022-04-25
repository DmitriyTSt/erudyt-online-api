package ru.erudyt.online.dto.model

class User(
    val id: Long,
    val email: String,
    val name: String,
    val surname: String,
    val patronymic: String?,
    val school: String?,
    val country: Country?,
    val city: String,
    val region: String?,
    val avatar: String?,
)