package com.hadify.omnicast.core.data.local.converter

import androidx.room.TypeConverter
import com.hadify.omnicast.core.domain.model.ZodiacElement
import com.hadify.omnicast.core.domain.model.ZodiacQuality
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Room TypeConverters for custom data types
 * These converters tell Room how to store custom objects in SQLite
 */
class TypeConverters {

    // LocalDate converters
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.format(DateTimeFormatter.ISO_LOCAL_DATE)
    }

    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? {
        return dateString?.let { LocalDate.parse(it, DateTimeFormatter.ISO_LOCAL_DATE) }
    }

    // LocalDateTime converters
    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): String? {
        return dateTime?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

    @TypeConverter
    fun toLocalDateTime(dateTimeString: String?): LocalDateTime? {
        return dateTimeString?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME) }
    }

    // ZodiacElement converters
    @TypeConverter
    fun fromZodiacElement(element: ZodiacElement?): String? {
        return element?.name
    }

    @TypeConverter
    fun toZodiacElement(elementName: String?): ZodiacElement? {
        return elementName?.let {
            try {
                ZodiacElement.valueOf(it)
            } catch (e: IllegalArgumentException) {
                null
            }
        }
    }

    // ZodiacQuality converters
    @TypeConverter
    fun fromZodiacQuality(quality: ZodiacQuality?): String? {
        return quality?.name
    }

    @TypeConverter
    fun toZodiacQuality(qualityName: String?): ZodiacQuality? {
        return qualityName?.let {
            try {
                ZodiacQuality.valueOf(it)
            } catch (e: IllegalArgumentException) {
                null
            }
        }
    }

    // String List converters (for JSON lists like tags, luckyDays, etc.)
    @TypeConverter
    fun fromStringList(list: List<String>?): String? {
        return list?.joinToString(separator = ",")
    }

    @TypeConverter
    fun toStringList(listString: String?): List<String>? {
        return listString?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() }
    }
}