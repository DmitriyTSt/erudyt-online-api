package ru.erudyt.online.dto.model

/**
 * Версия приложения
 */
data class AppVersion(
    /**
     * Версия приложения в человеческом виде
     *
     * Пример: 1.2.3
     */
    val versionString: String,

    /**
     * Версия приложения, составленная из человеского вида (под каждое число два разряда)
     *
     * Пример: 10203
     */
    val versionInt: Int,

    /**
     * Версия сборки
     *
     * Пример: internal
     */
    val buildType: String,

    /**
     * Номер сборки
     *
     * Пример: 123
     */
    val versionCode: Int,
)