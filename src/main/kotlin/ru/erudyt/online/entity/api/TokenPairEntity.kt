package ru.erudyt.online.entity.api

import ru.erudyt.online.dto.enums.Os
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "tokens")
class TokenPairEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,
    var accessToken: String,
    var refreshToken: String,
    var isActive: Boolean = true,
    val isAnonym: Boolean,
    val userId: Long,
    val deviceId: String,
    val os: Os,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),
)