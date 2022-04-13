package ru.erudyt.online.controller.competition

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.erudyt.online.controller.base.BaseResponse
import ru.erudyt.online.dto.model.CompetitionItem
import ru.erudyt.online.dto.request.CompetitionFilterRequest
import ru.erudyt.online.dto.response.CompetitionFilterResponse
import ru.erudyt.online.dto.response.CompetitionItemResponse
import ru.erudyt.online.service.CompetitionItemService

@RestController
@RequestMapping("/api/competition/", produces = [MediaType.APPLICATION_JSON_VALUE])
class CompetitionController @Autowired constructor(
    private val service: CompetitionItemService,
) {
    @PostMapping("items")
    fun getItems(
        @RequestBody request: CompetitionFilterRequest
    ): ResponseEntity<BaseResponse<CompetitionFilterResponse>> {
        return ResponseEntity.ok(
            BaseResponse(
                service.getItems(
                    request.query,
                    request.ageIds.orEmpty(),
                    request.subjectIds.orEmpty(),
                    request.offset ?: 0,
                    request.limit ?: 10,
                )
            )
        )
    }

    @GetMapping("item/{id}")
    fun getItem(@PathVariable("id") id: Long): ResponseEntity<BaseResponse<CompetitionItemResponse>> {
        return ResponseEntity.ok(BaseResponse(CompetitionItemResponse(service.getItem(id))))
    }
}