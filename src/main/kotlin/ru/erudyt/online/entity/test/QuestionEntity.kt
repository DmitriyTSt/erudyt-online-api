package ru.erudyt.online.entity.test

sealed class QuestionEntity(
    /** Идентификатор вопроса */
    val id: Int,
    /** Текст вопроса */
    val text: String,
    /** Тип вопроса по отображению */
    val viewType: QuestionViewTypeEntity,
    /** Тип вопроса по способу ответа */
    val answerType: QuestionAnswerTypeEntity,
    /** Относительный путь к изображению вопроса */
    val imagePath: String?,
) {
    class ListAnswer(
        /** Идентификатор вопроса */
        id: Int,
        /** Текст вопроса */
        text: String,
        /** Тип вопроса по отображению */
        viewType: QuestionViewTypeEntity,
        /** Относительный путь к изображению вопроса */
        imagePath: String?,
        /** Список ответов */
        val answers: List<AnswerEntity>,
        /** Идентификатор правильного овета */
        val correctAnswerId: String,
        /** Перемешивать ли ответы */
        val shuffle: Boolean,
    ) : QuestionEntity(id, text, viewType, QuestionAnswerTypeEntity.ANSWER_LIST, imagePath)

    class SingleAnswer(
        /** Идентификатор вопроса */
        id: Int,
        /** Текст вопроса */
        text: String,
        /** Тип вопроса по отображению */
        viewType: QuestionViewTypeEntity,
        /** Относительный путь к изображению вопроса */
        imagePath: String?,
        /** Подпись к полю ответа */
        val answerLabel: String,
        /** Правильный ответ */
        val correctAnswer: String,
    ) : QuestionEntity(id, text, viewType, QuestionAnswerTypeEntity.SINGLE_ANSWER, imagePath)
}