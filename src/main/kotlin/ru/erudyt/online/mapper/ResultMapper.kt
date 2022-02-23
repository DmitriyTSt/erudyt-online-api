package ru.erudyt.online.mapper

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.erudyt.online.dto.model.CommonResultRow
import ru.erudyt.online.dto.model.ResultStatus
import ru.erudyt.online.dto.model.Score
import ru.erudyt.online.dto.model.UserResultRow
import ru.erudyt.online.entity.resource.ResultEntity

private const val WINNER_PLACE_FULL = "III"

@Component
class ResultMapper @Autowired constructor(
    private val imageMapper: ImageMapper,
) {
    fun fromEntityToCommonModel(entity: ResultEntity): CommonResultRow {
        val (_, resultText) = getResultStatusToText(entity.place)
        return CommonResultRow(
            date = entity.date,
            username = entity.name,
            city = entity.city,
            countryIcon = imageMapper.fromCountryToImage(entity.country),
            competitionId = 0,
            competitionTitle = entity.competitionTitle,
            resultText = resultText,
        )
    }

    fun fromEntityToUserModel(entity: ResultEntity): UserResultRow {
        val (status, _) = getResultStatusToText(entity.place)
        return UserResultRow(
            id = entity.id,
            date = entity.date,
            username = entity.name,
            testId = entity.code,
            competitionTitle = entity.competitionTitle,
            place = entity.place.takeIf { it != 0 }?.toString() ?: "-",
            score = buildScore(entity.result, entity.maxBall),
        )
    }

    private fun buildScore(current: Int, max: Int, status: ResultStatus? = null): Score {
        return Score(
            current = current,
            max = max,
            color = status?.let { getColorFromStatus(it) },
        )
    }

    private fun getColorFromStatus(status: ResultStatus): String {
        return when (status) {
            ResultStatus.WINNER_1 -> "#f8ddec"
            ResultStatus.WINNER_2 -> "#fbe1a9"
            ResultStatus.WINNER_3 -> "#a9f1fb"
            ResultStatus.PRIZE -> "#b7fba9"
            ResultStatus.PARTICIPANT -> "#dddef8"
        }
    }

    private fun getResultStatusToText(place: Int): Pair<ResultStatus, String> {
        return if (place > 0) {
            if (place < 4) {
                ResultStatus.valueOf("WINNER_${place}") to
                        "Победитель (${WINNER_PLACE_FULL.substring(0, place)} место)"
            } else {
                ResultStatus.PRIZE to "Призёр ($place место)"
            }
        } else {
            ResultStatus.PARTICIPANT to "Участник"
        }
    }
}