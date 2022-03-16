package ru.erudyt.online.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MailService @Autowired constructor() {
    fun sendEmail(to: String, text: String) {

    }
}