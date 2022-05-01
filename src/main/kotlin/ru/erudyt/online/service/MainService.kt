package ru.erudyt.online.service

import org.springframework.stereotype.Component
import ru.erudyt.online.dto.model.CompetitionViewType
import ru.erudyt.online.dto.model.MainSection
import ru.erudyt.online.dto.model.Tagline

@Component
class MainService(
    private val competitionItemService: CompetitionItemService,
    private val recommendationService: RecommendationService,
    private val taglineService: TaglineService,
) {
    fun getMain(): List<MainSection> {
        return listOfNotNull(
            taglineService.getTaglines().takeIf { it.isNotEmpty() }?.let { taglines ->
                MainSection.TaglineBlock(
                    taglines = taglines,
                )
            },
            MainSection.CompetitionItemsBlock(
                title = "Новые конкурсы",
                competitionViewType = CompetitionViewType.ROW,
                competitionItems = competitionItemService.getItems(null, emptyList(), emptyList(), 0, 5).list,
            ),
            recommendationService.getRecommendation().takeIf { it.isNotEmpty() }?.let { recommendations ->
                MainSection.CompetitionItemsBlock(
                    title = "Рекомендации",
                    competitionViewType = CompetitionViewType.CARD,
                    competitionItems = recommendations,
                )
            },
        )
    }
}