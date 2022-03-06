package ru.erudyt.online.dto.request

class CheckTestRequest(
    val testId: String,
    val questionResults: List<QuestionResult>,
    val spentTime: Long,
) {
    class QuestionResult(
        val questionId: Int,
        val answerId: String?,
        val textAnswer: String?,
    )
}