package ru.erudyt.online.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import ru.erudyt.online.dto.model.CompetitionItemShort
import ru.erudyt.online.dto.model.OffsetBasedPageRequest
import ru.erudyt.online.entity.resource.CompetitionItemEntity
import ru.erudyt.online.entity.resource.ResultEntity
import ru.erudyt.online.mapper.CompetitionItemMapper
import ru.erudyt.online.repository.resource.CustomCompetitionItemRepository
import kotlin.math.min

@Service
class RecommendationService @Autowired constructor(
    private val tokenService: TokenService,
    private val authService: AuthService,
    private val customItemRepository: CustomCompetitionItemRepository,
    private val resultService: CompetitionResultService,
    private val mapper: CompetitionItemMapper,
    private val fileService: FileService,
) {
    fun getRecommendation(): List<CompetitionItemShort> {
        val results = getAnonOrUserResults(15)
        if (results.isEmpty()) {
            return emptyList()
        }
        val codes = results.map { it.code }
        val competitionItems = customItemRepository.findAllByCodes(codes)
        val ages = competitionItems.asSequence().map { mapper.parseAgeGroups(it.ageGroupsBlob) }
            .flatten().toSet().toList()
            .map { it.toLong() }
        val subjects = competitionItems.asSequence().map { mapper.parseSubjects(it.subjectBlob) }
            .flatten().toSet().toList()
            .map { it.toLong() }
        val difficulty = getDifficulty(results, competitionItems)

        return customItemRepository.findAllByQueryAndAgesAndSubjects(
            searchQuery = null,
            ageIds = ages,
            subjectIds = subjects,
            difficulty = difficulty,
            notContainCodes = codes,
            pageable = getPagination(10),
        )
            .toList()
            .map { fromEntityToModelShort(it) }
    }

    private fun fromEntityToModelShort(entity: CompetitionItemEntity): CompetitionItemShort {
        return mapper.fromEntityToModelShort(
            entity = entity,
            imagePath = fileService.findImagePathByUuid(entity.icon)
        )
    }

    private fun getPagination(limit: Int): Pageable {
        return OffsetBasedPageRequest(0, limit, Sort.by(Sort.Direction.DESC, "id"))
    }

    private fun getAnonOrUserResults(limit: Int): List<ResultEntity> {
        val currentToken = tokenService.getCurrentTokenPair()
        return if (!currentToken.isAnonym) {
            resultService.getUserResultEntities(currentToken, "", 0, limit).toList()
        } else {
            val anonUser = authService.getAnonymousProfile(currentToken.userId)
            anonUser.lastEmail?.takeIf { it.isNotEmpty() }?.let { email ->
                resultService.searchResultEntities(email, 0, limit).toList()
            } ?: run {
                emptyList()
            }
        }
    }

    /**
     * За последние 5 результатов вычисляем какую сложность проходил человек и с какой процентной успешностью
     * Если ничего не проходил - null
     * Если проходил всегда одну сложность - она же
     * Если пробовал проходить более сложные тесты удачнее тем более легкие, то более сложная, иначе более легкая
     */
    private fun getDifficulty(results: List<ResultEntity>, competitionItems: List<CompetitionItemEntity>): Int? {
        val lastResults = results.subList(0, min(5, results.size))
        val difficultyToScoreSortedByDifficulty = lastResults.map { result ->
            competitionItems.find { it.charIdsBlob.contains(result.code) }?.stars to result.result / result.maxBall
        }.filter { it.first != null }.map { it.first!! to it.second }
        val scoreByDifficulty = difficultyToScoreSortedByDifficulty
            .groupBy { it.first }.map { it.key to it.value.map { it.second } }
            .map { it.first to it.second.let { it.sum().toFloat() / it.size } }
            .sortedByDescending { it.first }
        return if (scoreByDifficulty.isEmpty()) {
            null
        } else if (scoreByDifficulty.size == 1) {
            scoreByDifficulty.first().first
        } else {
            if (scoreByDifficulty.firstOrNull()?.second!! > scoreByDifficulty.getOrNull(1)?.second!!) {
                scoreByDifficulty.first().first
            } else {
                scoreByDifficulty[1].first
            }
        }
    }
}