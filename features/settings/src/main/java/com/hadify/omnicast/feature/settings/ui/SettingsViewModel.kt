package com.hadify.omnicast.feature.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadify.omnicast.core.domain.model.AppSettings
import com.hadify.omnicast.core.domain.model.ThemeMode
import com.hadify.omnicast.core.domain.usecase.settings.GetAppSettingsUseCase
import com.hadify.omnicast.core.domain.usecase.settings.UpdateAppSettingsUseCase
import com.hadify.omnicast.core.ui.theme.ThemeManager
import com.hadify.omnicast.core.ui.theme.ThemeSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for SettingsScreen
 * Manages app settings and preferences
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getAppSettingsUseCase: GetAppSettingsUseCase,
    private val updateAppSettingsUseCase: UpdateAppSettingsUseCase,
    private val themeManager: ThemeManager
) : ViewModel() {

    // UI State
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    /**
     * Load current app settings
     */
    private fun loadSettings() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                getAppSettingsUseCase().collect { appSettings ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        appSettings = appSettings,
                        selectedThemeMode = appSettings.themeMode,
                        selectedLanguage = appSettings.language,
                        notificationsEnabled = appSettings.notificationsEnabled,
                        dailyReminderTime = appSettings.dailyReminderTime,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to load settings: ${e.message}"
                )
            }
        }
    }

    /**
     * Update theme mode
     */
    fun updateThemeMode(themeMode: ThemeMode) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isSaving = true, error = null)

                val currentSettings = _uiState.value.appSettings
                val updatedSettings = currentSettings.copy(themeMode = themeMode)

                updateAppSettingsUseCase(updatedSettings)

                // Also update the theme manager
                val themeSource = when (themeMode) {
                    ThemeMode.SYSTEM -> ThemeSource.SYSTEM
                    ThemeMode.LIGHT -> ThemeSource.SYSTEM
                    ThemeMode.DARK -> ThemeSource.SYSTEM
                    ThemeMode.ZODIAC_BASED -> ThemeSource.ZODIAC
                    ThemeMode.BIORHYTHM_BASED -> ThemeSource.BIORHYTHM
                    ThemeMode.SEASONAL -> ThemeSource.SEASONAL  // FIXED: Added missing SEASONAL case
                }
                themeManager.setThemeSource(themeSource)

                _uiState.value = _uiState.value.copy(
                    selectedThemeMode = themeMode,
                    isSaving = false,
                    showSuccess = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    error = "Failed to update theme: ${e.message}"
                )
            }
        }
    }

    /**
     * Update language
     */
    fun updateLanguage(language: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isSaving = true, error = null)

                val currentSettings = _uiState.value.appSettings
                val updatedSettings = currentSettings.copy(language = language)

                updateAppSettingsUseCase(updatedSettings)

                _uiState.value = _uiState.value.copy(
                    selectedLanguage = language,
                    isSaving = false,
                    showSuccess = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    error = "Failed to update language: ${e.message}"
                )
            }
        }
    }

    /**
     * Toggle notifications
     */
    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isSaving = true, error = null)

                val currentSettings = _uiState.value.appSettings
                val updatedSettings = currentSettings.copy(notificationsEnabled = enabled)

                updateAppSettingsUseCase(updatedSettings)

                _uiState.value = _uiState.value.copy(
                    notificationsEnabled = enabled,
                    isSaving = false,
                    showSuccess = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    error = "Failed to update notifications: ${e.message}"
                )
            }
        }
    }

    /**
     * Update daily reminder time
     */
    fun updateDailyReminderTime(time: String?) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isSaving = true, error = null)

                val currentSettings = _uiState.value.appSettings
                val updatedSettings = currentSettings.copy(dailyReminderTime = time)

                updateAppSettingsUseCase(updatedSettings)

                _uiState.value = _uiState.value.copy(
                    dailyReminderTime = time,
                    isSaving = false,
                    showSuccess = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    error = "Failed to update reminder time: ${e.message}"
                )
            }
        }
    }

    /**
     * Clear success message
     */
    fun clearSuccess() {
        _uiState.value = _uiState.value.copy(showSuccess = false)
    }

    /**
     * Clear error message
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    /**
     * Clear app cache (placeholder for future implementation)
     */
    fun clearCache() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isSaving = true, error = null)

                // TODO: Implement actual cache clearing logic
                // This could clear Room database cache, image cache, etc.
                kotlinx.coroutines.delay(500) // Simulate clearing operation

                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    showSuccess = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    error = "Failed to clear cache: ${e.message}"
                )
            }
        }
    }
}

/**
 * UI State for SettingsScreen
 */
data class SettingsUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val appSettings: AppSettings = AppSettings(),
    val selectedThemeMode: ThemeMode = ThemeMode.SYSTEM,
    val selectedLanguage: String = "en",
    val notificationsEnabled: Boolean = true,
    val dailyReminderTime: String? = null,
    val error: String? = null,
    val showSuccess: Boolean = false
)

/**
 * Available languages in the app
 */
enum class AppLanguage(val code: String, val displayName: String, val nativeName: String) {
    ENGLISH("en", "English", "English"),
    PERSIAN("fa", "Persian", "فارسی"),
    SPANISH("es", "Spanish", "Español")
}