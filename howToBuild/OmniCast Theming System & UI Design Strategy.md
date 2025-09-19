# OmniCast Theming System & UI Design Strategy

## 🎨 Theming Strategy

### 1. Dynamic Theming Implementation

OmniCast implements a multi-layered theming approach that allows themes to be determined by several factors:

#### Theme Sources

1. **System-Based Themes**
   - Light/Dark themes that follow the system preference
   - Automatic switching based on Android's day/night mode
   - User override option in settings

2. **Zodiac-Based Themes**
   - 12 unique themes based on zodiac signs
   - Color schemes derived from the elemental associations:
     - **Fire** (Aries, Leo, Sagittarius): Warm reds, oranges, and yellows
     - **Earth** (Taurus, Virgo, Capricorn): Grounded greens, browns, and soft yellows
     - **Air** (Gemini, Libra, Aquarius): Light blues, whites, and silvers
     - **Water** (Cancer, Scorpio, Pisces): Deep blues, purples, and sea greens
   - Each elemental group has light and dark variants

3. **Biorhythm-Driven Themes**
   - Dynamic themes that shift based on the user's calculated energy levels
   - Physical cycle (high energy): More vibrant, saturated colors
   - Physical cycle (low energy): More subdued, calming colors
   - Emotional cycle affects accent color warmth/coolness
   - Intellectual cycle influences contrast and typography weight

4. **User Preference Themes**
   - Base option that allows users to select their preferred theme manually
   - Overrides all automatic theme selection

#### Implementation Architecture

The theme system is implemented through a `ThemeModel` class that contains all necessary information:

```kotlin
data class ThemeModel(
    val source: ThemeSource,
    val isDark: Boolean,
    val zodiacElement: ZodiacElement? = null,
    val energyLevel: EnergyLevel? = null,
    val customColorScheme: CustomColorScheme? = null
)

enum class ThemeSource {
    SYSTEM, ZODIAC, BIORHYTHM, USER_PREFERENCE
}
```

### 2. Theme Management Tools & Techniques

The theming system utilizes several tools and techniques:

1. **ThemeManager**
   - Central coordinator for theme state
   - Exposes `currentTheme` as a Flow for reactive UI updates
   - Provides methods to update theme preferences
   - Handles persistence of theme selections

2. **DataStore for Persistence**
   - Uses Jetpack DataStore for storing theme preferences
   - Provides type-safe access to theme settings
   - Enables preference migration from SharedPreferences if needed

3. **Material 3 Dynamic Color Engine**
   - Leverages Material 3's dynamic color capabilities
   - Creates harmonious color schemes based on seed colors
   - Ensures accessibility with proper contrast ratios

4. **Custom Color Palettes**
   - Predefined zodiac element color palettes (Fire, Earth, Air, Water)
   - Biorhythm-based color intensity mappings
   - Season-specific color variations for special events

5. **Theme Composition**
   - `OmnicastTheme.kt` composable that wraps content with appropriate theme
   - Dynamically generates `ColorScheme` based on active theme
   - Applies appropriate Typography and Shape schemes

```kotlin
@Composable
fun OmnicastTheme(
    themeModel: ThemeModel = LocalThemeManager.current.currentTheme.collectAsState().value,
    content: @Composable () -> Unit
) {
    val colorScheme = when (themeModel.source) {
        ThemeSource.SYSTEM -> if (themeModel.isDark) DarkColorScheme else LightColorScheme
        ThemeSource.ZODIAC -> getZodiacColorScheme(themeModel.zodiacElement!!, themeModel.isDark)
        ThemeSource.BIORHYTHM -> getBiorhythmColorScheme(themeModel.energyLevel!!, themeModel.isDark)
        ThemeSource.USER_PREFERENCE -> themeModel.customColorScheme!!.toColorScheme(themeModel.isDark)
    }
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
```

### 3. Adding New Theme Variations

The theming system is designed for easy extension:

1. **Theme Extension Points**
   - New `ThemeSource` enum values can be added for additional sources
   - Color scheme generator functions follow a consistent pattern
   - Theme selector UI dynamically populates from available themes

2. **Adding a Seasonal Theme**
   - Create new color schemes in a `SeasonalThemes.kt` file
   - Add `SEASONAL` to the `ThemeSource` enum
   - Update `ThemeManager` to check for seasonal applicability
   - Add seasonal theme options to the theme selector

3. **Premium Theme Integration**
   - Create a `PremiumThemes.kt` file with exclusive themes
   - Add premium theme metadata with flag for premium status
   - Update theme selector to show premium indicators
   - Implement entitlement checking in `ThemeManager`

4. **User Color Profile Support**
   - Add color customization UI in settings
   - Create `UserColorProfile` data class for storing selections
   - Implement conversion to Material `ColorScheme`
   - Store custom themes in DataStore with unique IDs

## 🧩 UI & UX Design Principles

### 1. Design System Guidelines

OmniCast follows Material Design 3 principles with custom extensions:

1. **Material Design 3 Foundation**
   - Uses M3 components and elevation system
   - Implements dynamic color principles
   - Follows accessibility guidelines for touch targets

2. **Custom Design Extensions**
   - Mystic/divination-specific iconography
   - Specialized card layouts for prediction displays
   - Custom illustrations for divination methods

3. **Spacing and Layout System**
   - Consistent 8dp grid system for all spacing
   - Defined content padding (16dp standard)
   - Responsive layouts that adapt to different screen sizes

4. **Typography Hierarchy**
   - Clear heading structure with designated weights
   - Reading-optimized body text
   - Special display typography for prediction results

### 2. Reusable Components

The app includes several reusable components:

1. **Content Display Components**
   - `PredictionCard`: Standard card for displaying predictions
   - `DetailExpandableCard`: Expandable card for showing additional details
   - `ReadingResultCard`: Specialized layout for divination results

2. **Navigation & Structure Components**
   - `OmniCastAppBar`: Consistent top bar with theme-aware styling
   - `NavigationDrawer`: Side drawer for app navigation
   - `BottomNavBar`: Alternative navigation for frequent actions

3. **Input & Interaction Components**
   - `DateSelector`: Custom date picker for birthdate and calculation inputs
   - `ThemeSelector`: Visual selector for theme options
   - `FeatureCarousel`: Horizontally scrolling feature showcase

4. **Feedback & Status Components**
   - `LoadingIndicator`: Themed loading animation
   - `ErrorView`: Standardized error display with retry action
   - `SuccessAnimation`: Visual feedback for completed actions

### 3. User Journey Vision

The overall user journey is designed around a seamless, intuitive flow:

1. **Onboarding Experience**
   - Brief introduction to app capabilities
   - Essential profile information collection (birthdate for calculations)
   - Permission requests with clear explanations
   - Theme preference introduction

2. **Daily Core Experience**
   - Home screen with daily overview summary
   - Quick access to today's predictions
   - Prominent journal prompt based on predictions
   - Visual indicators of biorhythm status

3. **Feature Exploration**
   - Clear categorization of divination methods
   - Interactive introduction to each method
   - Consistent prediction display pattern
   - Related predictions and cross-references

4. **Personalization Path**
   - Profile completion incentives
   - Gradual theme customization discovery
   - Notification preferences
   - Data backup options (Firebase)

5. **Engagement Loops**
   - Daily notification for new predictions
   - Journal reflection prompts
   - Weekly summary of biorhythm patterns
   - Special occasion readings (birthdays, full moons)

## 💡 Future Considerations

### Accessibility Support

OmniCast is designed with comprehensive accessibility features:

1. **Visual Accessibility**
   - Dynamic font scaling that respects system settings
   - High contrast theme option for visibility
   - Color-blind friendly palettes with adequate contrast ratios
   - Content descriptions for all images and icons

2. **Interaction Accessibility**
   - Touch targets meeting minimum size requirements (48dp)
   - Keyboard navigation support for external devices
   - TalkBack compatibility with meaningful descriptions
   - Reduced motion option for animations

### Animation & Transitions

The animation system follows a cohesive strategy:

1. **Animation Principles**
   - Meaningful motion that enhances understanding
   - Consistent timing and easing curves
   - Purpose-driven animations rather than decorative
   - Performance-optimized implementations

2. **Key Animation Types**
   - Navigation transitions (slide, fade, shared element)
   - Content reveal animations (cards, predictions)
   - Interactive feedback (button presses, selections)
   - Celebratory animations (special readings, achievements)

3. **Technical Implementation**
   - Compose animation APIs for UI transitions
   - Lottie for complex illustrative animations
   - Animation duration and easing constants in `AnimationUtils`
   - Motion specification documentation

### Design Documentation

Design assets and documentation include:

1. **Design System Documentation**
   - Component specifications with usage guidelines
   - Color system with accessibility verification
   - Typography scale with reading optimization
   - Spacing and layout principles

2. **Screen Flows & Wireframes**
   - User journey maps for primary flows
   - Screen transition diagrams
   - Wireframes for all main screens
   - Interaction specifications

3. **Asset Management**
   - SVG icons for crisp rendering at all densities
   - Optimized illustrations for divination methods
   - Animation assets with performance considerations
   - Image optimization for offline package size

4. **Future Design Expansion**
   - Guidelines for adding new feature visualizations
   - Theme extension documentation
   - Accessibility checklist for new components
   - Animation guidelines for new interactions

## Code Implementation Examples

### Theme Model

```kotlin
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
    SYSTEM,      // Based on system light/dark mode
    ZODIAC,      // Based on user's zodiac sign
    BIORHYTHM,   // Based on user's biorhythm state
    SEASONAL,    // Based on calendar season or special events
    USER_PREFERENCE  // Manually selected by user
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
) {
    /**
     * Convert custom colors to Material ColorScheme
     */
    fun toColorScheme(isDark: Boolean): ColorScheme {
        return if (isDark) {
            darkColorScheme(
                primary = primary,
                secondary = secondary,
                tertiary = tertiary,
                background = background
                // Other color slots would be derived automatically
            )
        } else {
            lightColorScheme(
                primary = primary,
                secondary = secondary,
                tertiary = tertiary,
                background = background
                // Other color slots would be derived automatically
            )
        }
    }
}
```

### Theme Manager

```kotlin
/**
 * Manager class for handling theme state and operations
 */
@Singleton
class ThemeManager @Inject constructor(
    private val themeRepository: ThemeRepository,
    private val userRepository: UserRepository,
    private val biorhythmRepository: BiorhythmRepository
) {
    /**
     * Current theme as a StateFlow for reactive UI updates
     */
    val currentTheme: StateFlow<ThemeModel> = themeRepository.getCurrentTheme()
        .stateIn(
            scope = CoroutineScope(Dispatchers.Default + SupervisorJob()),
            started = SharingStarted.Eagerly,
            initialValue = ThemeModel.DEFAULT
        )
    
    /**
     * Updates the current theme
     */
    suspend fun updateTheme(themeModel: ThemeModel) {
        themeRepository.setCurrentTheme(themeModel)
    }
    
    /**
     * Sets the theme source and generates appropriate theme
     */
    suspend fun setThemeSource(source: ThemeSource) {
        val newTheme = when (source) {
            ThemeSource.SYSTEM -> createSystemTheme()
            ThemeSource.ZODIAC -> createZodiacTheme()
            ThemeSource.BIORHYTHM -> createBiorhythmTheme()
            ThemeSource.SEASONAL -> createSeasonalTheme()
            ThemeSource.USER_PREFERENCE -> currentTheme.value.copy(source = source)
        }
        updateTheme(newTheme)
    }
    
    /**
     * Creates theme based on system light/dark setting
     */
    private fun createSystemTheme(): ThemeModel {
        val isDark = isSystemInDarkTheme()
        return ThemeModel(
            id = "system_${if (isDark) "dark" else "light"}",
            name = if (isDark) "System Dark" else "System Light",
            source = ThemeSource.SYSTEM,
            isDark = isDark
        )
    }
    
    /**
     * Creates theme based on user's zodiac sign
     */
    private suspend fun createZodiacTheme(): ThemeModel {
        val user = userRepository.getUserProfile().first()
        val birthdate = user.birthdate ?: return ThemeModel.DEFAULT
        
        val zodiacSign = ZodiacUtils.determineZodiacSign(birthdate)
        val element = when (zodiacSign) {
            ZodiacSign.ARIES, ZodiacSign.LEO, ZodiacSign.SAGITTARIUS -> ZodiacElement.FIRE
            ZodiacSign.TAURUS, ZodiacSign.VIRGO, ZodiacSign.CAPRICORN -> ZodiacElement.EARTH
            ZodiacSign.GEMINI, ZodiacSign.LIBRA, ZodiacSign.AQUARIUS -> ZodiacElement.AIR
            ZodiacSign.CANCER, ZodiacSign.SCORPIO, ZodiacSign.PISCES -> ZodiacElement.WATER
        }
        
        return ThemeModel(
            id = "zodiac_${zodiacSign.name.lowercase()}",
            name = "${zodiacSign.displayName} Theme",
            source = ThemeSource.ZODIAC,
            isDark = isSystemInDarkTheme(),
            zodiacElement = element
        )
    }
    
    /**
     * Creates theme based on user's current biorhythm
     */
    private suspend fun createBiorhythmTheme(): ThemeModel {
        val user = userRepository.getUserProfile().first()
        val birthdate = user.birthdate ?: return ThemeModel.DEFAULT
        
        val today = LocalDate.now()
        val biorhythm = biorhythmRepository.calculateBiorhythm(birthdate, today).first()
        
        val energyLevel = EnergyLevel(
            physical = biorhythm.physical.normalizedValue,
            emotional = biorhythm.emotional.normalizedValue,
            intellectual = biorhythm.intellectual.normalizedValue
        )
        
        return ThemeModel(
            id = "biorhythm_${today}",
            name = "Today's Energy Theme",
            source = ThemeSource.BIORHYTHM,
            isDark = isSystemInDarkTheme(),
            energyLevel = energyLevel
        )
    }
    
    /**
     * Creates theme based on current season or special events
     */
    private fun createSeasonalTheme(): ThemeModel {
        val today = LocalDate.now()
        val month = today.monthValue
        val dayOfMonth = today.dayOfMonth
        
        // Define seasonal periods
        val season = when {
            // Spring theme (March-May)
            month in 3..5 -> "spring"
            // Summer theme (June-August)
            month in 6..8 -> "summer"
            // Autumn theme (September-November)
            month in 9..11 -> "autumn"
            // Winter theme (December-February)
            else -> "winter"
        }
        
        // Check for special dates
        val specialEvent = when {
            month == 12 && dayOfMonth in 21..31 -> "winter_solstice"
            month == 3 && dayOfMonth in 19..21 -> "spring_equinox"
            month == 6 && dayOfMonth in 20..22 -> "summer_solstice"
            month == 9 && dayOfMonth in 21..23 -> "autumn_equinox"
            else -> null
        }
        
        val themeId = specialEvent ?: season
        val themeName = when (themeId) {
            "spring" -> "Spring Renewal"
            "summer" -> "Summer Vibrance"
            "autumn" -> "Autumn Reflection"
            "winter" -> "Winter Serenity"
            "winter_solstice" -> "Winter Solstice"
            "spring_equinox" -> "Spring Equinox"
            "summer_solstice" -> "Summer Solstice"
            "autumn_equinox" -> "Autumn Equinox"
            else -> "Seasonal Theme"
        }
        
        return ThemeModel(
            id = "seasonal_$themeId",
            name = themeName,
            source = ThemeSource.SEASONAL,
            isDark = isSystemInDarkTheme()
        )
    }
}
```

### Theme Composable

```kotlin
/**
 * CompositionLocal to provide theme manager throughout the composition
 */
val LocalThemeManager = compositionLocalOf<ThemeManager> { 
    error("ThemeManager not provided") 
}

/**
 * Main theme composable for OmniCast
 */
@Composable
fun OmnicastTheme(
    themeModel: ThemeModel = LocalThemeManager.current.currentTheme.collectAsState().value,
    content: @Composable () -> Unit
) {
    // Generate color scheme based on theme source
    val colorScheme = when (themeModel.source) {
        ThemeSource.SYSTEM -> {
            if (themeModel.isDark) DarkColorScheme else LightColorScheme
        }
        ThemeSource.ZODIAC -> {
            val element = themeModel.zodiacElement ?: ZodiacElement.FIRE
            getZodiacColorScheme(element, themeModel.isDark)
        }
        ThemeSource.BIORHYTHM -> {
            val energy = themeModel.energyLevel ?: EnergyLevel(0.5f, 0.5f, 0.5f)
            getBiorhythmColorScheme(energy, themeModel.isDark)
        }
        ThemeSource.SEASONAL -> {
            val season = themeModel.id.substringAfter("seasonal_")
            getSeasonalColorScheme(season, themeModel.isDark)
        }
        ThemeSource.USER_PREFERENCE -> {
            themeModel.customColorScheme?.toColorScheme(themeModel.isDark)
                ?: if (themeModel.isDark) DarkColorScheme else LightColorScheme
        }
    }
    
    // Apply Material theme with dynamic color scheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

/**
 * Creates a zodiac-based color scheme
 */
fun getZodiacColorScheme(element: ZodiacElement, isDark: Boolean): ColorScheme {
    return when (element) {
        ZodiacElement.FIRE -> {
            if (isDark) {
                darkColorScheme(
                    primary = FirePrimaryDark,
                    secondary = FireSecondaryDark,
                    tertiary = FireTertiaryDark,
                    // Additional color slots
                )
            } else {
                lightColorScheme(
                    primary = FirePrimaryLight,
                    secondary = FireSecondaryLight,
                    tertiary = FireTertiaryLight,
                    // Additional color slots
                )
            }
        }
        ZodiacElement.EARTH -> {
            // Earth element color scheme
            if (isDark) darkEarthColorScheme else lightEarthColorScheme
        }
        ZodiacElement.AIR -> {
            // Air element color scheme
            if (isDark) darkAirColorScheme else lightAirColorScheme
        }
        ZodiacElement.WATER -> {
            // Water element color scheme
            if (isDark) darkWaterColorScheme else lightWaterColorScheme
        }
    }
}

/**
 * Creates a biorhythm-based color scheme
 */
fun getBiorhythmColorScheme(energyLevel: EnergyLevel, isDark: Boolean): ColorScheme {
    // Base color scheme (dark or light)
    val baseScheme = if (isDark) darkColorScheme() else lightColorScheme()
    
    // Calculate color intensity based on physical energy
    val intensityFactor = 0.5f + (energyLevel.physical * 0.5f)
    
    // Calculate color temperature based on emotional energy
    // Higher emotional energy = warmer colors
    val emotionalFactor = energyLevel.emotional
    
    // Calculate contrast based on intellectual energy
    val contrastFactor = 0.7f + (energyLevel.intellectual * 0.3f)
    
    // Create tonal palettes with adjusted intensity
    val primaryTones = TonalPalette.fromSeed(
        seed = adjustColorTemperature(BiorhythmSeedColors.primary, emotionalFactor),
        contrast = contrastFactor
    )
    
    val secondaryTones = TonalPalette.fromSeed(
        seed = adjustColorTemperature(BiorhythmSeedColors.secondary, emotionalFactor),
        contrast = contrastFactor
    )
    
    val tertiaryTones = TonalPalette.fromSeed(
        seed = adjustColorTemperature(BiorhythmSeedColors.tertiary, emotionalFactor),
        contrast = contrastFactor
    )
    
    // Create dynamic color scheme
    return if (isDark) {
        baseScheme.copy(
            primary = primaryTones.tone(80),
            secondary = secondaryTones.tone(80),
            tertiary = tertiaryTones.tone(80),
            // Other color slots with appropriate tones
        )
    } else {
        baseScheme.copy(
            primary = primaryTones.tone(40),
            secondary = secondaryTones.tone(40),
            tertiary = tertiaryTones.tone(40),
            // Other color slots with appropriate tones
        )
    }
}

/**
 * Adjusts color temperature based on emotional factor
 */
private fun adjustColorTemperature(color: Color, emotionalFactor: Float): Color {
    // Shift color toward warmer tones as emotional factor increases
    val targetHue = if (emotionalFactor > 0.5f) {
        // Shift toward red/orange for high emotional energy
        30f
    } else {
        // Shift toward blue/green for low emotional energy
        210f
    }
    
    val hslColor = convertToHSL(color)
    val shiftFactor = abs(emotionalFactor - 0.5f) * 2 // 0.0-1.0
    
    val newHue = lerpAngle(
        start = hslColor.hue,
        stop = targetHue,
        fraction = shiftFactor * 0.3f // Limit the shift to 30%
    )
    
    return hslToColor(
        hue = newHue,
        saturation = hslColor.saturation,
        lightness = hslColor.lightness
    )
}
```

### Component Example: PredictionCard

```kotlin
/**
 * Reusable card component for displaying predictions
 */
@Composable
fun PredictionCard(
    title: String,
    description: String,
    tags: List<String> = emptyList(),
    icon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val cardElevation = CardDefaults.cardElevation(
        defaultElevation = 2.dp,
        pressedElevation = 8.dp,
        focusedElevation = 4.dp
    )
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        elevation = cardElevation,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (icon != null) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                shape = CircleShape
                            )
                            .padding(8.dp)
                    ) {
                        icon()
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                }
                
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )
            
            if (tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    tags.forEach { tag ->
                        TagChip(tag = tag)
                    }
                }
            }
        }
    }
}

/**
 * Tag chip for displaying prediction tags
 */
@Composable
fun TagChip(
    tag: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f),
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    ) {
        Text(
            text = tag,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
```