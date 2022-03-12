package ru.erudyt.online.mapper

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.erudyt.online.entity.test.QuestionEntity
import java.lang.reflect.Type

private const val TYPE_KEY = "answerType"
private const val TYPE_LIST_ANSWER = "ANSWER_LIST"
private const val TYPE_SINGLE_ANSWER = "SINGLE_ANSWER"

@Component
class QuestionEntityTypeAdapter @Autowired constructor() : JsonDeserializer<QuestionEntity> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): QuestionEntity? {
        return when (json?.asJsonObject?.get(TYPE_KEY)?.asJsonPrimitive?.asString) {
            TYPE_LIST_ANSWER -> context?.deserialize(json, QuestionEntity.ListAnswer::class.java)
            TYPE_SINGLE_ANSWER -> context?.deserialize(json, QuestionEntity.SingleAnswer::class.java)
            else -> null
        }
    }

}