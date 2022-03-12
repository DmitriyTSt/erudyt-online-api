package ru.erudyt.online.entity.resource

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "tl_comp_result")
class ResultEntity(
    @Id val id: Long,
    val date: Long,
    val paid: String,
    val code: String,
    val place: Int,
    @Column(name = "konkurs") val competitionTitle: String,
    val name: String,
    val email: String,
    val school: String,
    val country: String,
    val city: String,
    val region: String,
    val position: String,
    val teacher: String,
    @Column(name = "t_school") val teacherSchool: String,
    @Column(name = "t_email") val teacherEmail: String,
    val coord: String,
    val result: Int,
    @Column(name = "max_ball") val maxBall: Int,
    val answers: String,
    val sequence: String,
    val correct: String,
    @Column(name = "member_id") val userId: Long,
    val summ: Double,
    val lastModified: Long?,
    val comment: String?,
    val pid: Long?,
    val tstamp: Long?,
    @Column(name = "`key`") val key: String,
    @Column(name = "date_reg") val dateReg: Long?,
    val cost: Int,
    val time: Long,
    val ip: String,
    val template: String,
    val hide: String,
    @Column(name = "group_id") val groupId: Int,
    @Column(name = "medal_p") val medalP: Int,
    @Column(name = "medal_t") val medalT: Int,
    val rating1: Int,
    val rating2: Int,
    val rating3: Int,
) {
    companion object {
        const val TABLE_NAME = "tl_comp_result"
    }
}