package ru.erudyt.online.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "erudyt")
class CountrySettings {
    var countries: String = ""
}