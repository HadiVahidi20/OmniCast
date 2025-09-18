package com.hadify.omnicast.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

/**
 * Main navigation graph for OmniCast app
 * Defines all navigation routes and connects screens
 * Now includes the real ZodiacScreen functionality!
 */
@Composable
fun OmniCastNavGraph(
    navController: NavHostController,
    navigationManager: NavigationManager,
    homeScreen: @Composable () -> Unit = { PlaceholderScreen("Home", "Welcome to OmniCast") },
    zodiacScreen: @Composable () -> Unit = { PlaceholderScreen("Zodiac", "Zodiac predictions and horoscope readings") },
    profileScreen: @Composable () -> Unit = { PlaceholderScreen("Profile", "User profile and settings") }
) {
    // Set up the navigation controller in our manager
    LaunchedEffect(navController) {
        navigationManager.setNavController(navController)
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {

        // Home screen - now accepts composable from app level
        composable(Screen.Home.route) {
            homeScreen()
        }

        // Daily overview screen
        composable(Screen.Overview.route) {
            PlaceholderScreen("Daily Overview", "Your personalized daily insights combining all divination methods in one comprehensive view")
        }

        // ✅ REAL ZODIAC SCREEN - Now uses actual ZodiacScreen!
        composable(Screen.Zodiac.route) {
            zodiacScreen()
        }

        // Other divination features - still placeholders for now
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

        // User features
        composable(Screen.Profile.route) {
            profileScreen()
        }

        composable(Screen.Journal.route) {
            PlaceholderScreen("Journal", "Record your thoughts, track prediction accuracy, and reflect on insights")
        }

        composable(Screen.Settings.route) {
            PlaceholderScreen("Settings", "Customize themes, notifications, language, and app preferences")
        }
    }
}

/**
 * Enhanced placeholder screen component for features not yet implemented
 */
@Composable
private fun PlaceholderScreen(
    title: String,
    description: String
) {
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$title\n\n$description\n\n🚧 Coming Soon 🚧\n\nThis feature will be implemented by other team developers",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}