package com.hadify.omnicast.feature.profile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadify.omnicast.feature.profile.domain.model.User
import com.hadify.omnicast.feature.profile.domain.usecase.GetUserProfileUseCase
import com.hadify.omnicast.feature.profile.domain.usecase.SaveUserUseCase
import com.hadify.omnicast.core.data.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

/**
 * ViewModel for ProfileScreen
 * Manages profile UI state and handles user interactions
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val saveUserUseCase: SaveUserUseCase
) : ViewModel() {

    // UI State
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    /**
     * Load user profile from database
     */
    private fun loadUserProfile() {
        viewModelScope.launch {
            // از first() استفاده می‌کنیم تا فقط اولین نتیجه را بگیریم و flow را ببندیم
            when (val resource = getUserProfileUseCase().first()) {
                is Resource.Success -> {
                    val user = resource.data
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
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = resource.message
                    )
                }
                is Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                }
            }
        }
    }

    /**
     * Update name field
     */
    fun updateName(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
    }

    /**
     * Update birthdate field - CRITICAL for other developers!
     */
    fun updateBirthdate(birthdate: LocalDate) {
        _uiState.value = _uiState.value.copy(birthdate = birthdate)
    }

    /**
     * Update email field
     */
    fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    /**
     * Update location field
     */
    fun updateLocation(location: String) {
        _uiState.value = _uiState.value.copy(location = location)
    }

    /**
     * Save user profile
     */
    fun saveProfile() {
        val currentState = _uiState.value

        // Validation
        if (currentState.name.isBlank()) {
            _uiState.value = currentState.copy(error = "Name is required")
            return
        }

        if (currentState.birthdate == null) {
            _uiState.value = currentState.copy(error = "Birthdate is required")
            return
        }

        viewModelScope.launch {
            _uiState.value = currentState.copy(isSaving = true, error = null)

            val user = User(
                id = currentState.user?.id ?: UUID.randomUUID().toString(),
                name = currentState.name,
                birthdate = currentState.birthdate,
                email = currentState.email.ifBlank { null },
                location = currentState.location.ifBlank { null },
                createdAt = currentState.user?.createdAt ?: LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )

            val result = saveUserUseCase(user)
            when (result) {
                is Resource.Success -> {
                    val savedUser = result.data
                    // وضعیت UI را با اطلاعات جدید و ذخیره شده به روز می‌کنیم
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        user = savedUser,
                        name = savedUser.name,
                        birthdate = savedUser.birthdate,
                        email = savedUser.email ?: "",
                        location = savedUser.location ?: "",
                        showSuccess = true,
                        error = null
                    )
                }
                is Resource.Error -> {
                    _uiState.value = currentState.copy(
                        isSaving = false,
                        error = result.message
                    )
                }
                else -> {
                    // Handle loading state if needed
                }
            }
        }
    }

    /**
     * Clear success message
     */
    fun clearSuccess() {
        _uiState.value = _uiState.value.copy(showSuccess = false)
    }

    /**
     * Clear error message
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

/**
 * UI State for ProfileScreen
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