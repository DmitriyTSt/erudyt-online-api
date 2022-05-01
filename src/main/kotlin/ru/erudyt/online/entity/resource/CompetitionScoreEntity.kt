package ru.erudyt.online.entity.resource

import javax.persistence.Column
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "tl_compscore_score")
class CompetitionScoreEntity(
    val place: Int,
    @EmbeddedId val id: CompetitionScoreEntityPK,
    val score: Int,
    val country: String,
    @Column(name = "old_place") val oldPlace: Int,
)