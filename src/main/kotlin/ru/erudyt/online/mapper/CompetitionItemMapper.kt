package ru.erudyt.online.mapper

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.erudyt.online.dto.model.CompetitionItem
import ru.erudyt.online.dto.model.CompetitionItemShort
import ru.erudyt.online.entity.resource.CompetitionItemEntity

@Component
class CompetitionItemMapper @Autowired constructor(
    private val imageMapper: ImageMapper,
) {

    fun fromEntityToModelShort(entity: CompetitionItemEntity, imagePath: String?): CompetitionItemShort {
        return CompetitionItemShort(
            id = entity.id,
            title = entity.shortTitle,
            subject = entity.shortSubj,
            ages = entity.age,
            icon = imageMapper.fromPathToUrl(imagePath),
        )
    }

    fun fromEntityToModel(model: CompetitionItemEntity, imagePath: String?): CompetitionItem {
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
            icon = imageMapper.fromPathToUrl(imagePath),
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
            .map { parseTest(it as Map<*, *>) }
    }

    private fun parseTest(map: Map<*, *>): CompetitionItem.Test {
        return CompetitionItem.Test(
            map["charid"] as String,
            (map["value"] as Map<*, *>).values.map { it as String }
        )
    }
}