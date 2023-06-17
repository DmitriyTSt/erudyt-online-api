package ru.erudyt.online.mapper

import org.junit.jupiter.api.Test
import org.springframework.test.util.AssertionErrors.assertEquals
import ru.erudyt.online.config.property.BackendAppSettings

class CompetitionItemMapperTest {

    private val mapper = CompetitionItemMapper(ImageMapper(BackendAppSettings()))

    @Test
    fun testSeparateDescriptionToDescriptionAndInfos() {
        val rawDescription = """
            <p style="text-align: justify;">Международный онлайн-конкурс по экологии&nbsp;«Великие экологи и их открытия» с моментальным подведением итогов для учеников 8-11 классов, студентов и педагогов. Участникам предлагается ответить на 10-15 вопросов с вариантами ответов (в зависимости от возрастной категории). Ограничение по времени не предусмотрено. Участие в конкурсе бесплатное, после сохранения результата можно заказать диплом для участника, грамоту для руководителя и <a href="medals.html" target="_blank">именные медали</a>.</p>
            <p style="text-align: justify;">Дополнительные материалы:</p>
            <ul>
            <li style="text-align: justify;"><a href="https://dzen.ru/a/ZHuOIqNgB3W2BcvV" target="_blank">Знаменитые ученые-экологи и их вклад в науку</a></li>
            <li style="text-align: justify;"><a href="https://zen.yandex.ru/media/id/5e50e88411638a2a18c13e31/darvinskii-gosudarstvennyi-prirodnyi-biosfernyi-zapovednik-62d4fc245f37c21646cd1885" target="_blank">Дарвинский государственный природный биосферный заповедник</a></li>
            <li style="text-align: justify;"><a href="https://zen.yandex.ru/media/id/5e50e88411638a2a18c13e31/ryjii-les-kak-radiaciia-vliiaet-na-derevia-60a3dd38d00cb53fcbd6e7a1" target="_blank">Рыжий лес. Как радиация влияет на деревья?</a></li>
            <li style="text-align: justify;"><a href="https://zen.yandex.ru/media/id/5e50e88411638a2a18c13e31/ischeznovenie-ozonovogo-sloia-prognoz-na-buduscee-6140c4509955383a7071cf33" target="_blank">Исчезновение озонового слоя: прогноз на будущее</a></li>
            <li style="text-align: justify;"><a href="https://zen.yandex.ru/media/id/5e50e88411638a2a18c13e31/pochemu-taiut-ledniki-i-k-chemu-eto-privedet-60a6721ace8b300078a6fc0e" target="_blank">Почему тают ледники и к чему это приведёт?</a></li>
            <li style="text-align: justify;"><a href="https://zen.yandex.ru/media/id/5e50e88411638a2a18c13e31/kofe-i-vred-ekologii-6075b4f11ed05104130c2912" target="_blank">Кофе и вред экологии</a></li>
            <li style="text-align: justify;"><a title="Чай и вред экологии" href="https://zen.yandex.ru/media/id/5e50e88411638a2a18c13e31/chai-i-vred-ekologii-609690d4c235fb60b68ca164" target="_blank">Чай и вред экологии</a></li>
            <li style="text-align: justify;"><a href="https://zen.yandex.ru/media/id/5e50e88411638a2a18c13e31/kak-sohranit-nashu-planetu-solnechnaia-energiia-603d00b0a3c2fb47748bc20c" target="_blank">Как сохранить нашу планету? Солнечная энергетика</a></li>
            </ul>
        """.trimIndent()

        val (description, infos) = mapper.separateDescriptionAndInfos(rawDescription)

        assertEquals("Incorrect description", "Международный онлайн-конкурс по экологии «Великие экологи и их открытия» с моментальным подведением итогов для учеников 8-11 классов, студентов и педагогов. Участникам предлагается ответить на 10-15 вопросов с вариантами ответов (в зависимости от возрастной категории). Ограничение по времени не предусмотрено. Участие в конкурсе бесплатное, после сохранения результата можно заказать диплом для участника, грамоту для руководителя и именные медали.", description)
    }
}
