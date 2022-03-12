package ru.erudyt.online.mapper

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import ru.erudyt.online.config.DomainSettings

@Component
@EnableConfigurationProperties(DomainSettings::class)
class ImageMapper @Autowired constructor(
    private val domainSettings: DomainSettings,
) {

    fun fromPathToUrl(path: String?): String? {
        if (path == null) return null
        return "${domainSettings.baseUrl}$path"
    }

    fun fromCountryToImage(country: String?): String? {
        if (country == null) return null

        return fromPathToUrl("files/flags/16/${country.uppercase()}.png")
    }
}