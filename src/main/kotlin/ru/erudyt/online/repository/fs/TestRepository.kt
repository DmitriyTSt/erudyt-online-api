package ru.erudyt.online.repository.fs

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.IOException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Repository
import ru.erudyt.online.config.property.TestSettings
import ru.erudyt.online.dto.enums.ApiError
import ru.erudyt.online.dto.enums.getException
import ru.erudyt.online.entity.test.TestEntity
import ru.erudyt.online.entity.test.TestNotSupportedEntity

@Repository
@EnableConfigurationProperties(TestSettings::class)
class TestRepository @Autowired constructor(
    private val testSettings: TestSettings,
    private val gson: Gson,
) {
    fun getTest(code: String): TestEntity {
        val file = try {
            File(testSettings.directoryPath, "$code.json")
        } catch (e: IOException) {
            throw ApiError.NOT_FOUND.getException()
        }
        if (!file.exists()) {
            throw ApiError.NOT_FOUND.getException()
        }
        val content = file.readText()
        val testNotSupported = gson.fromJson(content, TestNotSupportedEntity::class.java)
        if (testNotSupported.errorCode != null) {
            throw ApiError.TEST_NOT_SUPPORTED.getException()
        }
        return gson.fromJson(file.readText(), TestEntity::class.java)
    }

    /**
     * Получение списка неподдерживаемых конкурсов, которые были выявлены при конвертации
     */
    fun getUnsupportedIds(): List<String> {
        val file = File(testSettings.unsupportedDirPath, testSettings.unsupportedFileName)
        val typeToken = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(file.readText(), typeToken)
    }
}