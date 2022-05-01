package ru.erudyt.online.repository.fs

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Repository
import ru.erudyt.online.config.TaglineSettings
import ru.erudyt.online.dto.enums.ApiError
import ru.erudyt.online.dto.enums.getException
import ru.erudyt.online.entity.test.TaglineEntity
import java.io.File
import java.io.IOException

@Repository
@EnableConfigurationProperties(TaglineSettings::class)
class TaglineRepository @Autowired constructor(
    private val taglineSettings: TaglineSettings,
    private val gson: Gson,
) {
    fun getTaglines(): List<TaglineEntity> {
        val file = try {
            File(taglineSettings.directoryPath, taglineSettings.fileName)
        } catch (e: IOException) {
            throw ApiError.NOT_FOUND.getException()
        }
        if (!file.exists()) {
            throw ApiError.NOT_FOUND.getException()
        }
        val typeToken = object : TypeToken<List<TaglineEntity>>() {}.type
        return gson.fromJson(file.readText(), typeToken)
    }
}