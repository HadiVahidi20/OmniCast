# OmniCast - Complete Project Structure

```
omnicast/
├── buildSrc/                          # Centralized dependency management
│   ├── src/main/kotlin/
│   │   ├── Dependencies.kt            # Library dependencies
│   │   ├── Versions.kt                # Version constants
│   │   └── BuildConfig.kt             # Build configuration
│   └── build.gradle.kts
│
├── app/                               # Application module (entry point)
│   ├── src/
│   │   ├── main/
│   │   │   ├── kotlin/com/hadify/omnicast/
│   │   │   │   ├── MainActivity.kt    # Main entry point
│   │   │   │   ├── OmniCastApplication.kt # Application class
│   │   │   │   └── di/
│   │   │   │       ├── AppModule.kt   # App-level DI module
│   │   │   │       ├── ServiceModule.kt # Firebase and other services
│   │   │   │       └── FeatureModules.kt # Feature module bindings
│   │   │   ├── res/                   # Resources
│   │   │   │   ├── drawable/          # Images and icons
│   │   │   │   ├── values/            # Strings, colors, styles
│   │   │   │   ├── values-fa/         # Persian strings
│   │   │   │   ├── values-es/         # Spanish strings
│   │   │   │   └── mipmap/            # App icons
│   │   │   └── AndroidManifest.xml    # App manifest
│   │   ├── test/                      # Unit tests
│   │   └── androidTest/               # Instrumentation tests
│   ├── build.gradle.kts               # App-level build config
│   └── proguard-rules.pro             # ProGuard rules
│
├── core/                              # Core module (shared functionality)
│   ├── common/                        # Common utilities and extensions
│   │   ├── src/main/kotlin/com/hadify/omnicast/core/common/
│   │   │   ├── extension/             # Kotlin extensions
│   │   │   │   ├── DateExtensions.kt
│   │   │   │   ├── StringExtensions.kt
│   │   │   │   └── ComposeExtensions.kt
│   │   │   ├── util/                  # Utility classes
│   │   │   │   ├── DateUtils.kt
│   │   │   │   ├── NotificationUtils.kt
│   │   │   │   ├── LogUtils.kt
│   │   │   │   ├── NetworkUtils.kt    # Network connectivity
│   │   │   │   └── LocaleUtils.kt     # Locale management
│   │   │   └── constants/
│   │   │       ├── Constants.kt       # Global constants
│   │   │       └── ErrorConstants.kt  # Error messages
│   │   └── build.gradle.kts
│   │
│   ├── ui/                            # Core UI components
│   │   ├── src/main/kotlin/com/hadify/omnicast/core/ui/
│   │   │   ├── theme/                 # Theming system
│   │   │   │   ├── OmnicastTheme.kt   # Theme composable
│   │   │   │   ├── Color.kt           # Color definitions
│   │   │   │   ├── Shape.kt           # Shape definitions
│   │   │   │   ├── Type.kt            # Typography definitions
│   │   │   │   ├── DynamicThemeController.kt # Theme switching
│   │   │   │   ├── ThemeManager.kt    # Theme state management
│   │   │   │   └── themes/            # Theme variations
│   │   │   │       ├── ZodiacThemes.kt # Zodiac-based themes
│   │   │   │       ├── BiorhythmThemes.kt # Energy-based themes
│   │   │   │       └── BaseThemes.kt  # Light/dark/system themes
│   │   │   ├── component/             # Reusable UI components
│   │   │   │   ├── OmniCastAppBar.kt  # Top app bar
│   │   │   │   ├── NavigationDrawer.kt # Navigation drawer
│   │   │   │   ├── PredictionCard.kt  # Card for predictions
│   │   │   │   ├── LoadingIndicator.kt # Loading state
│   │   │   │   └── ErrorView.kt       # Error handling view
│   │   │   └── animation/             # Custom animations
│   │   │       └── TransitionAnimations.kt
│   │   └── build.gradle.kts
│   │
│   ├── navigation/                    # Navigation framework
│   │   ├── src/main/kotlin/com/hadify/omnicast/core/navigation/
│   │   │   ├── NavigationManager.kt   # Central navigation controller
│   │   │   ├── OmniCastNavGraph.kt    # Main nav graph definition
│   │   │   ├── Screen.kt              # Screen destinations
│   │   │   └── NavigationExtensions.kt # Nav helpers
│   │   └── build.gradle.kts
│   │
│   ├── data/                          # Core data handling
│   │   ├── src/main/kotlin/com/hadify/omnicast/core/data/
│   │   │   ├── di/                    # DI for data layer
│   │   │   │   └── DataModule.kt
│   │   │   ├── local/                 # Local storage
│   │   │   │   ├── database/          # Room database
│   │   │   │   │   ├── OmniCastDatabase.kt
│   │   │   │   │   └── converter/     # Type converters
│   │   │   │   │       └── Converters.kt
│   │   │   │   ├── datastore/         # DataStore preferences
│   │   │   │   │   ├── UserPreferences.kt
│   │   │   │   │   ├── ThemePreferences.kt
│   │   │   │   │   └── LocalePreferences.kt
│   │   │   │   └── asset/             # Asset management
│   │   │   │       └── AssetManager.kt
│   │   │   ├── remote/                # Remote data sources
│   │   │   │   ├── firebase/          # Firebase services
│   │   │   │   │   ├── FirebaseAuthManager.kt
│   │   │   │   │   ├── FirebaseConfigManager.kt
│   │   │   │   │   ├── FirestoreManager.kt
│   │   │   │   │   └── FirebaseMessagingManager.kt
│   │   │   │   └── model/             # Remote DTOs
│   │   │   │       └── ContentUpdateDto.kt
│   │   │   └── repository/            # Core repositories
│   │   │       ├── UserRepositoryImpl.kt
│   │   │       ├── ThemeRepositoryImpl.kt
│   │   │       └── LocalizationRepositoryImpl.kt
│   │   └── build.gradle.kts
│   │
│   └── domain/                        # Core domain layer
│       ├── src/main/kotlin/com/hadify/omnicast/core/domain/
│       │   ├── model/                 # Domain models
│       │   │   ├── user/              # User-related models
│       │   │   │   ├── User.kt
│       │   │   │   └── UserPreferences.kt
│       │   │   ├── theme/             # Theme models
│       │   │   │   ├── ThemeModel.kt
│       │   │   │   └── ThemeSource.kt # Enum for theme sources
│       │   │   ├── localization/      # Localization models
│       │   │   │   └── AppLocale.kt   # Supported locales
│       │   │   └── result/            # Result wrapper
│       │   │       └── Resource.kt    # Generic result wrapper
│       │   ├── repository/            # Repository interfaces
│       │   │   ├── UserRepository.kt
│       │   │   ├── ThemeRepository.kt
│       │   │   └── LocalizationRepository.kt
│       │   └── usecase/               # Core use cases
│       │       ├── user/              # User-related use cases
│       │       │   ├── GetUserProfileUseCase.kt
│       │       │   └── UpdateUserProfileUseCase.kt
│       │       ├── theme/             # Theme use cases
│       │       │   ├── GetCurrentThemeUseCase.kt
│       │       │   └── UpdateThemeUseCase.kt
│       │       └── localization/      # Localization use cases
│       │           ├── GetAvailableLocalesUseCase.kt
│       │           └── UpdateAppLocaleUseCase.kt
│       └── build.gradle.kts
│
├── features/                          # Feature modules
│   ├── home/                          # Home screen feature
│   │   ├── src/main/kotlin/com/hadify/omnicast/feature/home/
│   │   │   ├── di/                    # Feature DI
│   │   │   │   └── HomeModule.kt
│   │   │   ├── ui/                    # UI layer
│   │   │   │   ├── HomeScreen.kt      # Main home composable
│   │   │   │   ├── HomeViewModel.kt   # Home ViewModel
│   │   │   │   └── component/         # Home-specific components
│   │   │   │       └── FeaturesCarousel.kt
│   │   │   ├── domain/                # Domain layer
│   │   │   │   ├── model/             # Feature models
│   │   │   │   │   └── HomeSummary.kt
│   │   │   │   └── usecase/           # Feature use cases
│   │   │   │       └── GetHomeSummaryUseCase.kt
│   │   │   └── data/                  # Data layer
│   │   │       └── repository/
│   │   │           └── HomeSummaryRepositoryImpl.kt
│   │   └── build.gradle.kts
│   │
│   ├── zodiac/                        # Zodiac feature
│   │   ├── src/main/kotlin/com/hadify/omnicast/feature/zodiac/
│   │   │   ├── di/
│   │   │   │   └── ZodiacModule.kt
│   │   │   ├── ui/
│   │   │   │   ├── ZodiacScreen.kt
│   │   │   │   ├── ZodiacViewModel.kt
│   │   │   │   └── component/
│   │   │   │       ├── ZodiacChart.kt
│   │   │   │       └── HoroscopeCard.kt
│   │   │   ├── domain/
│   │   │   │   ├── model/
│   │   │   │   │   ├── ZodiacSign.kt
│   │   │   │   │   └── HoroscopeReading.kt
│   │   │   │   ├── repository/
│   │   │   │   │   └── ZodiacRepository.kt
│   │   │   │   └── usecase/
│   │   │   │       ├── GetDailyHoroscopeUseCase.kt
│   │   │   │       └── DetermineZodiacSignUseCase.kt
│   │   │   └── data/
│   │   │       ├── local/
│   │   │       │   ├── dao/
│   │   │       │   │   └── ZodiacDao.kt
│   │   │       │   ├── entity/
│   │   │       │   │   └── ZodiacEntity.kt
│   │   │       │   └── source/
│   │   │       │       └── ZodiacLocalDataSource.kt
│   │   │       └── repository/
│   │   │           └── ZodiacRepositoryImpl.kt
│   │   └── build.gradle.kts
│   │
│   ├── biorhythm/                     # Biorhythm feature
│   │   ├── src/main/kotlin/com/hadify/omnicast/feature/biorhythm/
│   │   │   ├── di/
│   │   │   │   └── BiorhythmModule.kt
│   │   │   ├── ui/
│   │   │   │   ├── BiorhythmScreen.kt
│   │   │   │   ├── BiorhythmViewModel.kt
│   │   │   │   └── component/
│   │   │   │       ├── BiorhythmChart.kt
│   │   │   │       └── BiorhythmInfoCard.kt
│   │   │   ├── domain/
│   │   │   │   ├── model/
│   │   │   │   │   ├── Biorhythm.kt
│   │   │   │   │   └── BiorhythmCycle.kt
│   │   │   │   ├── repository/
│   │   │   │   │   └── BiorhythmRepository.kt
│   │   │   │   └── usecase/
│   │   │   │       ├── CalculateBiorhythmUseCase.kt
│   │   │   │       └── GetWeeklyBiorhythmTrendUseCase.kt
│   │   │   └── data/
│   │   │       ├── local/
│   │   │       │   ├── dao/
│   │   │       │   │   └── BiorhythmDao.kt
│   │   │       │   └── entity/
│   │   │       │       └── BiorhythmEntity.kt
│   │   │       └── repository/
│   │   │           └── BiorhythmRepositoryImpl.kt
│   │   └── build.gradle.kts
│   │
│   ├── tarot/                         # Tarot feature
│   │   ├── src/main/kotlin/com/hadify/omnicast/feature/tarot/
│   │   │   ├── di/
│   │   │   │   └── TarotModule.kt
│   │   │   ├── ui/
│   │   │   │   ├── TarotScreen.kt
│   │   │   │   ├── TarotViewModel.kt
│   │   │   │   └── component/
│   │   │   │       ├── TarotCardView.kt
│   │   │   │       └── TarotInterpretation.kt
│   │   │   ├── domain/
│   │   │   │   ├── model/
│   │   │   │   │   ├── TarotCard.kt
│   │   │   │   │   └── TarotReading.kt
│   │   │   │   ├── repository/
│   │   │   │   │   └── TarotRepository.kt
│   │   │   │   └── usecase/
│   │   │   │       └── GetDailyTarotCardUseCase.kt
│   │   │   └── data/
│   │   │       ├── local/
│   │   │       │   ├── dao/
│   │   │       │   │   └── TarotDao.kt
│   │   │       │   └── entity/
│   │   │       │       └── TarotEntity.kt
│   │   │       └── repository/
│   │   │           └── TarotRepositoryImpl.kt
│   │   └── build.gradle.kts
│   │
│   ├── numerology/                    # Numerology feature
│   │   ├── src/main/kotlin/com/hadify/omnicast/feature/numerology/
│   │   │   ├── di/
│   │   │   │   └── NumerologyModule.kt
│   │   │   ├── ui/
│   │   │   │   ├── NumerologyScreen.kt
│   │   │   │   ├── NumerologyViewModel.kt
│   │   │   │   └── component/
│   │   │   │       └── NumerologyChartView.kt
│   │   │   ├── domain/
│   │   │   │   ├── model/
│   │   │   │   │   ├── NumerologyProfile.kt
│   │   │   │   │   └── DailyNumber.kt
│   │   │   │   ├── repository/
│   │   │   │   │   └── NumerologyRepository.kt
│   │   │   │   └── usecase/
│   │   │   │       ├── CalculateLifePathNumberUseCase.kt
│   │   │   │       └── GetDailyNumerologyUseCase.kt
│   │   │   └── data/
│   │   │       ├── local/
│   │   │       │   ├── dao/
│   │   │       │   │   └── NumerologyDao.kt
│   │   │       │   └── entity/
│   │   │       │       └── NumerologyEntity.kt
│   │   │       └── repository/
│   │   │           └── NumerologyRepositoryImpl.kt
│   │   └── build.gradle.kts
│   │
│   ├── abjad/                         # Abjad feature
│   │   ├── src/main/kotlin/com/hadify/omnicast/feature/abjad/
│   │   │   ├── di/
│   │   │   │   └── AbjadModule.kt
│   │   │   ├── ui/
│   │   │   │   ├── AbjadScreen.kt
│   │   │   │   ├── AbjadViewModel.kt
│   │   │   │   └── component/
│   │   │   │       └── AbjadCalculator.kt
│   │   │   ├── domain/
│   │   │   │   ├── model/
│   │   │   │   │   └── AbjadCalculation.kt
│   │   │   │   ├── repository/
│   │   │   │   │   └── AbjadRepository.kt
│   │   │   │   └── usecase/
│   │   │   │       └── CalculateAbjadValueUseCase.kt
│   │   │   └── data/
│   │   │       └── repository/
│   │   │           └── AbjadRepositoryImpl.kt
│   │   └── build.gradle.kts
│   │
│   ├── iching/                        # I Ching feature
│   │   ├── src/main/kotlin/com/hadify/omnicast/feature/iching/
│   │   │   ├── di/
│   │   │   │   └── IChingModule.kt
│   │   │   ├── ui/
│   │   │   │   ├── IChingScreen.kt
│   │   │   │   ├── IChingViewModel.kt
│   │   │   │   └── component/
│   │   │   │       └── HexagramView.kt
│   │   │   ├── domain/
│   │   │   │   ├── model/
│   │   │   │   │   └── Hexagram.kt
│   │   │   │   ├── repository/
│   │   │   │   │   └── IChingRepository.kt
│   │   │   │   └── usecase/
│   │   │   │       └── GetDailyHexagramUseCase.kt
│   │   │   └── data/
│   │   │       └── repository/
│   │   │           └── IChingRepositoryImpl.kt
│   │   └── build.gradle.kts
│   │
│   ├── coffee/                        # Coffee Reading feature
│   │   ├── src/main/kotlin/com/hadify/omnicast/feature/coffee/
│   │   │   ├── di/
│   │   │   │   └── CoffeeModule.kt
│   │   │   ├── ui/
│   │   │   │   ├── CoffeeReadingScreen.kt
│   │   │   │   ├── CoffeeReadingViewModel.kt
│   │   │   │   └── component/
│   │   │   │       └── CoffeeSymbolView.kt
│   │   │   ├── domain/
│   │   │   │   ├── model/
│   │   │   │   │   └── CoffeeSymbol.kt
│   │   │   │   ├── repository/
│   │   │   │   │   └── CoffeeReadingRepository.kt
│   │   │   │   └── usecase/
│   │   │   │       └── GetCoffeeReadingUseCase.kt
│   │   │   └── data/
│   │   │       └── repository/
│   │   │           └── CoffeeReadingRepositoryImpl.kt
│   │   └── build.gradle.kts
│   │
│   ├── rune/                          # Rune feature
│   │   ├── src/main/kotlin/com/hadify/omnicast/feature/rune/
│   │   │   ├── di/
│   │   │   │   └── RuneModule.kt
│   │   │   ├── ui/
│   │   │   │   ├── RuneScreen.kt
│   │   │   │   ├── RuneViewModel.kt
│   │   │   │   └── component/
│   │   │   │       └── RuneSymbolView.kt
│   │   │   ├── domain/
│   │   │   │   ├── model/
│   │   │   │   │   └── RuneSymbol.kt
│   │   │   │   ├── repository/
│   │   │   │   │   └── RuneRepository.kt
│   │   │   │   └── usecase/
│   │   │   │       └── GetDailyRuneUseCase.kt
│   │   │   └── data/
│   │   │       └── repository/
│   │   │           └── RuneRepositoryImpl.kt
│   │   └── build.gradle.kts
│   │
│   ├── overview/                      # Daily Overview feature
│   │   ├── src/main/kotlin/com/hadify/omnicast/feature/overview/
│   │   │   ├── di/
│   │   │   │   └── OverviewModule.kt
│   │   │   ├── ui/
│   │   │   │   ├── DailyOverviewScreen.kt
│   │   │   │   ├── DailyOverviewViewModel.kt
│   │   │   │   └── component/
│   │   │   │       └── OverviewCard.kt
│   │   │   ├── domain/
│   │   │   │   ├── model/
│   │   │   │   │   └── DailyOverview.kt
│   │   │   │   ├── repository/
│   │   │   │   │   └── OverviewRepository.kt
│   │   │   │   └── usecase/
│   │   │   │       └── GenerateDailyOverviewUseCase.kt
│   │   │   └── data/
│   │   │       └── repository/
│   │   │           └── OverviewRepositoryImpl.kt
│   │   └── build.gradle.kts
│   │
│   ├── journal/                       # Journal feature
│   │   ├── src/main/kotlin/com/hadify/omnicast/feature/journal/
│   │   │   ├── di/
│   │   │   │   └── JournalModule.kt
│   │   │   ├── ui/
│   │   │   │   ├── JournalScreen.kt
│   │   │   │   ├── JournalViewModel.kt
│   │   │   │   └── component/
│   │   │   │       ├── JournalEntryItem.kt
│   │   │   │       └── JournalEntryEditor.kt
│   │   │   ├── domain/
│   │   │   │   ├── model/
│   │   │   │   │   └── JournalEntry.kt
│   │   │   │   ├── repository/
│   │   │   │   │   └── JournalRepository.kt
│   │   │   │   └── usecase/
│   │   │   │       ├── AddJournalEntryUseCase.kt
│   │   │   │       ├── GetJournalEntriesUseCase.kt
│   │   │   │       └── DeleteJournalEntryUseCase.kt
│   │   │   └── data/
│   │   │       ├── local/
│   │   │       │   ├── dao/
│   │   │       │   │   └── JournalDao.kt
│   │   │       │   └── entity/
│   │   │       │       └── JournalEntity.kt
│   │   │       └── repository/
│   │   │           └── JournalRepositoryImpl.kt
│   │   └── build.gradle.kts
│   │
│   ├── profile/                       # User Profile feature
│   │   ├── src/main/kotlin/com/hadify/omnicast/feature/profile/
│   │   │   ├── di/
│   │   │   │   └── ProfileModule.kt
│   │   │   ├── ui/
│   │   │   │   ├── ProfileScreen.kt
│   │   │   │   ├── ProfileViewModel.kt
│   │   │   │   └── component/
│   │   │   │       ├── ProfileEditor.kt
│   │   │   │       └── ProfileSummary.kt
│   │   │   └── domain/
│   │   │       └── usecase/
│   │   │           ├── ValidateProfileInputUseCase.kt
│   │   │           └── UpdateProfileUseCase.kt
│   │   └── build.gradle.kts
│   │
│   └── settings/                      # Settings feature
│       ├── src/main/kotlin/com/hadify/omnicast/feature/settings/
│       │   ├── di/
│       │   │   └── SettingsModule.kt
│       │   ├── ui/
│       │   │   ├── SettingsScreen.kt
│       │   │   ├── SettingsViewModel.kt
│       │   │   └── component/
│       │   │       ├── ThemeSelector.kt
│       │   │       ├── LanguageSelector.kt
│       │   │       └── NotificationSettings.kt
│       │   └── domain/
│       │       └── usecase/
│       │           ├── UpdateThemeSettingsUseCase.kt
│       │           ├── UpdateLanguageSettingsUseCase.kt
│       │           └── ToggleNotificationSettingsUseCase.kt
│       └── build.gradle.kts
│
├── build.gradle.kts                   # Project-level build file
├── settings.gradle.kts                # Settings and module declarations
├── gradle.properties                  # Gradle properties
└── gradlew                            # Gradle wrapper
```

## Architecture Overview

### Modularization Strategy

The project follows a modular architecture with:

1. **App Module**: Entry point with DI setup and main activity
2. **Core Modules**: Shared functionality across features
   - `core:common` - Utilities and extensions
   - `core:ui` - Base UI components and theming
   - `core:navigation` - Navigation framework
   - `core:data` - Core data handling
   - `core:domain` - Core domain models and use cases
3. **Feature Modules**: Individual features with their own UI, domain, and data layers
   - Each feature module is completely self-contained
   - Features depend only on core modules, not on each other

### Key Components

1. **Navigation System**
   - `NavigationManager.kt` orchestrates app navigation
   - `Screen.kt` defines all destinations
   - Each feature contributes its screens to the central nav graph

2. **Theme Management**
   - `DynamicThemeController.kt` handles theme switching
   - Support for zodiac, biorhythm, and custom themes
   - Theme preferences stored in DataStore

3. **Localization Framework**
   - `LocalizationRepository` handles language settings
   - Resources organized for multi-language support
   - Direction-aware UI components for RTL languages

4. **Data Layer**
   - Room Database for offline storage
   - Firebase integration for optional online features
   - Asset management for bundled data

5. **Domain Layer**
   - Clean separation of business logic in use cases
   - Domain models free from framework dependencies
   - Repository interfaces defining data contracts

## Data Flow

1. **UI Layer**: Compose UI → ViewModel → Use Cases
2. **Domain Layer**: Use Cases → Repository Interfaces
3. **Data Layer**: Repository Implementations → Local/Remote Data Sources

## Testing Structure

Each module has dedicated test directories:
- `src/test/` for unit tests
- `src/androidTest/` for instrumentation tests