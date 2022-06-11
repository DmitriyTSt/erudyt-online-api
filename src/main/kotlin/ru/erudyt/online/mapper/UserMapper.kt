package ru.erudyt.online.mapper

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.erudyt.online.dto.model.Country
import ru.erudyt.online.dto.model.User
import ru.erudyt.online.entity.resource.UserEntity

@Component
class UserMapper @Autowired constructor(
    private val imageMapper: ImageMapper,
) {
    fun fromEntityToModel(entity: UserEntity, country: Country?): User {
        return User(
            id = entity.id,
            email = entity.email,
            name = entity.firstName,
            surname = entity.lastName,
            patronymic = entity.middleName.takeIf { it.isNotEmpty() },
            school = entity.company.takeIf { it.isNotEmpty() },
            city = entity.city,
            region = entity.state.takeIf { it.isNotEmpty() },
            country = country,
            avatar = imageMapper.fromProfileIdToAvatar(entity.id),
        )
    }
}