package ru.erudyt.online.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.erudyt.online.entity.test.QuestionEntity
import ru.erudyt.online.mapper.QuestionEntityTypeAdapter


@Configuration
class GsonConfig {
    @Bean
    fun gson(questionEntityTypeAdapter: QuestionEntityTypeAdapter): Gson {
        return GsonBuilder()
            .registerTypeAdapter(QuestionEntity::class.java, questionEntityTypeAdapter)
            .create()
    }
}