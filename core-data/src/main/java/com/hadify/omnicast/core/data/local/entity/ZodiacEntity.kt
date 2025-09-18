package com.hadify.omnicast.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Room entity for caching horoscope readings
 * Located in core-data to avoid circular dependencies
 */
@Entity(tableName = "horoscope_readings")
data class ZodiacEntity(
    @PrimaryKey
    val id: String,
    val zodiacSignName: String, // Store as string for Room compatibility
    val date: LocalDate,
    val general: String,
    val love: String,
    val career: String,
    val health: String,
    val luckyNumber: Int,
    val luckyColor: String,
    val compatibilitySignName: String, // Store compatible sign as string
    val mood: String,
    val tags: List<String>, // Room will use converter for this
    val intensity: Float,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

/**
 * Entity for weekly horoscope readings
 */
@Entity(tableName = "weekly_horoscope_readings")
data class WeeklyZodiacEntity(
    @PrimaryKey
    val id: String,
    val zodiacSignName: String,
    val weekStartDate: LocalDate,
    val weekEndDate: LocalDate,
    val general: String,
    val love: String,
    val career: String,
    val health: String,
    val luckyDays: List<String>,
    val challengingDays: List<String>,
    val overallTrend: String,
    val tags: List<String>,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)