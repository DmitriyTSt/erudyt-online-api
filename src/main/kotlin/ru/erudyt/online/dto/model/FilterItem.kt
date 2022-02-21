package ru.erudyt.online.dto.model

data class FilterItem(
    val id: Long,
    val title: String,
    val selected: Boolean = false,
)