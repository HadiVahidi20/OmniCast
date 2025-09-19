package com.hadify.omnicast.core.domain.model

/**
 * Zodiac Elements enumeration
 * Core domain model that can be used across all modules
 */
enum class ZodiacElement(val displayName: String) {
    FIRE("Fire"),
    EARTH("Earth"),
    AIR("Air"),
    WATER("Water");

    companion object {
        /**
         * Get compatible elements for this element
         */
        fun ZodiacElement.getCompatibleElements(): List<ZodiacElement> {
            return when (this) {
                FIRE -> listOf(AIR)  // Fire needs Air to burn
                EARTH -> listOf(WATER)  // Earth needs Water to grow
                AIR -> listOf(FIRE)  // Air feeds Fire
                WATER -> listOf(EARTH)  // Water nourishes Earth
            }
        }

        /**
         * Get element color for theming
         */
        fun ZodiacElement.getThemeColor(): String {
            return when (this) {
                FIRE -> "#FF4444" // Red
                EARTH -> "#8B4513" // Brown
                AIR -> "#87CEEB" // Sky Blue
                WATER -> "#4169E1" // Royal Blue
            }
        }
    }
}