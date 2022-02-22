package ru.erudyt.online.repository.resource

import org.springframework.data.jpa.repository.JpaRepository
import ru.erudyt.online.entity.resource.TaxonomyEntity

interface TaxonomyRepository : JpaRepository<TaxonomyEntity, Long> {
    fun getTaxonomyEntitiesByType(type: Int): List<TaxonomyEntity>
}