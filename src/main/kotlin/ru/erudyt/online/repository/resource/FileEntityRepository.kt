package ru.erudyt.online.repository.resource

import org.springframework.data.jpa.repository.JpaRepository
import ru.erudyt.online.entity.resource.FileEntity

interface FileEntityRepository : JpaRepository<FileEntity, Long> {
    fun findByUuid(uuid: ByteArray): FileEntity?
}