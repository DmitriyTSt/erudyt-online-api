package ru.erudyt.online.dto.model

class CompetitionItem(
    val id: Long,
    val title: String,
    val subject: String,
    val ages: String,
    val icon: String?,
    val difficulty: Int,
    val tests: List<TestAgeGroup>,
    val annotation: String?,
    val description: String?,
    val infos: List<String>,
)