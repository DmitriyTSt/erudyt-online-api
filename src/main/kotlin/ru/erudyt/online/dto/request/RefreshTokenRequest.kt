package ru.erudyt.online.dto.request

class RefreshTokenRequest(
    val refreshToken: String,
    val deviceId: String,
)