package ru.erudyt.online.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.erudyt.online.entity.api.TokenPairEntity

interface TokenPairRepository : JpaRepository<TokenPairEntity, Long> {
    fun findAllByDeviceId(deviceId: String): List<TokenPairEntity>
    fun findByDeviceIdAndRefreshToken(deviceId: String, refreshToken: String): TokenPairEntity?
    fun findByAccessTokenAndIsActiveIsTrue(accessToken: String): TokenPairEntity?
}