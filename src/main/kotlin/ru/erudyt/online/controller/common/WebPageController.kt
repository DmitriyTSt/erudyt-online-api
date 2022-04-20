package ru.erudyt.online.controller.common

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.erudyt.online.controller.base.BaseResponse
import ru.erudyt.online.controller.base.ListResponse
import ru.erudyt.online.dto.model.WebPageItem
import ru.erudyt.online.dto.response.WebPageResponse
import ru.erudyt.online.service.WebPageService

@RestController
@RequestMapping("/api/", produces = [MediaType.APPLICATION_JSON_VALUE])
class WebPageController @Autowired constructor(
    private val webPageService: WebPageService,
) {
    @GetMapping("webpages")
    fun getWebPages(): ResponseEntity<BaseResponse<ListResponse<WebPageItem>>> {
        return ResponseEntity.ok(BaseResponse(ListResponse(webPageService.getInformationPages())))
    }

    @GetMapping("webpage")
    fun getWebPage(@RequestParam("path") path: String): ResponseEntity<BaseResponse<WebPageResponse>> {
        return ResponseEntity.ok(BaseResponse(WebPageResponse(webPageService.getPage(path))))
    }
}