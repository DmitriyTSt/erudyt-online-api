package ru.erudyt.online

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ErudytOnlineApiApplication

fun main(args: Array<String>) {
    runApplication<ErudytOnlineApiApplication>(*args)
}
