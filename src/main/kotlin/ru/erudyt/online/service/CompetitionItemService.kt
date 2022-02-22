package ru.erudyt.online.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.erudyt.online.dto.enums.ApiError
import ru.erudyt.online.dto.enums.getException
import ru.erudyt.online.dto.model.CompetitionFilter
import ru.erudyt.online.dto.model.CompetitionFilterResponse
import ru.erudyt.online.dto.model.CompetitionItem
import ru.erudyt.online.dto.model.CompetitionItemShort
import ru.erudyt.online.entity.resource.CompetitionItemEntity
import ru.erudyt.online.mapper.CompetitionItemMapper
import ru.erudyt.online.repository.resource.CompetitionItemRepository
import ru.erudyt.online.repository.resource.CustomCompetitionItemRepository

@Service
class CompetitionItemService @Autowired constructor(
    private val filterItemService: FilterItemService,
    private val fileService: FileService,
    private val customItemRepository: CustomCompetitionItemRepository,
    private val itemRepository: CompetitionItemRepository,
    private val mapper: CompetitionItemMapper,
) {

    fun getItems(
        query: String?,
        ageIds: List<Long>,
        subjectIds: List<Long>,
        offset: Long,
        limit: Long
    ): CompetitionFilterResponse {
        val (items, total) = customItemRepository.findAllByQueryAndAgesAndSubjects(
            query,
            ageIds,
            subjectIds,
            offset,
            limit,
        )
        val filters = if (offset == 0L) {
            CompetitionFilter(
                ages = filterItemService.getAgeFilterItems()
                    .map { it.copy(selected = ageIds.contains(it.id)) },
                subjects = filterItemService.getSubjectFilterItems()
                    .map { it.copy(selected = subjectIds.contains(it.id)) }
            )
        } else {
            null
        }
        return CompetitionFilterResponse(
            list = items.map { fromEntityToModelShort(it) },
            hasMore = offset + limit < total,
            filters = filters
        )
    }

    fun getItem(id: Long): CompetitionItem {
        return itemRepository.findByIdOrNull(id)
            ?.let { fromEntityToModel(it) }
            ?: throw ApiError.NOT_FOUND.getException()
    }

    private fun fromEntityToModelShort(entity: CompetitionItemEntity): CompetitionItemShort {
        return mapper.fromEntityToModelShort(
            entity = entity,
            imagePath = fileService.findImagePathByUuid(entity.icon)
        )
    }

    private fun fromEntityToModel(entity: CompetitionItemEntity): CompetitionItem {
        return mapper.fromEntityToModel(
            model = entity,
            imagePath = fileService.findImagePathByUuid(entity.icon)
        )
    }
}