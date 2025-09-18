package com.hadify.omnicast.core.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * DataStore for user preferences
 * Handles storage of app settings, theme preferences, etc.
 */
@Singleton
class UserPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    companion object {
        private val THEME_MODE_KEY = stringPreferencesKey("theme_mode")
        private val LANGUAGE_KEY = stringPreferencesKey("language")
        private val NOTIFICATIONS_ENABLED_KEY = booleanPreferencesKey("notifications_enabled")
        private val DAILY_REMINDER_TIME_KEY = stringPreferencesKey("daily_reminder_time")
        private val FIRST_LAUNCH_KEY = booleanPreferencesKey("first_launch")
        private val USER_NAME_KEY = stringPreferencesKey("user_name")
        private val USER_BIRTHDATE_KEY = stringPreferencesKey("user_birthdate")
    }

    // Theme preferences
    val themeMode: Flow<String> = dataStore.data.map { preferences ->
        preferences[THEME_MODE_KEY] ?: "SYSTEM"
    }

    suspend fun setThemeMode(themeMode: String) {
        dataStore.edit { preferences ->
            preferences[THEME_MODE_KEY] = themeMode
        }
    }

    // Language preferences
    val language: Flow<String> = dataStore.data.map { preferences ->
        preferences[LANGUAGE_KEY] ?: "en"
    }

    suspend fun setLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = language
        }
    }

    // Notification preferences
    val notificationsEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[NOTIFICATIONS_ENABLED_KEY] ?: true
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[NOTIFICATIONS_ENABLED_KEY] = enabled
        }
    }

    // Daily reminder time
    val dailyReminderTime: Flow<String?> = dataStore.data.map { preferences ->
        preferences[DAILY_REMINDER_TIME_KEY]
    }

    suspend fun setDailyReminderTime(time: String?) {
        dataStore.edit { preferences ->
            if (time != null) {
                preferences[DAILY_REMINDER_TIME_KEY] = time
            } else {
                preferences.remove(DAILY_REMINDER_TIME_KEY)
            }
        }
    }

    // First launch detection
    val isFirstLaunch: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[FIRST_LAUNCH_KEY] ?: true
    }

    suspend fun setFirstLaunchComplete() {
        dataStore.edit { preferences ->
            preferences[FIRST_LAUNCH_KEY] = false
        }
    }

    // Basic user info (for quick access)
    val userName: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USER_NAME_KEY]
    }

    suspend fun setUserName(name: String?) {
        dataStore.edit { preferences ->
            if (name != null) {
                preferences[USER_NAME_KEY] = name
            } else {
                preferences.remove(USER_NAME_KEY)
            }
        }
    }

    val userBirthdate: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USER_BIRTHDATE_KEY]
    }

    suspend fun setUserBirthdate(birthdate: String?) {
        dataStore.edit { preferences ->
            if (birthdate != null) {
                preferences[USER_BIRTHDATE_KEY] = birthdate
            } else {
                preferences.remove(USER_BIRTHDATE_KEY)
            }
        }
    }
}