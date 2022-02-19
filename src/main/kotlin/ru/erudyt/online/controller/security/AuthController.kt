package ru.erudyt.online.controller.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.erudyt.online.controller.base.BaseResponse
import ru.erudyt.online.dto.model.Token
import ru.erudyt.online.dto.request.DeviceRequest
import ru.erudyt.online.dto.request.LoginRequest
import ru.erudyt.online.dto.request.RefreshTokenRequest
import ru.erudyt.online.dto.request.RegistrationRequest
import ru.erudyt.online.service.AuthService

@RestController
@RequestMapping("/api/auth", produces = [MediaType.APPLICATION_JSON_VALUE])
class AuthController @Autowired constructor(
    private val authService: AuthService,
) {

    @PostMapping("/anonym")
    fun createAnonym(@RequestBody request: DeviceRequest): ResponseEntity<BaseResponse<Token>> {
        return ResponseEntity.ok(BaseResponse(authService.createAnonym(request.device)))
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<BaseResponse<Token>> {
        return ResponseEntity.ok(BaseResponse(authService.login(request.login, request.password)))
    }

    @PostMapping("/registration")
    fun registration(@RequestBody request: RegistrationRequest): ResponseEntity<BaseResponse<Token>> {
        return ResponseEntity.ok(BaseResponse(authService.registration(request.login, request.password)))
    }

    @PostMapping("/refresh")
    fun refreshToken(@RequestBody request: RefreshTokenRequest): ResponseEntity<BaseResponse<Token>> {
        return ResponseEntity.ok(BaseResponse(authService.refreshToken(request.deviceId, request.refreshToken)))
    }
}