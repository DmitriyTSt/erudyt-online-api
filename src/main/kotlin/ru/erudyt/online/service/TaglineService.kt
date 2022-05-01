package ru.erudyt.online.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.erudyt.online.dto.model.Tagline
import ru.erudyt.online.mapper.TaglineMapper
import ru.erudyt.online.repository.fs.TaglineRepository

@Service
class TaglineService @Autowired constructor(
    private val repository: TaglineRepository,
    private val mapper: TaglineMapper,
) {
    fun getTaglines(): List<Tagline> {
        return try {
            repository.getTaglines().map { mapper.fromEntityToModel(it) }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}