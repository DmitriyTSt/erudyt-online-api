package ru.erudyt.online.entity.fs

class AppUpdateEntity(
    /** Версия, до которой ОБЯТАТЕЛЬНО обновиться */
    val minForceUpdateVersion: String?,
    /** Версия, до которой ЖЕЛАТЕЛЬНО обновиться */
    val minSoftUpdateVersion: String?,
)