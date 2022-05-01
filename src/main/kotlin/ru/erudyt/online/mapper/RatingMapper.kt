package ru.erudyt.online.mapper

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.erudyt.online.dto.model.RatingRow
import ru.erudyt.online.entity.resource.CompetitionScoreEntity
import ru.erudyt.online.entity.resource.RatingEntity

@Component
class RatingMapper @Autowired constructor(
    private val imageMapper: ImageMapper,
) {
    fun fromEntityToModel(entity: RatingEntity, index: Int): RatingRow {
        return RatingRow(
            rank = index + 1,
            username = entity.name,
            score = entity.score,
            countryIcon = imageMapper.fromCountryToImage(entity.country),
        )
    }

    fun fromEntityToModel(entity: CompetitionScoreEntity): RatingRow {
        return RatingRow(
            rank = entity.place,
            username = entity.id.name,
            score = entity.score,
            countryIcon = imageMapper.fromCountryToImage(entity.country),
            oldRank = entity.oldPlace,
        )
    }
}