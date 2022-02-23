package ru.erudyt.online.repository.resource

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import ru.erudyt.online.entity.resource.ResultEntity

interface ResultRepository : JpaRepository<ResultEntity, Long> {
    fun findAllByNameNotOrderByIdDesc(nameNot: String = "", pageable: Pageable): Page<ResultEntity>
    fun findAllByEmail(email: String, pageable: Pageable): Page<ResultEntity>
    fun findAllByEmailAndCompetitionTitleLikeOrUserIdAndCompetitionTitleLike(
        email: String,
        query1: String,
        userId: Long,
        query2: String,
        pageable: Pageable
    ): Page<ResultEntity>
}