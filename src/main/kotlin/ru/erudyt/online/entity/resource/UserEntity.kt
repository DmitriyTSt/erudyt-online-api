package ru.erudyt.online.entity.resource

import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "tl_member")
class UserEntity(
    @Id
    val id: Long, // TODO use normal autoincrement id
    val account: Int = 0,
    @Column(name = "firstname") val firstName: String,
    @Column(name = "lastname") val lastName: String,
    @Column(name = "middlename") val middleName: String = "",
    val dateOfBirth: String = "",
    val gender: String = "",
    val company: String = "",
    val street: String = "",
    val postal: String = "",
    val city: String,
    val state: String = "",
    val country: String,
    val phone: String = "",
    val mobile: String = "",
    val fax: String = "",
    val email: String,
    val website: String = "",
    val language: String = "",
    @Column(name = "`groups`") var groups: String = "a:1:{i:0;s:1:\"1\";}",
    val login: String = "1",
    val username: String,
    var password: String,
    val assignDir: String = "",
    val homeDir: String? = null,
    val disable: String,
    val start: String = "",
    val stop: String = "",
    val dateAdded: Long = LocalDateTime.now().atOffset(ZoneOffset.UTC).toEpochSecond(),
    var lastLogin: Long = 0,
    var currentLogin: Long = 0,
    var loginCount: Int,
    var locked: Long = 0,
    val session: String = "a:0:{}",
    val autologin: String? = null,
    val createdOn: Long = 0,
    val activation: String,
    val newsletter: String = "",
    @Column(name = "email_agreement") val emailAgreement: String,
    val tstamp: Long = LocalDateTime.now().atOffset(ZoneOffset.UTC).toEpochSecond(),
) {
    companion object {
        const val TABLE_NAME = "tl_member"
    }
}