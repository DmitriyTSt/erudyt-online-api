package ru.erudyt.online.controller.common

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.erudyt.online.controller.base.BaseResponse
import ru.erudyt.online.controller.base.ListResponse
import ru.erudyt.online.dto.model.Country
import ru.erudyt.online.service.CountryService

@RestController
@RequestMapping("/api/v1/", produces = [MediaType.APPLICATION_JSON_VALUE])
class CountryController @Autowired constructor(
    private val countryService: CountryService,
) {
    @GetMapping("countries")
    fun getCountries(): ResponseEntity<BaseResponse<ListResponse<Country>>> {
        return ResponseEntity.ok(BaseResponse(ListResponse(countryService.getCountries())))
    }
}