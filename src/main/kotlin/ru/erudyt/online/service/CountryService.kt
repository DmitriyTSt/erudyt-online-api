package ru.erudyt.online.service

import java.util.Locale
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Service
import ru.erudyt.online.config.BackendAppSettings
import ru.erudyt.online.dto.model.Country

private const val COUNTRY_IMAGE_TEMPLATE = "files/flags/24/%s.png"

@Service
@EnableConfigurationProperties(BackendAppSettings::class)
class CountryService @Autowired constructor(
    private val appSettings: BackendAppSettings,
) {
    fun getCountries(): List<Country> {
        return appSettings.countries.split(",")
            .map { code ->
                val locale = Locale("", code.uppercase())
                Country(
                    code = code,
                    name = locale.getDisplayName(Locale("ru")),
                    image = "${appSettings.baseUrl}${COUNTRY_IMAGE_TEMPLATE.format(code.uppercase())}"
                )
            }
    }

    fun getCountry(code: String): Country? {
        return getCountries().find { it.code == code }
    }
}