package com.hadify.omnicast.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hadify.omnicast.core.navigation.NavigationManager
import com.hadify.omnicast.core.ui.component.OmniCastAppBar
import com.hadify.omnicast.core.ui.component.PredictionCard
import javax.inject.Inject

/**
 * Data class representing a feature in the app
 */
data class FeatureItem(
    val id: String,
    val title: String,
    val description: String,
    val tags: List<String>,
    val navigationAction: () -> Unit
)

/**
 * Home screen showing overview of all features
 */
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            OmniCastAppBar(
                title = "OmniCast",
                showBackButton = false,
                showMenuButton = true
            )
        }
    ) { paddingValues ->
        HomeContent(
            modifier = Modifier.padding(paddingValues),
            navigationManager = viewModel.navigationManager
        )
    }
}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    navigationManager: NavigationManager
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Welcome card
        item {
            PredictionCard(
                title = "Welcome to OmniCast",
                description = "Discover your future through various divination methods. Choose from zodiac readings, biorhythm analysis, tarot cards, and more! Your personalized spiritual journey awaits.",
                icon = Icons.Default.Star,
                tags = listOf("Welcome", "Getting Started", "Discover"),
                onClick = {
                    navigationManager.navigateToOverview()
                }
            )
        }

        // Divination Features
        val divinationFeatures = listOf(
            FeatureItem(
                id = "daily_overview",
                title = "Daily Overview",
                description = "Get your personalized daily insights combining all divination methods in one comprehensive view.",
                tags = listOf("Daily", "Overview", "All Methods"),
                navigationAction = { navigationManager.navigateToOverview() }
            ),
            FeatureItem(
                id = "zodiac",
                title = "Zodiac Signs",
                description = "Discover your horoscope and celestial influences based on your birth date and zodiac sign.",
                tags = listOf("Astrology", "Horoscope", "Stars"),
                navigationAction = { navigationManager.navigateToZodiac() }
            ),
            FeatureItem(
                id = "biorhythm",
                title = "Biorhythm Cycles",
                description = "Track your physical, emotional, and intellectual energy cycles throughout the month.",
                tags = listOf("Energy", "Cycles", "Wellness"),
                navigationAction = { navigationManager.navigateToBiorhythm() }
            ),
            FeatureItem(
                id = "tarot",
                title = "Tarot Cards",
                description = "Uncover insights through mystical card readings and ancient tarot wisdom.",
                tags = listOf("Cards", "Mystical", "Guidance"),
                navigationAction = { navigationManager.navigateToTarot() }
            ),
            FeatureItem(
                id = "numerology",
                title = "Numerology",
                description = "Explore the power and meaning of numbers in your life path and destiny.",
                tags = listOf("Numbers", "Mathematics", "Destiny"),
                navigationAction = { navigationManager.navigateTo(com.hadify.omnicast.core.navigation.Screen.Numerology) }
            ),
            FeatureItem(
                id = "iching",
                title = "I Ching",
                description = "Ancient Chinese wisdom and guidance for modern decisions and life questions.",
                tags = listOf("Chinese", "Ancient", "Wisdom"),
                navigationAction = { navigationManager.navigateTo(com.hadify.omnicast.core.navigation.Screen.IChiing) }
            ),
            FeatureItem(
                id = "abjad",
                title = "Abjad Numerology",
                description = "Persian numerical mysticism and letter-based calculations for deeper insights.",
                tags = listOf("Persian", "Letters", "Mysticism"),
                navigationAction = { navigationManager.navigateTo(com.hadify.omnicast.core.navigation.Screen.Abjad) }
            ),
            FeatureItem(
                id = "runes",
                title = "Nordic Runes",
                description = "Norse symbols and ancient Scandinavian divination methods for guidance.",
                tags = listOf("Norse", "Symbols", "Ancient"),
                navigationAction = { navigationManager.navigateTo(com.hadify.omnicast.core.navigation.Screen.Rune) }
            ),
            FeatureItem(
                id = "coffee",
                title = "Coffee Reading",
                description = "Discover meanings in coffee cup symbols and traditional tasseography patterns.",
                tags = listOf("Coffee", "Symbols", "Traditional"),
                navigationAction = { navigationManager.navigateTo(com.hadify.omnicast.core.navigation.Screen.Coffee) }
            )
        )

        items(divinationFeatures) { feature ->
            PredictionCard(
                title = feature.title,
                description = feature.description,
                tags = feature.tags,
                icon = when (feature.id) {
                    "daily_overview" -> Icons.Default.Home
                    "zodiac" -> Icons.Default.Star
                    "biorhythm" -> Icons.Default.Favorite
                    "tarot" -> Icons.Default.Info
                    "numerology" -> Icons.Default.DateRange
                    "iching" -> Icons.Default.LocationOn
                    else -> Icons.Default.Star
                },
                onClick = feature.navigationAction
            )
        }

        // User Features Section
        val userFeatures = listOf(
            FeatureItem(
                id = "profile",
                title = "User Profile",
                description = "Manage your personal information, birth details, and preferences for accurate predictions.",
                tags = listOf("Personal", "Settings", "Accuracy"),
                navigationAction = { navigationManager.navigateToProfile() }
            ),
            FeatureItem(
                id = "journal",
                title = "Prediction Journal",
                description = "Record your thoughts, track prediction accuracy, and reflect on your spiritual insights.",
                tags = listOf("Journal", "Tracking", "Reflection"),
                navigationAction = { navigationManager.navigateToJournal() }
            ),
            FeatureItem(
                id = "settings",
                title = "App Settings",
                description = "Customize themes, notifications, language, and other app preferences to suit your needs.",
                tags = listOf("Customize", "Themes", "Preferences"),
                navigationAction = { navigationManager.navigateToSettings() }
            )
        )

        items(userFeatures) { feature ->
            PredictionCard(
                title = feature.title,
                description = feature.description,
                tags = feature.tags,
                icon = when (feature.id) {
                    "profile" -> Icons.Default.AccountCircle
                    "journal" -> Icons.Default.DateRange
                    "settings" -> Icons.Default.Settings
                    else -> Icons.Default.Info
                },
                onClick = feature.navigationAction
            )
        }
    }
}