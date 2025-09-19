package com.hadify.omnicast.core.common.util

import android.os.Build
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

/**
 * Utility class to handle DateTime operations safely across all API levels
 * Fixes the API 26+ compatibility issue with LocalDate/LocalDateTime
 */
object DateTimeUtils {

    /**
     * Get current LocalDateTime safely across all API levels
     */
    fun now(): LocalDateTime {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now()
        } else {
            // Fallback for API < 26
            val calendar = Calendar.getInstance()
            LocalDateTime.of(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1, // Calendar months are 0-based
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND)
            )
        }
    }

    /**
     * Get current LocalDate safely across all API levels
     */
    fun today(): LocalDate {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.now()
        } else {
            // Fallback for API < 26
            val calendar = Calendar.getInstance()
            LocalDate.of(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1, // Calendar months are 0-based
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        }
    }

    /**
     * Validate if a date is reasonable for a birthdate
     */
    fun isValidBirthdate(date: LocalDate): Boolean {
        val today = today()
        val minDate = today.minusYears(150) // No one is older than 150
        val maxDate = today.minusDays(1)    // Must be at least 1 day old

        return date.isAfter(minDate) && date.isBefore(maxDate.plusDays(1))
    }

    /**
     * Calculate age from birthdate safely
     */
    fun calculateAge(birthdate: LocalDate): Int {
        val today = today()
        var age = today.year - birthdate.year

        // Check if birthday hasn't occurred this year yet
        if (today.monthValue < birthdate.monthValue ||
            (today.monthValue == birthdate.monthValue && today.dayOfMonth < birthdate.dayOfMonth)) {
            age--
        }

        return maxOf(0, age) // Ensure non-negative age
    }

    /**
     * Format date for display
     */
    fun formatDisplayDate(date: LocalDate): String {
        return "${date.monthValue}/${date.dayOfMonth}/${date.year}"
    }

    /**
     * Create LocalDateTime safely with validation
     */
    fun createDateTime(year: Int, month: Int, day: Int, hour: Int = 0, minute: Int = 0): LocalDateTime? {
        return try {
            val dateTime = LocalDateTime.of(year, month, day, hour, minute)
            // Validate the created date
            if (dateTime.year in 1900..2100) dateTime else null
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Create LocalDate safely with validation
     */
    fun createDate(year: Int, month: Int, day: Int): LocalDate? {
        return try {
            val date = LocalDate.of(year, month, day)
            // Basic validation
            if (date.year in 1900..2100) date else null
        } catch (e: Exception) {
            null
        }
    }
}