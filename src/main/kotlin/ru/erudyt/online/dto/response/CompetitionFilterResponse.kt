package ru.erudyt.online.dto.response

import ru.erudyt.online.dto.model.CompetitionFilter
import ru.erudyt.online.dto.model.CompetitionItemShort

class CompetitionFilterResponse(
    val list: List<CompetitionItemShort>,
    val hasMore: Boolean,
    val filters: CompetitionFilter?,
)