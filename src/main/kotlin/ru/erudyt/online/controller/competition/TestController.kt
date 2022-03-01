package ru.erudyt.online.controller.competition

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.erudyt.online.controller.base.BaseResponse
import ru.erudyt.online.dto.response.CompetitionTestResponse
import ru.erudyt.online.entity.test.TestEntity
import ru.erudyt.online.service.TestService

@RestController
@RequestMapping("/api/competition/", produces = [MediaType.APPLICATION_JSON_VALUE])
class TestController @Autowired constructor(
    private val testService: TestService,
) {

    // TODO remove method
    @GetMapping("rawTestEntity/{code}")
    fun getRawTest(@PathVariable("code") code: String): ResponseEntity<BaseResponse<TestEntity>> {
        return ResponseEntity.ok(BaseResponse(testService.getRawTest(code)))
    }

    @GetMapping("/test/{id}")
    fun getTest(@PathVariable("id") id: String): ResponseEntity<BaseResponse<CompetitionTestResponse>> {
        return ResponseEntity.ok(BaseResponse(CompetitionTestResponse(testService.getTestForPassing(id))))
    }
}