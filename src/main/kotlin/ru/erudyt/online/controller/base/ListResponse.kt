package ru.erudyt.online.controller.base

class ListResponse<T>(
    val list: List<T>,
    val hasMore: Boolean? = null,
)