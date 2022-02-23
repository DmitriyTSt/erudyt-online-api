package ru.erudyt.online.repository.resource

import org.springframework.data.jpa.repository.JpaRepository
import ru.erudyt.online.entity.resource.ResultEntity

interface ResultRepository : JpaRepository<ResultEntity, Long> {
    fun findAllByEmail(email: String): List<ResultEntity>
    fun findAllByEmailOrUserId(email: String, userId: Long): List<ResultEntity>
}