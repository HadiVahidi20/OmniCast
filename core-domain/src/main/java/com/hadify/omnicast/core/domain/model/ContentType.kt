package com.hadify.omnicast.core.data.model

/**
 * Defines all content types that can be loaded from assets
 * Each type corresponds to a JSON file in the assets folder
 */
enum class ContentType(val fileName: String) {
    ZODIAC("zodiac.json"),
    TAROT("tarot.json"),
    BIORHYTHM("biorhythm.json"),
    NUMEROLOGY("numerology.json"),
    I_CHING("iching.json"),
    ABJAD("abjad.json"),
    RUNE("rune.json"),
    COFFEE("coffee.json"),
    QUOTES("quotes.json")
}

/**
 * Base content metadata that all JSON files should include
 */
data class ContentMetadata(
    val version: String,
    val lastUpdated: String,
    val language: String,
    val itemCount: Int? = null,
    val description: String? = null
)