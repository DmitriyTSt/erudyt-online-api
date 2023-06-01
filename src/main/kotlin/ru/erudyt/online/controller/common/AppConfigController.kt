package ru.erudyt.online.controller.common

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.erudyt.online.controller.base.BaseResponse
import ru.erudyt.online.dto.model.AppConfig
import ru.erudyt.online.dto.request.AppConfigRequest
import ru.erudyt.online.service.AppConfigService

@RestController
@RequestMapping("/api/v1/", produces = [MediaType.APPLICATION_JSON_VALUE])
class AppConfigController @Autowired constructor(
    private val appConfigService: AppConfigService,
) {

    @PostMapping("app/config")
    fun getAppConfig(@RequestBody request: AppConfigRequest): ResponseEntity<BaseResponse<AppConfig>> {
        return ResponseEntity.ok(BaseResponse(appConfigService.getAppConfig(request.appVersion)))
    }
}