package com.hadify.omnicast.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hadify.omnicast.core.ui.component.PlaceholderScreen

@Composable
fun OmniCastNavGraph(
    navController: NavHostController,
    navigationManager: NavigationManager,
    homeScreen: @Composable () -> Unit = { PlaceholderScreen("Home", "Welcome to OmniCast") },
    zodiacScreen: @Composable () -> Unit = { PlaceholderScreen("Zodiac", "Zodiac predictions and horoscope readings") },
    profileScreen: @Composable () -> Unit = { PlaceholderScreen("Profile", "User profile and settings") },
    settingsScreen: @Composable () -> Unit = { PlaceholderScreen("Settings", "App settings and preferences") }
) {
    LaunchedEffect(navController) {
        navigationManager.setNavController(navController)
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            homeScreen()
        }
        composable(Screen.Overview.route) {
            PlaceholderScreen("Daily Overview", "Your personalized daily insights combining all divination methods in one comprehensive view")
        }
        composable(Screen.Zodiac.route) {
            zodiacScreen()
        }
        composable(Screen.Biorhythm.route) {
            PlaceholderScreen("Biorhythm", "Track your physical, emotional, and intellectual energy cycles")
        }
        composable(Screen.Tarot.route) {
            PlaceholderScreen("Tarot Cards", "Uncover insights through mystical card readings and ancient wisdom")
        }
        composable(Screen.Numerology.route) {
            PlaceholderScreen("Numerology", "Explore the power and meaning of numbers in your life")
        }
        composable(Screen.IChiing.route) {
            PlaceholderScreen("I Ching", "Ancient Chinese wisdom and guidance for modern decisions")
        }
        composable(Screen.Abjad.route) {
            PlaceholderScreen("Abjad", "Persian numerical mysticism and letter-based calculations")
        }
        composable(Screen.Rune.route) {
            PlaceholderScreen("Runes", "Norse symbols and ancient divination methods")
        }
        composable(Screen.Coffee.route) {
            PlaceholderScreen("Coffee Reading", "Discover meanings in coffee cup symbols and patterns")
        }
        composable(Screen.Profile.route) {
            profileScreen()
        }
        composable(Screen.Journal.route) {
            PlaceholderScreen("Journal", "Record your thoughts, track prediction accuracy, and reflect on insights")
        }
        composable(Screen.Settings.route) {
            settingsScreen()
        }
    }
}