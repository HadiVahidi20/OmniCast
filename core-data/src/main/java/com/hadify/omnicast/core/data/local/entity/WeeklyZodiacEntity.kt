package com.hadify.omnicast.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hadify.omnicast.core.domain.model.ZodiacElement
import com.hadify.omnicast.core.domain.model.ZodiacQuality
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Room entity for storing weekly zodiac horoscope readings
 * This stores weekly horoscope predictions in the local database
 */
@Entity(tableName = "weekly_horoscope_readings")
data class WeeklyZodiacEntity(
    @PrimaryKey
    val id: String, // Primary key for the weekly horoscope reading

    val zodiacSignName: String, // e.g., "ARIES", "TAURUS"
    val weekStartDate: LocalDate, // Monday of the week this horoscope covers
    val weekEndDate: LocalDate, // Sunday of the week this horoscope covers
    val year: Int,
    val weekNumber: Int, // Week number in the year

    // Weekly horoscope content
    val weeklyOverview: String, // General overview for the week
    val mondayFocus: String, // Monday's focus area
    val wednesdayHighlight: String, // Mid-week highlight
    val fridayFinish: String, // How to finish the week strong
    val weekendVibes: String, // Weekend energy and activities

    // Weekly predictions by category
    val loveWeekly: String,
    val careerWeekly: String,
    val healthWeekly: String,
    val financialWeekly: String,

    // Weekly lucky elements
    val luckyDays: String, // JSON string of lucky days list
    val luckyColors: String, // JSON string of lucky colors for the week
    val luckyNumbers: String, // JSON string of lucky numbers list
    val avoidDays: String, // JSON string of days to be cautious

    // Energy and themes
    val weeklyTheme: String, // Main theme for the week
    val energyLevel: Float = 0.5f, // 0.0 to 1.0 - overall energy for the week
    val challengeAreas: String, // JSON string of areas that may be challenging
    val opportunityAreas: String, // JSON string of areas with opportunities

    // Zodiac sign properties (for quick access)
    val symbol: String,
    val element: ZodiacElement,
    val quality: ZodiacQuality,
    val rulingPlanet: String,

    // Metadata
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)