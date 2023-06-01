package ru.erudyt.online.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import ru.erudyt.online.config.property.MailSettings

@Service
@EnableConfigurationProperties(MailSettings::class)
class MailService @Autowired constructor(
    private val mailSettings: MailSettings,
    private val emailSender: JavaMailSender,
) {
    fun sendEmail(to: String, subject: String, text: String) {
        val message = SimpleMailMessage().apply {
            setFrom(mailSettings.email)
            setTo(to)
            setSubject(subject)
            setText(text)
        }
        emailSender.send(message)
    }

    fun sendConfirmEmail(to: String, token: String) {
        val text = """
            Спасибо за регистрацию на erudyt-online.ru.

            Пожалуйста, нажмите на <a href="http://erudyt-online.ru/registration.html?token=$token">http://erudyt-online.ru/registration.html?token=$token</a> , чтобы завершить регистрацию и активировать вашу учетную запись. Если вы не запрашивали регистрацию этой учетной записи, пропустите это сообщение.
        """.trimIndent()
        val subject = "Ваша регистрация на erudyt-online.ru"
        sendEmail(to, subject, text)
    }
}