# OmniCast Developer 3 Tasks
## Biorhythm, Journal & Overview

This document outlines your specific responsibilities, deliverables, and guidelines as Developer 3 on the OmniCast project. You are responsible for implementing the Biorhythm calculator, Journal functionality, and Daily Overview features.

## Your Primary Responsibilities

As Developer 3, you are responsible for:

1. Creating the Biorhythm calculation and visualization system
2. Building the Journal feature for user reflection and tracking
3. Implementing the Daily Overview that combines insights from multiple sources
4. Developing engaging data visualizations and charts
5. Creating feedback mechanisms for prediction accuracy

## Modules You Own

You have complete ownership of the following modules:

### 1. `features:biorhythm` Module
- Biorhythm calculation algorithms
- Cycle visualization charts
- Energy level interpretation
- Trend analysis and forecasting

### 2. `features:journal` Module
- Journal entry creation and management
- Mood and energy tracking
- Prediction accuracy feedback
- Historical entry browsing

### 3. `features:overview` Module
- Daily insights summary
- Cross-feature data integration
- Personalized recommendations
- Content organization and presentation

## Detailed Task Breakdown

### Phase 1: Biorhythm Implementation (Weeks 1-3)

#### 1.1 Biorhythm Domain Layer
- [ ] Create `Biorhythm` domain model
- [ ] Implement `BiorhythmCycle` model for each cycle type
- [ ] Create `BiorhythmRepository` interface
- [ ] Implement `CalculateBiorhythmUseCase`
- [ ] Build `GetWeeklyBiorhythmTrendUseCase`
- [ ] Create `GetBiorhythmInterpretationUseCase`

#### 1.2 Biorhythm Data Layer
- [ ] Create `BiorhythmEntity` for caching results
- [ ] Implement `BiorhythmDao` interface
- [ ] Build `BiorhythmRepositoryImpl` class
- [ ] Create loader for biorhythm interpretation content
- [ ] Implement calculation algorithms for all cycle types

#### 1.3 Biorhythm UI Layer
- [ ] Create `BiorhythmScreen` composable
- [ ] Implement `BiorhythmViewModel`
- [ ] Build `BiorhythmChart` visualization component
- [ ] Create `CycleDetailsCard` component
- [ ] Implement `BiorhythmForecastView`
- [ ] Add date selection for historical/future viewing

### Phase 2: Journal Implementation (Weeks 4-6)

#### 2.1 Journal Domain Layer
- [ ] Create `JournalEntry` domain model
- [ ] Implement `JournalRepository` interface
- [ ] Create `AddJournalEntryUseCase`
- [ ] Build `GetJournalEntriesUseCase`
- [ ] Implement `DeleteJournalEntryUseCase`
- [ ] Create `UpdateJournalEntryUseCase`

#### 2.2 Journal Data Layer
- [ ] Create `JournalEntity` for Room
- [ ] Implement `JournalDao` interface
- [ ] Build `JournalRepositoryImpl` class
- [ ] Add Firebase sync capability (optional, coordinate with Dev 5)
- [ ] Implement efficient querying and pagination

#### 2.3 Journal UI Layer
- [ ] Create `JournalScreen` composable
- [ ] Implement `JournalViewModel`
- [ ] Build `JournalEntryEditor` component
- [ ] Create `JournalEntryItem` list item
- [ ] Implement `JournalCalendarView` (optional)
- [ ] Add mood and energy tracking UI
- [ ] Create prediction feedback mechanism

### Phase 3: Daily Overview Implementation (Weeks 7-8)

#### 3.1 Overview Domain Layer
- [ ] Create `DailyOverview` domain model
- [ ] Implement `OverviewRepository` interface
- [ ] Create `GenerateDailyOverviewUseCase`
- [ ] Build integration with other features' data

#### 3.2 Overview Data Layer
- [ ] Create necessary entities for caching
- [ ] Implement `OverviewRepositoryImpl`
- [ ] Build aggregation logic for multiple data sources

#### 3.3 Overview UI Layer
- [ ] Create `DailyOverviewScreen` composable
- [ ] Implement `DailyOverviewViewModel`
- [ ] Build `OverviewCard` component
- [ ] Create section components for each prediction type
- [ ] Implement transitions and animations
- [ ] Add "share insights" functionality

## Implementation Guidelines

### Biorhythm Implementation

Biorhythm is a calculation-intensive feature that requires accurate math and engaging visualizations.

1. **Calculation Requirements**
   - Implement standard biorhythm formulas:
     - Physical cycle: 23 days
     - Emotional cycle: 28 days
     - Intellectual cycle: 33 days
     - Intuitive cycle: 38 days (optional)
   - Calculate values using: sin(2π × (days since birth / cycle length))
   - Handle edge cases like leap years correctly

2. **Visualization Requirements**
   - Create smooth, animated line charts
   - Implement pinch-to-zoom and scrolling
   - Use appropriate colors for each cycle
   - Clearly mark today's position
   - Show "critical days" (crossing the zero line)

3. **Interpretation Content**
   - Display appropriate text based on cycle positions
   - Combine insights from multiple cycles
   - Provide actionable advice based on energy levels

### Journal Implementation

The journal feature captures user reflections and tracks prediction accuracy.

1. **Entry Management**
   - Allow creating, editing, and deleting entries
   - Support rich text formatting (optional)
   - Implement date-based organization
   - Add search functionality

2. **Tracking Capabilities**
   - Allow mood tracking (5-point scale)
   - Enable energy level tracking
   - Implement prediction accuracy feedback
   - Support tagging and categorization

3. **Cloud Integration (Coordinate with Dev 5)**
   - Design for optional cloud backup
   - Support offline-first operation
   - Implement conflict resolution strategy
   - Ensure privacy and security

### Daily Overview Implementation

The overview feature combines insights from multiple prediction sources.

1. **Data Integration**
   - Pull data from Zodiac, Biorhythm, Tarot, etc.
   - Prioritize insights based on relevance
   - Avoid contradictions between different sources
   - Create cohesive narrative from disparate data

2. **UI Organization**
   - Present most important insights first
   - Group related content visually
   - Implement progressive disclosure for details
   - Use appropriate visualizations for different data types

3. **User Experience**
   - Create a delightful morning check-in experience
   - Implement pull-to-refresh for updates
   - Add animations for engagement
   - Support sharing insights

## Dependencies on Other Modules

Your implementation will interact with several other modules:

### Incoming Dependencies
- Core navigation system (from Dev 1)
- Base theming (from Dev 1)
- Database and DataStore foundation (from Dev 1)
- UI component library (from Dev 1)
- User profile data (from Dev 2) - critical for biorhythm calculations

### Outgoing Dependencies
- Daily Overview provides data to Home screen
- Biorhythm energy levels may affect theming
- Journal entries may reference other prediction features

## Technical Requirements

### 1. Calculation Performance
- Optimize biorhythm calculations for efficiency
- Cache results appropriately
- Use background processing for intensive calculations
- Implement lazy loading for historical data

### 2. Data Visualization
- Use efficient chart libraries or custom implementations
- Optimize rendering for smooth animations
- Support different screen sizes and orientations
- Ensure accessibility of visual data

### 3. State Management
- Use StateFlow/SharedFlow for reactive UI
- Implement proper state restoration
- Handle all states (loading, error, empty, content)

### 4. Storage Efficiency
- Implement pagination for journal entries
- Use efficient query patterns
- Consider data compression for long-term storage
- Implement appropriate caching strategies

## Testing Requirements

Implement comprehensive tests for your modules:

### 1. Unit Tests
- Test all calculation algorithms thoroughly
- Verify journal CRUD operations
- Test overview generation logic
- Test all use cases

### 2. Integration Tests
- Test database operations
- Test data aggregation functions
- Verify proper data flow between components

### 3. UI Tests
- Test chart interactions
- Verify journal entry workflows
- Test overview display and interactions

## Deliverables & Acceptance Criteria

### 1. Biorhythm Module
**Deliverables**:
- Complete biorhythm calculator
- Interactive cycle visualization charts
- Daily interpretations and advice
- Trend analysis view

**Acceptance Criteria**:
- Calculations are mathematically accurate
- Charts render smoothly and are interactive
- UI adapts to different screen sizes
- Interpretations change based on cycle positions

### 2. Journal Module
**Deliverables**:
- Journal entry management system
- Mood and energy tracking
- Prediction accuracy feedback
- Historical browsing interface

**Acceptance Criteria**:
- Create, read, update, delete functionality works correctly
- Entries persist across app restarts
- UI is intuitive and responsive
- Tracking features are easy to use

### 3. Overview Module
**Deliverables**:
- Daily insights summary screen
- Integration with multiple prediction sources
- Personalized recommendations
- Shareable insights

**Acceptance Criteria**:
- Correctly aggregates data from all available sources
- Prioritizes information effectively
- UI is visually appealing and informative
- Performance remains smooth with all data sources

## Integration Notes

To ensure smooth integration with other modules:

1. **Data Sharing**
   - Expose read-only views of data for other features
   - Document data structures and access patterns
   - Implement appropriate synchronization

2. **UI Consistency**
   - Follow the established design system
   - Reuse core UI components
   - Maintain consistent theming and animations

3. **Performance Considerations**
   - Minimize main thread work
   - Use appropriate background processing
   - Implement efficient data loading patterns

## Final Notes

Your modules form the analytical core of OmniCast, combining mathematical precision with personal reflection. The biorhythm feature offers objective, calculation-based predictions, while the journal captures subjective experiences, creating a powerful feedback loop for users.

The daily overview is particularly important as it ties together multiple divination methods into a cohesive experience, becoming many users' primary interaction with the app.

Please reach out if you have questions about the calculation algorithms, visualization techniques, or integration strategies.

---

**Project Manager Contact**: [Name & Contact Method]