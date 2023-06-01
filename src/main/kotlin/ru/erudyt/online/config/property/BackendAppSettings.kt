package ru.erudyt.online.config.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "erudyt")
class BackendAppSettings {
    var baseUrl: String = "localhost/"
    var env: String = ENV_DEV
    var countries: String = ""

    companion object {
        const val ENV_DEV = "dev"
        const val PROD = "prod"
    }
}