package ru.erudyt.online.dto.model

class CommonResultRow(
    val date: Long,
    val username: String,
    val city: String,
    val countryIcon: String?,
    val competitionId: Long,
    val competitionTitle: String,
    val resultText: String,
)