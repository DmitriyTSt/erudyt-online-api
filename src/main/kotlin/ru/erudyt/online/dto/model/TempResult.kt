package ru.erudyt.online.dto.model

class TempResult(
    val isAnon: Boolean,
    val userId: Long,
    val code: String,
    val competitionTitle: String,
    val place: Int,
    val result: Int,
    val maxBall: Int,
    val ip: String,
    val cost: Int,
    val spentTime: Long,
    val questions: List<Question>,
) {
    class Question(
        val id: Int,
        val userAnswer: String,
        val correctAnswer: String,
    )
}