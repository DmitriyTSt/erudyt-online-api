package ru.erudyt.online.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.erudyt.online.dto.model.FilterItem
import ru.erudyt.online.entity.resource.FilterItemEntity
import ru.erudyt.online.mapper.FilterItemMapper
import ru.erudyt.online.repository.resource.FilterItemRepository

@Service
class FilterItemService @Autowired constructor(
    private val repository: FilterItemRepository,
    private val mapper: FilterItemMapper,
) {
    fun getAgeFilterItems(): List<FilterItem> {
        return repository.getFilterItemEntitiesByType(FilterItemEntity.TYPE_AGE)
            .map { mapper.fromEntityToModel(it) }
    }

    fun getSubjectFilterItems(): List<FilterItem> {
        return repository.getFilterItemEntitiesByType(FilterItemEntity.TYPE_SUBJECT)
            .map { mapper.fromEntityToModel(it) }
    }
}