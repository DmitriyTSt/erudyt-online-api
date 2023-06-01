package ru.erudyt.online.mapper

import org.springframework.stereotype.Component
import ru.erudyt.online.dto.enums.ApiError
import ru.erudyt.online.dto.enums.getException
import ru.erudyt.online.dto.model.AppVersion

@Component
class AppVersionMapper {

    /**
     * Маппинг полной версии приложения
     *
     * Пример: X.Y.Z-buildType(versionCode)
     */
    fun fromFullString(appVersionFull: String): AppVersion {
        val versionBlocks = appVersionFull.split(Regex("[\\-()]")).filter { it.isNotEmpty() }
        if (versionBlocks.size != 3) throwWrongVersion()

        val versionString = versionBlocks.first()

        return fromShortString(versionString).copy(
            buildType = versionBlocks[1],
            versionCode = versionBlocks[2].toIntOrNull() ?: throwWrongVersion(),
        )
    }

    /**
     * Маппинг сокращенной версии приложения
     *
     * Пример: X.Y.Z
     */
    fun fromShortString(versionString: String): AppVersion {
        val versionNumbers = versionString.split(".").map { it.toIntOrNull() ?: throwWrongVersion() }
        if (versionNumbers.size != 3) throwWrongVersion()
        val versionInt = versionNumbers.first() * 10000 + versionNumbers[1] * 100 + versionNumbers[2]

        return AppVersion(
            versionString = versionString,
            versionInt = versionInt,
            buildType = "",
            versionCode = -1,
        )
    }

    private fun throwWrongVersion(): Nothing {
        throw ApiError.INCORRECT_APP_VERSION.getException()
    }
}