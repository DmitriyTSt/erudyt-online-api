package ru.erudyt.online.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.erudyt.online.dto.model.AgeGroup
import ru.erudyt.online.entity.resource.TaxonomyEntity
import ru.erudyt.online.mapper.AgeGroupMapper
import ru.erudyt.online.repository.resource.TaxonomyRepository

@Service
class AgeGroupService @Autowired constructor(
    private val repository: TaxonomyRepository,
    private val mapper: AgeGroupMapper,
) {
    fun getAgeGroups(): List<AgeGroup> {
        return repository.getTaxonomyEntitiesByType(TaxonomyEntity.TYPE_AGE)
            .map { mapper.fromEntityToModel(it) }
    }
}