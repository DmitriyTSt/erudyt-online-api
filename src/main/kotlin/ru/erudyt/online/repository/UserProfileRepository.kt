package ru.erudyt.online.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.erudyt.online.entity.resource.UserEntity

interface UserProfileRepository : JpaRepository<UserEntity, Long> {
    fun findByLogin(login: String): UserEntity?
}