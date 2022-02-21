package ru.erudyt.online.dto.model

class CompetitionFilterResponse(
    val list: List<CompetitionItem>,
    val hasMore: Boolean,
    val filters: CompetitionFilter?,
)