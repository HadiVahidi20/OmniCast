package com.hadify.omnicast.core.domain.model

/**
 * App settings domain model
 * Contains all user preferences and app configuration settings
 */
data class AppSettings(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val language: String = "en", // ISO language code
    val notificationsEnabled: Boolean = true,
    val dailyReminderTime: String? = null, // Format: "HH:mm" e.g., "09:00"

    // Advanced settings
    val animationsEnabled: Boolean = true,
    val soundEnabled: Boolean = true,
    val hapticFeedbackEnabled: Boolean = true,

    // Privacy settings
    val analyticsEnabled: Boolean = false,
    val crashReportingEnabled: Boolean = true,

    // Feature flags
    val betaFeaturesEnabled: Boolean = false
) {
    companion object {
        /**
         * Default app settings for new installations
         */
        val DEFAULT = AppSettings()

        /**
         * Available languages supported by the app
         */
        val SUPPORTED_LANGUAGES = listOf("en", "fa", "es", "fr", "de")

        /**
         * Validates if a time string is in correct format
         */
        fun isValidReminderTime(time: String?): Boolean {
            if (time == null) return true
            val timeRegex = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$".toRegex()
            return timeRegex.matches(time)
        }
    }

    /**
     * Validates the current settings
     */
    fun isValid(): Boolean {
        return language in SUPPORTED_LANGUAGES &&
                isValidReminderTime(dailyReminderTime)
    }

    /**
     * Creates a copy with validation
     */
    fun copyWithValidation(
        themeMode: ThemeMode = this.themeMode,
        language: String = this.language,
        notificationsEnabled: Boolean = this.notificationsEnabled,
        dailyReminderTime: String? = this.dailyReminderTime,
        animationsEnabled: Boolean = this.animationsEnabled,
        soundEnabled: Boolean = this.soundEnabled,
        hapticFeedbackEnabled: Boolean = this.hapticFeedbackEnabled,
        analyticsEnabled: Boolean = this.analyticsEnabled,
        crashReportingEnabled: Boolean = this.crashReportingEnabled,
        betaFeaturesEnabled: Boolean = this.betaFeaturesEnabled
    ): AppSettings {
        val newSettings = AppSettings(
            themeMode = themeMode,
            language = language,
            notificationsEnabled = notificationsEnabled,
            dailyReminderTime = dailyReminderTime,
            animationsEnabled = animationsEnabled,
            soundEnabled = soundEnabled,
            hapticFeedbackEnabled = hapticFeedbackEnabled,
            analyticsEnabled = analyticsEnabled,
            crashReportingEnabled = crashReportingEnabled,
            betaFeaturesEnabled = betaFeaturesEnabled
        )

        return if (newSettings.isValid()) newSettings else this
    }
}