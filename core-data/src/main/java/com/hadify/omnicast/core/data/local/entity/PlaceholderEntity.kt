package com.hadify.omnicast.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Temporary placeholder entity to satisfy Room database requirements
 * Will be removed once real entities are added by other developers
 */
@Entity(tableName = "placeholder")
data class PlaceholderEntity(
    @PrimaryKey
    val id: String = "placeholder"
)