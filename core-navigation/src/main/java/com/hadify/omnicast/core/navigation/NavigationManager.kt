package com.hadify.omnicast.core.navigation

import androidx.navigation.NavController
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Central navigation controller for the OmniCast app
 * Manages all navigation operations and provides type-safe navigation
 */
@Singleton
class NavigationManager @Inject constructor() {

    private var navController: NavController? = null

    /**
     * Sets the NavController instance
     */
    fun setNavController(controller: NavController) {
        navController = controller
    }

    /**
     * Navigate to a specific screen
     */
    fun navigateTo(screen: Screen) {
        navController?.navigate(screen.route) {
            // Avoid multiple copies of the same destination
            launchSingleTop = true
        }
    }

    /**
     * Navigate back to previous screen
     */
    fun navigateBack() {
        navController?.popBackStack()
    }

    /**
     * Navigate to home and clear back stack
     */
    fun navigateToHome() {
        navController?.navigate(Screen.Home.route) {
            // Clear all previous screens
            popUpTo(Screen.Home.route) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    /**
     * Navigate to a screen and clear back stack
     */
    fun navigateAndClearStack(screen: Screen) {
        navController?.navigate(screen.route) {
            popUpTo(0) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    // Convenience methods for common navigations
    fun navigateToZodiac() = navigateTo(Screen.Zodiac)
    fun navigateToBiorhythm() = navigateTo(Screen.Biorhythm)
    fun navigateToTarot() = navigateTo(Screen.Tarot)
    fun navigateToProfile() = navigateTo(Screen.Profile)
    fun navigateToSettings() = navigateTo(Screen.Settings)
    fun navigateToJournal() = navigateTo(Screen.Journal)
    fun navigateToOverview() = navigateTo(Screen.Overview)
}