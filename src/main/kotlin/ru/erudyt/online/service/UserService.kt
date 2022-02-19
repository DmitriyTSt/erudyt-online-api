package ru.erudyt.online.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.erudyt.online.entity.resource.UserEntity
import ru.erudyt.online.repository.resource.UserProfileRepository

@Service
class UserService @Autowired constructor(
    private val repository: UserProfileRepository,
    private val passwordEncoder: PasswordEncoder,
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

    fun getUsers(): List<UserEntity> {
        return repository.findAll()
    }
}