package ru.erudyt.online.dto.enums

import com.fasterxml.jackson.annotation.JsonValue

enum class Os(@JsonValue val value: String) {
    IOS("ios"),
    ANDROID("android"),
}