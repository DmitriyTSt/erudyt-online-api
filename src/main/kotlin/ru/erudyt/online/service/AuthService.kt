package ru.erudyt.online.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.erudyt.online.dto.enums.ApiError
import ru.erudyt.online.dto.enums.Os
import ru.erudyt.online.dto.enums.getException
import ru.erudyt.online.dto.model.Device
import ru.erudyt.online.dto.model.Token
import ru.erudyt.online.entity.api.AnonymousProfileEntity
import ru.erudyt.online.entity.resource.UserEntity
import ru.erudyt.online.repository.api.AnonymousProfileRepository

@Service
class AuthService @Autowired constructor(
	private val tokenService: TokenService,
	private val userService: UserService,
	private val anonymousProfileRepository: AnonymousProfileRepository,
) {
	fun createAnonym(device: Device): Token {
		val profile = anonymousProfileRepository
			.findByDeviceId(device.id)
			?.let { updateAnonymousUser(it, device.os) }
			?: createAnonymousUser(device)
		return tokenService.createToken(profile.id, device.id, true, device.os)
	}

	fun login(login: String, password: String): Token {
		val currentTokenPair = tokenService.getCurrentTokenPair()
		val anonymousProfile = anonymousProfileRepository.findByDeviceIdAndIsActiveIsTrue(currentTokenPair.deviceId)
			?: throw ApiError.ANONYM_NOT_EXISTS.getException()
		val userProfile = userService.findByLoginAndPassword(login, password)
			?: throw ApiError.AUTH_ERROR.getException()
		return tokenService.createToken(userProfile.id, anonymousProfile.deviceId, false, anonymousProfile.os)
	}

	// TODO refactor
	fun registration(login: String, password: String): Token {
		val currentTokenPair = tokenService.getCurrentTokenPair()
		val anonymousProfile = anonymousProfileRepository.findByDeviceIdAndIsActiveIsTrue(currentTokenPair.deviceId)
			?: throw ApiError.ANONYM_NOT_EXISTS.getException()
		val userProfile = userService.create(
            UserEntity(username = login, password = password, firstName = "", lastName = "", email = login)
        )
		return tokenService.createToken(userProfile.id, anonymousProfile.deviceId, false, anonymousProfile.os)
	}

	fun refreshToken(deviceId: String, refreshToken: String): Token {
		return tokenService.refreshToken(deviceId, refreshToken)
	}

	private fun updateAnonymousUser(user: AnonymousProfileEntity, os: Os): AnonymousProfileEntity {
		user.os = os
		return anonymousProfileRepository.save(user)
	}

	private fun createAnonymousUser(device: Device): AnonymousProfileEntity {
		val user = AnonymousProfileEntity(
			deviceId = device.id,
			os = device.os,
		)
		return anonymousProfileRepository.save(user)
	}
}