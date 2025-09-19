package com.hadify.omnicast.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hadify.omnicast.core.domain.model.ZodiacElement
import com.hadify.omnicast.core.domain.model.ZodiacQuality

@Entity(tableName = "zodiac_entity")
data class ZodiacEntity(
    @PrimaryKey
    val name: String,
    val description: String,
    val symbol: String,
    val element: ZodiacElement,
    val quality: ZodiacQuality,
    val rulingPlanet: String,
    val dateRange: String
)