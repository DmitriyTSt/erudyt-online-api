package ru.erudyt.online.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "erudyt.tests")
class TestSettings(
    var directoryPath: String = ""
)