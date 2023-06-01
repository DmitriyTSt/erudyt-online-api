package ru.erudyt.online.repository.fs

import com.google.gson.Gson
import java.io.File
import java.io.IOException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Repository
import ru.erudyt.online.config.AppConfigSettings
import ru.erudyt.online.entity.fs.AppUpdateEntity

@Repository
@EnableConfigurationProperties(AppConfigSettings::class)
class AppConfigRepository @Autowired constructor(
    private val appConfigSettings: AppConfigSettings,
    private val gson: Gson,
) {

    fun getAppUpdateConfig(): AppUpdateEntity? {
        val file = try {
            File(appConfigSettings.directoryPath, appConfigSettings.appUpdate)
        } catch (e: IOException) {
            return null
        }
        return try {
            gson.fromJson(file.readText(), AppUpdateEntity::class.java)
        } catch (e: Exception) {
            null
        }
    }
}