package com.hadify.omnicast.feature.zodiac.domain.model

import java.time.LocalDate

/**
 * Zodiac signs with their properties and date ranges
 */
enum class ZodiacSign(
    val displayName: String,
    val startMonth: Int,
    val startDay: Int,
    val endMonth: Int,
    val endDay: Int,
    val element: ZodiacElement,
    val quality: ZodiacQuality,
    val rulingPlanet: String,
    val symbol: String,
    val luckyNumbers: List<Int>,
    val luckyColors: List<String>,
    val compatibleSigns: List<String>
) {
    ARIES(
        displayName = "Aries",
        startMonth = 3, startDay = 21,
        endMonth = 4, endDay = 19,
        element = ZodiacElement.FIRE,
        quality = ZodiacQuality.CARDINAL,
        rulingPlanet = "Mars",
        symbol = "Ram",
        luckyNumbers = listOf(1, 8, 17),
        luckyColors = listOf("Red", "Orange"),
        compatibleSigns = listOf("Leo", "Sagittarius", "Gemini", "Aquarius")
    ),

    TAURUS(
        displayName = "Taurus",
        startMonth = 4, startDay = 20,
        endMonth = 5, endDay = 20,
        element = ZodiacElement.EARTH,
        quality = ZodiacQuality.FIXED,
        rulingPlanet = "Venus",
        symbol = "Bull",
        luckyNumbers = listOf(2, 6, 9, 12, 24),
        luckyColors = listOf("Green", "Pink"),
        compatibleSigns = listOf("Virgo", "Capricorn", "Cancer", "Pisces")
    ),

    GEMINI(
        displayName = "Gemini",
        startMonth = 5, startDay = 21,
        endMonth = 6, endDay = 20,
        element = ZodiacElement.AIR,
        quality = ZodiacQuality.MUTABLE,
        rulingPlanet = "Mercury",
        symbol = "Twins",
        luckyNumbers = listOf(5, 7, 14, 23),
        luckyColors = listOf("Yellow", "Blue"),
        compatibleSigns = listOf("Libra", "Aquarius", "Aries", "Leo")
    ),

    CANCER(
        displayName = "Cancer",
        startMonth = 6, startDay = 21,
        endMonth = 7, endDay = 22,
        element = ZodiacElement.WATER,
        quality = ZodiacQuality.CARDINAL,
        rulingPlanet = "Moon",
        symbol = "Crab",
        luckyNumbers = listOf(2, 7, 11, 16, 20, 25),
        luckyColors = listOf("White", "Silver"),
        compatibleSigns = listOf("Scorpio", "Pisces", "Taurus", "Virgo")
    ),

    LEO(
        displayName = "Leo",
        startMonth = 7, startDay = 23,
        endMonth = 8, endDay = 22,
        element = ZodiacElement.FIRE,
        quality = ZodiacQuality.FIXED,
        rulingPlanet = "Sun",
        symbol = "Lion",
        luckyNumbers = listOf(1, 3, 10, 19),
        luckyColors = listOf("Gold", "Orange"),
        compatibleSigns = listOf("Aries", "Sagittarius", "Gemini", "Libra")
    ),

    VIRGO(
        displayName = "Virgo",
        startMonth = 8, startDay = 23,
        endMonth = 9, endDay = 22,
        element = ZodiacElement.EARTH,
        quality = ZodiacQuality.MUTABLE,
        rulingPlanet = "Mercury",
        symbol = "Virgin",
        luckyNumbers = listOf(3, 6, 7, 12, 20),
        luckyColors = listOf("Navy Blue", "Grey"),
        compatibleSigns = listOf("Taurus", "Capricorn", "Cancer", "Scorpio")
    ),

    LIBRA(
        displayName = "Libra",
        startMonth = 9, startDay = 23,
        endMonth = 10, endDay = 22,
        element = ZodiacElement.AIR,
        quality = ZodiacQuality.CARDINAL,
        rulingPlanet = "Venus",
        symbol = "Scales",
        luckyNumbers = listOf(4, 6, 13, 15, 24),
        luckyColors = listOf("Blue", "Green"),
        compatibleSigns = listOf("Gemini", "Aquarius", "Leo", "Sagittarius")
    ),

    SCORPIO(
        displayName = "Scorpio",
        startMonth = 10, startDay = 23,
        endMonth = 11, endDay = 21,
        element = ZodiacElement.WATER,
        quality = ZodiacQuality.FIXED,
        rulingPlanet = "Mars/Pluto",
        symbol = "Scorpion",
        luckyNumbers = listOf(8, 11, 18, 22),
        luckyColors = listOf("Deep Red", "Black"),
        compatibleSigns = listOf("Cancer", "Pisces", "Virgo", "Capricorn")
    ),

    SAGITTARIUS(
        displayName = "Sagittarius",
        startMonth = 11, startDay = 22,
        endMonth = 12, endDay = 21,
        element = ZodiacElement.FIRE,
        quality = ZodiacQuality.MUTABLE,
        rulingPlanet = "Jupiter",
        symbol = "Archer",
        luckyNumbers = listOf(3, 9, 15, 21, 24),
        luckyColors = listOf("Purple", "Turquoise"),
        compatibleSigns = listOf("Aries", "Leo", "Libra", "Aquarius")
    ),

    CAPRICORN(
        displayName = "Capricorn",
        startMonth = 12, startDay = 22,
        endMonth = 1, endDay = 19,
        element = ZodiacElement.EARTH,
        quality = ZodiacQuality.CARDINAL,
        rulingPlanet = "Saturn",
        symbol = "Goat",
        luckyNumbers = listOf(4, 8, 13, 22),
        luckyColors = listOf("Brown", "Black"),
        compatibleSigns = listOf("Taurus", "Virgo", "Scorpio", "Pisces")
    ),

    AQUARIUS(
        displayName = "Aquarius",
        startMonth = 1, startDay = 20,
        endMonth = 2, endDay = 18,
        element = ZodiacElement.AIR,
        quality = ZodiacQuality.FIXED,
        rulingPlanet = "Saturn/Uranus",
        symbol = "Water Bearer",
        luckyNumbers = listOf(4, 7, 11, 22, 29),
        luckyColors = listOf("Blue", "Silver"),
        compatibleSigns = listOf("Gemini", "Libra", "Aries", "Sagittarius")
    ),

    PISCES(
        displayName = "Pisces",
        startMonth = 2, startDay = 19,
        endMonth = 3, endDay = 20,
        element = ZodiacElement.WATER,
        quality = ZodiacQuality.MUTABLE,
        rulingPlanet = "Jupiter/Neptune",
        symbol = "Fish",
        luckyNumbers = listOf(3, 9, 12, 15, 18, 24),
        luckyColors = listOf("Sea Green", "Lavender"),
        compatibleSigns = listOf("Cancer", "Scorpio", "Taurus", "Capricorn")
    );

    companion object {
        /**
         * Determine zodiac sign from birthdate
         */
        fun fromBirthdate(birthdate: LocalDate): ZodiacSign {
            val month = birthdate.monthValue
            val day = birthdate.dayOfMonth

            return values().find { sign ->
                when {
                    // Handle Capricorn (spans year boundary)
                    sign == CAPRICORN -> {
                        (month == 12 && day >= 22) || (month == 1 && day <= 19)
                    }
                    // Handle other signs
                    sign.startMonth == sign.endMonth -> {
                        month == sign.startMonth && day >= sign.startDay && day <= sign.endDay
                    }
                    // Handle signs that span two months
                    else -> {
                        (month == sign.startMonth && day >= sign.startDay) ||
                                (month == sign.endMonth && day <= sign.endDay)
                    }
                }
            } ?: ARIES // Default fallback
        }
    }
}

/**
 * Zodiac elements
 */
enum class ZodiacElement(val displayName: String) {
    FIRE("Fire"),
    EARTH("Earth"),
    AIR("Air"),
    WATER("Water")
}

/**
 * Zodiac qualities
 */
enum class ZodiacQuality(val displayName: String) {
    CARDINAL("Cardinal"),
    FIXED("Fixed"),
    MUTABLE("Mutable")
}