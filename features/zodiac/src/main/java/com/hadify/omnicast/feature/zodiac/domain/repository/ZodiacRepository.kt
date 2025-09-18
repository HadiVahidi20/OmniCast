package com.hadify.omnicast.feature.zodiac.domain.repository

import com.hadify.omnicast.feature.zodiac.domain.model.HoroscopeReading
import com.hadify.omnicast.feature.zodiac.domain.model.WeeklyHoroscopeReading
import com.hadify.omnicast.feature.zodiac.domain.model.ZodiacSign
import com.hadify.omnicast.core.data.util.Resource
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * Repository interface for zodiac-related operations
 */
interface ZodiacRepository {

    /**
     * Get daily horoscope for a specific zodiac sign and date
     */
    suspend fun getDailyHoroscope(
        zodiacSign: ZodiacSign,
        date: LocalDate = LocalDate.now()
    ): Flow<Resource<HoroscopeReading>>

    /**
     * Get weekly horoscope for a specific zodiac sign
     */
    suspend fun getWeeklyHoroscope(
        zodiacSign: ZodiacSign,
        startDate: LocalDate = LocalDate.now()
    ): Flow<Resource<WeeklyHoroscopeReading>>

    /**
     * Get horoscope reading by ID
     */
    suspend fun getHoroscopeById(id: String): Resource<HoroscopeReading?>

    /**
     * Get compatibility analysis between two zodiac signs
     */
    suspend fun getCompatibilityReading(
        sign1: ZodiacSign,
        sign2: ZodiacSign
    ): Resource<String>

    /**
     * Cache horoscope reading locally
     */
    suspend fun cacheHoroscope(reading: HoroscopeReading): Resource<Unit>

    /**
     * Get cached horoscopes for a zodiac sign
     */
    fun getCachedHoroscopes(zodiacSign: ZodiacSign): Flow<List<HoroscopeReading>>

    /**
     * Clear old cached horoscopes (older than specified days)
     */
    suspend fun clearOldCache(daysToKeep: Int = 30): Resource<Unit>

    /**
     * Check if fresh horoscope data is available for today
     */
    suspend fun hasTodaysHoroscope(zodiacSign: ZodiacSign): Boolean
}