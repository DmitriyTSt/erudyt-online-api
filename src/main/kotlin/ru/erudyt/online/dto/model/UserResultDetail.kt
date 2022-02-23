package ru.erudyt.online.dto.model

class UserResultDetail(
    val id: Long,
    val date: Long,
    val username: String,
    val testId: String,
    val competitionTitle: String,
    val place: String,
    val score: Score,
    val spentTime: Long,
    val answers: List<Answer>,
) {
    class Question(
        val title: String,
        val text: String,
    )

    class Answer(
        val question: Question,
        val answerText: String,
    )
}