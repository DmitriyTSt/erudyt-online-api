package ru.erudyt.online.repository.resource

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import ru.erudyt.online.entity.resource.ResultEntity
import ru.erudyt.online.entity.resource.UserEntity

interface UserProfileRepository : JpaRepository<UserEntity, Long> {
    fun findByUsername(login: String): UserEntity?

    @Query("select t.id from ${UserEntity.TABLE_NAME} t order by t.id desc limit 1", nativeQuery = true)
    fun getLastId(): Long
}