package ru.erudyt.online.repository.customjpa

//@Repository
//interface StatisticRepository : JpaRepository<CommonResultInfoEntity, Long> {
//    @Query(
//        """
//        SELECT
//            new ru.erudyt.online.entity.resource.CommonResultInfoEntity(
//                SUM(t.result),
//                SUM(t.max_ball),
//                count(t)
//            )
//        FROM ${ResultEntity.TABLE_NAME} t
//        WHERE t.code = '?1'
//    """
//    )
//    fun getCommonResult(code: String): List<CommonResultInfoEntity>
//}