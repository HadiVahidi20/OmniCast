package com.hadify.omnicast.feature.profile.data.repository

import com.hadify.omnicast.feature.profile.domain.model.User
import com.hadify.omnicast.feature.profile.domain.repository.UserRepository
import com.hadify.omnicast.core.data.local.dao.UserDao
import com.hadify.omnicast.core.data.util.Resource
import com.hadify.omnicast.core.common.util.DateTimeUtils
import com.hadify.omnicast.feature.profile.data.mapper.toDomain // Import
import com.hadify.omnicast.feature.profile.data.mapper.toEntity   // Import
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton
import android.util.Log

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    companion object {
        private const val TAG = "UserRepositoryImpl"
    }

    override fun getCurrentUser(): Flow<Resource<User?>> {
        return userDao.getCurrentUser().map { userEntity ->
            try {
                Log.d(TAG, "Loading user from database: ${userEntity?.name}")
                Resource.success(userEntity?.toDomain())
            } catch (e: Exception) {
                Log.e(TAG, "Failed to get user from database", e)
                Resource.error("Failed to get user: ${e.message}")
            }
        }
    }

    override suspend fun getUserById(userId: String): Resource<User?> {
        return try {
            Log.d(TAG, "Getting user by ID: $userId")
            val userEntity = userDao.getUserById(userId)
            Resource.success(userEntity?.toDomain())
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get user by ID: $userId", e)
            Resource.error("Failed to get user by ID: ${e.message}")
        }
    }

    override suspend fun saveUser(user: User): Resource<User> {
        return try {
            Log.d(TAG, "Saving user: ${user.name}, birthdate: ${user.birthdate}")

            if (!DateTimeUtils.isValidBirthdate(user.birthdate)) {
                Log.e(TAG, "Invalid birthdate provided: ${user.birthdate}")
                return Resource.error("Invalid birthdate. Please select a valid date.")
            }

            val updatedUser = user.copy(updatedAt = DateTimeUtils.now())
            val userEntity = updatedUser.toEntity()

            val insertId = userDao.insertUser(userEntity)
            Log.d(TAG, "User saved successfully with ID: $insertId")

            Resource.success(updatedUser)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save user: ${user.name}", e)
            Resource.error("Failed to save profile: ${e.localizedMessage ?: e.message ?: "Unknown error"}")
        }
    }

    override suspend fun updateUser(user: User): Resource<User> {
        return try {
            Log.d(TAG, "Updating user: ${user.name}")
            if (!DateTimeUtils.isValidBirthdate(user.birthdate)) {
                Log.e(TAG, "Invalid birthdate provided for update: ${user.birthdate}")
                return Resource.error("Invalid birthdate. Please select a valid date.")
            }
            val updatedUser = user.copy(updatedAt = DateTimeUtils.now())
            val userEntity = updatedUser.toEntity()
            userDao.updateUser(userEntity)
            Log.d(TAG, "User updated successfully: ${user.name}")
            Resource.success(updatedUser)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to update user: ${user.name}", e)
            Resource.error("Failed to update profile: ${e.localizedMessage ?: e.message ?: "Unknown error"}")
        }
    }

    override suspend fun deleteUser(userId: String): Resource<Unit> {
        return try {
            Log.d(TAG, "Deleting user: $userId")
            userDao.deleteUser(userId)
            Log.d(TAG, "User deleted successfully: $userId")
            Resource.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to delete user: $userId", e)
            Resource.error("Failed to delete profile: ${e.message}")
        }
    }

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

    override suspend fun getUserBirthdate(): Resource<LocalDate?> {
        return try {
            val birthdate = userDao.getUserBirthdate()
            Log.d(TAG, "Retrieved user birthdate: $birthdate")
            Resource.success(birthdate)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get user birthdate", e)
            Resource.error(e)
        }
    }

    override suspend fun getUserAge(): Int? {
        return try {
            val userEntity = userDao.getCurrentUserSync()
            userEntity?.birthdate?.let { DateTimeUtils.calculateAge(it) }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to calculate user age", e)
            null
        }
    }
}