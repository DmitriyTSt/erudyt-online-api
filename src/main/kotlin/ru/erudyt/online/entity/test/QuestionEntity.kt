package ru.erudyt.online.entity.test

class QuestionEntity(
    /** Идентификатор вопроса */
    val id: Int,
    /** Текст вопроса */
    val text: String,
    /** Тип вопроса */
    val type: QuestionTypeEntity,
    /** Относительный путь к изображению вопроса */
    val imagePath: String?,
    /** Список ответов */
    val answers: List<AnswerEntity>,
    /** Идентификатор правильного овета */
    val correctAnswerId: String,
    /** Вес вопроса ??? */
    val weight: Int,
)