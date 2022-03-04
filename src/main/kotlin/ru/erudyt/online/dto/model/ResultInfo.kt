package ru.erudyt.online.dto.model

class ResultInfo(
    /** Место (Победитель (2 место)) */
    val placeText: String?,
    /** Средний балл по вем участникам в процентах */
    val averageScore: Int,
    /** Описание результата */
    val resultText: String,
)