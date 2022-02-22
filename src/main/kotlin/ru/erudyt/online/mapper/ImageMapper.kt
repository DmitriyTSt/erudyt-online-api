package ru.erudyt.online.mapper

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import ru.erudyt.online.config.ImageSettings

@Component
@EnableConfigurationProperties(ImageSettings::class)
class ImageMapper @Autowired constructor(
    private val imageSettings: ImageSettings,
) {

    fun fromPathToUrl(path: String?): String? {
        if (path == null) return null
        return "${imageSettings.baseUrl}$path"
    }
}