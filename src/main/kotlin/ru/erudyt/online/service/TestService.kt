package ru.erudyt.online.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.erudyt.online.dto.model.CompetitionTest
import ru.erudyt.online.entity.test.QuestionEntity
import ru.erudyt.online.entity.test.TestEntity
import ru.erudyt.online.mapper.TestMapper
import ru.erudyt.online.repository.test.TestRepository

@Service
class TestService @Autowired constructor(
    private val testRepository: TestRepository,
    private val testMapper: TestMapper,
) {
    fun getTestForPassing(code: String): CompetitionTest {
        val test = testRepository.getTest(code)

        return CompetitionTest(
            id = code,
            title = test.name,
            ageCategoryTitle = test.age,
            questions = getQuestions(test),
        )
    }

    private fun getQuestions(testEntity: TestEntity): List<CompetitionTest.Question> {
        val max = testEntity.max
        return if (testEntity.groups.isNotEmpty()) {
            testEntity.groups.map { group ->
                val questionsInGroup = testEntity.questions
                    .filter { group.startIdInclusive <= it.id && it.id <= group.endIdInclusive }
                getQuestionSublistShuffledIfNeed(questionsInGroup, group.count, testEntity.shuffle)
            }.flatten()
        } else {
            testEntity.questions
                .let { if (testEntity.shuffle) it.shuffled() else it }
                .subList(0, max)
                .map { testMapper.fromEntityToModel(it) }
        }
    }

    private fun getQuestionSublistShuffledIfNeed(
        questions: List<QuestionEntity>,
        max: Int,
        shuffle: Boolean
    ): List<CompetitionTest.Question> {
        return questions
            .let { if (shuffle) it.shuffled() else it }
            .subList(0, max)
            .map { testMapper.fromEntityToModel(it) }
    }

    fun getRawTest(code: String): TestEntity {
        return testRepository.getTest(code)
    }
}