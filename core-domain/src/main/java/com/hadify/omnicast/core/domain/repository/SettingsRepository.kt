package com.hadify.omnicast.core.domain.repository

import com.hadify.omnicast.core.domain.model.AppSettings
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for app settings
 * Implementation will be provided by the data layer
 */
interface SettingsRepository {

    /**
     * Get current app settings as a Flow
     */
    fun getAppSettings(): Flow<AppSettings>

    /**
     * Update app settings
     */
    suspend fun updateAppSettings(settings: AppSettings)

    /**
     * Get individual setting values
     */
    fun getThemeMode(): Flow<String>
    fun getLanguage(): Flow<String>
    fun getNotificationsEnabled(): Flow<Boolean>
    fun getDailyReminderTime(): Flow<String?>

    /**
     * Update individual settings
     */
    suspend fun setThemeMode(themeMode: String)
    suspend fun setLanguage(language: String)
    suspend fun setNotificationsEnabled(enabled: Boolean)
    suspend fun setDailyReminderTime(time: String?)
}