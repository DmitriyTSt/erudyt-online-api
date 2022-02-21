package ru.erudyt.online.mapper

import org.springframework.stereotype.Component
import ru.erudyt.online.dto.model.FilterItem
import ru.erudyt.online.entity.resource.FilterItemEntity

@Component
class FilterItemMapper {
    fun fromEntityToModel(entity: FilterItemEntity): FilterItem {
        return FilterItem(
            id = entity.id,
            title = entity.title,
        )
    }
}