package com.hadify.omnicast.home

import androidx.lifecycle.ViewModel
import com.hadify.omnicast.core.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel for the Home screen
 * Manages navigation and home screen state
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    val navigationManager: NavigationManager
) : ViewModel() {

    // Home screen state and logic will be expanded here as needed
    // For now, it primarily provides navigation functionality
}