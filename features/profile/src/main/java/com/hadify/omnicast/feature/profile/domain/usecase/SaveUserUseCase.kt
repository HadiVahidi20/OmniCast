package com.hadify.omnicast.feature.profile.domain.usecase

import com.hadify.omnicast.feature.profile.domain.model.User
import com.hadify.omnicast.feature.profile.domain.repository.UserRepository
import com.hadify.omnicast.core.data.util.Resource
import com.hadify.omnicast.core.domain.usecase.BaseUseCase
import com.hadify.omnicast.core.common.util.DateTimeUtils
import javax.inject.Inject
import android.util.Log

/**
 * Use case to save/update user profile
 * ENHANCED: Better validation and error handling
 */
class SaveUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) : BaseUseCase<User, Resource<User>> {

    companion object {
        private const val TAG = "SaveUserUseCase"
    }

    /**
     * Save or update user profile
     * ENHANCED: Comprehensive validation logic
     */
    override suspend fun invoke(params: User): Resource<User> {
        Log.d(TAG, "Attempting to save user: ${params.name}")

        // Comprehensive validation
        val validationError = validateUser(params)
        if (validationError != null) {
            Log.w(TAG, "User validation failed: $validationError")
            return Resource.Error(validationError)
        }

        return try {
            Log.d(TAG, "Calling repository to save user: ${params.name}")
            val result = userRepository.saveUser(params)

            when (result) {
                is Resource.Success -> {
                    Log.d(TAG, "User saved successfully: ${result.data.name}")
                }
                is Resource.Error -> {
                    Log.e(TAG, "Repository failed to save user: ${result.message}")
                }
                is Resource.Loading -> {
                    Log.d(TAG, "Repository save operation in progress")
                }
            }

            result
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error in SaveUserUseCase", e)
            Resource.Error("Failed to save profile: ${e.localizedMessage ?: e.message ?: "Unknown error"}")
        }
    }

    /**
     * Comprehensive user validation
     */
    private fun validateUser(user: User): String? {
        // Name validation
        if (user.name.isBlank()) {
            return "Name cannot be empty"
        }

        if (user.name.length < 2) {
            return "Name must be at least 2 characters long"
        }

        if (user.name.length > 100) {
            return "Name is too long (maximum 100 characters)"
        }

        // Name character validation (allow letters, spaces, and common symbols)
        if (!user.name.matches(Regex("^[a-zA-Z\\s'.-]+$"))) {
            return "Name contains invalid characters"
        }

        // Birthdate validation
        if (!DateTimeUtils.isValidBirthdate(user.birthdate)) {
            return "Invalid birthdate. Please select a date between 1900 and today."
        }

        // Age validation (must be reasonable)
        val age = DateTimeUtils.calculateAge(user.birthdate)
        if (age > 150) {
            return "Age cannot be more than 150 years"
        }

        if (age < 0) {
            return "Birthdate cannot be in the future"
        }

        // Email validation (if provided)
        if (!user.email.isNullOrBlank()) {
            if (user.email.length > 200) {
                return "Email address is too long"
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(user.email).matches()) {
                return "Invalid email address format"
            }
        }

        // Location validation (if provided)
        if (!user.location.isNullOrBlank()) {
            if (user.location.length > 200) {
                return "Location is too long (maximum 200 characters)"
            }
        }

        // ID validation
        if (user.id.isBlank()) {
            return "User ID cannot be empty"
        }

        // DateTime validation
        try {
            if (user.createdAt.year < 2020 || user.createdAt.year > 2100) {
                return "Invalid creation date"
            }

            if (user.updatedAt.year < 2020 || user.updatedAt.year > 2100) {
                return "Invalid update date"
            }
        } catch (e: Exception) {
            return "Invalid date format"
        }

        return null // All validation passed
    }
}