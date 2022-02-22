package ru.erudyt.online.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "erudyt.image")
class ImageSettings(
    var baseUrl: String = "localhost"
)