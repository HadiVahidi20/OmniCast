package com.hadify.omnicast.core.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.hadify.omnicast.core.ui.theme.EnergyLevel
import com.hadify.omnicast.core.ui.theme.ThemeManager
import com.hadify.omnicast.core.ui.theme.ThemeModel
import com.hadify.omnicast.core.ui.theme.ThemeSource
import com.hadify.omnicast.core.ui.theme.ZodiacElement

// OmniCast brand colors - mystical and elegant
private val DeepPurple = Color(0xFF6A4C93)
private val MysticBlue = Color(0xFF4C7C9B)
private val StarGold = Color(0xFFE6B800)
private val MoonSilver = Color(0xFFB8BCC8)
private val DarkViolet = Color(0xFF2D1B47)
private val LightLavender = Color(0xFFE8E4F3)
private val CosmicTeal = Color(0xFF1B998B)
private val SunsetOrange = Color(0xFFFF7B54)

// Fire element colors
private val FireRed = Color(0xFFE74C3C)
private val FireOrange = Color(0xFFFF6B35)

// Earth element colors
private val EarthGreen = Color(0xFF27AE60)
private val EarthBrown = Color(0xFF8B4513)

// Air element colors
private val AirBlue = Color(0xFF3498DB)
private val AirSilver = Color(0xFFC0C0C0)

// Water element colors
private val WaterDeepBlue = Color(0xFF2C3E50)
private val WaterTeal = Color(0xFF16A085)

// Light theme colors
private val LightColorScheme = lightColorScheme(
    primary = DeepPurple,
    onPrimary = Color.White,
    primaryContainer = LightLavender,
    onPrimaryContainer = DarkViolet,

    secondary = MysticBlue,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD1E4F3),
    onSecondaryContainer = Color(0xFF1A365D),

    tertiary = CosmicTeal,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFB2F7EF),
    onTertiaryContainer = Color(0xFF002E26),

    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),

    background = Color(0xFFFFFBFE),
    onBackground = Color(0xFF1C1B1F),
    surface = Color(0xFFFFFBFE),
    onSurface = Color(0xFF1C1B1F),

    surfaceVariant = Color(0xFFE7E0EC),
    onSurfaceVariant = Color(0xFF49454F),
    outline = Color(0xFF79747E),
    outlineVariant = Color(0xFFCAC4D0),
)

// Dark theme colors
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFD0BCFF),
    onPrimary = DarkViolet,
    primaryContainer = Color(0xFF4F378B),
    onPrimaryContainer = Color(0xFFEADDFF),

    secondary = Color(0xFFCCC2DC),
    onSecondary = Color(0xFF332D41),
    secondaryContainer = Color(0xFF4A4458),
    onSecondaryContainer = Color(0xFFE8DEF8),

    tertiary = Color(0xFFEFB8C8),
    onTertiary = Color(0xFF492532),
    tertiaryContainer = Color(0xFF633B48),
    onTertiaryContainer = Color(0xFFFFD8E4),

    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),

    background = Color(0xFF1C1B1F),
    onBackground = Color(0xFFE6E1E5),
    surface = Color(0xFF1C1B1F),
    onSurface = Color(0xFFE6E1E5),

    surfaceVariant = Color(0xFF49454F),
    onSurfaceVariant = Color(0xFFCAC4D0),
    outline = Color(0xFF938F99),
    outlineVariant = Color(0xFF49454F),
)

/**
 * Main theme composable for OmniCast with dynamic theming support
 * Fixed to properly handle system theme detection
 */
@Composable
fun OmniCastTheme(
    themeManager: ThemeManager? = null,
    content: @Composable () -> Unit
) {
    // Detect system dark mode
    val isSystemDark = isSystemInDarkTheme()

    // Update theme manager with system state
    LaunchedEffect(isSystemDark) {
        themeManager?.updateSystemDarkMode(isSystemDark)
    }

    // Get current theme from manager or fallback to system
    val currentTheme by (themeManager?.currentTheme?.collectAsState()
        ?: run {
            androidx.compose.runtime.remember {
                kotlinx.coroutines.flow.MutableStateFlow(
                    ThemeModel.DEFAULT.copy(isDark = isSystemDark)
                )
            }.collectAsState()
        })

    val colorScheme = generateColorScheme(currentTheme)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = OmniCastTypography,
        content = content
    )
}

/**
 * Generates appropriate color scheme based on theme model
 */
private fun generateColorScheme(themeModel: ThemeModel): ColorScheme {
    return when (themeModel.source) {
        ThemeSource.SYSTEM -> {
            if (themeModel.isDark) DarkColorScheme else LightColorScheme
        }
        ThemeSource.ZODIAC -> {
            val element = themeModel.zodiacElement ?: ZodiacElement.FIRE
            getZodiacColorScheme(element, themeModel.isDark)
        }
        ThemeSource.BIORHYTHM -> {
            val energy = themeModel.energyLevel
            getBiorhythmColorScheme(energy, themeModel.isDark)
        }
        ThemeSource.SEASONAL -> {
            getSeasonalColorScheme(themeModel.id, themeModel.isDark)
        }
        ThemeSource.USER_PREFERENCE -> {
            // For now, use default scheme
            // This will be enhanced when custom color schemes are implemented
            if (themeModel.isDark) DarkColorScheme else LightColorScheme
        }
    }
}

/**
 * Creates zodiac element-based color scheme
 */
private fun getZodiacColorScheme(element: ZodiacElement, isDark: Boolean): ColorScheme {
    val baseScheme = if (isDark) DarkColorScheme else LightColorScheme

    return when (element) {
        ZodiacElement.FIRE -> baseScheme.copy(
            primary = if (isDark) FireOrange else FireRed,
            secondary = if (isDark) FireRed else FireOrange
        )
        ZodiacElement.EARTH -> baseScheme.copy(
            primary = if (isDark) EarthBrown else EarthGreen,
            secondary = if (isDark) EarthGreen else EarthBrown
        )
        ZodiacElement.AIR -> baseScheme.copy(
            primary = if (isDark) AirSilver else AirBlue,
            secondary = if (isDark) AirBlue else AirSilver
        )
        ZodiacElement.WATER -> baseScheme.copy(
            primary = if (isDark) WaterTeal else WaterDeepBlue,
            secondary = if (isDark) WaterDeepBlue else WaterTeal
        )
    }
}

/**
 * Creates biorhythm energy-based color scheme
 */
private fun getBiorhythmColorScheme(energyLevel: EnergyLevel?, isDark: Boolean): ColorScheme {
    val baseScheme = if (isDark) DarkColorScheme else LightColorScheme

    // If no energy level provided, use default
    if (energyLevel == null) return baseScheme

    // Adjust colors based on energy levels
    val intensity = (energyLevel.physical + energyLevel.emotional + energyLevel.intellectual) / 3f

    return if (intensity > 0.7f) {
        // High energy - more vibrant colors
        baseScheme.copy(primary = SunsetOrange)
    } else if (intensity < 0.3f) {
        // Low energy - more calming colors
        baseScheme.copy(primary = MysticBlue)
    } else {
        // Balanced energy - use default
        baseScheme
    }
}

/**
 * Creates seasonal color scheme
 */
private fun getSeasonalColorScheme(themeId: String, isDark: Boolean): ColorScheme {
    val baseScheme = if (isDark) DarkColorScheme else LightColorScheme

    return when {
        themeId.contains("spring") -> baseScheme.copy(primary = EarthGreen)
        themeId.contains("summer") -> baseScheme.copy(primary = SunsetOrange)
        themeId.contains("autumn") -> baseScheme.copy(primary = EarthBrown)
        themeId.contains("winter") -> baseScheme.copy(primary = AirBlue)
        else -> baseScheme
    }
}

/**
 * Preview versions of the theme for different scenarios
 */
@Composable
fun OmniCastThemeLight(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = OmniCastTypography,
        content = content
    )
}

@Composable
fun OmniCastThemeDark(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = OmniCastTypography,
        content = content
    )
}