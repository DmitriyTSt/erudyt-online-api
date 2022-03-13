package ru.erudyt.online.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.erudyt.online.dto.model.RatingRow
import ru.erudyt.online.dto.request.RatingRequest
import ru.erudyt.online.mapper.RatingMapper
import ru.erudyt.online.repository.resource.ResultRepository
import ru.erudyt.online.repository.resource.ScoreRepository
import java.time.LocalDateTime
import java.time.ZoneOffset

private const val RATING_DAY_LIMIT = 25
private const val RATING_MONTH_LIMIT = 50
private const val RATING_YEAR_LIMIT = 100

@Service
class RatingService @Autowired constructor(
	private val resultRepository: ResultRepository,
	private val scoreRepository: ScoreRepository,
	private val ratingMapper: RatingMapper,
) {

	fun getRating(request: RatingRequest): List<RatingRow> = with(request) {
		return when {
			day != null && month != null -> getDayRating(year, month, day)
			month != null -> getMonthRating(year, month)
			else -> getYearRating(year)
		}
	}

	private fun getDayRating(year: Int, month: Int, day: Int): List<RatingRow> {
		val date = LocalDateTime.of(year, month, day, 0, 0, 0)
		val nextDate = date.plusDays(1)
		val startDay = date.toMillis() / 1000
		val endDay = nextDate.toMillis() / 1000
		return resultRepository.getDayRating(startDay, endDay, RATING_DAY_LIMIT)
			.mapIndexed { index, entity -> ratingMapper.fromEntityToModel(entity, index) }
	}

	private fun getMonthRating(year: Int, month: Int): List<RatingRow> {
		return scoreRepository.findAllByIdPeriod(year * 100 + month)
			.map { ratingMapper.fromEntityToModel(it) }
			.take(RATING_MONTH_LIMIT)
	}

	private fun getYearRating(year: Int): List<RatingRow> {
		return scoreRepository.findAllByIdPeriod(year)
			.map { ratingMapper.fromEntityToModel(it) }
			.take(RATING_YEAR_LIMIT)
	}

	private fun LocalDateTime.toMillis(): Long {
		return this.toInstant(ZoneOffset.UTC).toEpochMilli()
	}
}