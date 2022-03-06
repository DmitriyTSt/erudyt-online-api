package ru.erudyt.online.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.erudyt.online.dto.enums.ApiError
import ru.erudyt.online.dto.enums.getException
import ru.erudyt.online.dto.model.CompetitionTest
import ru.erudyt.online.dto.model.ResultInfo
import ru.erudyt.online.dto.model.Score
import ru.erudyt.online.dto.model.TempResult
import ru.erudyt.online.dto.request.CheckTestRequest
import ru.erudyt.online.dto.response.CheckTestResponse
import ru.erudyt.online.entity.resource.CommonResultInfoEntity
import ru.erudyt.online.entity.test.QuestionEntity
import ru.erudyt.online.entity.test.TestEntity
import ru.erudyt.online.mapper.ResultMapper
import ru.erudyt.online.mapper.TestMapper
import ru.erudyt.online.repository.test.TestRepository
import javax.servlet.http.HttpServletRequest
import kotlin.math.round

@Service
class TestService @Autowired constructor(
    private val testRepository: TestRepository,
    private val testMapper: TestMapper,
    private val statisticService: StatisticService,
    private val resultMapper: ResultMapper,
    private val tempResultService: TempResultService,
    private val tokenService: TokenService,
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

    fun check(request: CheckTestRequest, httpRequest: HttpServletRequest): CheckTestResponse {
        if (request.questionResults.isEmpty()) {
            throw ApiError.NOT_ALL_ANSWERS.getException()
        }

        val ip = httpRequest.getHeader("HTTP_CLIENT_IP")?.takeIf { it.isNotEmpty() }
            ?: httpRequest.getHeader("HTTP_X_FORWARDED_FOR")?.takeIf { it.isNotEmpty() }
            ?: httpRequest.remoteAddr

        val tempResultQuestions = mutableListOf<TempResult.Question>()

        val test = testRepository.getTest(request.testId)
        var maxBall = 0
        var ball = 0
        var emptyAnswers = 0
        val questionById = mutableMapOf<Int, QuestionEntity>()
        request.questionResults.forEach { answer ->
            maxBall++
            test.questions.find { it.id == answer.questionId }?.let { question ->
                questionById[answer.questionId] = question
                val currentBall = checkAnswer(question, answer)
                if (currentBall != null) {
                    ball += currentBall
                    tempResultQuestions.add(
                        TempResult.Question(
                            id = question.id,
                            userAnswer = getAnswer(question, answer).orEmpty(),
                            correctAnswer = getCorrectAnswer(question).orEmpty()
                        )
                    )
                } else {
                    emptyAnswers++
                }
            } ?: run {
                emptyAnswers++
            }
        }

        // TODO add warning if maxMall != test.max

        if (emptyAnswers > 2 && test.type < 2) {
            throw ApiError.NOT_ALL_ANSWERS.getException()
        }

        val place = if (test.places.isNotEmpty()) {
            getPlace(test.places, ball)
        } else {
            null
        }

        val resultInfo = getResultInfo(test, ball, place)

        val currentToken = tokenService.getCurrentTokenPair()
        val tempResultEntity = tempResultService.save(
            TempResult(
                isAnon = currentToken.isAnonym,
                userId = currentToken.userId,
                code = request.testId,
                competitionTitle = test.name,
                place = place ?: 0,
                result = ball,
                maxBall = maxBall,
                ip = ip,
                cost = test.cost,
                spentTime = request.spentTime,
                questions = tempResultQuestions,
            )
        )

        return CheckTestResponse(
            id = tempResultEntity.id,
            answers = request.questionResults.mapIndexed { index, answerResult ->
                val question = questionById[answerResult.questionId]!!
                CheckTestResponse.Answer(
                    question = CheckTestResponse.Question(
                        title = "Вопрос № ${index + 1}",
                        text = question.text,
                    ),
                    answerText = getAnswer(question, answerResult).orEmpty()
                )
            },
            score = Score(ball, maxBall),
            spentTime = request.spentTime,
            resultInfo = resultInfo,
        )
    }

    private fun getAnswer(question: QuestionEntity, answer: CheckTestRequest.QuestionResult): String? {
        return if (answer.answerId != null && question is QuestionEntity.ListAnswer) {
            question.answers.find { it.id == answer.answerId }?.text
        } else if (answer.textAnswer != null && question is QuestionEntity.SingleAnswer) {
            answer.textAnswer
        } else {
            null
        }
    }

    private fun getCorrectAnswer(question: QuestionEntity): String? {
        return when (question) {
            is QuestionEntity.ListAnswer -> question.answers.find { it.id == question.correctAnswerId }?.text
            is QuestionEntity.SingleAnswer -> question.correctAnswer
        }
    }

    private fun checkAnswer(question: QuestionEntity, answer: CheckTestRequest.QuestionResult): Int? {
        if (answer.answerId != null && question is QuestionEntity.ListAnswer) {
            return if (question.correctAnswerId == answer.answerId) 1 else 0
        } else if (answer.textAnswer != null && question is QuestionEntity.SingleAnswer) {
            return if (isTextAnswerCorrect(answer.textAnswer, question.correctAnswer)) 1 else 0
        }
        return null
    }

    /**
     * @return null - для олимпиад (test.type == 2)
     */
    private fun getResultInfo(test: TestEntity, ball: Int, place: Int?): ResultInfo? {
        if (test.type > 1) {
            return null
        }
        val totalNullable = statisticService.getCommonResult(test.id)
        val total = object : CommonResultInfoEntity {
            override val sumResult: Int = totalNullable.sumResult ?: 0
            override val sumMax: Int = totalNullable.sumMax ?: 0
            override val cnt: Int = totalNullable.sumMax ?: 0

        }
        val difficultAll = if (total.sumMax > 0) round(total.sumResult * 100f / total.sumMax).toInt() else 100
        val totalStudents = total.cnt
        val totalWorst = statisticService.getCountByTestAndLessOrEqualResult(test.id, ball)
        val totalWorstPercent = if (totalStudents > 0) round(totalWorst * 100f / totalStudents).toInt() else 100
        val totalBetter = totalStudents - totalWorst
        val totalBetterPercent = if (totalStudents > 0) round(totalBetter * 100f / totalStudents).toInt() else 0


        val placeText = place?.let { resultMapper.getResultStatusToText(it) }?.second
        // TODO add test_time
        val totalWorstStr = if (totalWorst > 0) {
            "Ваш результат лучше, чем у $totalWorstPercent% участников."
        } else {
            ""
        }
        val messageResult = when {
            ball == test.max -> "Вы набрали максимально возможный балл в этом конкурсе, поздравляем! Не забудьте сохранить свой результат, чтобы он не пропал."
            totalStudents == 0 -> "Вы стали первым участником в этом конкурсе! Не забудьте сохранить свой результат, чтобы он не пропал, а потом попробуйте ответить на все вопросы без ошибок."
            place == 1 -> "Вы заняли 1 место в этом конкурсе, поздравляем! Ваш результат лучше, чем у $totalWorstPercent% участников. Не забудьте сохранить свой результат, чтобы он не пропал, а потом попробуйте ответить на все вопросы без ошибок."
            place == 2 || place == 3 -> "Вы стали победителем в этом конкурсе, поздравляем! $totalWorstStr Проанализируйте, где могли быть допущены ошибки, и попробуйте занять 1 место. Не забудьте сохранить свой результат, чтобы он не пропал, и Вы сможете его улучшить!"
            totalBetter == 0 -> "Ваш результат в этом конкурсе лучший на данный момент, но этого недостаточно, чтобы стать победителем. Сохраните свой результат, чтобы он не пропал, и попробуйте пройти конкурс ещё раз, чтобы набрать больший балл!"
            ball < test.max / 3f || totalWorst == 0 -> "Вы познакомились с заданиями конкурса. $totalBetterPercent% участников справились с этими заданиями лучше. Попробуйте пройти конкурс ещё раз и давать ответы более внимательно."
            totalBetterPercent < 20 -> "Вы показали хороший результат! Только $totalBetterPercent% участников справились с этим заданием лучше, чем Вы. Сохраните свой результат, чтобы потом его улучшить!"
            ball * 100f / test.max > difficultAll -> "Ваш результат лучше, чем средний балл, который набрали другие участники. Сохраните свой результат, чтобы потом его улучшить!"
            (place
                ?: 0) > 3 -> "Вы стали призёром в этом конкурсе, поздравляем! $totalWorstStr Проанализируйте, где могли быть допущены ошибки, и попробуйте занять 1 место. Не забудьте сохранить свой результат, чтобы он не пропал, и Вы сможете его улучшить!"
            else -> totalWorstStr
        }
        return ResultInfo(
            placeText = placeText,
            averageScore = difficultAll,
            resultText = messageResult,
        )
    }

    private fun isTextAnswerCorrect(textAnswer: String, correctAnswer: String): Boolean {
        return textAnswer.replace(" ", "").uppercase() ==
                correctAnswer.replace(" ", "").uppercase()
    }

    private fun getPlace(places: List<Int>, ball: Int): Int {
        var place = places.size
        while (place > 0 && places[place - 1] <= ball) {
            place--
        }
        place++
        if (place > places.size) {
            place = 0
        }
        return place
    }
}