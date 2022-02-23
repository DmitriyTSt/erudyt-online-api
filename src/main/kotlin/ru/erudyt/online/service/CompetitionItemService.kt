package ru.erudyt.online.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.erudyt.online.dto.enums.ApiError
import ru.erudyt.online.dto.enums.getException
import ru.erudyt.online.dto.model.AgeGroup
import ru.erudyt.online.dto.model.CompetitionFilter
import ru.erudyt.online.dto.model.CompetitionItem
import ru.erudyt.online.dto.model.CompetitionItemShort
import ru.erudyt.online.dto.model.OffsetBasedPageRequest
import ru.erudyt.online.dto.model.TestAgeGroup
import ru.erudyt.online.dto.response.CompetitionFilterResponse
import ru.erudyt.online.entity.resource.CompetitionItemEntity
import ru.erudyt.online.mapper.CompetitionItemMapper
import ru.erudyt.online.repository.resource.CompetitionItemRepository
import ru.erudyt.online.repository.resource.CustomCompetitionItemRepository

private const val SINGLE_TEST_AGE_GROUP_TITLE = "Принять участие в конкурсе"

@Service
class CompetitionItemService @Autowired constructor(
    private val filterItemService: FilterItemService,
    private val fileService: FileService,
    private val customItemRepository: CustomCompetitionItemRepository,
    private val itemRepository: CompetitionItemRepository,
    private val mapper: CompetitionItemMapper,
    ageGroupService: AgeGroupService,
) {
    private val allAgeGroups = mutableListOf<AgeGroup>()

    init {
        if (allAgeGroups.isEmpty()) {
            allAgeGroups.addAll(ageGroupService.getAgeGroups())
        }
    }

    fun getItems(
        query: String?,
        ageIds: List<Long>,
        subjectIds: List<Long>,
        offset: Int,
        limit: Int
    ): CompetitionFilterResponse {
        val page = customItemRepository.findAllByQueryAndAgesAndSubjects(
            query,
            ageIds,
            subjectIds,
            null,
            emptyList(),
            OffsetBasedPageRequest(offset, limit, Sort.by(Sort.Direction.DESC, "id")),
        )
        val filters = if (offset == 0) {
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
            list = page.toList().map { fromEntityToModelShort(it) },
            hasMore = offset + limit < page.totalElements,
            filters = filters
        )
    }

    fun getItem(id: Long): CompetitionItem {
        val entity = itemRepository.findByIdOrNull(id) ?: throw ApiError.NOT_FOUND.getException()
        return fromEntityToModel(entity)
    }

    fun getCodesMap(): Map<String, Long> {
        val result = mutableMapOf<String, Long>()
        itemRepository.findAll().map { it.id to mapper.parseTests(it.charIdsBlob) }.forEach { (id, rawTests) ->
            rawTests.forEach { rawTest ->
                result[rawTest.id] = id
            }
        }
        return result
    }

    private fun fromEntityToModelShort(entity: CompetitionItemEntity): CompetitionItemShort {
        return mapper.fromEntityToModelShort(
            entity = entity,
            imagePath = fileService.findImagePathByUuid(entity.icon)
        )
    }

    private fun fromEntityToModel(entity: CompetitionItemEntity): CompetitionItem {
        return mapper.fromEntityToModel(
            entity = entity,
            imagePath = fileService.findImagePathByUuid(entity.icon),
            testAgeGroups = buildTestsByAges(entity.charIdsBlob),
        )
    }

    private fun buildTestsByAges(charIdsBlob: String): List<TestAgeGroup> {
        return mapper.parseTests(charIdsBlob).let { tests ->
            if (tests.size == 1) {
                listOf(
                    TestAgeGroup(
                        id = tests.first().id,
                        title = SINGLE_TEST_AGE_GROUP_TITLE,
                    )
                )
            } else {
                tests.map { test ->
                    TestAgeGroup(
                        id = test.id,
                        title = formatAgeGroup(test.value).joinToString()
                    )
                }
            }
        }
    }

    private fun formatAgeGroup(ageGroups: List<Long>): List<String> {
        var toIndex = -1
        var fromIndex = -1

        val result = mutableListOf<String>()

        repeat(allAgeGroups.size) { i ->
            val curGroup = allAgeGroups[i]

            val chosenGroup = ageGroups.contains(curGroup.id)

            if (!chosenGroup || !curGroup.useGroup || i == allAgeGroups.lastIndex) {
                if (fromIndex > -1) {
                    val lastGroup = allAgeGroups[i - 1]
                    val format = if (toIndex > -1 && toIndex != fromIndex) {
                        "$fromIndex-$toIndex ${lastGroup.groupPostfix}"
                    } else {
                        lastGroup.title
                    }
                    result.add(format)
                }

                fromIndex = -1
                toIndex = -1
            }

            if (chosenGroup) {
                if (curGroup.useGroup) {
                    if (fromIndex == -1) {
                        fromIndex = curGroup.groupIndex
                    }
                    toIndex = curGroup.groupIndex
                } else {
                    result.add(curGroup.title)
                }
            }
        }
        return result
    }
}