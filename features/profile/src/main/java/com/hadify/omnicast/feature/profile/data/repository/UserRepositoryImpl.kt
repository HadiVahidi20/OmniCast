package com.hadify.omnicast.feature.profile.data.repository

import com.hadify.omnicast.feature.profile.domain.model.User
import com.hadify.omnicast.feature.profile.domain.repository.UserRepository
import com.hadify.omnicast.core.data.local.dao.UserDao
import com.hadify.omnicast.core.data.local.entity.UserEntity
import com.hadify.omnicast.core.data.util.Resource
import com.hadify.omnicast.core.common.util.DateTimeUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton
import android.util.Log

/**
 * Implementation of UserRepository
 * FIXED: Removed API version requirements and added proper error handling
 */
@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    companion object {
        private const val TAG = "UserRepositoryImpl"
    }

    /**
     * Get current user profile as Flow
     * UI will automatically update when this changes
     */
    override fun getCurrentUser(): Flow<Resource<User?>> {
        return userDao.getCurrentUser().map { userEntity ->
            try {
                Log.d(TAG, "Loading user from database: ${userEntity?.name}")
                Resource.Success(userEntity?.toDomain())
            } catch (e: Exception) {
                Log.e(TAG, "Failed to get user from database", e)
                Resource.Error("Failed to get user: ${e.message}")
            }
        }
    }

    /**
     * Get user by ID
     */
    override suspend fun getUserById(userId: String): Resource<User?> {
        return try {
            Log.d(TAG, "Getting user by ID: $userId")
            val userEntity = userDao.getUserById(userId)
            Resource.Success(userEntity?.toDomain())
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get user by ID: $userId", e)
            Resource.Error("Failed to get user by ID: ${e.message}")
        }
    }

    /**
     * Save user profile (insert or update)
     * FIXED: No longer requires API 26+, uses DateTimeUtils
     */
    override suspend fun saveUser(user: User): Resource<User> {
        return try {
            Log.d(TAG, "Saving user: ${user.name}, birthdate: ${user.birthdate}")

            // Validate birthdate
            if (!DateTimeUtils.isValidBirthdate(user.birthdate)) {
                Log.e(TAG, "Invalid birthdate provided: ${user.birthdate}")
                return Resource.Error("Invalid birthdate. Please select a valid date.")
            }

            // Use DateTimeUtils for safe DateTime creation
            val updatedUser = user.copy(updatedAt = DateTimeUtils.now())
            val userEntity = updatedUser.toEntity()

            val insertId = userDao.insertUser(userEntity)
            Log.d(TAG, "User saved successfully with ID: $insertId")

            Resource.Success(updatedUser)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save user: ${user.name}", e)
            Resource.Error("Failed to save profile: ${e.localizedMessage ?: e.message ?: "Unknown error"}")
        }
    }

    /**
     * Update user profile
     * FIXED: No longer requires API 26+, uses DateTimeUtils
     */
    override suspend fun updateUser(user: User): Resource<User> {
        return try {
            Log.d(TAG, "Updating user: ${user.name}")

            // Validate birthdate
            if (!DateTimeUtils.isValidBirthdate(user.birthdate)) {
                Log.e(TAG, "Invalid birthdate provided for update: ${user.birthdate}")
                return Resource.Error("Invalid birthdate. Please select a valid date.")
            }

            // Use DateTimeUtils for safe DateTime creation
            val updatedUser = user.copy(updatedAt = DateTimeUtils.now())
            val userEntity = updatedUser.toEntity()

            userDao.updateUser(userEntity)
            Log.d(TAG, "User updated successfully: ${user.name}")

            Resource.Success(updatedUser)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to update user: ${user.name}", e)
            Resource.Error("Failed to update profile: ${e.localizedMessage ?: e.message ?: "Unknown error"}")
        }
    }

    /**
     * Delete user profile
     */
    override suspend fun deleteUser(userId: String): Resource<Unit> {
        return try {
            Log.d(TAG, "Deleting user: $userId")
            userDao.deleteUser(userId)
            Log.d(TAG, "User deleted successfully: $userId")
            Resource.Success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to delete user: $userId", e)
            Resource.Error("Failed to delete profile: ${e.message}")
        }
    }

    /**
     * Check if user exists
     */
    override suspend fun userExists(): Boolean {
        return try {
            val exists = userDao.userExists()
            Log.d(TAG, "User exists check: $exists")
            exists
        } catch (e: Exception) {
            Log.e(TAG, "Error checking if user exists", e)
            false
        }
    }

    /**
     * Get user's birthdate - CRITICAL for other developers!
     * Other features (zodiac, biorhythm, numerology) depend on this
     */
    override suspend fun getUserBirthdate(): Resource<LocalDate?> {
        return try {
            val birthdate = userDao.getUserBirthdate()
            Log.d(TAG, "Retrieved user birthdate: $birthdate")
            Resource.Success(birthdate)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get user birthdate", e)
            Resource.Error("Failed to get birthdate: ${e.message}")
        }
    }

    // FIXED: Conversion methods with better error handling

    /**
     * Convert domain User to database UserEntity
     */
    private fun User.toEntity(): UserEntity {
        return try {
            UserEntity(
                id = id,
                name = name,
                birthdate = birthdate,
                email = email,
                profilePicture = profilePicture,
                location = location,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error converting User to UserEntity", e)
            throw e
        }
    }

    /**
     * Convert database UserEntity to domain User
     */
    private fun UserEntity.toDomain(): User {
        return try {
            User(
                id = id,
                name = name,
                birthdate = birthdate,
                email = email,
                profilePicture = profilePicture,
                location = location,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error converting UserEntity to User", e)
            throw e
        }
    }
}