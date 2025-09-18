package com.hadify.omnicast.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Room database entity for User
 * This defines how user data is stored in the local database
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val birthdate: LocalDate, // CRITICAL: This is what other developers need!
    val email: String? = null,
    val profilePicture: String? = null,
    val location: String? = null,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)