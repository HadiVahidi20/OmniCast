# OmniCast Developer 1 Tasks
## Core Infrastructure & App Foundation

This document outlines your specific responsibilities, deliverables, and guidelines as Developer 1 on the OmniCast project. You are the foundation builder for the entire application, responsible for creating the architecture that all other developers will build upon.

## Your Primary Responsibilities

As Developer 1, you are responsible for:

1. Building the core application infrastructure
2. Implementing the navigation system
3. Creating the theming framework
4. Developing base UI components
5. Setting up the data layer foundations
6. Building the home screen

## Modules You Own

You have complete ownership of the following modules:

### 1. `app` Module
- Main application entry point
- Dependency injection setup
- Module integration

### 2. `core:common` Module
- Utility classes and extension functions
- Constants and shared resources
- Error handling mechanisms

### 3. `core:ui` Module
- Material theme implementation
- Base UI components
- Animation utilities
- Dynamic theming system

### 4. `core:navigation` Module
- Navigation controller
- Screen destinations
- Navigation actions
- Transition animations

### 5. `core:data` Module
- Room database configuration
- DataStore setup
- Asset manager for JSON content
- Base repository implementations

### 6. `core:domain` Module
- Core domain models
- Base use case structure
- Resource wrapper for results

### 7. `features:home` Module
- Home screen UI
- Feature overview carousel
- Daily summary presentation

## Detailed Task Breakdown

### Phase 1: Project Setup & Core Architecture (Weeks 1-2)

#### 1.1 Project Configuration
- [ ] Create project structure with all modules
- [ ] Set up Gradle with buildSrc for dependency management
- [ ] Configure ProGuard/R8 rules
- [ ] Initialize Git repository with .gitignore

#### 1.2 Core Libraries & Dependencies
- [ ] Add all required dependencies (Compose, Room, Hilt, etc.)
- [ ] Configure Compose compiler options
- [ ] Set up Material 3 theme basics
- [ ] Add analytics dependencies (optional)

#### 1.3 Navigation System
- [ ] Create `NavigationManager` class
- [ ] Define `Screen` sealed class with all destinations
- [ ] Implement `OmniCastNavGraph` with navigation routes
- [ ] Build navigation transition animations

#### 1.4 Data Layer Foundation
- [ ] Set up Room database and entity converters
- [ ] Create DataStore for preferences 
- [ ] Build asset loading system for JSON files
- [ ] Implement Resource class for result wrapping

### Phase 2: UI Components & Theming (Weeks 3-4)

#### 2.1 Dynamic Theming System
- [ ] Implement `ThemeModel` data class
- [ ] Create `ThemeManager` for theme switching
- [ ] Set up theme persistence in DataStore
- [ ] Build theme source generators (system, zodiac, biorhythm)

#### 2.2 Base UI Components
- [ ] Create `OmniCastAppBar` component
- [ ] Build `PredictionCard` for predictions display
- [ ] Implement `NavigationDrawer` component
- [ ] Create loading and error state components

#### 2.3 Animation Framework
- [ ] Define animation specs and durations
- [ ] Create transition animations between screens
- [ ] Implement content reveal animations
- [ ] Build interactive feedback animations

### Phase 3: Core Feature Implementation (Weeks 5-6)

#### 3.1 Home Screen Development
- [ ] Create `HomeScreen` composable
- [ ] Implement `HomeViewModel` with data gathering
- [ ] Build feature carousel component
- [ ] Create daily overview summary card

#### 3.2 Integration Support
- [ ] Set up navigation destinations for all features
- [ ] Create placeholder screens for in-development features
- [ ] Implement feature flags system (optional)
- [ ] Build debugging tools for development

## Implementation Guidelines

### Architecture Standards to Maintain

1. **Clean Architecture Separation**
   - UI layer: Compose UI, ViewModels, UI States
   - Domain layer: Use cases, domain models, repository interfaces
   - Data layer: Repository implementations, data sources, DTOs

2. **Navigation Pattern**
   - All navigation must go through NavigationManager
   - Screen routes are defined in Screen sealed class
   - Use composable parameters for necessary data passing

3. **Theming System Structure**
   - All UI components must use MaterialTheme colors/typography
   - Themes are applied via OmnicastTheme composable
   - Custom theme colors are generated through ThemeManager

### Dependencies on Other Developers

Your work will be the foundation that all other developers build upon. However, you have minimal dependencies on others, which positions you as the starting point for development.

#### Outgoing Dependencies:
- You will need to know all planned feature screens to set up navigation
- You should understand all content JSON structures for content loading system
- Understanding of UI requirements for all features to create appropriate base components

#### Incoming Dependencies:
- All other developers depend on your navigation system
- All feature UIs depend on your theming system
- All data operations depend on your Room/DataStore setup

### Technical Guidelines

1. **Composable Design**
   - Keep composables small and focused
   - Extract reusable parts into separate composables
   - Use preview functions for all UI components
   - Follow Compose performance best practices

2. **Navigation Implementation**
   - Use type-safe navigation with arguments
   - Handle back press behavior consistently
   - Support deep linking where appropriate
   - Implement proper navigation animations

3. **Theme Implementation**
   - Ensure all colors meet accessibility contrast requirements
   - Implement both light and dark theme variants
   - Support dynamic color from Material 3
   - Create theme extensions for zodiac and biorhythm

4. **Database & Preferences**
   - Set up appropriate migrations strategy
   - Use type converters for complex types
   - Implement efficient queries with indices
   - Properly handle background operations

## Deliverables & Acceptance Criteria

### 1. Core Infrastructure
- Complete module structure with proper dependencies
- Working navigation between all screens
- Fully functional theming system
- DataStore preferences implementation
- Room database configuration

**Acceptance Criteria**:
- Project builds successfully with all modules
- Navigation works between placeholder screens
- Theme switching is functional with persistence
- Database operations can be performed
- DataStore operations functional

### 2. Base UI Components
- Common UI components library
- Consistent styling across components
- Accessible UI elements with proper semantics
- Loading and error states

**Acceptance Criteria**:
- All components render correctly across themes
- Components adapt to different screen sizes
- UI meets accessibility guidelines
- Components follow Material Design 3 specifications

### 3. Home Screen
- Fully functional home screen with navigation to features
- Feature carousel with icons and descriptions
- Daily overview summary with data from multiple sources
- Proper state handling (loading, error, content)

**Acceptance Criteria**:
- Home screen renders all features correctly
- Navigation to each feature works
- UI is responsive across device sizes
- State transitions are smooth and predictable

## Integration Notes

As Developer 1, you are creating the foundation that others will build upon. To ensure smooth integration:

1. **Documentation**
   - Document all public APIs with KDoc comments
   - Create usage examples for common patterns
   - Document expected navigation parameters
   - Create README files for each module

2. **Flexibility vs. Standardization**
   - Design components to be flexible but enforce consistency
   - Create clear patterns that other developers can follow
   - Anticipate variations in feature requirements

3. **Performance Considerations**
   - Optimize database access patterns
   - Use efficient Compose rendering techniques
   - Consider lazy loading for better performance
   - Implement proper Coroutine dispatchers

## Testing Requirements

Implement comprehensive tests for your code:

1. **Unit Tests**
   - ThemeManager functionality
   - Navigation actions and routing
   - Repository implementations
   - Use cases

2. **Integration Tests**
   - Database operations with Room
   - DataStore preference saving/loading
   - Asset loading from JSON files

3. **UI Tests**
   - Home screen rendering
   - Theme switching behavior
   - Navigation between screens

## Final Notes

As Developer 1, you are establishing the foundation and patterns that all other developers will follow. Your work should prioritize:

1. **Stability**: Core components must be reliable
2. **Flexibility**: Design for extension and varied use cases
3. **Performance**: Efficient patterns for others to follow
4. **Documentation**: Clear guidelines for other developers

Please reach out if you have any questions about architectural decisions or implementation priorities.

---

**Project Manager Contact**: [Name & Contact Method]