package ru.erudyt.online.service

import org.jsoup.Jsoup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.erudyt.online.config.DomainSettings
import ru.erudyt.online.dto.model.WebPage

@Service
class WebPageService @Autowired constructor(
    private val domainSettings: DomainSettings,
) {
    fun getPage(path: String): WebPage {
        val fixedPath = if (path.firstOrNull() == '/') {
            path.substring(1)
        } else {
            path
        }
        val doc = Jsoup.connect("${domainSettings.baseUrl}${fixedPath}").get()
        val container = doc.body().select("#container")
        val title1 = container.select("h1").takeIf { it.isNotEmpty() }?.first()?.html()
        val title2 = if (title1 == null) {
            container.select("h2").takeIf { it.isNotEmpty() }?.first()?.html()
        } else {
            null
        }
        if (title1 != null) {
            container.select("h1").first().remove()
        }
        if (title2 != null) {
            container.select("h2").first().remove()
        }
        return WebPage((title1 ?: title2).orEmpty(), container.html())
    }
}