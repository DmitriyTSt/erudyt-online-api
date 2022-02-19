package ru.erudyt.online.dto.model

import ru.erudyt.online.entity.api.TokenPairEntity

class Token(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long,
) {
    companion object {
        fun fromEntity(tokenPairEntity: TokenPairEntity, expiresIn: Long): Token {
            return Token(
                accessToken = tokenPairEntity.accessToken,
                refreshToken = tokenPairEntity.refreshToken,
                expiresIn = expiresIn,
            )
        }
    }
}