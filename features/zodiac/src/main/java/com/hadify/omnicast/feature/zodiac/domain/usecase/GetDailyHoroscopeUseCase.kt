package com.hadify.omnicast.feature.zodiac.domain.usecase

import com.hadify.omnicast.feature.zodiac.domain.model.HoroscopeReading
import com.hadify.omnicast.feature.zodiac.domain.model.ZodiacSign
import com.hadify.omnicast.feature.zodiac.domain.repository.ZodiacRepository
import com.hadify.omnicast.core.domain.usecase.BaseUseCase
import com.hadify.omnicast.core.data.util.Resource
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

/**
 * Use case to get daily horoscope reading
 */
class GetDailyHoroscopeUseCase @Inject constructor(
    private val zodiacRepository: ZodiacRepository
) : BaseUseCase<GetDailyHoroscopeUseCase.Params, Flow<Resource<HoroscopeReading>>> {

    /**
     * Get daily horoscope for specified parameters
     */
    override suspend fun invoke(params: Params): Flow<Resource<HoroscopeReading>> {
        return zodiacRepository.getDailyHoroscope(
            zodiacSign = params.zodiacSign,
            date = params.date
        )
    }

    /**
     * Parameters for getting daily horoscope
     */
    data class Params(
        val zodiacSign: ZodiacSign,
        val date: LocalDate = LocalDate.now()
    )
}

/**
 * Use case to get horoscope from user's birthdate
 */
class GetUserDailyHoroscopeUseCase @Inject constructor(
    private val zodiacRepository: ZodiacRepository,
    private val determineZodiacSignUseCase: DetermineZodiacSignUseCase
) : BaseUseCase<GetUserDailyHoroscopeUseCase.Params, Flow<Resource<HoroscopeReading>>> {

    /**
     * Get daily horoscope based on user's birthdate
     */
    override suspend fun invoke(params: Params): Flow<Resource<HoroscopeReading>> {
        // First determine the zodiac sign from birthdate
        val zodiacSign = determineZodiacSignUseCase(params.birthdate)

        // Then get the horoscope for that sign
        return zodiacRepository.getDailyHoroscope(
            zodiacSign = zodiacSign,
            date = params.date
        )
    }

    /**
     * Parameters for getting user's daily horoscope
     */
    data class Params(
        val birthdate: LocalDate,
        val date: LocalDate = LocalDate.now()
    )
}

/**
 * Use case to get multiple days of horoscopes
 */
class GetHoroscopeHistoryUseCase @Inject constructor(
    private val zodiacRepository: ZodiacRepository
) : BaseUseCase<GetHoroscopeHistoryUseCase.Params, Flow<Resource<List<HoroscopeReading>>>> {

    override suspend fun invoke(params: Params): Flow<Resource<List<HoroscopeReading>>> {
        // This will be implemented when we have the repository implementation
        // For now, return empty flow
        return kotlinx.coroutines.flow.flow {
            emit(Resource.Success(emptyList()))
        }
    }

    data class Params(
        val zodiacSign: ZodiacSign,
        val startDate: LocalDate,
        val endDate: LocalDate
    )
}