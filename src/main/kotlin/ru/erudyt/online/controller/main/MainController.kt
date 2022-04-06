package ru.erudyt.online.controller.main

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.erudyt.online.controller.base.BaseResponse
import ru.erudyt.online.controller.base.ListResponse
import ru.erudyt.online.dto.model.MainSection
import ru.erudyt.online.service.MainService

@RestController
@RequestMapping("/api/", produces = [MediaType.APPLICATION_JSON_VALUE])
class MainController @Autowired constructor(
    private val mainService: MainService,
) {
    @GetMapping("main")
    fun main(): ResponseEntity<BaseResponse<ListResponse<MainSection>>> {
        return ResponseEntity.ok(BaseResponse(ListResponse(mainService.getMain())))
    }
}