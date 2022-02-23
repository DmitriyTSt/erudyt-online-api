package ru.erudyt.online.service

import org.springframework.stereotype.Component
import ru.erudyt.online.dto.model.CompetitionViewType
import ru.erudyt.online.dto.model.MainSection
import ru.erudyt.online.dto.model.Tagline

@Component
class MainService(
    private val competitionItemService: CompetitionItemService,
    private val recommendationService: RecommendationService,
) {
    fun getMain(): List<MainSection> {
        return listOfNotNull(
            MainSection.TaglineBlock(
                taglines = getTaglines()
            ),
            MainSection.CompetitionItemsBlock(
                title = "Новые конкурсы",
                viewType = CompetitionViewType.ROW,
                items = competitionItemService.getItems(null, emptyList(), emptyList(), 0, 5).list,
            ),
            recommendationService.getRecommendation().takeIf { it.isNotEmpty() }?.let { recommendations ->
                MainSection.CompetitionItemsBlock(
                    title = "Рекомендации",
                    viewType = CompetitionViewType.CARD,
                    items = recommendations,
                )
            },
        )
    }

    private fun getTaglines(): List<Tagline> {
        return listOf(
            Tagline(
                title = "Мгновенный диплом!",
                text = "Результат и наградные материалы доступны сразу после прохождения конкурса",
                icon = "https://erudit-online.ru/files/design/slogan_green.png",
            ),
            Tagline(
                title = "Отличное портфолио!",
                text = "Диплом участнику грамота руководителю = 100р. Именные медали",
                icon = "https://erudit-online.ru/files/design/slogan_orange.png",
            )
        )
    }
}