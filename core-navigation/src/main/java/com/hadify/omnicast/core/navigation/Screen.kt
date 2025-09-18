package com.hadify.omnicast.core.navigation

/**
 * Defines all navigation destinations in the OmniCast app
 */
sealed class Screen(val route: String) {

    // Main screens
    object Home : Screen("home")
    object Overview : Screen("overview")

    // Divination features
    object Zodiac : Screen("zodiac")
    object Biorhythm : Screen("biorhythm")
    object Tarot : Screen("tarot")
    object Numerology : Screen("numerology")
    object IChiing : Screen("iching")
    object Abjad : Screen("abjad")
    object Rune : Screen("rune")
    object Coffee : Screen("coffee")

    // User features
    object Profile : Screen("profile")
    object Journal : Screen("journal")
    object Settings : Screen("settings")
}