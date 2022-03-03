package ru.erudyt.online.dto.request

class CheckTestRequest(
    val testId: String,
    val questionResults: List<QuestionResult>,
    val spentTime: Int,
) {
    class QuestionResult(
        val questionId: Int,
        val answerId: String?,
        val textAnswer: String?,
    )
}