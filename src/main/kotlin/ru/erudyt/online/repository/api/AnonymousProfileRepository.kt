package ru.erudyt.online.repository.api

import org.springframework.data.jpa.repository.JpaRepository
import ru.erudyt.online.entity.api.AnonymousProfileEntity

interface AnonymousProfileRepository : JpaRepository<AnonymousProfileEntity, Long> {
    fun findByDeviceId(deviceId: String): AnonymousProfileEntity?
    fun findByDeviceIdAndId(deviceId: String, id: Long): AnonymousProfileEntity?
    fun findByDeviceIdAndIsActiveIsTrue(deviceId: String): AnonymousProfileEntity?
}