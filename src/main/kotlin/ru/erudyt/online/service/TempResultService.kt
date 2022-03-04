package ru.erudyt.online.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
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

    fun delete(id: Long) {
        repository.delete(repository.getById(id))
    }
}