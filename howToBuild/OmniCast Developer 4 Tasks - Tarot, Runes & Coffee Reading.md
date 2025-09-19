# OmniCast Developer 4 Tasks
## Tarot, Runes & Coffee Reading

This document outlines your specific responsibilities, deliverables, and guidelines as Developer 4 on the OmniCast project. You are responsible for implementing the symbol-based divination features: Tarot Card readings, Rune casting, and Coffee symbolism interpretations.

## Your Primary Responsibilities

As Developer 4, you are responsible for:

1. Creating the Tarot card reading system with full deck
2. Building the Rune casting feature with Norse runestones
3. Implementing the Coffee reading symbol interpretation
4. Developing rich visual presentations for divination tools
5. Creating engaging card/symbol reveal animations

## Modules You Own

You have complete ownership of the following modules:

### 1. `features:tarot` Module
- Tarot card selection algorithm
- Card meaning interpretation
- Card spread layouts and interactions
- Card visualization and animations

### 2. `features:rune` Module
- Rune symbol selection
- Rune meaning interpretations
- Rune casting visualization
- Historical and mythological context

### 3. `features:coffee` Module
- Coffee symbol identification
- Symbol meaning interpretations
- Symbol visualization
- Cultural context and explanations

## Detailed Task Breakdown

### Phase 1: Tarot Implementation (Weeks 1-3)

#### 1.1 Tarot Domain Layer
- [ ] Create `TarotCard` domain model
- [ ] Implement `TarotReading` domain model
- [ ] Create `TarotRepository` interface
- [ ] Implement `GetDailyTarotCardUseCase`
- [ ] Build `GetCardMeaningUseCase`
- [ ] Create `PerformTarotSpreadUseCase` (optional for multi-card readings)

#### 1.2 Tarot Data Layer
- [ ] Create `TarotEntity` for caching results
- [ ] Implement `TarotDao` interface
- [ ] Build `TarotRepositoryImpl` class
- [ ] Create loader for tarot JSON content
- [ ] Implement deterministic card selection algorithm

#### 1.3 Tarot UI Layer
- [ ] Create `TarotScreen` composable
- [ ] Implement `TarotViewModel`
- [ ] Build `TarotCardView` component
- [ ] Create `TarotInterpretation` component
- [ ] Implement card reveal animations
- [ ] Add card flip interaction
- [ ] Create optional multi-card spread layouts

### Phase 2: Rune Implementation (Weeks 4-6)

#### 2.1 Rune Domain Layer
- [ ] Create `RuneSymbol` domain model
- [ ] Implement `RuneReading` domain model
- [ ] Create `RuneRepository` interface
- [ ] Implement `GetDailyRuneUseCase`
- [ ] Build `GetRuneMeaningUseCase`

#### 2.2 Rune Data Layer
- [ ] Create necessary entities for caching
- [ ] Implement `RuneDao` interface
- [ ] Build `RuneRepositoryImpl` class
- [ ] Create loader for rune JSON content
- [ ] Implement deterministic rune selection

#### 2.3 Rune UI Layer
- [ ] Create `RuneScreen` composable
- [ ] Implement `RuneViewModel`
- [ ] Build `RuneSymbolView` component
- [ ] Create `RuneInterpretation` component
- [ ] Implement rune casting animation
- [ ] Add historical and mythological context view

### Phase 3: Coffee Reading Implementation (Weeks 7-8)

#### 3.1 Coffee Reading Domain Layer
- [ ] Create `CoffeeSymbol` domain model
- [ ] Implement `CoffeeReading` domain model
- [ ] Create `CoffeeReadingRepository` interface
- [ ] Implement `GetCoffeeReadingUseCase`
- [ ] Build `GetSymbolMeaningUseCase`

#### 3.2 Coffee Reading Data Layer
- [ ] Create necessary entities for caching
- [ ] Implement repository implementation
- [ ] Create loader for coffee reading JSON content
- [ ] Implement deterministic symbol selection

#### 3.3 Coffee Reading UI Layer
- [ ] Create `CoffeeReadingScreen` composable
- [ ] Implement `CoffeeReadingViewModel`
- [ ] Build `CoffeeSymbolView` component
- [ ] Create `SymbolInterpretation` component
- [ ] Implement symbol reveal animations
- [ ] Add cultural context explanation

## Implementation Guidelines

### Tarot Card Implementation

The Tarot feature requires careful implementation of a complete card system with rich visuals.

1. **Card System Requirements**
   - Implement complete 78-card deck:
     - 22 Major Arcana
     - 56 Minor Arcana (4 suits of 14 cards)
   - Support both upright and reversed card meanings
   - Implement appropriate card selection algorithm using date seed
   - Create single card and optional multi-card spreads

2. **Visualization Requirements**
   - High-quality card images with proper assets
   - Smooth card flip animations
   - Card spread layouts (if implementing multi-card readings)
   - Visually distinguish Major and Minor Arcana

3. **Interpretation Content**
   - Display card name, keywords, and meanings
   - Show appropriate interpretation based on upright/reversed
   - Include questions for reflection
   - Display card symbolism explanation

### Rune Implementation

The Rune feature focuses on Norse divination with rich historical context.

1. **Rune System Requirements**
   - Implement complete Elder Futhark (24 runes)
   - Support both upright and reversed (merkstave) meanings
   - Implement appropriate selection algorithm
   - Consider aettir groupings for organization

2. **Visualization Requirements**
   - Clear rune symbol rendering
   - Casting animation for random selection
   - Historical context and visual references
   - Proper rune stone appearance

3. **Interpretation Content**
   - Display rune name, phonetic value, and meaning
   - Show appropriate interpretation based on position
   - Include divinatory significance
   - Provide mythological and historical context

### Coffee Reading Implementation

The Coffee Reading feature presents symbols found in coffee grounds with cultural context.

1. **Symbol System Requirements**
   - Implement 40-50 common coffee reading symbols
   - Support symbol combinations and positions
   - Implement appropriate selection algorithm
   - Consider cultural variations in interpretation

2. **Visualization Requirements**
   - Visual representation of coffee cup
   - Symbol placement within the cup
   - Cultural and traditional visual references
   - Intuitive symbol discovery interactions

3. **Interpretation Content**
   - Display symbol name and meaning
   - Show interpretation based on position in cup
   - Include cultural context and origins
   - Provide practical advice based on symbols

## Dependencies on Other Modules

Your implementation will interact with several other modules:

### Incoming Dependencies
- Core navigation system (from Dev 1)
- Base theming (from Dev 1)
- Database and DataStore foundation (from Dev 1)
- UI component library (from Dev 1)
- User profile data (from Dev 2) - may be used for personalization

### Outgoing Dependencies
- Your features will provide data to the Daily Overview (Dev 3)
- Prediction results may be referenced in Journal entries (Dev 3)

## Technical Requirements

### 1. Content Management
- Efficiently load and parse JSON content files
- Implement proper caching for performance
- Handle content updates gracefully
- Support multiple languages (coordinate with localization strategy)

### 2. Animation and Interaction
- Create smooth, engaging animations
- Implement intuitive touch interactions
- Ensure animations are performant on all devices
- Add haptic feedback where appropriate

### 3. Visual Asset Management
- Optimize image assets for size and quality
- Implement appropriate image loading strategies
- Handle different screen densities
- Consider accessibility for visually impaired users

### 4. State Management
- Use StateFlow/SharedFlow for reactive UI
- Implement proper state restoration
- Handle all states (loading, error, empty, content)
- Save and restore interaction state

## Testing Requirements

Implement comprehensive tests for your modules:

### 1. Unit Tests
- Test selection algorithms thoroughly
- Verify interpretation logic
- Test all use cases
- Verify content loading and parsing

### 2. Integration Tests
- Test database operations
- Test interactions with other features
- Verify persistent state saving

### 3. UI Tests
- Test card/symbol interactions
- Verify animations and transitions
- Test accessibility features

## Deliverables & Acceptance Criteria

### 1. Tarot Module
**Deliverables**:
- Complete tarot card reading feature
- Card visualization and animation
- Detailed card interpretations
- Optional multi-card spreads

**Acceptance Criteria**:
- All 78 cards implemented with proper meanings
- Selection algorithm produces consistent daily cards
- UI is visually appealing and interactive
- Card flip and reveal animations are smooth

### 2. Rune Module
**Deliverables**:
- Complete rune casting feature
- Rune symbol visualization
- Detailed rune interpretations
- Historical and mythological context

**Acceptance Criteria**:
- All 24 Elder Futhark runes implemented
- Selection algorithm works properly
- UI presents runes clearly with proper context
- Animations are smooth and engaging

### 3. Coffee Reading Module
**Deliverables**:
- Coffee symbol reading feature
- Symbol visualization
- Detailed symbol interpretations
- Cultural context and background

**Acceptance Criteria**:
- Comprehensive symbol library implemented
- Selection algorithm works properly
- Symbols are clearly presented and explained
- Cultural context adds depth to interpretations

## Integration Notes

To ensure smooth integration with other modules:

1. **JSON Content Structure**
   - Follow the established JSON schema for all content
   - Ensure content is properly formatted and validated
   - Work with content creators to ensure quality and consistency

2. **UI Consistency**
   - Follow the established design system
   - Reuse core UI components
   - Ensure your features match the overall app aesthetic
   - Implement proper dark/light mode support

3. **Performance Considerations**
   - Optimize image loading and caching
   - Use efficient animation techniques
   - Implement proper state restoration
   - Consider memory usage for image assets

## Final Notes

Your modules represent the mystical, symbolic heart of OmniCast. These divination methods have rich historical and cultural significance, and your implementation should capture their depth while making them accessible and engaging for users.

Pay special attention to the visual presentation and interactive aspects—these features benefit greatly from thoughtful animation and tactile interaction. The reveal of a card or symbol should feel momentous and special to the user.

Please reach out if you have questions about the divination systems, visualization techniques, or integration with other features.

---

**Project Manager Contact**: [Name & Contact Method]