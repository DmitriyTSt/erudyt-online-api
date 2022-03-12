package ru.erudyt.online.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.erudyt.online.dto.model.Diploma
import ru.erudyt.online.mapper.ImageMapper

private const val DIPLOMA_TYPE_TEMPLATE = "type%d"
private const val DIPLOMA_TEMPLATE = "files/templates/type%d.jpg"

@Service
class DiplomaService @Autowired constructor(
    private val imageMapper: ImageMapper,
) {
    fun getDiplomas(): List<Diploma> {
        return IntRange(1, 28).map {
            Diploma(
                type = DIPLOMA_TYPE_TEMPLATE.format(it),
                image = imageMapper.fromPathToUrl(DIPLOMA_TEMPLATE.format(it)).orEmpty(),
            )
        }
    }
}