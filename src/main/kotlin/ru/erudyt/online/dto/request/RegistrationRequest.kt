package ru.erudyt.online.dto.request

class RegistrationRequest(
    val email: String,
    val password: String,
    val name: String,
    val surname: String,
    val patronymic: String?,
    val birthday: Long?,
    val gender: Gender,
    val company: String?,
    val city: String,
    val region: String?,
    val country: String,
    val emailAgreement: Boolean,
) {
    enum class Gender {
        MALE,
        FEMALE,
    }
}