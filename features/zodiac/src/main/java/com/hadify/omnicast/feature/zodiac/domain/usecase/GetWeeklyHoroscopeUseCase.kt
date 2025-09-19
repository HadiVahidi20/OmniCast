package com.hadify.omnicast.feature.zodiac.domain.usecase

import com.hadify.omnicast.feature.zodiac.domain.model.WeeklyHoroscopeReading
import com.hadify.omnicast.feature.zodiac.domain.model.ZodiacSign
import com.hadify.omnicast.feature.zodiac.domain.repository.ZodiacRepository
import javax.inject.Inject

class GetWeeklyHoroscopeUseCase @Inject constructor(
    private val zodiacRepository: ZodiacRepository
) {
    suspend operator fun invoke(sign: ZodiacSign): Result<WeeklyHoroscopeReading> {
        return zodiacRepository.getWeeklyHoroscope(sign)
    }
}