package ru.erudyt.online.dto.model

sealed class MainSection {
    class TaglineBlock(
        val taglines: List<Tagline>,
        val type: MainSectionType = MainSectionType.TAGLINE,
    )

    class CompetitionItemsBlock(
        val title: String,
        val viewType: CompetitionViewType,
        val items: List<CompetitionItemShort>,
        val type: MainSectionType = MainSectionType.COMPETITION_ITEMS,
    )
}