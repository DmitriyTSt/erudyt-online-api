package ru.erudyt.online.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import ru.erudyt.online.controller.base.ListResponse
import ru.erudyt.online.dto.model.CommonResultRow
import ru.erudyt.online.dto.model.OffsetBasedPageRequest
import ru.erudyt.online.dto.model.UserResultRow
import ru.erudyt.online.mapper.ResultMapper
import ru.erudyt.online.repository.resource.ResultRepository

@Service
class CompetitionResultService @Autowired constructor(
    private val userService: UserService,
    private val resultRepository: ResultRepository,
    private val resultMapper: ResultMapper,
) {
    fun getCommonResult(offset: Int, limit: Int): ListResponse<CommonResultRow> {
        val list = resultRepository.findAllByNameNotOrderByIdDesc(pageable = getPagination(offset, limit))
            .map { resultMapper.fromEntityToCommonModel(it) }
        return ListResponse(list, true)
    }

    fun searchResults(email: String, offset: Int, limit: Int): ListResponse<UserResultRow> {
        val list = resultRepository.findAllByEmail(email, getPagination(offset, limit))
            .map { resultMapper.fromEntityToUserModel(it) }
        return ListResponse(list, true)
    }

    fun getUserResults(query: String, offset: Int, limit: Int): ListResponse<UserResultRow> {
        val currentUser = userService.getCurrentUser()
        val list = resultRepository.findAllByEmailAndCompetitionTitleLikeOrUserIdAndCompetitionTitleLike(
            email = currentUser.email,
            query1 = query,
            userId = currentUser.id,
            query2 = query,
            pageable = getPagination(offset, limit),
        )
            .map { resultMapper.fromEntityToUserModel(it) }
        return ListResponse(list, true)
    }

    private fun getPagination(offset: Int, limit: Int): Pageable {
        return OffsetBasedPageRequest(offset, limit, Sort.by(Sort.Direction.DESC, "id"))
    }
}