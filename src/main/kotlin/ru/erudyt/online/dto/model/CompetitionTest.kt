package ru.erudyt.online.dto.model

class CompetitionTest(
    val id: String,
    val title: String,
    val ageCategoryTitle: String,
    val questions: List<Question>,
) {

    enum class QuestionType {
        LIST_ANSWER,
        SINGLE_ANSWER,
    }

    sealed class Question(
        val id: Int,
        val text: String,
        val image: String?,
        val type: QuestionType,
    ) {
        class ListAnswer(
            id: Int,
            text: String,
            image: String?,
            val answers: List<Answer>,
        ) : Question(id, text, image, QuestionType.LIST_ANSWER)

        class SingleAnswer(
            id: Int,
            text: String,
            image: String?,
            val label: String,
        ) : Question(id, text, image, QuestionType.SINGLE_ANSWER)
    }

    class Answer(
        val id: String,
        val text: String,
        val image: String?,
    )
}