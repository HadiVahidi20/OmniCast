package com.hadify.omnicast.feature.profile.domain.repository

import com.hadify.omnicast.feature.profile.domain.model.User
import com.hadify.omnicast.core.data.util.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for User operations
 * This defines what operations we need for user management
 */
interface UserRepository {

    /**
     * Get current user profile
     * Returns Flow so UI can react to changes
     */
    fun getCurrentUser(): Flow<Resource<User?>>

    /**
     * Get user by ID
     */
    suspend fun getUserById(userId: String): Resource<User?>

    /**
     * Create or update user profile
     */
    suspend fun saveUser(user: User): Resource<User>

    /**
     * Update user profile
     */
    suspend fun updateUser(user: User): Resource<User>

    /**
     * Delete user profile
     */
    suspend fun deleteUser(userId: String): Resource<Unit>

    /**
     * Check if user exists
     */
    suspend fun userExists(): Boolean

    /**
     * Get user's birthdate (critical for other features!)
     */
    suspend fun getUserBirthdate(): Resource<java.time.LocalDate?>
    suspend fun getUserAge(): Int?

}