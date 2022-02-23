package ru.erudyt.online.repository.resource

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import ru.erudyt.online.entity.resource.CompetitionItemEntity

interface CustomCompetitionItemRepository {
    fun findAllByQueryAndAgesAndSubjects(
        searchQuery: String?,
        ageIds: List<Long>,
        subjectIds: List<Long>,
        pageable: Pageable,
    ): Page<CompetitionItemEntity>
}