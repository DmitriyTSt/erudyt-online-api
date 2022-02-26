package ru.erudyt.online.dto.model

class CompetitionTest(
    val id: String,
    val type: Type,
    val title: String,
    val subject: String,
    val ageCategoryTitle: String,
    val questions: List<String>,
) {
    enum class Type {
        LIST_ANSWER,
    }

    class Question(
        val id: String,
        val text: String,
        val answers: List<String>,
    )

    class Answer(
        val id: String,
        val text: String,
    )
}