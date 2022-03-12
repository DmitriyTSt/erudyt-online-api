package ru.erudyt.online.repository.resource

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import ru.erudyt.online.entity.resource.CommonResultInfoEntity
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

    @Query(
        "SELECT SUM(t.result) as sumResult, SUM(t.max_ball) as sumMax, count(*) as cnt FROM ${ResultEntity.TABLE_NAME} t WHERE t.code = ?1",
        nativeQuery = true,
    )
    fun getCommonResult(code: String): CommonResultInfoEntity

    @Query("select t.id from ${ResultEntity.TABLE_NAME} t order by t.id desc limit 1", nativeQuery = true)
    fun getLastId(): Long

    fun countByCodeAndResultLessThanEqual(code: String, ball: Int): Int
}