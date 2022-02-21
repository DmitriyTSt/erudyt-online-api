package ru.erudyt.online.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.erudyt.online.repository.resource.FileEntityRepository

@Service
class FileService @Autowired constructor(
    private val repository: FileEntityRepository,
) {
    fun findImagePathByUuid(uuid: ByteArray): String? {
        return repository.findByUuid(uuid)?.path
    }
}