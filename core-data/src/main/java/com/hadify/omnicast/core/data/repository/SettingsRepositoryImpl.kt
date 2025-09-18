package com.hadify.omnicast.core.data.repository

import com.hadify.omnicast.core.data.local.datastore.UserPreferences
import com.hadify.omnicast.core.domain.model.AppSettings
import com.hadify.omnicast.core.domain.model.ThemeMode
import com.hadify.omnicast.core.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of SettingsRepository using DataStore
 */
@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val userPreferences: UserPreferences
) : SettingsRepository {

    override fun getAppSettings(): Flow<AppSettings> {
        return combine(
            userPreferences.themeMode,
            userPreferences.language,
            userPreferences.notificationsEnabled,
            userPreferences.dailyReminderTime
        ) { themeMode, language, notificationsEnabled, dailyReminderTime ->
            AppSettings(
                themeMode = ThemeMode.valueOf(themeMode),
                language = language,
                notificationsEnabled = notificationsEnabled,
                dailyReminderTime = dailyReminderTime
            )
        }
    }

    override suspend fun updateAppSettings(settings: AppSettings) {
        userPreferences.setThemeMode(settings.themeMode.name)
        userPreferences.setLanguage(settings.language)
        userPreferences.setNotificationsEnabled(settings.notificationsEnabled)
        userPreferences.setDailyReminderTime(settings.dailyReminderTime)
    }

    override fun getThemeMode(): Flow<String> = userPreferences.themeMode

    override fun getLanguage(): Flow<String> = userPreferences.language

    override fun getNotificationsEnabled(): Flow<Boolean> = userPreferences.notificationsEnabled

    override fun getDailyReminderTime(): Flow<String?> = userPreferences.dailyReminderTime

    override suspend fun setThemeMode(themeMode: String) {
        userPreferences.setThemeMode(themeMode)
    }

    override suspend fun setLanguage(language: String) {
        userPreferences.setLanguage(language)
    }

    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        userPreferences.setNotificationsEnabled(enabled)
    }

    override suspend fun setDailyReminderTime(time: String?) {
        userPreferences.setDailyReminderTime(time)
    }
}