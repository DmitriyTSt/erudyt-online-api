package ru.erudyt.online.repository.resource

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import ru.erudyt.online.entity.resource.CompetitionItemEntity
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.Predicate
import kotlin.streams.toList

@Repository
class CustomCompetitionItemRepositoryImpl @Autowired constructor(
    @PersistenceContext val entityManager: EntityManager,
) : CustomCompetitionItemRepository {

    override fun findAllByQueryAndAgesAndSubjects(
        searchQuery: String?,
        ageIds: List<Long>,
        subjectIds: List<Long>,
        offset: Long,
        limit: Long
    ): Pair<List<CompetitionItemEntity>, Long> {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(CompetitionItemEntity::class.java)
        val item = query.from(CompetitionItemEntity::class.java)

        val agePath = item.get<String>("ageGroupsBlob")
        val subjectPath = item.get<String>("subjectBlob")

        val predicates = mutableListOf<Predicate>()

        ageIds.forEach { ageId ->
            predicates.add(cb.like(agePath, "%$ageId%"))
        }
        subjectIds.forEach { subjectId ->
            predicates.add(cb.like(subjectPath, "%$subjectId%"))
        }
        if (searchQuery != null) {
            val titlePath = item.get<String>("title")
            predicates.add(cb.like(titlePath, "%$searchQuery%"))
        }

        val publishedPath = item.get<String>("published")
        predicates.add(cb.equal(publishedPath, "1"))

        query.select(item).apply {
            if (predicates.isNotEmpty()) {
                where(cb.or(*predicates.toTypedArray()))
            }
        }

        return entityManager.createQuery(query).resultStream.let {
            it.skip(offset).limit(limit).toList() to 100
        }
    }
}