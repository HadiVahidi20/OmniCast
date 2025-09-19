package com.hadify.omnicast.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hadify.omnicast.core.domain.model.ZodiacElement
import com.hadify.omnicast.core.domain.model.ZodiacQuality
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Room entity for storing zodiac horoscope readings
 * This stores daily horoscope predictions in the local database
 */
@Entity(tableName = "horoscope_readings")
data class ZodiacEntity(
    @PrimaryKey
    val id: String, // Primary key for the horoscope reading

    val zodiacSignName: String, // e.g., "ARIES", "TAURUS"
    val date: LocalDate, // Date this horoscope is for

    // Horoscope content
    val general: String,
    val love: String,
    val career: String,
    val health: String,

    // Additional details
    val luckyNumber: Int,
    val luckyColor: String,
    val compatibleSign: String,
    val mood: String,
    val tags: String, // JSON string of tags list
    val intensity: Float = 0.5f, // 0.0 to 1.0

    // Zodiac sign properties (for quick access)
    val symbol: String,
    val element: ZodiacElement,
    val quality: ZodiacQuality,
    val rulingPlanet: String,
    val dateRange: String,

    // Metadata
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)