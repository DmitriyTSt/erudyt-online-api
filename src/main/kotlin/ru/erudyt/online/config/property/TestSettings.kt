package ru.erudyt.online.config.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "erudyt.tests")
class TestSettings(
    var directoryPath: String = ""
)