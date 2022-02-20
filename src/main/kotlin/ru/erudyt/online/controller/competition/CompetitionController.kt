package ru.erudyt.online.controller.competition

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.erudyt.online.controller.base.BaseResponse
import ru.erudyt.online.dto.model.CompetitionItem
import ru.erudyt.online.service.CompetitionItemService

@RestController
@RequestMapping("/api/events", produces = [MediaType.APPLICATION_JSON_VALUE])
class CompetitionController @Autowired constructor(
	private val service: CompetitionItemService,
) {
	@GetMapping("/")
	fun getItems(): ResponseEntity<BaseResponse<List<CompetitionItem>>> {
		return ResponseEntity.ok(BaseResponse(service.getItems()))
	}
}