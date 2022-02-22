package ru.erudyt.online.mapper

import org.springframework.stereotype.Component
import ru.erudyt.online.dto.model.AgeGroup
import ru.erudyt.online.entity.resource.TaxonomyEntity

@Component
class AgeGroupMapper {

    fun fromEntityToModel(entity: TaxonomyEntity): AgeGroup {
        return AgeGroup(
            id = entity.id,
            title = entity.title,
            useGroup = entity.useGroup == "1",
            groupIndex = entity.groupIndex,
            groupPostfix = entity.groupPostfix,
        )
    }
}