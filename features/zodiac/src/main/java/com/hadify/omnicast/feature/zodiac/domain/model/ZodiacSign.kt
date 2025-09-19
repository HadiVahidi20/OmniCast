// ZodiacSign.kt
package com.hadify.omnicast.feature.zodiac.domain.model

import java.time.LocalDate

/**
 * Zodiac Sign enumeration with all 12 zodiac signs
 */
enum class ZodiacSign(
    val displayName: String,
    val symbol: String,
    val element: ZodiacElement,
    val quality: ZodiacQuality,
    val rulingPlanet: String,
    val dateRange: String,
    val startMonth: Int,
    val startDay: Int,
    val endMonth: Int,
    val endDay: Int
) {
    ARIES(
        displayName = "Aries",
        symbol = "♈",
        element = ZodiacElement.FIRE,
        quality = ZodiacQuality.CARDINAL,
        rulingPlanet = "Mars",
        dateRange = "March 21 - April 19",
        startMonth = 3, startDay = 21,
        endMonth = 4, endDay = 19
    ),
    TAURUS(
        displayName = "Taurus",
        symbol = "♉",
        element = ZodiacElement.EARTH,
        quality = ZodiacQuality.FIXED,
        rulingPlanet = "Venus",
        dateRange = "April 20 - May 20",
        startMonth = 4, startDay = 20,
        endMonth = 5, endDay = 20
    ),
    GEMINI(
        displayName = "Gemini",
        symbol = "♊",
        element = ZodiacElement.AIR,
        quality = ZodiacQuality.MUTABLE,
        rulingPlanet = "Mercury",
        dateRange = "May 21 - June 20",
        startMonth = 5, startDay = 21,
        endMonth = 6, endDay = 20
    ),
    CANCER(
        displayName = "Cancer",
        symbol = "♋",
        element = ZodiacElement.WATER,
        quality = ZodiacQuality.CARDINAL,
        rulingPlanet = "Moon",
        dateRange = "June 21 - July 22",
        startMonth = 6, startDay = 21,
        endMonth = 7, endDay = 22
    ),
    LEO(
        displayName = "Leo",
        symbol = "♌",
        element = ZodiacElement.FIRE,
        quality = ZodiacQuality.FIXED,
        rulingPlanet = "Sun",
        dateRange = "July 23 - August 22",
        startMonth = 7, startDay = 23,
        endMonth = 8, endDay = 22
    ),
    VIRGO(
        displayName = "Virgo",
        symbol = "♍",
        element = ZodiacElement.EARTH,
        quality = ZodiacQuality.MUTABLE,
        rulingPlanet = "Mercury",
        dateRange = "August 23 - September 22",
        startMonth = 8, startDay = 23,
        endMonth = 9, endDay = 22
    ),
    LIBRA(
        displayName = "Libra",
        symbol = "♎",
        element = ZodiacElement.AIR,
        quality = ZodiacQuality.CARDINAL,
        rulingPlanet = "Venus",
        dateRange = "September 23 - October 22",
        startMonth = 9, startDay = 23,
        endMonth = 10, endDay = 22
    ),
    SCORPIO(
        displayName = "Scorpio",
        symbol = "♏",
        element = ZodiacElement.WATER,
        quality = ZodiacQuality.FIXED,
        rulingPlanet = "Mars/Pluto",
        dateRange = "October 23 - November 21",
        startMonth = 10, startDay = 23,
        endMonth = 11, endDay = 21
    ),
    SAGITTARIUS(
        displayName = "Sagittarius",
        symbol = "♐",
        element = ZodiacElement.FIRE,
        quality = ZodiacQuality.MUTABLE,
        rulingPlanet = "Jupiter",
        dateRange = "November 22 - December 21",
        startMonth = 11, startDay = 22,
        endMonth = 12, endDay = 21
    ),
    CAPRICORN(
        displayName = "Capricorn",
        symbol = "♑",
        element = ZodiacElement.EARTH,
        quality = ZodiacQuality.CARDINAL,
        rulingPlanet = "Saturn",
        dateRange = "December 22 - January 19",
        startMonth = 12, startDay = 22,
        endMonth = 1, endDay = 19
    ),
    AQUARIUS(
        displayName = "Aquarius",
        symbol = "♒",
        element = ZodiacElement.AIR,
        quality = ZodiacQuality.FIXED,
        rulingPlanet = "Saturn/Uranus",
        dateRange = "January 20 - February 18",
        startMonth = 1, startDay = 20,
        endMonth = 2, endDay = 18
    ),
    PISCES(
        displayName = "Pisces",
        symbol = "♓",
        element = ZodiacElement.WATER,
        quality = ZodiacQuality.MUTABLE,
        rulingPlanet = "Jupiter/Neptune",
        dateRange = "February 19 - March 20",
        startMonth = 2, startDay = 19,
        endMonth = 3, endDay = 20
    );

    companion object {
        /**
         * Determine zodiac sign from birthdate
         */
        fun fromBirthdate(birthdate: LocalDate): ZodiacSign {
            val month = birthdate.monthValue
            val day = birthdate.dayOfMonth

            return values().find { sign ->
                if (sign.startMonth <= sign.endMonth) {
                    // Normal date range (e.g., Taurus: April 20 - May 20)
                    (month == sign.startMonth && day >= sign.startDay) ||
                            (month == sign.endMonth && day <= sign.endDay) ||
                            (month > sign.startMonth && month < sign.endMonth)
                } else {
                    // Year-crossing date range (e.g., Capricorn: Dec 22 - Jan 19)
                    (month == sign.startMonth && day >= sign.startDay) ||
                            (month == sign.endMonth && day <= sign.endDay) ||
                            (month > sign.startMonth) || (month < sign.endMonth)
                }
            } ?: ARIES // Default fallback
        }
    }
}

/**
 * Zodiac Elements
 */
enum class ZodiacElement(val displayName: String) {
    FIRE("Fire"),
    EARTH("Earth"),
    AIR("Air"),
    WATER("Water")
}

/**
 * Zodiac Qualities
 */
enum class ZodiacQuality(val displayName: String) {
    CARDINAL("Cardinal"),
    FIXED("Fixed"),
    MUTABLE("Mutable")
}

// HoroscopeReading.kt
package com.hadify.omnicast.feature.zodiac.domain.model

import java.time.LocalDate
import java.util.*

/**
 * Daily Horoscope Reading domain model
 */
data class HoroscopeReading(
    val id: String = UUID.randomUUID().toString(),
    val zodiacSign: ZodiacSign,
    val date: LocalDate,
    val general: String,
    val love: String,
    val career: String,
    val health: String,
    val luckyNumber: Int,
    val luckyColor: String,
    val compatibility: ZodiacSign,
    val mood: String,
    val tags: List<String> = emptyList(),
    val intensity: Float = 0.5f // 0.0 to 1.0
)

// WeeklyHoroscopeReading.kt
package com.hadify.omnicast.feature.zodiac.domain.model

import java.time.LocalDate
import java.util.*

/**
 * Weekly Horoscope Reading domain model
 */
data class WeeklyHoroscopeReading(
    val id: String = UUID.randomUUID().toString(),
    val zodiacSign: ZodiacSign,
    val weekStartDate: LocalDate,
    val weekEndDate: LocalDate,
    val general: String,
    val love: String,
    val career: String,
    val health: String,
    val luckyDays: List<String>,
    val challengingDays: List<String>,
    val overallTrend: String,
    val tags: List<String> = emptyList()
)

// ZodiacCompatibility.kt
package com.hadify.omnicast.feature.zodiac.domain.model

/**
 * Zodiac compatibility model
 */
data class ZodiacCompatibility(
    val sign1: ZodiacSign,
    val sign2: ZodiacSign,
    val compatibilityScore: Float, // 0.0 to 1.0
    val description: String,
    val strengths: List<String>,
    val challenges: List<String>
)