package ru.erudyt.online.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.erudyt.online.config.property.AppConfigSettings
import ru.erudyt.online.dto.model.Diploma
import ru.erudyt.online.mapper.ImageMapper

private const val DIPLOMA_TYPE_TEMPLATE = "type%d"

@Service
class DiplomaService @Autowired constructor(
    private val imageMapper: ImageMapper,
    private val appConfigSettings: AppConfigSettings,
) {
    fun getDiplomas(): List<Diploma> {
        return IntRange(1, appConfigSettings.diplomaCount).map {
            Diploma(
                type = DIPLOMA_TYPE_TEMPLATE.format(it),
                image = imageMapper.fromPathToUrl(appConfigSettings.diplomaPathTemplate.format(it)).orEmpty(),
            )
        }
    }
}