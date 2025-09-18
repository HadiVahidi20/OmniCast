package com.hadify.omnicast.core.ui.theme

import com.hadify.omnicast.core.domain.repository.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manager class for handling theme state and operations
 * Fixed to work properly with Compose and system theme detection
 */
@Singleton
class ThemeManager @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val _currentTheme = MutableStateFlow(ThemeModel.DEFAULT)
    val currentTheme: StateFlow<ThemeModel> = _currentTheme.asStateFlow()

    // Track system dark mode state
    private val _isSystemDarkMode = MutableStateFlow(false)
    val isSystemDarkMode: StateFlow<Boolean> = _isSystemDarkMode.asStateFlow()

    init {
        // Initialize theme from saved preferences
        coroutineScope.launch {
            settingsRepository.getThemeMode().collect { themeMode ->
                val newTheme = when (themeMode) {
                    "SYSTEM" -> createSystemTheme(_isSystemDarkMode.value)
                    "ZODIAC" -> createZodiacTheme(_isSystemDarkMode.value)
                    "BIORHYTHM" -> createBiorhythmTheme(_isSystemDarkMode.value)
                    "SEASONAL" -> createSeasonalTheme(_isSystemDarkMode.value)
                    else -> ThemeModel.DEFAULT
                }
                _currentTheme.value = newTheme
            }
        }
    }

    /**
     * Update system dark mode state from Composable context
     * Call this from your Composable to sync system theme
     */
    fun updateSystemDarkMode(isDark: Boolean) {
        if (_isSystemDarkMode.value != isDark) {
            _isSystemDarkMode.value = isDark

            // If current theme is system-based, update it
            val currentTheme = _currentTheme.value
            if (currentTheme.source == ThemeSource.SYSTEM) {
                coroutineScope.launch {
                    _currentTheme.value = createSystemTheme(isDark)
                }
            }
        }
    }

    /**
     * Updates the current theme
     */
    suspend fun updateTheme(themeModel: ThemeModel) {
        _currentTheme.value = themeModel
        settingsRepository.setThemeMode(themeModel.source.name)
    }

    /**
     * Sets the theme source and generates appropriate theme
     */
    suspend fun setThemeSource(source: ThemeSource) {
        val isDark = _isSystemDarkMode.value
        val newTheme = when (source) {
            ThemeSource.SYSTEM -> createSystemTheme(isDark)
            ThemeSource.ZODIAC -> createZodiacTheme(isDark)
            ThemeSource.BIORHYTHM -> createBiorhythmTheme(isDark)
            ThemeSource.SEASONAL -> createSeasonalTheme(isDark)
            ThemeSource.USER_PREFERENCE -> currentTheme.value.copy(source = source)
        }
        updateTheme(newTheme)
    }

    /**
     * Creates theme based on system light/dark setting
     */
    private fun createSystemTheme(isDark: Boolean): ThemeModel {
        return ThemeModel(
            id = "system_${if (isDark) "dark" else "light"}",
            name = if (isDark) "System Dark" else "System Light",
            source = ThemeSource.SYSTEM,
            isDark = isDark
        )
    }

    /**
     * Creates theme based on user's zodiac sign
     * NOTE: This will be enhanced by Developer 2 when user profile is implemented
     */
    private fun createZodiacTheme(isDark: Boolean): ThemeModel {
        // For now, return a default fire element theme
        // Developer 2 will integrate this with actual user birthdate
        return ThemeModel(
            id = "zodiac_fire",
            name = "Fire Element Theme",
            source = ThemeSource.ZODIAC,
            isDark = isDark,
            zodiacElement = ZodiacElement.FIRE
        )
    }

    /**
     * Creates theme based on user's current biorhythm
     * NOTE: This will be enhanced by Developer 3 when biorhythm is implemented
     */
    private fun createBiorhythmTheme(isDark: Boolean): ThemeModel {
        // For now, return a balanced energy theme
        // Developer 3 will integrate this with actual biorhythm calculations
        val energyLevel = EnergyLevel(
            physical = 0.7f,
            emotional = 0.6f,
            intellectual = 0.8f
        )

        return ThemeModel(
            id = "biorhythm_balanced",
            name = "Balanced Energy Theme",
            source = ThemeSource.BIORHYTHM,
            isDark = isDark,
            energyLevel = energyLevel
        )
    }

    /**
     * Creates theme based on current season or special events
     */
    private fun createSeasonalTheme(isDark: Boolean): ThemeModel {
        val today = LocalDate.now()
        val month = today.monthValue

        // Define seasonal periods
        val season = when {
            month in 3..5 -> "spring"
            month in 6..8 -> "summer"
            month in 9..11 -> "autumn"
            else -> "winter"
        }

        val themeName = when (season) {
            "spring" -> "Spring Renewal"
            "summer" -> "Summer Vibrance"
            "autumn" -> "Autumn Reflection"
            "winter" -> "Winter Serenity"
            else -> "Seasonal Theme"
        }

        return ThemeModel(
            id = "seasonal_$season",
            name = themeName,
            source = ThemeSource.SEASONAL,
            isDark = isDark
        )
    }
}