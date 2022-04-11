package ru.erudyt.online.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "erudyt")
class EnvSettings {
    var env: String = DEV

    companion object {
        const val DEV = "dev"
        const val PROD = "prod"
    }
}