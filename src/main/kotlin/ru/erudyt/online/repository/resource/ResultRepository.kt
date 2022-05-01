package ru.erudyt.online.repository.resource

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import ru.erudyt.online.entity.resource.CommonResultInfoEntity
import ru.erudyt.online.entity.resource.RatingEntity
import ru.erudyt.online.entity.resource.ResultEntity

interface ResultRepository : JpaRepository<ResultEntity, Long> {

    fun findAllByNameNotOrderByIdDesc(nameNot: String = "", pageable: Pageable): Page<ResultEntity>

    fun findAllByEmail(email: String, pageable: Pageable): Page<ResultEntity>

    fun findAllByEmailAndCompetitionTitleIgnoreCaseContainingOrUserIdAndCompetitionTitleIgnoreCaseContaining(
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

    fun countByCodeAndResultLessThanEqual(code: String, ball: Int): Int

    @Query(
        """
            SELECT email, name, ROUND(score) as score, country
                  FROM (
                           SELECT email, name, sum(score) as score, max(date) as date, min(IF(LENGTH(country) > 2, NULL, country)) as country
                           FROM (
                                    SELECT
                                        email,
                                        name,
                                        code,
                                        ((max(result) / max(max_ball)) * 100) as score,
                                        min(`date`) as `date`,
                                        min(IF(LENGTH(country) > 2, NULL, country)) as country
                                    FROM tl_comp_result
                                    WHERE ?1 <= `date` AND `date` < ?2 AND email rlike '[a-zA-Z0-9\\-_.]+@[a-zA-Z0-9\\-_.]+\\.[a-zA-Z0-9]+'
                                      AND code < 'C'
                                    GROUP BY email, code, name
                                ) as t
                           GROUP BY email, name
                           ORDER BY score DESC, `date` ASC, name
                       ) AS STBL LIMIT ?3
        """,
        nativeQuery = true,
    )
    fun getDayRating(startDay: Long, endDay: Long, limit: Int): List<RatingEntity>
}