package ru.erudyt.online.mapper

import org.springframework.stereotype.Component
import ru.erudyt.online.dto.model.TempResult
import ru.erudyt.online.entity.api.TempResultEntity

@Component
class TempResultMapper {
    fun fromModelToEntity(model: TempResult): TempResultEntity {
        return TempResultEntity(
            isAnon = model.isAnon,
            userId = model.userId,
            code = model.code,
            competitionTitle = model.competitionTitle,
            place = model.place,
            result = model.result,
            maxBall = model.maxBall,
            answers = model.questions
                .mapIndexed { index, question -> "${index + 1};${question.userAnswer}" }
                .joinToString(";"),
            sequence = model.questions.map { it.id }.joinToString(","),
            correct = model.questions
                .mapIndexed { index, question -> "${index + 1};${question.correctAnswer}" }
                .joinToString(";"),
        )
    }
}