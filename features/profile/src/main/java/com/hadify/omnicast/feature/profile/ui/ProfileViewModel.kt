package com.hadify.omnicast.feature.profile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadify.omnicast.feature.profile.domain.model.User
import com.hadify.omnicast.feature.profile.domain.usecase.GetUserProfileUseCase
import com.hadify.omnicast.feature.profile.domain.usecase.SaveUserUseCase
import com.hadify.omnicast.core.data.util.Resource
import com.hadify.omnicast.core.common.util.DateTimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject
import android.util.Log

/**
 * ViewModel for ProfileScreen
 * ENHANCED: Better error handling and API compatibility
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val saveUserUseCase: SaveUserUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "ProfileViewModel"
    }

    // UI State
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        Log.d(TAG, "ProfileViewModel initialized")
        loadUserProfile()
    }

    /**
     * Load user profile from database
     * ENHANCED: Better error handling and logging
     */
    private fun loadUserProfile() {
        Log.d(TAG, "Loading user profile...")
        viewModelScope.launch {
            try {
                getUserProfileUseCase().collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            Log.d(TAG, "Loading user profile...")
                            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                        }
                        is Resource.Success -> {
                            val user = resource.data
                            Log.d(TAG, "User profile loaded successfully: ${user?.name}")
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                user = user,
                                name = user?.name ?: "",
                                birthdate = user?.birthdate,
                                email = user?.email ?: "",
                                location = user?.location ?: "",
                                error = null
                            )
                        }
                        is Resource.Error -> {
                            Log.e(TAG, "Failed to load user profile: ${resource.message}")
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                error = resource.message ?: "Failed to load profile"
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Unexpected error loading profile", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Unexpected error: ${e.localizedMessage ?: e.message}"
                )
            }
        }
    }

    /**
     * Update name field with validation
     */
    fun updateName(name: String) {
        Log.d(TAG, "Updating name: $name")
        // Trim whitespace and limit length
        val cleanName = name.trim().take(100)
        _uiState.value = _uiState.value.copy(name = cleanName)
    }

    /**
     * Update birthdate field with validation
     * CRITICAL for other developers!
     */
    fun updateBirthdate(birthdate: LocalDate) {
        Log.d(TAG, "Updating birthdate: $birthdate")

        // Validate the birthdate
        if (!DateTimeUtils.isValidBirthdate(birthdate)) {
            _uiState.value = _uiState.value.copy(
                error = "Please select a valid birthdate"
            )
            return
        }

        _uiState.value = _uiState.value.copy(
            birthdate = birthdate,
            error = null // Clear any previous birthdate error
        )
    }

    /**
     * Update email field with basic validation
     */
    fun updateEmail(email: String) {
        Log.d(TAG, "Updating email")
        val cleanEmail = email.trim().take(200)

        // Basic email validation if not empty
        if (cleanEmail.isNotEmpty() && !isValidEmail(cleanEmail)) {
            _uiState.value = _uiState.value.copy(
                email = cleanEmail,
                error = "Please enter a valid email address"
            )
        } else {
            _uiState.value = _uiState.value.copy(
                email = cleanEmail,
                error = null
            )
        }
    }

    /**
     * Update location field
     */
    fun updateLocation(location: String) {
        Log.d(TAG, "Updating location")
        val cleanLocation = location.trim().take(200)
        _uiState.value = _uiState.value.copy(location = cleanLocation)
    }

    /**
     * Save user profile with comprehensive validation
     * ENHANCED: Better validation and error handling
     */
    fun saveProfile() {
        val currentState = _uiState.value
        Log.d(TAG, "Attempting to save profile: ${currentState.name}")

        // Comprehensive validation
        val validationError = validateProfile(currentState)
        if (validationError != null) {
            Log.w(TAG, "Profile validation failed: $validationError")
            _uiState.value = currentState.copy(error = validationError)
            return
        }

        viewModelScope.launch {
            try {
                _uiState.value = currentState.copy(isSaving = true, error = null)

                // Create user object with DateTimeUtils for safe DateTime handling
                val now = DateTimeUtils.now()
                val user = User(
                    id = currentState.user?.id ?: UUID.randomUUID().toString(),
                    name = currentState.name.trim(),
                    birthdate = currentState.birthdate!!, // Safe because we validated it
                    email = if (currentState.email.isBlank()) null else currentState.email.trim(),
                    location = if (currentState.location.isBlank()) null else currentState.location.trim(),
                    createdAt = currentState.user?.createdAt ?: now,
                    updatedAt = now
                )

                Log.d(TAG, "Calling saveUserUseCase for: ${user.name}")
                val result = saveUserUseCase(user)

                when (result) {
                    is Resource.Success -> {
                        Log.d(TAG, "Profile saved successfully: ${result.data.name}")
                        _uiState.value = currentState.copy(
                            isSaving = false,
                            user = result.data,
                            showSuccess = true,
                            error = null
                        )
                    }
                    is Resource.Error -> {
                        Log.e(TAG, "Failed to save profile: ${result.message}")
                        _uiState.value = currentState.copy(
                            isSaving = false,
                            error = result.message ?: "Failed to save profile"
                        )
                    }
                    is Resource.Loading -> {
                        // Keep loading state
                        Log.d(TAG, "Save operation still loading...")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Unexpected error saving profile", e)
                _uiState.value = currentState.copy(
                    isSaving = false,
                    error = "Unexpected error: ${e.localizedMessage ?: e.message ?: "Unknown error"}"
                )
            }
        }
    }

    /**
     * Comprehensive profile validation
     */
    private fun validateProfile(state: ProfileUiState): String? {
        // Name validation
        if (state.name.trim().isBlank()) {
            return "Name is required"
        }

        if (state.name.trim().length < 2) {
            return "Name must be at least 2 characters"
        }

        // Birthdate validation
        if (state.birthdate == null) {
            return "Birthdate is required"
        }

        if (!DateTimeUtils.isValidBirthdate(state.birthdate)) {
            return "Please select a valid birthdate"
        }

        // Email validation (if provided)
        if (state.email.isNotBlank() && !isValidEmail(state.email.trim())) {
            return "Please enter a valid email address"
        }

        return null // All validation passed
    }

    /**
     * Basic email validation
     */
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * Clear success message
     */
    fun clearSuccess() {
        Log.d(TAG, "Clearing success message")
        _uiState.value = _uiState.value.copy(showSuccess = false)
    }

    /**
     * Clear error message
     */
    fun clearError() {
        Log.d(TAG, "Clearing error message")
        _uiState.value = _uiState.value.copy(error = null)
    }

    /**
     * Retry profile loading
     */
    fun retryLoading() {
        Log.d(TAG, "Retrying profile load")
        clearError()
        loadUserProfile()
    }
}

/**
 * UI State for ProfileScreen
 * ENHANCED: Added more specific loading states
 */
data class ProfileUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val user: User? = null,
    val name: String = "",
    val birthdate: LocalDate? = null,
    val email: String = "",
    val location: String = "",
    val error: String? = null,
    val showSuccess: Boolean = false
)