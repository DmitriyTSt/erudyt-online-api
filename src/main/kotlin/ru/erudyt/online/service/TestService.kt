package ru.erudyt.online.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.erudyt.online.entity.test.TestEntity
import ru.erudyt.online.repository.test.TestRepository

@Service
class TestService @Autowired constructor(
    private val testRepository: TestRepository,
) {
    fun getRawTest(code: String): TestEntity {
        return testRepository.getTest(code)
    }
}