package ru.erudyt.online.dto.response

import ru.erudyt.online.dto.model.ResultInfo
import ru.erudyt.online.dto.model.Score

class CheckTestResponse(
    /** Временный идентификатор прохождения теста */
    val id: Long,
    /** Выбранные ответы */
    val answers: List<Answer>,
    /** Набранные баллы */
    val score: Score,
    /** Затраченное время, в секундах */
    val spentTime: Long,
    /** Данные результата (null если олимпиада) */
    val resultInfo: ResultInfo?,
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