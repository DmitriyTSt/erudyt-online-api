package ru.erudyt.online.entity.resource

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "tl_comp_items")
class CompetitionItemEntity(
    @Id val id: Long,
    val pid: Int,
    val title: String,
    val stars: Int,
    val published: String,
    val annotation: String?,
    val description: String?,
    val winner: String?,
    val useDate: String,
    val dateStart: Long,
    val dateEnd: Long,
    val dateSum: Long,
    val dateSend: Long,
    val useStatus: String,
    val status: String,
    val icon: ByteArray,
    val useRules: String,
    val rules: String?,
    val subjectBlob: String,
    val creativeBlob: String,
    val ageGroupsBlob: String,
    val charIdsBlob: String,
    val cost: String?,
    val contestFilesBlob: String?,
    val sorting: Long,
    val tstamp: Long,
    val shortTitle: String,
    val shortSubj: String,
    val age: String,
)