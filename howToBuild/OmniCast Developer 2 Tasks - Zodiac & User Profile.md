# OmniCast Developer 2 Tasks
## Zodiac & User Profile

This document outlines your specific responsibilities, deliverables, and guidelines as Developer 2 on the OmniCast project. You are responsible for implementing the Zodiac feature, user profile management, and app settings.

## Your Primary Responsibilities

As Developer 2, you are responsible for:

1. Creating the user profile system (crucial for all personalization)
2. Implementing the Zodiac sign prediction feature
3. Building the app settings functionality
4. Ensuring proper data persistence for user preferences
5. Implementing user profile visualization components

## Modules You Own

You have complete ownership of the following modules:

### 1. `features:zodiac` Module
- Zodiac sign determination logic
- Daily/weekly horoscope generation
- Zodiac visualization components
- Compatibility analysis

### 2. `features:profile` Module
- User profile creation and editing
- Birthdate selection and validation
- Profile data persistence
- Profile visualization

### 3. `features:settings` Module
- App preferences management
- Theme selection UI
- Language selection
- Notification preferences
- Privacy settings

## Detailed Task Breakdown

### Phase 1: User Profile Implementation (Weeks 1-2)

#### 1.1 Profile Domain Layer
- [ ] Create `User` domain model
- [ ] Implement `UserRepository` interface
- [ ] Create `GetUserProfileUseCase`
- [ ] Implement `UpdateUserProfileUseCase`
- [ ] Build `ValidateBirthdateUseCase`

#### 1.2 Profile Data Layer
- [ ] Create `UserEntity` for Room
- [ ] Implement `UserDao` interface
- [ ] Build `UserRepositoryImpl` class
- [ ] Set up profile preference storage

#### 1.3 Profile UI Layer
- [ ] Create `ProfileScreen` composable
- [ ] Implement `ProfileViewModel`
- [ ] Build `ProfileEditor` component
- [ ] Create `DateSelector` component
- [ ] Implement user avatar/image handling

### Phase 2: Zodiac Feature Implementation (Weeks 3-5)

#### 2.1 Zodiac Domain Layer
- [ ] Create `ZodiacSign` enum and domain model
- [ ] Implement `HoroscopeReading` domain model
- [ ] Create `ZodiacRepository` interface
- [ ] Implement `DetermineZodiacSignUseCase`
- [ ] Build `GetDailyHoroscopeUseCase`
- [ ] Add `GetWeeklyHoroscopeUseCase` (optional)
- [ ] Create `GetCompatibilityUseCase` (optional)

#### 2.2 Zodiac Data Layer
- [ ] Create `ZodiacEntity` for database caching
- [ ] Implement `ZodiacDao` interface
- [ ] Build `ZodiacRepositoryImpl` class
- [ ] Create loader for zodiac JSON content
- [ ] Implement deterministic reading selection

#### 2.3 Zodiac UI Layer
- [ ] Create `ZodiacScreen` composable
- [ ] Implement `ZodiacViewModel`
- [ ] Build `ZodiacSignSelector` component
- [ ] Create `HoroscopeCard` component
- [ ] Implement `ZodiacCompatibilityView` (optional)
- [ ] Add `ZodiacChart` visualization

### Phase 3: Settings Implementation (Weeks 6-8)

#### 3.1 Settings Domain Layer
- [ ] Create settings domain models
- [ ] Implement `SettingsRepository` interface
- [ ] Build settings-related use cases

#### 3.2 Settings Data Layer
- [ ] Set up settings persistence in DataStore
- [ ] Implement `SettingsRepositoryImpl`

#### 3.3 Settings UI Layer
- [ ] Create `SettingsScreen` composable
- [ ] Implement `SettingsViewModel`
- [ ] Build `ThemeSelector` component
- [ ] Create `LanguageSelector` component
- [ ] Implement `NotificationSettings` component
- [ ] Build `PrivacySettings` component

## Implementation Guidelines

### User Profile Implementation

The user profile is a critical component as it provides the birthdate data needed for all prediction features.

1. **Data Collection Requirements**
   - Basic information: name, birthdate (required)
   - Optional information: gender, location, profile picture
   - First-time user experience: collect minimal required data

2. **Birthdate Validation**
   - Must validate realistic date ranges (e.g., not future dates)
   - Handle leap years correctly
   - Consider cultural date formats (MM/DD/YYYY vs. DD/MM/YYYY)
   - Provide a user-friendly date picker

3. **Persistence Strategy**
   - Store basic profile in Room database
   - Cache frequently accessed data for performance
   - Handle migration if schema changes

### Zodiac Feature Implementation

The Zodiac feature is one of the core prediction systems and must be implemented with attention to detail.

1. **Zodiac Determination Logic**
   - Accurately determine sign based on birthdate
   - Handle edge cases (birthdays near sign transitions)
   - Support all 12 zodiac signs with proper data

2. **Horoscope Generation**
   - Implement deterministic but seemingly random selection
   - Use date + user ID as seed for consistency
   - Select horoscopes that feel personalized

3. **Content Requirements**
   - Display general, love, career, and health readings
   - Include lucky numbers, colors, and compatible signs
   - Show relevant imagery for the zodiac sign

### Settings Implementation

The settings module allows users to customize their experience.

1. **Theme Selection**
   - Allow selection from system, zodiac, biorhythm, or manual themes
   - Provide visual preview of theme options
   - Instantly apply theme changes

2. **Language Options**
   - Support language switching with proper resource loading
   - Include language-specific content files
   - Handle RTL languages if supporting Persian

3. **Notification Preferences**
   - Allow scheduling of daily reading notifications
   - Customize which predictions to receive
   - Set quiet hours and notification frequency

## Dependencies on Other Modules

Your implementation will rely on core modules created by Developer 1:

### Incoming Dependencies
- Core navigation system for screen routing
- Base theming for consistent UI
- Database and DataStore foundation
- UI component library

### Outgoing Dependencies
- Other features will depend on user profile data
- The home screen will need zodiac data for daily overview
- Theming system may use zodiac sign for theme selection

## Technical Requirements

### 1. Data Structure
- Follow the JSON structure from provided templates for zodiac content
- Ensure proper normalization of database entities
- Use appropriate type converters for complex types

### 2. UI Implementation
- Use the shared UI components from `core:ui`
- Follow Material Design 3 guidelines
- Ensure all UI is responsive and adaptive
- Implement proper loading and error states

### 3. State Management
- Use StateFlow/SharedFlow for reactive UI
- Follow unidirectional data flow principles
- Handle all possible states (loading, error, empty, content)

### 4. Performance
- Avoid unnecessary database operations
- Cache frequently accessed data
- Use efficient composables with proper keys

## Testing Requirements

Implement comprehensive tests for your modules:

### 1. Unit Tests
- Test all use cases thoroughly
- Test zodiac determination logic
- Test repository implementations
- Test ViewModels

### 2. Integration Tests
- Test database operations
- Test JSON content loading
- Test preferences persistence

### 3. UI Tests
- Test core user flows
- Test profile creation process
- Test zodiac sign selection
- Test settings changes

## Deliverables & Acceptance Criteria

### 1. User Profile Module
**Deliverables**:
- Complete profile creation and editing flow
- Birthdate selection with validation
- Profile data persistence
- Profile visualization

**Acceptance Criteria**:
- Users can create/edit profile with all required fields
- Birthdate validation prevents invalid dates
- Profile data persists across app restarts
- UI is polished and responsive

### 2. Zodiac Module
**Deliverables**:
- Zodiac sign determination feature
- Daily horoscope generation
- Zodiac sign visualization
- Compatibility view (optional)

**Acceptance Criteria**:
- Correct zodiac sign is determined from birthdate
- Daily horoscopes are consistent but change daily
- UI presents horoscope information clearly
- All zodiac sign data correctly displayed

### 3. Settings Module
**Deliverables**:
- Settings screen with all preference options
- Theme selection functionality
- Language selection (if implementing multilingual)
- Notification preferences

**Acceptance Criteria**:
- All settings are correctly saved and loaded
- Theme changes apply immediately
- Settings UI follows design guidelines
- Settings survive app restarts

## Integration Notes

To ensure smooth integration with other modules:

1. **Expose Necessary APIs**
   - Provide clean interfaces for profile data access
   - Document public API methods and models
   - Create read-only views of data where appropriate

2. **Respect Architecture Boundaries**
   - Keep UI, domain, and data layers separate
   - Use proper dependency injection
   - Follow the repository pattern

3. **Resource Naming**
   - Prefix resource IDs with module name to avoid conflicts
   - Follow the project naming conventions

## Final Notes

Your modules form the foundation of the user experience in OmniCast. The user profile module is particularly critical as it provides the core data needed for personalized predictions across all features.

Please reach out if you have any questions about implementation details or requirements.

---

**Project Manager Contact**: [Name & Contact Method]
