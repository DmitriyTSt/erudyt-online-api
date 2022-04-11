package ru.erudyt.online.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "erudite.database")
class DialectSettings {
    var apiDialect: String = "org.hibernate.dialect.MySQL5Dialect"
    var resourceDialect: String = "org.hibernate.dialect.MySQL5Dialect"
}