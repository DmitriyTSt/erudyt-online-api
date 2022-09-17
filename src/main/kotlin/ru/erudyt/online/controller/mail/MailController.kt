package ru.erudyt.online.controller.mail

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.erudyt.online.config.EnvSettings
import ru.erudyt.online.dto.enums.ApiError
import ru.erudyt.online.dto.enums.getException
import ru.erudyt.online.dto.request.TestMailRequest
import ru.erudyt.online.service.MailService

@RestController
@RequestMapping("/api/v1/", produces = [MediaType.APPLICATION_JSON_VALUE])
class MailController @Autowired constructor(
    private val mailService: MailService,
    private val envSettings: EnvSettings,
) {

    @GetMapping("test-mail")
    fun main(request: TestMailRequest): ResponseEntity<Unit> {
        if (envSettings.env != EnvSettings.DEV) throw ApiError.NOT_AVAILABLE_BY_ENV.getException()

        return ResponseEntity.ok(mailService.sendEmail(request.to, request.subject, request.text))
    }
}