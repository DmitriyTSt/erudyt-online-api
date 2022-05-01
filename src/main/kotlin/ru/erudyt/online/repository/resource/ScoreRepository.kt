package ru.erudyt.online.repository.resource

import org.springframework.data.jpa.repository.JpaRepository
import ru.erudyt.online.entity.resource.CompetitionScoreEntity

interface ScoreRepository : JpaRepository<CompetitionScoreEntity, Long> {
    fun findAllByIdPeriod(period: Int): List<CompetitionScoreEntity>
}