package ru.erudyt.online.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.erudyt.online.config.DomainSettings
import ru.erudyt.online.controller.base.ListResponse
import ru.erudyt.online.dto.enums.ApiError
import ru.erudyt.online.dto.enums.getException
import ru.erudyt.online.dto.model.CommonResultRow
import ru.erudyt.online.dto.model.CreatedResult
import ru.erudyt.online.dto.model.OffsetBasedPageRequest
import ru.erudyt.online.dto.model.UserResultRow
import ru.erudyt.online.dto.request.SaveResultRequest
import ru.erudyt.online.dto.response.ResultResponse
import ru.erudyt.online.entity.api.TokenPairEntity
import ru.erudyt.online.entity.resource.ResultEntity
import ru.erudyt.online.mapper.ResultMapper
import ru.erudyt.online.repository.resource.ResultRepository
import java.util.Date

@Service
@EnableConfigurationProperties(DomainSettings::class)
class CompetitionResultService @Autowired constructor(
    private val userService: UserService,
    private val resultRepository: ResultRepository,
    private val resultMapper: ResultMapper,
    private val tokenService: TokenService,
    private val testService: TestService,
    private val competitionItemService: CompetitionItemService,
    private val tempResultService: TempResultService,
    private val domainSettings: DomainSettings,
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

    @Transactional
    fun saveResult(request: SaveResultRequest): CreatedResult {
        val tempResult = tempResultService.get(request.completeId)
        val userId = tempResult.userId.takeIf { !tempResult.isAnon }

        val time = Date().time / 1000

        val key = "${tempResult.code}_%06d".format(((time + 8191) * 131071) % 1000000)

        // TODO try use normal id generate strategy
        val lastId = resultRepository.getLastId()
        val result = ResultEntity(
            id = lastId + 1,
            date = time,
            paid = "",
            code = tempResult.code,
            place = tempResult.place,
            competitionTitle = tempResult.competitionTitle,
            name = fixFio("${request.surname} ${request.name} ${request.patronymic}"),
            email = fixEmail(request.email),
            school = fixSchoolOrPosition(request.school.orEmpty()),
            country = request.country,
            city = request.city,
            region = request.region.orEmpty(),
            position = fixSchoolOrPosition(request.position.orEmpty()),
            teacher = fixFio(request.teacher.orEmpty()),
            teacherSchool = "",
            teacherEmail = fixEmail(request.teacherEmail.orEmpty()),
            coord = "",
            result = tempResult.result,
            maxBall = tempResult.maxBall,
            answers = tempResult.answers,
            sequence = tempResult.sequence,
            correct = tempResult.correct,
            userId = userId ?: 0,
            summ = 0.0,
            lastModified = null,
            comment = null,
            pid = null,
            tstamp = null,
            key = key,
            dateReg = null,
            cost = tempResult.cost,
            time = tempResult.spentTime,
            ip = tempResult.ip,
            template = request.diplomaType,
            hide = "",
            groupId = 0,
            medalP = 0,
            medalT = 0,
            rating1 = request.review.quality ?: 0,
            rating2 = request.review.difficulty ?: 0,
            rating3 = request.review.interest ?: 0,
        )
        val resultEntity = resultRepository.save(result)
        tempResultService.delete(request.completeId)

        return CreatedResult(
            id = resultEntity.id,
            username = resultEntity.name,
            resultLink = "${domainSettings.baseUrl}/diplom.html?id=${resultEntity.id}",
            achievementText = ""
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

    private fun fixEmail(email: String): String {
        return email.replace(" ", "")
    }

    private fun fixSchoolOrPosition(schoolOrPosition: String): String {
        return schoolOrPosition.let { " $schoolOrPosition" }
            .replace(" \"", " «")
            .replace("\\\"", "»")
            .replace("\"", "»")
            .substring(1)
            .replace("  ", " ")
            .trim()
    }

    private fun fixFio(username: String): String {
        return username.replace(Regex("/[\\s]{2,}/"), " ").trim().let { caseConvert(it) }
    }

    private fun caseConvert(s: String): String {
        return s.mapIndexed { index, c ->
            if (index > 0) {
                if (" ., -\"–«»".contains(s[index - 1])) {
                    c.uppercase()
                } else {
                    c.lowercase()
                }
            } else {
                c.uppercase()
            }
        }.joinToString("")
    }
}