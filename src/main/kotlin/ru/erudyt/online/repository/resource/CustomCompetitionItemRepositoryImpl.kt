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

    /**
     * @param searchQuery - И [CompetitionItemEntity.title] включает в себя [searchQuery] (если указан)
     * @param ageIds - И [CompetitionItemEntity.ageGroupsBlob] включает в себя любые [ageIds]
     * @param subjectIds - И [CompetitionItemEntity.subjectBlob] включает в себя любые [subjectIds]
     * @param difficulty - И [CompetitionItemEntity.stars] = [difficulty] (если указан)
     * @param notContainCodes - И [CompetitionItemEntity.charIdsBlob] не включает ни одного [notContainCodes]
     */
    override fun findAllByQueryAndAgesAndSubjects(
        searchQuery: String?,
        ageIds: List<Long>,
        subjectIds: List<Long>,
        difficulty: Int?,
        notContainCodes: List<String>,
        pageable: Pageable,
    ): Page<CompetitionItemEntity> {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(CompetitionItemEntity::class.java)
        val item = query.from(CompetitionItemEntity::class.java)

        val agePath = item.get<String>("ageGroupsBlob")
        val subjectPath = item.get<String>("subjectBlob")
        val charIdsPath = item.get<String>("charIdsBlob")

        val predicatesAges = mutableListOf<Predicate>()
        val predicatesSubjects = mutableListOf<Predicate>()
        val predicatesAnd = mutableListOf<Predicate>()

        ageIds.forEach { ageId ->
            predicatesAges.add(cb.like(agePath, "%$ageId%"))
        }
        if (predicatesAges.isNotEmpty()) {
            predicatesAnd.add(cb.or(*predicatesAges.toTypedArray()))
        }
        subjectIds.forEach { subjectId ->
            predicatesSubjects.add(cb.or(cb.like(subjectPath, "%$subjectId%")))
        }
        if (predicatesSubjects.isNotEmpty()) {
            predicatesAnd.add(cb.or(*predicatesSubjects.toTypedArray()))
        }
        notContainCodes.forEach { code ->
            predicatesAnd.add(cb.notLike(charIdsPath, "%$code%"))
        }
        if (searchQuery != null) {
            val titlePath = item.get<String>("title")
            predicatesAnd.add(cb.like(titlePath, "%$searchQuery%"))
        }
        if (difficulty != null) {
            val starsPath = item.get<Int>("stars")
            predicatesAnd.add(cb.equal(starsPath, difficulty))
        }
        val predicates = mutableListOf<Predicate>()
        if (predicatesAnd.isNotEmpty()) {
            predicates.add(cb.and(*predicatesAnd.toTypedArray()))
        }

        val publishedPath = item.get<String>("published")
        predicates.add(cb.equal(publishedPath, "1"))

        query.select(item).apply {
            if (predicates.isNotEmpty()) {
                where(*predicates.toTypedArray())
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
                where(*predicates.toTypedArray())
            }
        }
        val count = entityManager.createQuery(countQuery).singleResult

        return PageImpl(list, pageable, count)
    }

    override fun findAllByCodes(codes: List<String>): List<CompetitionItemEntity> {
        if (codes.isEmpty()) {
            return emptyList()
        }

        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(CompetitionItemEntity::class.java)
        val item = query.from(CompetitionItemEntity::class.java)

        val charIdsPath = item.get<String>("charIdsBlob")

        val predicates = mutableListOf<Predicate>()

        codes.forEach { code ->
            predicates.add(cb.like(charIdsPath, "%$code%"))
        }

        query.select(item).apply {
            if (predicates.isNotEmpty()) {
                where(cb.or(*predicates.toTypedArray()))
            }
        }

        return entityManager.createQuery(query).resultStream.toList()
    }
}