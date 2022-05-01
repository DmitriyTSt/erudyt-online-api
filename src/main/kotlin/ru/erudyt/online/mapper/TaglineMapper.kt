package ru.erudyt.online.mapper

import org.springframework.stereotype.Component
import ru.erudyt.online.dto.model.Tagline
import ru.erudyt.online.entity.test.TaglineEntity

@Component
class TaglineMapper {
    fun fromEntityToModel(entity: TaglineEntity): Tagline {
        return Tagline(
            title = entity.title.orEmpty(),
            text = entity.text.orEmpty(),
            icon = entity.icon.orEmpty(),
            titleColor = entity.titleColor.orEmpty(),
            url = entity.url,
        )
    }
}