package ru.erudyt.online.service

import org.jsoup.Jsoup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.erudyt.online.config.DomainSettings
import ru.erudyt.online.dto.model.WebPage
import ru.erudyt.online.dto.model.WebPageItem

@Service
class WebPageService @Autowired constructor(
    private val domainSettings: DomainSettings,
) {
    fun getInformationPages(): List<WebPageItem> {
        return listOf(
            WebPageItem(
                path = "rules.html",
                name = "Правила",
            ),
            WebPageItem(
                path = "faq.html",
                name = "Вопрос-ответ",
            ),
            WebPageItem(
                path = "reviews.html",
                name = "Отзывы",
            ),
            WebPageItem(
                path = "news.html",
                name = "Новости",
            ),
            WebPageItem(
                path = "contacts.html",
                name = "Контакты",
            ),
            WebPageItem(
                path = "smi.html",
                name = "Мы в СМИ",
            ),
            WebPageItem(
                path = "links.html",
                name = "Ресурсы",
            ),
        )
    }

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