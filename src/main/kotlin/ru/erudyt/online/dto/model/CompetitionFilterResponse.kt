package ru.erudyt.online.dto.model

class CompetitionFilterResponse(
    val list: List<CompetitionItemShort>,
    val hasMore: Boolean,
    val filters: CompetitionFilter?,
)