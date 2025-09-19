package com.hadify.omnicast.core.domain.model

/**
 * Zodiac Qualities (Modalities) enumeration
 * Core domain model that can be used across all modules
 */
enum class ZodiacQuality(val displayName: String, val description: String) {
    CARDINAL(
        displayName = "Cardinal",
        description = "Initiators, leaders, start new projects"
    ),
    FIXED(
        displayName = "Fixed",
        description = "Stabilizers, persistent, see things through"
    ),
    MUTABLE(
        displayName = "Mutable",
        description = "Adaptable, flexible, embrace change"
    );

    companion object {
        /**
         * Get quality characteristics for personality traits
         */
        fun ZodiacQuality.getCharacteristics(): List<String> {
            return when (this) {
                CARDINAL -> listOf(
                    "Natural leaders",
                    "Initiative takers",
                    "Project starters",
                    "Ambitious",
                    "Pioneer spirit"
                )
                FIXED -> listOf(
                    "Determined",
                    "Loyal",
                    "Persistent",
                    "Reliable",
                    "Strong-willed"
                )
                MUTABLE -> listOf(
                    "Flexible",
                    "Adaptable",
                    "Curious",
                    "Versatile",
                    "Quick learners"
                )
            }
        }
    }
}