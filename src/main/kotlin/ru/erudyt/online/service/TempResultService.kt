package ru.erudyt.online.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.erudyt.online.dto.enums.ApiError
import ru.erudyt.online.dto.enums.getException
import ru.erudyt.online.dto.model.TempResult
import ru.erudyt.online.entity.api.TempResultEntity
import ru.erudyt.online.mapper.TempResultMapper
import ru.erudyt.online.repository.api.TempResultRepository

@Service
class TempResultService @Autowired constructor(
    private val mapper: TempResultMapper,
    private val repository: TempResultRepository,
) {
    fun save(tempResult: TempResult): TempResultEntity {
        return repository.save(mapper.fromModelToEntity(tempResult))
    }

    fun get(id: Long): TempResultEntity {
        return repository.findByIdOrNull(id) ?: throw ApiError.NOT_FOUND.getException()
    }

    fun delete(id: Long) {
        repository.delete(repository.getById(id))
    }
}