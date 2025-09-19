# OmniCast Developer Guide

## Table of Contents

1. [Introduction](#introduction)
2. [Architecture Overview](#architecture-overview)
3. [Module Structure](#module-structure)
4. [Project Conventions](#project-conventions)
5. [Build and Setup Instructions](#build-and-setup-instructions)
6. [Dynamic Theming System](#dynamic-theming-system)
7. [Localization Framework](#localization-framework)
8. [Data Source Integration](#data-source-integration)
9. [Feature Implementation Guidelines](#feature-implementation-guidelines)
10. [Testing Strategy](#testing-strategy)
11. [Future Scalability](#future-scalability)
12. [Troubleshooting](#troubleshooting)

## Introduction

OmniCast is a comprehensive divination and prediction app that functions fully offline while offering optional online enhancements. This document serves as the authoritative guide for developers working on the OmniCast project, ensuring consistent implementation across modules and contributors.

### Project Philosophy

- **Offline-First**: All core functionality works without internet access
- **Modular Design**: Independent feature modules with clean architecture
- **Flexibility**: Support for multiple languages and customizable themes
- **Scalability**: Easy to extend with new prediction systems

## Architecture Overview

### Clean Architecture

OmniCast implements Clean Architecture principles to separate concerns and maintain testability:

![Clean Architecture Diagram](https://placeholder-for-clean-architecture-diagram.com)

1. **UI Layer** (Presentation)
   - Composable screens and components
   - ViewModels that manage UI state
   - UI events and navigation

2. **Domain Layer** (Business Logic)
   - Use cases (application-specific business rules)
   - Repository interfaces (abstract data requirements)
   - Domain models (business entities)

3. **Data Layer** (Data Access)
   - Repository implementations
   - Data sources (local and remote)
   - Data models (entities and DTOs)

### Key Technologies

- **UI**: Jetpack Compose with Material 3
- **Navigation**: Compose Navigation with custom NavigationManager
- **State Management**: MVI pattern with StateFlow
- **Dependency Injection**: Hilt
- **Local Storage**: Room Database + DataStore Preferences
- **Content Storage**: JSON in assets folder
- **Remote Services**: Firebase (Auth, Firestore, Remote Config, Messaging)
- **Concurrency**: Kotlin Coroutines + Flow
- **Localization**: Android Resources + Custom ContentProvider
- **Theming**: Dynamic theme system with Material 3

### Data Flow

```
UI Layer → ViewModel → Use Case → Repository Interface → Repository Implementation → Data Source
```

Example flow for fetching a daily horoscope:

1. `ZodiacScreen` calls `ZodiacViewModel.getDailyHoroscope()`
2. `ZodiacViewModel` invokes `GetDailyHoroscopeUseCase`
3. Use case calls `ZodiacRepository.getDailyHoroscope()`
4. Repository implementation decides which data source to use (local JSON, database, or remote)
5. Data is transformed from entity/DTO to domain model
6. Result flows back to the UI as a state update

## Module Structure

### App Module

The entry point that integrates all other modules.

**Key Components:**
- `MainActivity`: Main entry point for the application
- `OmniCastApplication`: Application class with Hilt setup
- `di/AppModule`: App-level dependencies

### Core Modules

Shared functionality used across feature modules.

#### core:common

Utilities and extensions that have no external dependencies.

**Key Components:**
- `DateUtils`: Date manipulation and formatting
- `LocaleUtils`: Locale management utilities
- `LogUtils`: Logging utilities
- `Constants`: Global constants

#### core:ui

Base UI components and theming system.

**Key Components:**
- `theme/OmnicastTheme`: Main theme composable
- `theme/DynamicThemeController`: Controls theme changes
- `component/OmniCastAppBar`: App bar with consistent styling
- `component/NavigationDrawer`: Global navigation drawer

#### core:navigation

Navigation framework for the application.

**Key Components:**
- `NavigationManager`: Central navigation controller
- `OmniCastNavGraph`: Main navigation graph definition
- `Screen`: Sealed class with all destinations

#### core:data

Core data handling shared across features.

**Key Components:**
- `local/database/OmniCastDatabase`: Room database definition
- `local/datastore/UserPreferences`: User preferences storage
- `remote/firebase/FirebaseAuthManager`: Authentication handler

#### core:domain

Core domain models and interfaces.

**Key Components:**
- `model/User`: User domain model
- `repository/UserRepository`: User data access interface
- `usecase/user/GetUserProfileUseCase`: Retrieves user information

### Feature Modules

Each prediction method has its own feature module.

#### feature:home

Home screen with feature overview.

**Key Components:**
- `ui/HomeScreen`: Main landing screen
- `ui/HomeViewModel`: Manages home screen state
- `ui/component/FeaturesCarousel`: Displays feature highlights

#### feature:zodiac

Zodiac sign-based predictions.

**Key Components:**
- `ui/ZodiacScreen`: Zodiac horoscope UI
- `domain/model/ZodiacSign`: Zodiac sign domain model
- `domain/usecase/GetDailyHoroscopeUseCase`: Retrieves daily horoscope

Similar structure applies to other feature modules:
- `feature:biorhythm`
- `feature:tarot`
- `feature:numerology`
- `feature:abjad`
- `feature:iching`
- `feature:coffee`
- `feature:rune`
- `feature:overview`
- `feature:journal`
- `feature:profile`
- `feature:settings`

## Project Conventions

### Naming Conventions

#### Files and Classes

- **Features**: `FeatureNameScreen.kt`, `FeatureNameViewModel.kt`
- **Use Cases**: `VerbNounUseCase.kt` (e.g., `GetDailyHoroscopeUseCase.kt`)
- **Repositories**: `FeatureNameRepository.kt` (interface), `FeatureNameRepositoryImpl.kt` (implementation)
- **Domain Models**: Noun without suffix (e.g., `User.kt`, `ZodiacSign.kt`)
- **Data Models**: `EntityName.kt` for Room entities, `DtoName.kt` for network models
- **Composables**: PascalCase without suffix (e.g., `OmniCastAppBar`)

#### Variables and Functions

- **Variables**: camelCase (e.g., `userId`, `dailyHoroscope`)
- **Functions**: camelCase verbs (e.g., `getDailyHoroscope()`, `updateUserProfile()`)
- **Composable Functions**: PascalCase (e.g., `ZodiacScreen()`, `HoroscopeCard()`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `MAX_PREDICTIONS`, `DEFAULT_LOCALE`)
- **Extension Functions**: Prefixed with type (e.g., `String.toTitleCase()`)

### Package Structure

```
com.hadify.omnicast.feature.{featurename}.
├── di/
├── ui/
│   ├── component/
│   └── screen/
├── domain/
│   ├── model/
│   ├── repository/
│   └── usecase/
└── data/
    ├── local/
    │   ├── dao/
    │   └── entity/
    ├── remote/
    │   └── dto/
    └── repository/
```

### Dependency Injection

Using Hilt for dependency injection with the following module hierarchy:

- `AppModule`: Application-wide dependencies
- `CoreModule`: Core module dependencies
- `FeatureModule`: Feature-specific dependencies

Each module should provide:
1. Repositories (bind interface to implementation)
2. Use cases 
3. Data sources (local and remote)

Example:

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object ZodiacModule {
    
    @Provides
    @Singleton
    fun provideZodiacDao(database: OmniCastDatabase): ZodiacDao {
        return database.zodiacDao()
    }
    
    @Provides
    @Singleton
    fun provideZodiacRepository(
        zodiacDao: ZodiacDao,
        assetManager: AssetManager
    ): ZodiacRepository {
        return ZodiacRepositoryImpl(zodiacDao, assetManager)
    }
    
    @Provides
    fun provideGetDailyHoroscopeUseCase(
        zodiacRepository: ZodiacRepository
    ): GetDailyHoroscopeUseCase {
        return GetDailyHoroscopeUseCase(zodiacRepository)
    }
}
```

### Recommended Design Patterns

- **MVI (Model-View-Intent)** for UI state management
- **Repository Pattern** for data access abstraction
- **Factory Pattern** for creating complex objects
- **Strategy Pattern** for algorithms that vary
- **Observer Pattern** (via Flow) for reactive updates
- **Adapter Pattern** for data transformation

## Build and Setup Instructions

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- JDK 17 or later
- Gradle 8.0 or later
- Firebase account for optional features

### Clone and Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/hadify/omnicast.git
   cd omnicast
   ```

2. Open the project in Android Studio

3. Create a `local.properties` file in the project root with:
   ```properties
   sdk.dir=/path/to/your/android/sdk
   ```

4. For Firebase functionality (optional):
   - Create a new Firebase project
   - Add an Android app to the project with package name `com.hadify.omnicast`
   - Download `google-services.json` and place it in the `app/` directory

### Building the Project

#### Debug Build

```bash
./gradlew assembleDebug
```

#### Release Build

1. Generate signing key (if you don't have one):
   ```bash
   keytool -genkey -v -keystore omnicast.keystore -alias omnicast -keyalg RSA -keysize 2048 -validity 10000
   ```

2. Create/update `keystore.properties` in the project root:
   ```properties
   storeFile=/path/to/omnicast.keystore
   storePassword=yourStorePassword
   keyAlias=omnicast
   keyPassword=yourKeyPassword
   ```

3. Build signed APK:
   ```bash
   ./gradlew assembleRelease
   ```

### Adding New Modules

1. Create a new module directory in the appropriate category:
   ```bash
   mkdir -p features/new-feature/src/main/kotlin/com/hadify/omnicast/feature/newfeature
   ```

2. Create module structure:
   ```
   features/new-feature/
   ├── build.gradle.kts
   └── src/
       └── main/
           ├── AndroidManifest.xml
           └── kotlin/com/hadify/omnicast/feature/newfeature/
               ├── di/
               ├── ui/
               ├── domain/
               └── data/
   ```

3. Add Gradle configuration in `features/new-feature/build.gradle.kts`

4. Include the module in `settings.gradle.kts`:
   ```kotlin
   include(":features:new-feature")
   ```

5. Add to the navigation graph in `core:navigation`

### Adding New Screens

1. Create screen composable and ViewModel:
   ```kotlin
   // NewFeatureScreen.kt
   @Composable
   fun NewFeatureScreen(
       viewModel: NewFeatureViewModel = hiltViewModel()
   ) {
       // Screen implementation
   }
   
   // NewFeatureViewModel.kt
   @HiltViewModel
   class NewFeatureViewModel @Inject constructor(
       private val someUseCase: SomeUseCase
   ) : ViewModel() {
       // ViewModel implementation
   }
   ```

2. Add screen to navigation graph in `OmniCastNavGraph.kt`:
   ```kotlin
   composable(
       route = Screen.NewFeature.route
   ) {
       NewFeatureScreen()
   }
   ```

3. Add destination to `Screen.kt`:
   ```kotlin
   sealed class Screen(val route: String) {
       // Existing screens
       object NewFeature : Screen("new_feature")
   }
   ```

## Dynamic Theming System

OmniCast implements a flexible theming system that adapts to user preferences, zodiac signs, or biorhythm states.

### Theme Structure

Themes are organized into three categories:

1. **Base Themes**: Light, dark, and system default
2. **Zodiac Themes**: Visual themes based on zodiac elements and qualities
3. **Biorhythm Themes**: Dynamic themes that adapt to energy levels

### Implementation Details

The core of the theming system is in `core:ui/theme`:

```kotlin
// ThemeManager.kt
class ThemeManager @Inject constructor(
    private val themeRepository: ThemeRepository
) {
    val currentTheme: Flow<ThemeModel> = themeRepository.getCurrentTheme()
    
    suspend fun updateTheme(themeModel: ThemeModel) {
        themeRepository.setCurrentTheme(themeModel)
    }
}

// OmnicastTheme.kt
@Composable
fun OmnicastTheme(
    themeModel: ThemeModel,
    content: @Composable () -> Unit
) {
    val colorScheme = when (themeModel.source) {
        ThemeSource.BASE -> getBaseColorScheme(themeModel.isDark)
        ThemeSource.ZODIAC -> getZodiacColorScheme(themeModel.zodiacElement, themeModel.isDark)
        ThemeSource.BIORHYTHM -> getBiorhythmColorScheme(themeModel.energyLevel, themeModel.isDark)
    }
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = getTypography(themeModel),
        shapes = getShapes(themeModel),
        content = content
    )
}
```

### Adding New Themes

To add a new theme:

1. Define the color scheme in the appropriate file:
   ```kotlin
   // For zodiac: ZodiacThemes.kt
   fun getFireElementColorScheme(isDark: Boolean): ColorScheme {
       return if (isDark) {
           darkColorScheme(
               primary = FireDark,
               // Other color definitions
           )
       } else {
           lightColorScheme(
               primary = FireLight,
               // Other color definitions
           )
       }
   }
   ```

2. Update the theme selector in `settings` feature to include the new theme option

3. Add the new theme to the `ThemeSource` enum if it's a new category

## Localization Framework

OmniCast supports multiple languages with a comprehensive localization framework.

### Resource-Based Localization

Standard Android resource localization is used for UI strings:

```
res/
├── values/
│   └── strings.xml           # Default (English)
├── values-fa/
│   └── strings.xml           # Persian
└── values-es/
    └── strings.xml           # Spanish
```

### Content Localization

Content localization is handled through language-specific JSON files in the assets directory:

```
assets/
└── content/
    ├── en/
    │   ├── zodiac.json
    │   ├── tarot.json
    │   └── ...
    ├── fa/
    │   ├── zodiac.json
    │   ├── tarot.json
    │   └── ...
    └── es/
        ├── zodiac.json
        ├── tarot.json
        └── ...
```

### Implementation

The localization system is managed through:

```kotlin
// LocalizationManager.kt
class LocalizationManager @Inject constructor(
    private val localizationRepository: LocalizationRepository,
    private val context: Context
) {
    // Get current app locale
    val currentLocale: Flow<Locale> = localizationRepository.getCurrentLocale()
    
    // Update app locale
    suspend fun updateLocale(locale: Locale) {
        localizationRepository.setCurrentLocale(locale)
    }
    
    // Get content in current language
    suspend fun getLocalizedContent(contentType: ContentType): String {
        val locale = localizationRepository.getCurrentLocale().first()
        val languageCode = locale.language
        
        return try {
            val path = "content/$languageCode/${contentType.fileName}"
            context.assets.open(path).bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            // Fallback to English if translation not available
            val fallbackPath = "content/en/${contentType.fileName}"
            context.assets.open(fallbackPath).bufferedReader().use { it.readText() }
        }
    }
}
```

### RTL Support

For right-to-left languages (e.g., Persian):

1. All layouts use `start`/`end` instead of `left`/`right`
2. Text alignment respects current locale directionality
3. Compose modifiers use directional padding and margins

```kotlin
@Composable
fun DirectionalAwareComponent() {
    val layoutDirection = LocalLayoutDirection.current
    
    Row(
        modifier = Modifier.padding(
            start = 16.dp,
            end = 16.dp
        )
    ) {
        // Component content
    }
}
```

## Data Source Integration

OmniCast uses multiple data sources with a clear priority order.

### Local JSON in Assets

Primary data source for prediction content:

```kotlin
class AssetManager @Inject constructor(
    private val context: Context,
    private val localizationManager: LocalizationManager
) {
    suspend fun loadJsonContent(contentType: ContentType): String {
        return localizationManager.getLocalizedContent(contentType)
    }
    
    suspend inline fun <reified T> loadAndParseJson(contentType: ContentType): T {
        val json = loadJsonContent(contentType)
        return Json.decodeFromString<T>(json)
    }
}
```

### Room Database

Used for user-generated content and caching:

```kotlin
@Database(
    entities = [
        UserEntity::class,
        JournalEntity::class,
        // Other entities
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class OmniCastDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun journalDao(): JournalDao
    // Other DAOs
}
```

### Firebase Integration

Optional services for enhanced functionality:

1. **Firebase Auth**: Optional user authentication
   ```kotlin
   class FirebaseAuthManager @Inject constructor() {
       private val auth = FirebaseAuth.getInstance()
       
       fun isUserSignedIn(): Boolean = auth.currentUser != null
       
       fun signInWithGoogle(idToken: String): Flow<Resource<FirebaseUser>> = flow {
           // Implementation
       }
   }
   ```

2. **Firebase Firestore**: Optional cloud backup
   ```kotlin
   class FirestoreManager @Inject constructor() {
       private val db = FirebaseFirestore.getInstance()
       
       suspend fun syncJournalEntries(userId: String, entries: List<JournalEntry>): Flow<Resource<Boolean>> = flow {
           // Implementation
       }
   }
   ```

3. **Firebase Remote Config**: Feature flags and content updates
   ```kotlin
   class FirebaseConfigManager @Inject constructor() {
       private val remoteConfig = FirebaseRemoteConfig.getInstance()
       
       fun getContentVersion(contentType: ContentType): String {
           return remoteConfig.getString("${contentType.name}_version")
       }
   }
   ```

4. **Firebase Cloud Messaging**: Optional notifications
   ```kotlin
   class FirebaseMessagingManager @Inject constructor() {
       fun subscribeToTopic(topic: String) {
           FirebaseMessaging.getInstance().subscribeToTopic(topic)
       }
   }
   ```

### Data Flow and Caching

The Repository layer manages data priority and caching:

```kotlin
class ZodiacRepositoryImpl @Inject constructor(
    private val zodiacDao: ZodiacDao,
    private val assetManager: AssetManager,
    private val firestoreManager: FirestoreManager,
    private val networkUtils: NetworkUtils
) : ZodiacRepository {
    
    override suspend fun getDailyHoroscope(zodiacSign: ZodiacSign, date: LocalDate): Flow<Resource<HoroscopeReading>> = flow {
        emit(Resource.Loading())
        
        // 1. Try local database cache first
        val cachedHoroscope = zodiacDao.getHoroscopeForDate(zodiacSign.name, date)
        if (cachedHoroscope != null) {
            emit(Resource.Success(cachedHoroscope.toDomain()))
            return@flow
        }
        
        // 2. Load from assets if not in cache
        try {
            val zodiacData = assetManager.loadAndParseJson<ZodiacDataModel>(ContentType.ZODIAC)
            val horoscope = zodiacData.generateDailyHoroscope(zodiacSign, date)
            
            // Cache the result
            zodiacDao.insertHoroscope(horoscope.toEntity())
            
            emit(Resource.Success(horoscope))
        } catch (e: Exception) {
            emit(Resource.Error("Failed to load horoscope: ${e.message}"))
        }
        
        // 3. Optionally sync with Firebase if online
        if (networkUtils.isConnected()) {
            try {
                firestoreManager.syncHoroscopeData()
            } catch (e: Exception) {
                // Non-blocking, just log error
                Log.e("ZodiacRepository", "Failed to sync with Firebase: ${e.message}")
            }
        }
    }
}
```

## Feature Implementation Guidelines

### Adding a New Prediction System

To add a new divination feature:

1. **Create the feature module**:
   ```
   features/new-feature/
   └── src/main/kotlin/com/hadify/omnicast/feature/newfeature/
       ├── di/
       │   └── NewFeatureModule.kt
       ├── ui/
       │   ├── NewFeatureScreen.kt
       │   ├── NewFeatureViewModel.kt
       │   └── component/
       │       └── NewFeatureCard.kt
       ├── domain/
       │   ├── model/
       │   │   └── NewFeatureModel.kt
       │   ├── repository/
       │   │   └── NewFeatureRepository.kt
       │   └── usecase/
       │       └── GetNewFeaturePredictionUseCase.kt
       └── data/
           ├── repository/
           │   └── NewFeatureRepositoryImpl.kt
           └── local/
               └── entity/
                   └── NewFeatureEntity.kt
   ```

2. **Create JSON content**:
   ```
   assets/content/en/new-feature.json
   ```

3. **Add to navigation**:
   ```kotlin
   // Add to Screen.kt
   object NewFeature : Screen("new_feature")
   
   // Add to navigation graph
   composable(Screen.NewFeature.route) {
       NewFeatureScreen()
   }
   ```

4. **Add to home screen**:
   ```kotlin
   // Add to feature list in HomeViewModel
   val features = listOf(
       // Existing features
       FeatureItem(
           id = "new_feature",
           nameRes = R.string.new_feature_name,
           iconRes = R.drawable.ic_new_feature,
           route = Screen.NewFeature.route
       )
   )
   ```

### Maintaining Compatibility

To ensure compatibility across modules:

1. Use the `Resource<T>` wrapper for all data operations
2. Follow established naming conventions
3. Use domain models for cross-module communication
4. Add new entities to the database version upgrade strategy
5. Adhere to the Clean Architecture principles

### Best Practices for New Features

1. Implement offline functionality first, then add online enhancements
2. Follow the established UI patterns for consistency
3. Support all configured languages
4. Include comprehensive unit tests
5. Document public API and complex logic
6. Follow accessibility guidelines

## Testing Strategy

OmniCast follows a comprehensive testing approach:

### Unit Tests

Located in `src/test/` directories, focus on:

- Use cases
- ViewModels
- Repositories
- Utility functions

Example:

```kotlin
@RunWith(MockitoJUnitRunner::class)
class GetDailyHoroscopeUseCaseTest {
    @Mock
    private lateinit var zodiacRepository: ZodiacRepository
    
    private lateinit var useCase: GetDailyHoroscopeUseCase
    
    @Before
    fun setup() {
        useCase = GetDailyHoroscopeUseCase(zodiacRepository)
    }
    
    @Test
    fun `invoke returns success when repository returns data`() = runTest {
        // Arrange
        val zodiacSign = ZodiacSign.ARIES
        val date = LocalDate.now()
        val horoscope = HoroscopeReading(/* parameters */)
        
        whenever(zodiacRepository.getDailyHoroscope(zodiacSign, date))
            .thenReturn(flowOf(Resource.Success(horoscope)))
            
        // Act
        val result = useCase(zodiacSign, date).first()
        
        // Assert
        assertThat(result).isInstanceOf(Resource.Success::class.java)
        assertThat((result as Resource.Success).data).isEqualTo(horoscope)
    }
}
```

### UI Tests

Located in `src/androidTest/` directories, focus on:

- Screen navigation
- UI component interactions
- End-to-end feature flows

Example:

```kotlin
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ZodiacScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    
    @Test
    fun zodiacScreen_displaysAllZodiacSigns() {
        // Navigate to zodiac screen
        composeTestRule.onNodeWithText("Zodiac").performClick()
        
        // Verify all signs are displayed
        composeTestRule.onNodeWithText("Aries").assertIsDisplayed()
        composeTestRule.onNodeWithText("Taurus").assertIsDisplayed()
        // Check other signs
    }
}
```

### Integration Tests

Test the interaction between components:

- Repository + local data source
- Use case + repository
- Complete feature flows

### Test Coverage Goals

- 80% code coverage for domain layer
- 70% code coverage for data layer
- 60% code coverage for UI layer

## Future Scalability

### Potential Expansions

1. **Premium Features**:
   - Enhanced prediction algorithms
   - More detailed interpretations
   - Historical data analysis

2. **Social Features**:
   - Share predictions with friends
   - Community discussions
   - Compatibility comparisons

3. **Advanced Personalization**:
   - AI-driven personalized insights
   - Integration with calendar events
   - Pattern recognition in journal entries

### Technical Preparation

1. **Architecture Flexibility**:
   - The modular approach allows adding/removing features without affecting others
   - Clean architecture enables swapping implementation details

2. **Versioning Strategy**:
   - Content versioning in JSON metadata
   - Database migration strategy
   - API versioning for future backend services

3. **Feature Flagging**:
   - Remote Config for controlled feature rollouts
   - Local feature flags for testing

## Troubleshooting

### Common Issues and Solutions

#### Build Failures

1. **Missing Dependencies**:
   - Run Gradle sync
   - Check dependency versions in `buildSrc`

2. **Compile Errors**:
   - Verify module dependencies
   - Check for API changes in libraries

#### Runtime Issues

1. **Content Loading Failures**:
   - Verify JSON file structure
   - Check file paths and language codes

2. **Navigation Problems**:
   - Ensure Screen route is correctly defined
   - Check NavGraph includes the destination

### Debugging Tools

1. **Firebase Crashlytics**:
   - Review crash reports
   - Analyze error patterns

2. **LogUtils**:
   - Use appropriate log levels
   - Include contextual information

3. **Room Database Inspector**:
   - Examine database state
   - Verify entity relationships

### Getting Help

1. Create detailed issue reports with:
   - Steps to reproduce
   - Expected vs. actual behavior
   - Relevant logs and screenshots

2. Reference this documentation for architectural guidance

3. Consult the project leads for design decisions

---

## Document Version

- **Version**: 1.0.0
- **Last Updated**: March 22, 2025
- **Author**: Hadify