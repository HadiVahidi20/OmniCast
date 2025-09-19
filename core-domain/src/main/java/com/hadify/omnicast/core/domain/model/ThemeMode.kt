package com.hadify.omnicast.core.domain.model

/**
 * Theme mode options for the application
 * Determines how the app's visual theme is selected and applied
 */
enum class ThemeMode(
    val displayName: String,
    val description: String
) {
    /**
     * Light theme - always use light colors
     */
    LIGHT(
        displayName = "Light",
        description = "Always use light theme"
    ),

    /**
     * Dark theme - always use dark colors
     */
    DARK(
        displayName = "Dark",
        description = "Always use dark theme"
    ),

    /**
     * System theme - follow system light/dark mode setting
     */
    SYSTEM(
        displayName = "System",
        description = "Follow system light/dark mode setting"
    ),

    /**
     * Zodiac-based theme - colors based on user's zodiac sign
     */
    ZODIAC_BASED(
        displayName = "Zodiac",
        description = "Theme based on your zodiac sign"
    ),

    /**
     * Biorhythm-based theme - colors based on current biorhythm state
     */
    BIORHYTHM_BASED(
        displayName = "Biorhythm",
        description = "Theme based on your current biorhythm"
    ),

    /**
     * Seasonal theme - colors based on current season or special events
     */
    SEASONAL(
        displayName = "Seasonal",
        description = "Theme that changes with seasons and events"
    );

    companion object {
        /**
         * Default theme mode for new users
         */
        val DEFAULT = SYSTEM

        /**
         * Get all available theme modes
         */
        fun getAllModes(): List<ThemeMode> = values().toList()

        /**
         * Get theme modes that don't require user profile data
         */
        fun getBasicModes(): List<ThemeMode> = listOf(LIGHT, DARK, SYSTEM)

        /**
         * Get theme modes that require user profile data
         */
        fun getPersonalizedModes(): List<ThemeMode> = listOf(ZODIAC_BASED, BIORHYTHM_BASED)

        /**
         * Check if a theme mode requires user birthdate
         */
        fun ThemeMode.requiresBirthdate(): Boolean = when (this) {
            ZODIAC_BASED, BIORHYTHM_BASED -> true
            else -> false
        }

        /**
         * Check if a theme mode changes automatically
         */
        fun ThemeMode.isDynamic(): Boolean = when (this) {
            BIORHYTHM_BASED, SEASONAL -> true
            else -> false
        }

        /**
         * Get a safe fallback theme mode if user data is not available
         */
        fun ThemeMode.getFallback(): ThemeMode = when (this) {
            ZODIAC_BASED, BIORHYTHM_BASED -> SYSTEM
            else -> this
        }
    }
}