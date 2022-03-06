package ru.erudyt.online.controller.competition

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.erudyt.online.controller.base.BaseResponse
import ru.erudyt.online.controller.base.ListResponse
import ru.erudyt.online.dto.model.Diploma
import ru.erudyt.online.service.DiplomaService

@RestController
@RequestMapping("/api/", produces = [MediaType.APPLICATION_JSON_VALUE])
class DiplomaController @Autowired constructor(
    private val service: DiplomaService,
) {

    @GetMapping("diplomas")
    fun getDiplomas(): ResponseEntity<BaseResponse<ListResponse<Diploma>>> {
        return ResponseEntity.ok(BaseResponse(ListResponse(service.getDiplomas())))
    }
}