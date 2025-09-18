package com.hadify.omnicast.core.ui.theme

import androidx.compose.ui.graphics.Color
import java.util.UUID

/**
 * Data model representing a theme configuration in OmniCast
 */
data class ThemeModel(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val source: ThemeSource,
    val isDark: Boolean,
    val zodiacElement: ZodiacElement? = null,
    val energyLevel: EnergyLevel? = null,
    val customColorScheme: CustomColorScheme? = null
) {
    companion object {
        val DEFAULT = ThemeModel(
            id = "system_default",
            name = "System Default",
            source = ThemeSource.SYSTEM,
            isDark = false
        )
    }
}

/**
 * Enum defining the possible sources for theme selection
 */
enum class ThemeSource {
    SYSTEM,         // Based on system light/dark mode
    ZODIAC,         // Based on user's zodiac sign
    BIORHYTHM,      // Based on user's biorhythm state
    SEASONAL,       // Based on calendar season or special events
    USER_PREFERENCE // Manually selected by user
}

/**
 * Zodiac elements used for theme generation
 */
enum class ZodiacElement {
    FIRE,   // Aries, Leo, Sagittarius
    EARTH,  // Taurus, Virgo, Capricorn
    AIR,    // Gemini, Libra, Aquarius
    WATER   // Cancer, Scorpio, Pisces
}

/**
 * Energy levels used for biorhythm-based themes
 */
data class EnergyLevel(
    val physical: Float,    // 0.0-1.0 scale
    val emotional: Float,   // 0.0-1.0 scale
    val intellectual: Float // 0.0-1.0 scale
)

/**
 * Custom color scheme for user-defined themes
 */
data class CustomColorScheme(
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
    val background: Color
)