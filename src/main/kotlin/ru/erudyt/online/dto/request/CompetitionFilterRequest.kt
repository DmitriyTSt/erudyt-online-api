package ru.erudyt.online.dto.request

class CompetitionFilterRequest(
    val query: String?,
    val ageIds: List<Long>?,
    val subjectIds: List<Long>?,
    val offset: Long?,
    val limit: Long?,
)