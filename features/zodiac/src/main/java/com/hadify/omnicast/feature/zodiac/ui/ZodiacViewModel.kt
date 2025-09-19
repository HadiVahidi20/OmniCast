package com.hadify.omnicast.feature.zodiac.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadify.omnicast.core.common.util.DateTimeUtils
import com.hadify.omnicast.core.data.util.Resource
import com.hadify.omnicast.feature.profile.domain.usecase.GetUserProfileUseCase
import com.hadify.omnicast.feature.zodiac.domain.model.HoroscopeReading
import com.hadify.omnicast.feature.zodiac.domain.model.ZodiacSign
import com.hadify.omnicast.feature.zodiac.domain.usecase.DetermineZodiacSignUseCase
import com.hadify.omnicast.feature.zodiac.domain.usecase.GetUserDailyHoroscopeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

/**
 * ViewModel for ZodiacScreen
 * Manages zodiac UI state and loads horoscope readings
 */
@HiltViewModel
class ZodiacViewModel @Inject constructor(
    private val getUserDailyHoroscopeUseCase: GetUserDailyHoroscopeUseCase,
    private val determineZodiacSignUseCase: DetermineZodiacSignUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase
) : ViewModel() {

    // UI State
    private val _uiState = MutableStateFlow(ZodiacUiState())
    val uiState: StateFlow<ZodiacUiState> = _uiState.asStateFlow()

    init {
        loadUserZodiacInfo()
    }

    /**
     * Load user's zodiac information and today's horoscope
     */
    private fun loadUserZodiacInfo() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            // First get user profile to get birthdate
            getUserProfileUseCase().collect { userResource ->
                when (userResource) {
                    is Resource.Success -> {
                        val user = userResource.data
                        if (user?.birthdate != null) {
                            // User has birthdate - determine zodiac sign and load horoscope
                            val zodiacSign = determineZodiacSignUseCase(user.birthdate)

                            _uiState.value = _uiState.value.copy(
                                userZodiacSign = zodiacSign,
                                hasBirthdate = true
                            )

                            // Load today's horoscope using the safe DateTimeUtils
                            loadDailyHoroscope(user.birthdate, DateTimeUtils.today())
                        } else {
                            // User hasn't set birthdate yet
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                hasBirthdate = false,
                                error = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "Failed to load user profile: ${userResource.message}"
                        )
                    }
                    is Resource.Loading -> {
                        // Keep loading state
                    }
                }
            }
        }
    }

    /**
     * Load daily horoscope for specific date
     */
    private fun loadDailyHoroscope(birthdate: LocalDate, date: LocalDate) {
        viewModelScope.launch {
            val params = GetUserDailyHoroscopeUseCase.Params(
                birthdate = birthdate,
                date = date
            )

            getUserDailyHoroscopeUseCase(params).collect { horoscopeResource ->
                when (horoscopeResource) {
                    is Resource.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoadingHoroscope = true)
                    }
                    is Resource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isLoadingHoroscope = false,
                            dailyHoroscope = horoscopeResource.data,
                            selectedDate = date,
                            error = null
                        )
                    }
                    is Resource.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isLoadingHoroscope = false,
                            error = "Failed to load horoscope: ${horoscopeResource.message}"
                        )
                    }
                }
            }
        }
    }

    /**
     * Load horoscope for a different date
     */
    fun loadHoroscopeForDate(date: LocalDate) {
        // Need user's birthdate to load horoscope
        viewModelScope.launch {
            getUserProfileUseCase().first().let { userResource ->
                if (userResource is Resource.Success && userResource.data?.birthdate != null) {
                    loadDailyHoroscope(userResource.data!!.birthdate, date)
                }
            }
        }
    }

    /**
     * Retry loading data
     */
    fun retry() {
        loadUserZodiacInfo()
    }

    /**
     * Clear error message
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    /**
     * Get zodiac sign info for display
     */
    fun getZodiacSignInfo(sign: ZodiacSign): ZodiacSignDisplayInfo {
        return ZodiacSignDisplayInfo(
            name = sign.displayName,
            dateRange = "${sign.startMonth}/${sign.startDay} - ${sign.endMonth}/${sign.endDay}",
            element = sign.element.displayName,
            symbol = sign.symbol,
            rulingPlanet = sign.rulingPlanet,
            luckyNumbers = sign.luckyNumbers,
            luckyColors = sign.luckyColors,
            compatibleSigns = sign.compatibleSigns
        )
    }
}

/**
 * UI State for ZodiacScreen
 */
data class ZodiacUiState(
    val isLoading: Boolean = false,
    val isLoadingHoroscope: Boolean = false,
    val hasBirthdate: Boolean = false,
    val userZodiacSign: ZodiacSign? = null,
    val dailyHoroscope: HoroscopeReading? = null,
    val selectedDate: LocalDate = DateTimeUtils.today(), // Use safe method for default date
    val error: String? = null
)

/**
 * Zodiac sign information for display
 */
data class ZodiacSignDisplayInfo(
    val name: String,
    val dateRange: String,
    val element: String,
    val symbol: String,
    val rulingPlanet: String,
    val luckyNumbers: List<Int>,
    val luckyColors: List<String>,
    val compatibleSigns: List<String>
)