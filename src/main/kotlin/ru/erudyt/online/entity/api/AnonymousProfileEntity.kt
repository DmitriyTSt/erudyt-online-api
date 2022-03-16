package ru.erudyt.online.entity.api

import ru.erudyt.online.dto.enums.Os
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "anonymous_users")
class AnonymousProfileEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,
    val deviceId: String,
    var os: Os,
    var isActive: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    /** Записывается email для нахождения результатов при сборке рекомендаций */
    var lastEmail: String? = null,
)