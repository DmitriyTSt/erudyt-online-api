package ru.erudyt.online.entity.api

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "temp_result")
class TempResultEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,
    val isAnon: Boolean,
    val userId: Long,
    val code: String,
    @Column(name = "competition_title") val competitionTitle: String,
    val place: Int,
    val result: Int,
    @Column(name = "max_ball") val maxBall: Int,
    val answers: String,
    val sequence: String,
    val correct: String,
)