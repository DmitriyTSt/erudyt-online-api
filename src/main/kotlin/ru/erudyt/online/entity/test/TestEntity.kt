package ru.erudyt.online.entity.test

data class TestEntity(
    /** Идентификатор теста */
    val id: String,
    /** Количество вопросов */
    val max: Int,
    /** Количество возможных вопросов */
    val total: Int,
    /** Тип теста (1 или 2) */
    val type: Int,
    /** Название теста */
    val name: String,
    /** Возрастная категория */
    val age: String,
    /** Перемешивать ли вопросы */
    val shuffle: Boolean,
    /** Все вопросы на одной странице */
    val allInOne: Boolean,
    /** Показывать ответы */
    val showAnswer: Boolean,
    /** Тип диплома */
    val diploma: String,
    /** ??? */
    val diplomaS: String,
    /** ??? */
    val diplomaT: String,
    /** ??? */
    val diplomaC: String,
    /** Текст для диплома */
    val diplomaText: String,
    /** Стоимость наградных материалов */
    val cost: Int,
    /** Места */
    val places: List<Int>,
    /** Разбитие вопросов на группы */
    val groups: List<QuestionGroupEntity>,
    /** Список вопросов */
    val questions: List<QuestionEntity> = emptyList(),
)