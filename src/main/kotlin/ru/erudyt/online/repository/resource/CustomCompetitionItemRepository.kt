package ru.erudyt.online.repository.resource

import ru.erudyt.online.entity.resource.CompetitionItemEntity

interface CustomCompetitionItemRepository {
    fun findAllByQueryAndAgesAndSubjects(
        searchQuery: String?,
        ageIds: List<Long>,
        subjectIds: List<Long>,
        offset: Long,
        limit: Long
    ): Pair<List<CompetitionItemEntity>, Long>
}