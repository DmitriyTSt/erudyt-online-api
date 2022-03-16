package ru.erudyt.online.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.erudyt.online.controller.base.EmptyResponse
import ru.erudyt.online.dto.enums.ApiError
import ru.erudyt.online.dto.enums.Os
import ru.erudyt.online.dto.enums.getException
import ru.erudyt.online.dto.model.Device
import ru.erudyt.online.dto.model.Token
import ru.erudyt.online.dto.request.RegistrationRequest
import ru.erudyt.online.dto.response.AnonymTokenResponse
import ru.erudyt.online.dto.response.LoginResponse
import ru.erudyt.online.entity.api.AnonymousProfileEntity
import ru.erudyt.online.entity.resource.UserEntity
import ru.erudyt.online.repository.api.AnonymousProfileRepository
import java.security.MessageDigest
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID
import java.util.regex.Pattern

private const val DEFAULT_LOGIN_COUNT = 3
private const val LOCK_PERIOD_SECONDS = 60
private const val MIN_PASSWORD_LENGTH = 8

@Service
class AuthService @Autowired constructor(
    private val tokenService: TokenService,
    private val userService: UserService,
    private val anonymousProfileRepository: AnonymousProfileRepository,
    private val countryService: CountryService,
    private val validatorService: ValidatorService,
    private val mailService: MailService,
) {
    fun createAnonym(device: Device): AnonymTokenResponse {
        val profile = anonymousProfileRepository
            .findByDeviceId(device.id)
            ?.let {
                updateAnonymousUser(it, device.os)
            }
            ?: createAnonymousUser(device)
        return AnonymTokenResponse(tokenService.createToken(profile.id, device.id, true, device.os))
    }

    fun logout(): AnonymTokenResponse {
        val currentToken = tokenService.getCurrentTokenPair()
        if (currentToken.isAnonym) {
            throw ApiError.TOKEN_BELONGS_ANONYM.getException()
        }
        if (!currentToken.isActive) {
            throw ApiError.WRONG_TOKEN.getException()
        }
        val anonProfile = anonymousProfileRepository.findByDeviceId(currentToken.deviceId)
            ?: throw ApiError.PROFILE_NOT_EXISTS.getException()
        currentToken.isActive = false
        tokenService.save(currentToken)
        anonProfile.isActive = true
        anonymousProfileRepository.save(anonProfile)

        return AnonymTokenResponse(
            tokenService.createToken(
                userId = anonProfile.id,
                deviceId = anonProfile.deviceId,
                isAnonym = true,
                os = anonProfile.os
            )
        )
    }

    fun login(login: String, password: String): LoginResponse {
        val currentTokenPair = tokenService.getCurrentTokenPair()
        val anonymousProfile = anonymousProfileRepository.findByDeviceIdAndIsActiveIsTrue(currentTokenPair.deviceId)
            ?: throw ApiError.ANONYM_NOT_EXISTS.getException()
        val userEntity = userService.findByLogin(login)
            ?: throw ApiError.AUTH_ERROR.getException()
        val time = LocalDateTime.now().atOffset(ZoneOffset.UTC).toEpochSecond()

        if (userEntity.loginCount < 1) {
            userEntity.locked = time
            userEntity.loginCount = DEFAULT_LOGIN_COUNT
            userService.save(userEntity)
            // TODO log login count limit
            throw ApiError.AUTH_ERROR.getException()
        }

        checkAccess(userEntity)

        val userProfile = userService.findByLoginAndPassword(login, password)
        if (userProfile == null) {
            userEntity.loginCount--
            userService.save(userEntity)
            throw ApiError.AUTH_ERROR.getException()
        }
        userProfile.apply {
            lastLogin = currentLogin
            currentLogin = time
            loginCount = DEFAULT_LOGIN_COUNT
        }
        userService.save(userProfile)
        anonymousProfile.isActive = false
        anonymousProfileRepository.save(anonymousProfile)
        return LoginResponse(
            tokenService.createToken(
                userId = userProfile.id,
                deviceId = anonymousProfile.deviceId,
                isAnonym = false,
                os = anonymousProfile.os
            )
        )
    }

    private fun checkAccess(userEntity: UserEntity) {
        val time = LocalDateTime.now().atOffset(ZoneOffset.UTC).toEpochSecond()
        if (userEntity.locked + LOCK_PERIOD_SECONDS > time) {
            throw ApiError.AUTH_LOCKED_BY_LOGIN_COUNT.getException()
        }
        if (userEntity.disable == "1") {
            throw ApiError.AUTH_ERROR.getException()
        }
        if (userEntity.login != "1") {
            throw ApiError.AUTH_ERROR.getException()
        }
        val timeFloorToMinute = floorToMinute(time)
        userEntity.start.toLongOrNull()?.let { start ->
            if (start > timeFloorToMinute) {
                throw ApiError.AUTH_ERROR.getException()
            }
        }
        userEntity.stop.toLongOrNull()?.let { stop ->
            if (stop <= (timeFloorToMinute + 60)) {
                throw ApiError.AUTH_ERROR.getException()
            }
        }
    }

    fun registration(request: RegistrationRequest): EmptyResponse {
        val currentTokenPair = tokenService.getCurrentTokenPair()
        val anonymousProfile = anonymousProfileRepository.findByDeviceIdAndIsActiveIsTrue(currentTokenPair.deviceId)
            ?: throw ApiError.ANONYM_NOT_EXISTS.getException()
        val activation = UUID.randomUUID().toString().let { uniqId ->
            MessageDigest.getInstance("MD5").digest(uniqId.toByteArray()).toHexString().lowercase()
        }
        if (!countryService.getCountries().map { it.code }.contains(request.country)) {
            throw ApiError.COUNTRY_NOT_FOUND.getException()
        }
        if (!validatorService.isEmail(request.email)) {
            throw ApiError.WRONG_EMAIL.getException()
        }
        if (request.password.length < MIN_PASSWORD_LENGTH) {
            throw ApiError.PASSWORD_INCORRECT_MIN.getException()
        }
        val lastUserId = userService.getLastId()
        val userProfile = userService.create(
            UserEntity(
                id = lastUserId + 1,
                username = request.email,
                password = request.password,
                firstName = request.name,
                lastName = request.surname,
                gender = when (request.gender) {
                    RegistrationRequest.Gender.NOT_SET -> ""
                    RegistrationRequest.Gender.MALE -> "male"
                    RegistrationRequest.Gender.FEMALE -> "female"
                },
                email = request.email,
                middleName = request.patronymic.orEmpty(),
                activation = activation,
                disable = "1",
                city = request.city,
                country = request.country,
                emailAgreement = if (request.emailAgreement) "1" else "",
                loginCount = DEFAULT_LOGIN_COUNT,
            )
        )
        try {
            mailService.sendConfirmEmail(request.email, activation)
        } catch (e: Exception) {
            throw ApiError.ERROR_SEND_CONFIRM_MESSAGE.getException()
        }

        return EmptyResponse()
    }

    fun refreshToken(deviceId: String, refreshToken: String): Token {
        return tokenService.refreshToken(deviceId, refreshToken)
    }

    fun getAnonymousProfile(id: Long): AnonymousProfileEntity {
        return anonymousProfileRepository.findByIdOrNull(id) ?: throw ApiError.NOT_FOUND.getException()
    }

    fun updateAnonymousUserLastEmail(id: Long, email: String): AnonymousProfileEntity {
        val user = getAnonymousProfile(id)
        user.lastEmail = email
        return anonymousProfileRepository.save(user)
    }

    private fun updateAnonymousUser(user: AnonymousProfileEntity, os: Os): AnonymousProfileEntity {
        user.os = os
        user.isActive = true
        return anonymousProfileRepository.save(user)
    }

    private fun createAnonymousUser(device: Device): AnonymousProfileEntity {
        val user = AnonymousProfileEntity(
            deviceId = device.id,
            os = device.os,
        )
        return anonymousProfileRepository.save(user)
    }

    private fun ByteArray.toHexString(): String {
        val sb = StringBuilder()
        for (b in this) sb.append(String.format("%02x", b.toInt() and 0xFF))
        return sb.toString()
    }

    private fun floorToMinute(time: Long): Long {
        return time - (time % 60)
    }
}