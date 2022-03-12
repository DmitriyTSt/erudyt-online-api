package ru.erudyt.online.mapper

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.erudyt.online.dto.model.CompetitionTest
import ru.erudyt.online.entity.test.AnswerEntity
import ru.erudyt.online.entity.test.QuestionEntity

@Component
class TestMapper @Autowired constructor(
    private val imageMapper: ImageMapper,
) {
    fun fromEntityToModel(questionEntity: QuestionEntity): CompetitionTest.Question {
        return when (questionEntity) {
            is QuestionEntity.ListAnswer -> CompetitionTest.Question.ListAnswer(
                id = questionEntity.id,
                text = questionEntity.text,
                image = imageMapper.fromPathToUrl(questionEntity.imagePath),
                answers = questionEntity.answers
                    .map { fromEntityToModel(it) }
                    .let { if (questionEntity.shuffle) it.shuffled() else it }
            )
            is QuestionEntity.SingleAnswer -> CompetitionTest.Question.SingleAnswer(
                id = questionEntity.id,
                text = questionEntity.text,
                image = imageMapper.fromPathToUrl(questionEntity.imagePath),
                label = questionEntity.answerLabel,
            )
        }
    }

    private fun fromEntityToModel(answerEntity: AnswerEntity): CompetitionTest.Answer {
        return CompetitionTest.Answer(
            id = answerEntity.id,
            text = answerEntity.text,
            image = imageMapper.fromPathToUrl(answerEntity.imagePath),
        )
    }
}