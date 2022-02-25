package ru.erudyt.online.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.erudyt.online.controller.base.ListResponse
import ru.erudyt.online.dto.enums.ApiError
import ru.erudyt.online.dto.enums.getException
import ru.erudyt.online.dto.model.CommonResultRow
import ru.erudyt.online.dto.model.OffsetBasedPageRequest
import ru.erudyt.online.dto.model.UserResultRow
import ru.erudyt.online.dto.response.ResultResponse
import ru.erudyt.online.entity.api.TokenPairEntity
import ru.erudyt.online.entity.resource.ResultEntity
import ru.erudyt.online.mapper.ResultMapper
import ru.erudyt.online.repository.resource.ResultRepository

@Service
class CompetitionResultService @Autowired constructor(
    private val userService: UserService,
    private val resultRepository: ResultRepository,
    private val resultMapper: ResultMapper,
    private val tokenService: TokenService,
    private val testService: TestService,
    private val competitionItemService: CompetitionItemService,
) {
    fun getCommonResult(offset: Int, limit: Int): ListResponse<CommonResultRow> {
        val codesMap = competitionItemService.getCodesMap()
        val page = resultRepository.findAllByNameNotOrderByIdDesc(pageable = getPagination(offset, limit))
            .map { resultMapper.fromEntityToCommonModel(it, codesMap[it.code]) }
        return ListResponse(page.toList(), page.totalElements > offset + limit)
    }

    fun getAnonOrUserResults(email: String?, query: String?, offset: Int, limit: Int): ListResponse<UserResultRow> {
        val currentToken = tokenService.getCurrentTokenPair()
        return if (currentToken.isAnonym) {
            val emailQuery = email?.takeIf { it.isNotEmpty() } ?: throw ApiError.SEARCH_EMPTY_EMAIL.getException()
            return searchResults(emailQuery, offset, limit)
        } else {
            getUserResults(currentToken, query.orEmpty(), offset, limit)
        }
    }

    fun getResult(id: Long): ResultResponse {
        val result = resultRepository.findByIdOrNull(id) ?: throw ApiError.NOT_FOUND.getException()
        val test = testService.getRawTest(result.code)
        return ResultResponse(
            resultMapper.fromEntityToDetailModel(result, test)
        )
    }

    /**
     * Получение результатов по email
     */
    private fun searchResults(email: String, offset: Int, limit: Int): ListResponse<UserResultRow> {
        val page = searchResultEntities(email, offset, limit)
        val list = page.toList().map { resultMapper.fromEntityToUserModel(it) }
        return ListResponse(list, page.totalElements > offset + limit)
    }

    /**
     * Получение результатов по email
     */
    fun searchResultEntities(email: String, offset: Int, limit: Int): Page<ResultEntity> {
        return resultRepository.findAllByEmail(email, getPagination(offset, limit))
    }

    /**
     * Получение результатов текущего пользователя
     */
    private fun getUserResults(
        currentToken: TokenPairEntity,
        query: String,
        offset: Int,
        limit: Int
    ): ListResponse<UserResultRow> {
        val page = getUserResultEntities(currentToken, query, offset, limit)
        val list = page.toList().map { resultMapper.fromEntityToUserModel(it) }
        return ListResponse(list, page.totalElements > offset + limit)
    }

    /**
     * Получение результатов текущего пользователя
     */
    fun getUserResultEntities(
        currentToken: TokenPairEntity,
        query: String,
        offset: Int,
        limit: Int
    ): Page<ResultEntity> {
        val currentUser = userService.getCurrentUser(currentToken)
        return resultRepository.findAllByEmailAndCompetitionTitleLikeOrUserIdAndCompetitionTitleLike(
            email = currentUser.email,
            query1 = query,
            userId = currentUser.id,
            query2 = query,
            pageable = getPagination(offset, limit),
        )
    }

    private fun getPagination(offset: Int, limit: Int): Pageable {
        return OffsetBasedPageRequest(offset, limit, Sort.by(Sort.Direction.DESC, "id"))
    }
}