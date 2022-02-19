package ru.erudyt.online.controller.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.erudyt.online.controller.base.BaseResponse
import ru.erudyt.online.entity.resource.UserEntity
import ru.erudyt.online.service.UserService

@RestController
@RequestMapping("/api/user", produces = [MediaType.APPLICATION_JSON_VALUE])
class UserController @Autowired constructor(
    private val userService: UserService,
) {
    @GetMapping("/")
    fun getUsers(): ResponseEntity<BaseResponse<List<UserEntity>>> {
        return ResponseEntity.ok(BaseResponse(userService.getUsers()))
    }
}