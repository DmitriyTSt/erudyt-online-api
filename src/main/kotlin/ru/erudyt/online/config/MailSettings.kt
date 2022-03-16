package ru.erudyt.online.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "erudyt.admin")
class MailSettings {
    var email: String = ""
    var name: String = ""
}