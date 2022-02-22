package ru.erudyt.online.entity.resource

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "tl_comp_taxonomy")
class TaxonomyEntity(
    @Id val id: Long,
    @Column(name = "pid") val type: Int,
    val title: String,
    val alias: String,
    val sorting: Int,
    val tstamp: Long,
    val useGroup: String,
    val groupPostfix: String,
    val groupIndex: Int,
    val annotation: String?,
    val pageTitle: String,
    val showInMenu: String,
) {
    companion object {
        const val TYPE_AGE = 5
        const val TYPE_SUBJECT = 3
    }
}