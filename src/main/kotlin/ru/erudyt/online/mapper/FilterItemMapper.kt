package ru.erudyt.online.mapper

import org.springframework.stereotype.Component
import ru.erudyt.online.dto.model.FilterItem
import ru.erudyt.online.entity.resource.TaxonomyEntity

@Component
class FilterItemMapper {
    fun fromEntityToModel(entity: TaxonomyEntity): FilterItem {
        return FilterItem(
            id = entity.id,
            title = entity.title,
        )
    }
}