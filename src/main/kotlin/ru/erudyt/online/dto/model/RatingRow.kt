package ru.erudyt.online.dto.model

class RatingRow(
    val rank: Int,
    val username: String,
    val score: Int,
    val countryIcon: String?,
    val oldRank: Int? = null,
)