package ru.erudyt.online.repository.resource

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
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
        pageable: Pageable,
    ): Page<CompetitionItemEntity> {
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

        val list = entityManager
            .createQuery(query)
            .resultStream
            .skip(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .toList()

        val countQuery = cb.createQuery(Long::class.java)
        val countItem = countQuery.from(CompetitionItemEntity::class.java)
        countQuery.select(cb.count(countItem)).apply {
            if (predicates.isNotEmpty()) {
                where(cb.or(*predicates.toTypedArray()))
            }
        }
        val count = entityManager.createQuery(countQuery).singleResult

        return PageImpl(list, pageable, count)
    }
}