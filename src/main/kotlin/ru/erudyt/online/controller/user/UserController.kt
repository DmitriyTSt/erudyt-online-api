package ru.erudyt.online.controller.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.erudyt.online.controller.base.BaseResponse
import ru.erudyt.online.controller.base.ListResponse
import ru.erudyt.online.dto.model.User
import ru.erudyt.online.dto.response.ProfileResponse
import ru.erudyt.online.service.UserService

@RestController
@RequestMapping("/api/v1/", produces = [MediaType.APPLICATION_JSON_VALUE])
class UserController @Autowired constructor(
    private val userService: UserService,
) {
    @GetMapping("users")
    fun getUsers(): ResponseEntity<ListResponse<User>> {
        return ResponseEntity.ok(ListResponse(userService.getUsers()))
    }

    @GetMapping("profile")
    fun getProfile(): ResponseEntity<BaseResponse<ProfileResponse>> {
        return ResponseEntity.ok(BaseResponse(ProfileResponse(userService.getProfile())))
    }
}