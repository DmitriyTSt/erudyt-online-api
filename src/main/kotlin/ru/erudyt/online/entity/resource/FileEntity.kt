package ru.erudyt.online.entity.resource

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "tl_files")
class FileEntity(
    @Id val id: Long,
    val pid: String,
    val tstamp: Long,
    val uuid: ByteArray,
    val type: String,
    val path: String,
    val extension: String,
    val hash: String,
    val found: String,
    val name: String,
    val meta: String?,
    val importantPartX: Long,
    val importantPartY: Long,
    val importantPartWidth: Long,
    val importantPartHeight: Long,
)