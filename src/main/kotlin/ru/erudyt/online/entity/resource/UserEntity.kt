package ru.erudyt.online.entity.resource

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,
    @Column(name = "firstname") val firstName: String,
    @Column(name = "lastname") val lastName: String,
    val username: String,
    var password: String,
    val email: String,
)