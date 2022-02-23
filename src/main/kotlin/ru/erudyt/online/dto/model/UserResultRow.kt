package ru.erudyt.online.dto.model

class UserResultRow(
    val id: Long,
    val date: Long,
    val username: String,
    val testId: String,
    val competitionTitle: String,
    val place: String,
    val score: Score,
)