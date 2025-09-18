package com.hadify.omnicast.core.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Basic user information domain model
 * Will be extended by Developer 2 in the profile feature
 */
data class BasicUser(
    val id: String,
    val name: String,
    val birthdate: LocalDate,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

/**
 * Common prediction result structure
 * Can be used by all divination features
 */
data class PredictionResult(
    val id: String,
    val type: PredictionType,
    val title: String,
    val description: String,
    val date: LocalDate,
    val tags: List<String> = emptyList(),
    val accuracy: Float? = null // User-rated accuracy (0.0 to 1.0)
)

/**
 * Types of predictions supported by the app
 */
enum class PredictionType {
    ZODIAC,
    BIORHYTHM,
    TAROT,
    NUMEROLOGY,
    I_CHING,
    ABJAD,
    RUNE,
    COFFEE,
    OVERVIEW
}

/**
 * App settings domain model
 */
data class AppSettings(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val language: String = "en",
    val notificationsEnabled: Boolean = true,
    val dailyReminderTime: String? = null // Format: "HH:mm"
)

/**
 * Theme mode options
 */
enum class ThemeMode {
    LIGHT,
    DARK,
    SYSTEM,
    ZODIAC_BASED,
    BIORHYTHM_BASED
}