package ru.erudyt.online.mapper

import org.springframework.stereotype.Component
import ru.erudyt.online.dto.model.User
import ru.erudyt.online.entity.resource.UserEntity

@Component
class UserMapper {
    fun fromEntityToModel(entity: UserEntity): User {
        return User(
            id = entity.id,
            email = entity.email,
            name = entity.firstName,
            surname = entity.lastName,
            patronymic = entity.middleName.takeIf { it.isNotEmpty() },
            avatar = null,
        )
    }
}