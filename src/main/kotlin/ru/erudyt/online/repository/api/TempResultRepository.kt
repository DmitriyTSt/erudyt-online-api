package ru.erudyt.online.repository.api

import org.springframework.data.jpa.repository.JpaRepository
import ru.erudyt.online.entity.api.TempResultEntity

interface TempResultRepository : JpaRepository<TempResultEntity, Long> {
}