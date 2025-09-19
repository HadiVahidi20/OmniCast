package com.hadify.omnicast.feature.profile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadify.omnicast.core.common.util.DateTimeUtils
import com.hadify.omnicast.core.data.util.Resource
import com.hadify.omnicast.feature.profile.domain.model.User
import com.hadify.omnicast.feature.profile.domain.usecase.GetUserProfileUseCase
import com.hadify.omnicast.feature.profile.domain.usecase.SaveUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

/**
 * ViewModel for ProfileScreen
 * Handles all logic for user profile creation and editing.
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val saveUserUseCase: SaveUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            getUserProfileUseCase().collect { resource ->
                _uiState.update { currentState ->
                    when (resource) {
                        is Resource.Loading -> currentState.copy(isLoading = true)
                        is Resource.Success -> {
                            val user = resource.data
                            currentState.copy(
                                isLoading = false,
                                user = user,
                                name = user?.name ?: "",
                                birthdate = user?.birthdate,
                                email = user?.email ?: "",
                                location = user?.location ?: ""
                            )
                        }
                        is Resource.Error -> currentState.copy(
                            isLoading = false,
                            error = resource.message
                        )
                    }
                }
            }
        }
    }

    /**
     * Updates the name in the UI state. Called every time the user types in the name field.
     */
    fun updateName(newName: String) {
        _uiState.update { it.copy(name = newName) }
    }

    /**
     * Updates the birthdate in the UI state.
     */
    fun updateBirthdate(newBirthdate: LocalDate) {
        _uiState.update { it.copy(birthdate = newBirthdate) }
    }

    /**
     * Updates the email in the UI state. Called every time the user types in the email field.
     */
    fun updateEmail(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }

    /**
     * Updates the location in the UI state. Called every time the user types in the location field.
     */
    fun updateLocation(newLocation: String) {
        _uiState.update { it.copy(location = newLocation) }
    }

    /**
     * Validates the current state and saves the user profile.
     */
    fun saveProfile() {
        val currentState = _uiState.value

        // --- Validation ---
        if (currentState.name.isBlank()) {
            _uiState.update { it.copy(error = "Name cannot be empty") }
            return
        }
        if (currentState.birthdate == null) {
            _uiState.update { it.copy(error = "Birthdate is required") }
            return
        }
        if (!DateTimeUtils.isValidBirthdate(currentState.birthdate)) {
            _uiState.update { it.copy(error = "Please select a valid birthdate") }
            return
        }
        // --- End Validation ---

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }

            val userToSave = User(
                id = currentState.user?.id ?: UUID.randomUUID().toString(),
                name = currentState.name.trim(),
                birthdate = currentState.birthdate,
                email = currentState.email.trim().takeIf { it.isNotBlank() },
                location = currentState.location.trim().takeIf { it.isNotBlank() },
                createdAt = currentState.user?.createdAt ?: DateTimeUtils.now(),
                updatedAt = DateTimeUtils.now()
            )

            val result = saveUserUseCase(userToSave)

            _uiState.update {
                when (result) {
                    is Resource.Success -> it.copy(isSaving = false, user = result.data, showSuccess = true)
                    is Resource.Error -> it.copy(isSaving = false, error = result.message)
                    is Resource.Loading -> it.copy(isSaving = true) // Should not happen here but handled
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun clearSuccess() {
        _uiState.update { it.copy(showSuccess = false) }
    }
}

/**
 * Represents the state of the Profile screen.
 */
data class ProfileUiState(
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val user: User? = null,
    val name: String = "",
    val birthdate: LocalDate? = null,
    val email: String = "",
    val location: String = "",
    val error: String? = null,
    val showSuccess: Boolean = false
)