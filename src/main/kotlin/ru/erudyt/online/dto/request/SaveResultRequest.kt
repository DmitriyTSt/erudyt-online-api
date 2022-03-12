package ru.erudyt.online.dto.request

class SaveResultRequest(
    val completeId: Long,
    val name: String,
    val surname: String,
    val patronymic: String?,
    val school: String?,
    val position: String?,
    val teacher: String?,
    val country: String,
    val city: String,
    val region: String?,
    val email: String,
    val teacherEmail: String?,
    val diplomaType: String,
    val review: Review,
) {
    class Review(
        /** Качество и понятность вопросов */
        val quality: Int?,
        /** Сложность вопросов */
        val difficulty: Int?,
        /** Насколько интересными были вопросы */
        val interest: Int?,
    )
}