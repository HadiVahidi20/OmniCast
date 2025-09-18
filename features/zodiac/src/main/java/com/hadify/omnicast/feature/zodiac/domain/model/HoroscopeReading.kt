package com.hadify.omnicast.feature.zodiac.domain.model

import java.time.LocalDate

/**
 * Domain model for a horoscope reading
 */
data class HoroscopeReading(
    val id: String,
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
    val intensity: Float = 0.5f // 0.0 to 1.0 scale for reading strength
) {
    /**
     * Get a summary of the horoscope reading
     */
    fun getSummary(): String {
        return "Today brings ${mood.lowercase()} energy for ${zodiacSign.displayName}. $general"
    }

    /**
     * Check if this is today's reading
     */
    fun isToday(): Boolean {
        return date == LocalDate.now()
    }

    /**
     * Get reading for specific category
     */
    fun getReadingForCategory(category: HoroscopeCategory): String {
        return when (category) {
            HoroscopeCategory.GENERAL -> general
            HoroscopeCategory.LOVE -> love
            HoroscopeCategory.CAREER -> career
            HoroscopeCategory.HEALTH -> health
        }
    }
}

/**
 * Categories of horoscope readings
 */
enum class HoroscopeCategory(val displayName: String) {
    GENERAL("General"),
    LOVE("Love & Relationships"),
    CAREER("Career & Finance"),
    HEALTH("Health & Wellness")
}

/**
 * Weekly horoscope reading
 */
data class WeeklyHoroscopeReading(
    val id: String,
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
) {
    /**
     * Check if this week contains today
     */
    fun isCurrentWeek(): Boolean {
        val today = LocalDate.now()
        return !today.isBefore(weekStartDate) && !today.isAfter(weekEndDate)
    }
}