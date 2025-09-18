package com.hadify.omnicast.core.data.local.dao

import androidx.room.*
import com.hadify.omnicast.core.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * Room DAO for User operations
 * This defines all database operations for users
 */
@Dao
interface UserDao {

    /**
     * Get current user (we'll assume single user app for now)
     * Returns Flow so UI can react to changes
     */
    @Query("SELECT * FROM users ORDER BY createdAt DESC LIMIT 1")
    fun getCurrentUser(): Flow<UserEntity?>

    /**
     * Get user by ID
     */
    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: String): UserEntity?

    /**
     * Insert new user
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    /**
     * Update existing user
     */
    @Update
    suspend fun updateUser(user: UserEntity)

    /**
     * Delete user by ID
     */
    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUser(userId: String)

    /**
     * Check if any user exists
     */
    @Query("SELECT COUNT(*) > 0 FROM users")
    suspend fun userExists(): Boolean

    /**
     * Get user's birthdate (critical for other features!)
     * This is what other developers will use for calculations
     */
    @Query("SELECT birthdate FROM users ORDER BY createdAt DESC LIMIT 1")
    suspend fun getUserBirthdate(): LocalDate?

    /**
     * Delete all users (useful for testing)
     */
    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()
}