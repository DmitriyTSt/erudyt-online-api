package ru.erudyt.online.repository.resource

import org.springframework.data.jpa.repository.JpaRepository
import ru.erudyt.online.entity.resource.FilterItemEntity

interface FilterItemRepository : JpaRepository<FilterItemEntity, Long> {
    fun getFilterItemEntitiesByType(type: Int): List<FilterItemEntity>
}