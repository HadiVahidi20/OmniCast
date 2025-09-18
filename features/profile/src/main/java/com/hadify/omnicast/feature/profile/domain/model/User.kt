package com.hadify.omnicast.feature.profile.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * User domain model
 * This is the core user data that other features will depend on
 */
data class User(
    val id: String,
    val name: String,
    val birthdate: LocalDate, // CRITICAL: All other features need this for calculations!
    val email: String? = null,
    val profilePicture: String? = null,
    val location: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    /**
     * Calculate age from birthdate
     */
    fun getAge(): Int {
        val today = LocalDate.now()
        return today.year - birthdate.year -
                if (today.dayOfYear < birthdate.dayOfYear) 1 else 0
    }

    /**
     * Check if profile is complete (has required fields)
     */
    fun isComplete(): Boolean {
        return name.isNotBlank()
    }

    /**
     * Get a display-friendly birthdate string
     */
    fun getBirthdateString(): String {
        return "${birthdate.monthValue}/${birthdate.dayOfMonth}/${birthdate.year}"
    }
}