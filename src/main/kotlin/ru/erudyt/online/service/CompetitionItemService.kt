package ru.erudyt.online.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.erudyt.online.dto.model.CompetitionItem
import ru.erudyt.online.mapper.CompetitionItemMapper
import ru.erudyt.online.repository.resource.CompetitionItemRepository

@Service
class CompetitionItemService @Autowired constructor(
	private val repository: CompetitionItemRepository,
	private val mapper: CompetitionItemMapper,
) {

	fun getItems(): List<CompetitionItem> {
		return repository.findAll().map { mapper.fromEntityToModel(it) }
	}
}