package ru.erudyt.online.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Service
import ru.erudyt.online.config.CountrySettings
import ru.erudyt.online.config.DomainSettings
import ru.erudyt.online.dto.model.Country
import java.util.Locale

private const val COUNTRY_IMAGE_TEMPLATE = "files/flags/24/%s.png"

@Service
@EnableConfigurationProperties(CountrySettings::class, DomainSettings::class)
class CountryService @Autowired constructor(
    private val countrySettings: CountrySettings,
    private val domainSettings: DomainSettings,
) {
    fun getCountries(): List<Country> {
        return countrySettings.countries.split(",")
            .map { code ->
                val locale = Locale("", code.uppercase())
                Country(
                    code = code,
                    name = locale.getDisplayName(Locale("ru")),
                    image = "${domainSettings.baseUrl}${COUNTRY_IMAGE_TEMPLATE.format(code.uppercase())}"
                )
            }
    }

    fun getCountry(code: String): Country? {
        return getCountries().find { it.code == code }
    }
}