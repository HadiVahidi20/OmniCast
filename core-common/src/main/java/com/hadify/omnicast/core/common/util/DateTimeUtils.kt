package com.hadify.omnicast.core.common.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * Date and time utility functions
 * Provides common date/time operations and validations
 */
object DateTimeUtils {

    // Common date formatters
    val ISO_DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    val DISPLAY_DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
    val DISPLAY_DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")

    // Date range constants
    private val MIN_BIRTH_YEAR = 1900
    private val MAX_FUTURE_YEARS = 0 // No future dates allowed for birthdate

    /**
     * Gets current LocalDateTime - ADDED for compatibility
     */
    fun now(): LocalDateTime {
        return LocalDateTime.now()
    }

    /**
     * Gets current LocalDate
     */
    fun today(): LocalDate {
        return LocalDate.now()
    }

    /**
     * Validates if a birthdate is reasonable
     * Returns true if the date is between 1900 and today
     */
    fun isValidBirthdate(birthdate: LocalDate): Boolean {
        val today = LocalDate.now()
        val minDate = LocalDate.of(MIN_BIRTH_YEAR, 1, 1)
        val maxDate = today.minusYears(MAX_FUTURE_YEARS.toLong())

        return birthdate.isAfter(minDate.minusDays(1)) &&
                birthdate.isBefore(maxDate.plusDays(1))
    }

    /**
     * Calculates age from birthdate
     */
    fun calculateAge(birthdate: LocalDate): Int {
        val today = LocalDate.now()
        var age = today.year - birthdate.year

        if (today.dayOfYear < birthdate.dayOfYear) {
            age--
        }

        return maxOf(0, age)
    }

    /**
     * Formats a LocalDate for display
     */
    fun formatDisplayDate(date: LocalDate): String {
        return date.format(DISPLAY_DATE_FORMATTER)
    }

    /**
     * Formats a LocalDateTime for display
     */
    fun formatDisplayDateTime(dateTime: LocalDateTime): String {
        return dateTime.format(DISPLAY_DATE_TIME_FORMATTER)
    }

    /**
     * Parses a date string safely
     * Returns null if parsing fails
     */
    fun parseDate(dateString: String): LocalDate? {
        return try {
            LocalDate.parse(dateString, ISO_DATE_FORMATTER)
        } catch (e: DateTimeParseException) {
            null
        }
    }

    /**
     * Parses a date-time string safely
     * Returns null if parsing fails
     */
    fun parseDateTime(dateTimeString: String): LocalDateTime? {
        return try {
            LocalDateTime.parse(dateTimeString)
        } catch (e: DateTimeParseException) {
            null
        }
    }

    /**
     * Gets the start of the current week (Monday)
     */
    fun getStartOfWeek(date: LocalDate = LocalDate.now()): LocalDate {
        return date.minusDays(date.dayOfWeek.value - 1L)
    }

    /**
     * Gets the end of the current week (Sunday)
     */
    fun getEndOfWeek(date: LocalDate = LocalDate.now()): LocalDate {
        return date.plusDays(7L - date.dayOfWeek.value)
    }

    /**
     * Checks if a date is today
     */
    fun isToday(date: LocalDate): Boolean {
        return date.isEqual(LocalDate.now())
    }

    /**
     * Checks if a date is in the current week
     */
    fun isCurrentWeek(date: LocalDate): Boolean {
        val startOfWeek = getStartOfWeek()
        val endOfWeek = getEndOfWeek()
        return date.isAfter(startOfWeek.minusDays(1)) &&
                date.isBefore(endOfWeek.plusDays(1))
    }
}