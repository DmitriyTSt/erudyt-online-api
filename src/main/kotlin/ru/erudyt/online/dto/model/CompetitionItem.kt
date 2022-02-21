package ru.erudyt.online.dto.model

class CompetitionItem(
	val id: Long,
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
	val icon: String?,
	val useRules: String,
	val rules: String?,
	val subjects: List<String>,
	val ageGroups: List<String>,
	val tests: List<Test>,
	val cost: String?,
	val contestFilesBlob: String?,
	val sorting: Long,
	val tstamp: Long,
	val shortTitle: String,
	val shortSubj: String,
	val age: String,
) {
	class Test(
		val id: String,
		val value: List<String>,
	)
}