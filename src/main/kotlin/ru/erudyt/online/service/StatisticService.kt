package ru.erudyt.online.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.erudyt.online.entity.resource.CommonResultInfoEntity
import ru.erudyt.online.repository.resource.ResultRepository

@Service
class StatisticService @Autowired constructor(
    private val resultRepository: ResultRepository,
) {
    fun getCommonResult(code: String): CommonResultInfoEntity {
        return resultRepository.getCommonResult(code)
    }

    fun getCountByTestAndLessOrEqualResult(code: String, ball: Int): Int {
        return resultRepository.countByCodeAndResultLessThanEqual(code, ball)
    }
}