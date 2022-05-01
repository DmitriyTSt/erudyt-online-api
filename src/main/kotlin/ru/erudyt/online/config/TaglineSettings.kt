package ru.erudyt.online.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "erudyt.taglines")
class TaglineSettings(
    var directoryPath: String = "",
    var fileName: String = "taglines.json"
)