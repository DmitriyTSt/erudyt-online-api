package ru.erudyt.online.mapper

import org.springframework.stereotype.Component
import ru.erudyt.online.dto.model.CompetitionItem
import ru.erudyt.online.entity.resource.CompetitionItemEntity
import java.util.UUID

@Component
class CompetitionItemMapper {

	fun fromEntityToModel(model: CompetitionItemEntity): CompetitionItem {
		return CompetitionItem(
			id = model.id,
			pid = model.pid,
			title = model.title,
			stars = model.stars,
			published = model.published,
			annotation = model.annotation,
			description = model.description,
			winner = model.winner,
			useDate = model.useDate,
			dateStart = model.dateStart,
			dateEnd = model.dateEnd,
			dateSum = model.dateSum,
			dateSend = model.dateSend,
			useStatus = model.useStatus,
			status = model.status,
			icon = UUID.nameUUIDFromBytes(model.icon).toString(),
			useRules = model.useRules,
			rules = model.rules,
			subjects = parseSubjects(model.subjectBlob),
			ageGroups = parseAgeGroups(model.ageGroupsBlob),
			tests = parseTests(model.charIdsBlob),
			cost = model.cost,
			contestFilesBlob = model.contestFilesBlob,
			sorting = model.sorting,
			tstamp = model.tstamp,
			shortTitle = model.shortTitle,
			shortSubj = model.shortSubj,
			age = model.age,
		)
	}

	private fun parseSubjects(blob: String): List<String> {
		return (SerializedPhpParser(blob).parse() as Map<*, *>).values.map { it as String }
	}

	private fun parseAgeGroups(blob: String): List<String> {
		return (SerializedPhpParser(blob).parse() as Map<*, *>).values.map { it as String }
	}

	private fun parseTests(blob: String): List<CompetitionItem.Test> {
		return (SerializedPhpParser(blob).parse() as Map<*, *>).values
			.map { parseTest (it as Map<*, *>) }
	}

	private fun parseTest(map: Map<*, *>): CompetitionItem.Test {
		return CompetitionItem.Test(
			map["charid"] as String,
			(map["value"] as Map<*, *>).values.map { it as String }
		)
	}
}