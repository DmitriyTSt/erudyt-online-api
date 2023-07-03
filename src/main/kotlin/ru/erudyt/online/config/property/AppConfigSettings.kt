package ru.erudyt.online.config.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "erudyt.config")
class AppConfigSettings(
    var directoryPath: String = "",
    var appUpdate: String = "appUpdate.json",
    var diplomaPathTemplate: String = "%d",
    var diplomaCount: Int = 28,
)