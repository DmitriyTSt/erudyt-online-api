package ru.erudyt.online.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.erudyt.online.dto.enums.ApiError
import ru.erudyt.online.dto.enums.getException
import ru.erudyt.online.entity.api.TokenPairEntity
import ru.erudyt.online.entity.resource.UserEntity
import ru.erudyt.online.repository.resource.UserProfileRepository

@Service
class UserService @Autowired constructor(
    private val repository: UserProfileRepository,
    private val passwordEncoder: PasswordEncoder,
    private val tokenService: TokenService,
) {
    fun create(userEntity: UserEntity): UserEntity {
        userEntity.password = passwordEncoder.encode(userEntity.password)
        return repository.save(userEntity)
    }

    fun save(userEntity: UserEntity): UserEntity {
        return repository.save(userEntity)
    }

    fun findByLogin(login: String): UserEntity? {
        return repository.findByUsername(login)
    }

    fun findByLoginAndPassword(login: String, password: String): UserEntity? {
        val userEntity = findByLogin(login)
        return userEntity?.takeIf { passwordEncoder.matches(password, userEntity.password) }
    }

    fun getCurrentUser(_currentToken: TokenPairEntity? = null): UserEntity {
        val currentToken = _currentToken ?: tokenService.getCurrentTokenPair()
        return repository.findByIdOrNull(currentToken.userId) ?: throw ApiError.NOT_FOUND.getException()
    }

    fun getUsers(): List<UserEntity> {
        return repository.findAll()
    }
}