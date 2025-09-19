package com.hadify.omnicast.core.data.util

/**
 * Application-wide constants
 * Contains all constant values used across the application
 */
object Constants {

    // Database
    const val DATABASE_NAME = "omnicast_db"
    const val DATABASE_VERSION = 1

    // DataStore
    const val USER_PREFERENCES_NAME = "user_preferences"
    const val THEME_PREFERENCES_NAME = "theme_preferences"
    const val LOCALE_PREFERENCES_NAME = "locale_preferences"

    // Asset Paths
    const val ZODIAC_CONTENT_PATH = "content/en/zodiac.json"
    const val TAROT_CONTENT_PATH = "content/en/tarot.json"
    const val NUMEROLOGY_CONTENT_PATH = "content/en/numerology.json"
    const val BIORHYTHM_CONTENT_PATH = "content/en/biorhythm.json"
    const val ICHING_CONTENT_PATH = "content/en/iching.json"
    const val RUNE_CONTENT_PATH = "content/en/runes.json"
    const val COFFEE_CONTENT_PATH = "content/en/coffee.json"
    const val ABJAD_CONTENT_PATH = "content/en/abjad.json"

    // Localized Asset Paths
    const val CONTENT_BASE_PATH = "content"
    const val DEFAULT_LOCALE = "en"

    // Cache Settings
    const val HOROSCOPE_CACHE_DAYS = 30
    const val WEEKLY_HOROSCOPE_CACHE_WEEKS = 8
    const val CONTENT_CACHE_HOURS = 24

    // UI Constants
    const val ANIMATION_DURATION_SHORT = 200L
    const val ANIMATION_DURATION_MEDIUM = 400L
    const val ANIMATION_DURATION_LONG = 600L

    // Prediction Settings
    const val MAX_DAILY_PREDICTIONS = 5
    const val BIORHYTHM_CYCLE_PHYSICAL = 23
    const val BIORHYTHM_CYCLE_EMOTIONAL = 28
    const val BIORHYTHM_CYCLE_INTELLECTUAL = 33

    // Notification Settings
    const val DEFAULT_NOTIFICATION_HOUR = 9
    const val DEFAULT_NOTIFICATION_MINUTE = 0
    const val NOTIFICATION_CHANNEL_ID = "omnicast_daily_predictions"
    const val NOTIFICATION_CHANNEL_NAME = "Daily Predictions"

    // App Settings
    const val MIN_APP_VERSION = 1
    const val CURRENT_APP_VERSION = 1

    // Firebase Remote Config Keys
    const val REMOTE_CONFIG_FEATURE_FLAGS = "feature_flags"
    const val REMOTE_CONFIG_CONTENT_VERSION = "content_version"
    const val REMOTE_CONFIG_MIN_APP_VERSION = "min_app_version"

    // Error Messages
    const val ERROR_NETWORK_UNAVAILABLE = "Network unavailable"
    const val ERROR_CONTENT_LOADING_FAILED = "Failed to load content"
    const val ERROR_DATABASE_ERROR = "Database error occurred"
    const val ERROR_UNKNOWN = "Unknown error occurred"
}