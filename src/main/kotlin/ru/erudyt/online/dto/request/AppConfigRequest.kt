package ru.erudyt.online.dto.request

class AppConfigRequest(
    /** Полная версия приложения вида X.Y.Z-buildType(versionCode) */
    val appVersion: String,
)