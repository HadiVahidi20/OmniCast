package com.hadify.omnicast.feature.profile.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.hadify.omnicast.feature.profile.domain.model.User
import com.hadify.omnicast.feature.profile.domain.repository.UserRepository
import com.hadify.omnicast.core.data.local.dao.UserDao  // ← UPDATED: Now from core-data
import com.hadify.omnicast.core.data.local.entity.UserEntity  // ← UPDATED: Now from core-data
import com.hadify.omnicast.core.data.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of UserRepository
 * This connects the domain layer with the data layer
 */
@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    /**
     * Get current user profile as Flow
     * UI will automatically update when this changes
     */
    override fun getCurrentUser(): Flow<Resource<User?>> {
        return userDao.getCurrentUser().map { userEntity ->
            try {
                Resource.Success(userEntity?.toDomain())
            } catch (e: Exception) {
                Resource.Error("Failed to get user: ${e.message}")
            }
        }
    }

    /**
     * Get user by ID
     */
    override suspend fun getUserById(userId: String): Resource<User?> {
        return try {
            val userEntity = userDao.getUserById(userId)
            Resource.Success(userEntity?.toDomain())
        } catch (e: Exception) {
            Resource.Error("Failed to get user by ID: ${e.message}")
        }
    }

    /**
     * Save user profile (insert or update)
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun saveUser(user: User): Resource<User> {
        return try {
            val updatedUser = user.copy(updatedAt = LocalDateTime.now())
            val userEntity = updatedUser.toEntity()
            userDao.insertUser(userEntity)
            Resource.Success(updatedUser)
        } catch (e: Exception) {
            Resource.Error("Failed to save user: ${e.message}")
        }
    }

    /**
     * Update user profile
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun updateUser(user: User): Resource<User> {
        return try {
            val updatedUser = user.copy(updatedAt = LocalDateTime.now())
            val userEntity = updatedUser.toEntity()
            userDao.updateUser(userEntity)
            Resource.Success(updatedUser)
        } catch (e: Exception) {
            Resource.Error("Failed to update user: ${e.message}")
        }
    }

    /**
     * Delete user profile
     */
    override suspend fun deleteUser(userId: String): Resource<Unit> {
        return try {
            userDao.deleteUser(userId)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Failed to delete user: ${e.message}")
        }
    }

    /**
     * Check if user exists
     */
    override suspend fun userExists(): Boolean {
        return try {
            userDao.userExists()
        } catch (e: Exception) {
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
            Resource.Success(birthdate)
        } catch (e: Exception) {
            Resource.Error("Failed to get birthdate: ${e.message}")
        }
    }

    // ← ADDED: Conversion methods between domain and data models

    /**
     * Convert domain User to database UserEntity
     */
    private fun User.toEntity(): UserEntity {
        return UserEntity(
            id = id,
            name = name,
            birthdate = birthdate,
            email = email,
            profilePicture = profilePicture,
            location = location,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    /**
     * Convert database UserEntity to domain User
     */
    private fun UserEntity.toDomain(): User {
        return User(
            id = id,
            name = name,
            birthdate = birthdate,
            email = email,
            profilePicture = profilePicture,
            location = location,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}