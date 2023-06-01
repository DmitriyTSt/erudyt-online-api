package ru.erudyt.online.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "erudyt.config")
class AppConfigSettings(
    var directoryPath: String = "",
    var appUpdate: String = "appUpdate.json"
)