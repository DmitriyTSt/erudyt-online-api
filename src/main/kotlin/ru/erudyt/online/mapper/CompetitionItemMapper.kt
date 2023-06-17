package ru.erudyt.online.mapper

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.erudyt.online.dto.model.CompetitionItem
import ru.erudyt.online.dto.model.CompetitionItemRawTest
import ru.erudyt.online.dto.model.CompetitionItemShort
import ru.erudyt.online.dto.model.TestAgeGroup
import ru.erudyt.online.entity.resource.CompetitionItemEntity

private val P_CONTENT_REGEX = "<p((?!>).)+>(((?!</p>).)+)</p>".toRegex()
private val LI_CONTENT_REGEX = "<li((?!>).)+>(((?!<\\\\).)+)</li>".toRegex()

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
            difficulty = entity.stars,
            icon = imageMapper.fromPathToUrl(imagePath),
        )
    }

    fun fromEntityToModel(
        entity: CompetitionItemEntity,
        imagePath: String?,
        testAgeGroups: List<TestAgeGroup>,
    ): CompetitionItem {
        val (description, infos) = separateDescriptionAndInfos(entity.description)
        return CompetitionItem(
            id = entity.id,
            title = entity.shortTitle,
            subject = entity.shortSubj,
            ages = entity.age,
            difficulty = entity.stars,
            icon = imageMapper.fromPathToUrl(imagePath),
            annotation = entity.annotation,
            description = description,
            tests = testAgeGroups,
            infos = infos,
        )
    }

    fun separateDescriptionAndInfos(rawDescription: String?): Pair<String?, List<String>> {
        if (rawDescription == null) return null to emptyList()
        val pContents = P_CONTENT_REGEX.findAll(rawDescription).toList()
            .mapNotNull { it.groupValues.toList().getOrNull(2) }
        val description = if (pContents.isEmpty()) {
            ""
        } else {
            pContents.subList(0, pContents.lastIndex.takeIf { it >= 0 } ?: 0)
                .joinToString("\n")
                .replace("<[^>]*>".toRegex(), "")
        }
        val infos = LI_CONTENT_REGEX.findAll(rawDescription).toList()
            .mapNotNull { it.groupValues.toList().getOrNull(2) }
        return description to infos
    }

    fun parseSubjects(blob: String): List<String> {
        return (SerializedPhpParser(blob).parse() as Map<*, *>).values.map { it as String }
    }

    fun parseAgeGroups(blob: String): List<String> {
        return (SerializedPhpParser(blob).parse() as Map<*, *>).values.map { it as String }
    }

    fun parseTests(blob: String): List<CompetitionItemRawTest> {
        return (SerializedPhpParser(blob).parse() as Map<*, *>).values
            .map { parseTest(it as Map<*, *>) }
    }

    private fun parseTest(map: Map<*, *>): CompetitionItemRawTest {
        return CompetitionItemRawTest(
            map["charid"] as String,
            (map["value"] as Map<*, *>).values.map { it as String }.map { it.toLong() }
        )
    }
}