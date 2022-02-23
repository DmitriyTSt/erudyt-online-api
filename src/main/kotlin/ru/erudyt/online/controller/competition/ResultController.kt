package ru.erudyt.online.controller.competition

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.erudyt.online.controller.base.ListResponse
import ru.erudyt.online.dto.model.CommonResultRow
import ru.erudyt.online.dto.model.UserResultRow
import ru.erudyt.online.service.CompetitionResultService

@RestController
@RequestMapping("/api/", produces = [MediaType.APPLICATION_JSON_VALUE])
class ResultController @Autowired constructor(
    private val resultService: CompetitionResultService,
) {

    @GetMapping("results")
    fun getCommonResults(
        @RequestParam(required = false) offset: Int?,
        @RequestParam(required = false) limit: Int?,
    ): ResponseEntity<ListResponse<CommonResultRow>> {
        return ResponseEntity.ok(resultService.getCommonResult(offset ?: 0, limit ?: 10))
    }

    @GetMapping("user/results")
    fun getUserResults(
        @RequestParam(required = false) email: String?,
        @RequestParam(required = false) query: String?,
        @RequestParam(required = false) offset: Int?,
        @RequestParam(required = false) limit: Int?,
    ): ResponseEntity<ListResponse<UserResultRow>> {
        return ResponseEntity.ok(resultService.getAnonOrUserResults(email, query, offset ?: 0, limit ?: 10))
    }
}