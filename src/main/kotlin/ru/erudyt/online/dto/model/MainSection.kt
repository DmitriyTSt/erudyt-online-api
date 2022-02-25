package ru.erudyt.online.dto.model

sealed class MainSection(
    val type: MainSectionType
) {
    class TaglineBlock(
        val taglines: List<Tagline>,
    ) : MainSection(MainSectionType.TAGLINE)

    class CompetitionItemsBlock(
        val title: String,
        val viewType: CompetitionViewType,
        val items: List<CompetitionItemShort>,
    ) : MainSection(MainSectionType.COMPETITION_ITEM)
}