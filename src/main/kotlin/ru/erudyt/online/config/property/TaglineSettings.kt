package ru.erudyt.online.config.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "erudyt.taglines")
class TaglineSettings(
    var directoryPath: String = "",
    var fileName: String = "taglines.json"
)