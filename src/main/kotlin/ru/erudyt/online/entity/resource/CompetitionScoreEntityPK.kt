package ru.erudyt.online.entity.resource

import javax.persistence.Embeddable

@Embeddable
class CompetitionScoreEntityPK(
	var email: String,
	var name: String,
	var period: Int,
) : java.io.Serializable