package ru.erudyt.online.repository.resource

import org.springframework.data.jpa.repository.JpaRepository
import ru.erudyt.online.entity.resource.CompetitionItemEntity

interface CompetitionItemRepository : JpaRepository<CompetitionItemEntity, Long> {
}