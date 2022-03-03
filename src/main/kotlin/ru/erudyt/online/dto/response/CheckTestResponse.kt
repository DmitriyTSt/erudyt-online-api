package ru.erudyt.online.dto.response

import ru.erudyt.online.dto.model.Score

class CheckTestResponse(
    /** Временный идентификатор прохождения теста */
    val id: String,
    /** Выбранные ответы */
    val answers: List<Answer>,
    /** Место (Победитель (2 место)) */
    val place: String,
    /** Набранные баллы */
    val score: Score,
    /** Затраченное время, в секундах */
    val spentTime: Int,
    /** Средний балл по вем участникам в процентах */
    val averageScore: Int,
    /** Описание результата */
    val resultText: String,
) {
    class Answer(
        val question: Question,
        val answerText: String,
    )

    class Question(
        val title: String,
        val text: String,
    )
}