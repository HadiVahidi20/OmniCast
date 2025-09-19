// DetermineZodiacSignUseCase.kt
package com.hadify.omnicast.feature.zodiac.domain.usecase

import com.hadify.omnicast.feature.zodiac.domain.model.ZodiacSign
import java.time.LocalDate
import javax.inject.Inject

/**
 * Use case to determine zodiac sign from birthdate
 * Critical for all other zodiac-related features
 */
class DetermineZodiacSignUseCase @Inject constructor() {

    /**
     * Determine zodiac sign from birthdate
     */
    operator fun invoke(birthdate: LocalDate): ZodiacSign {
        return ZodiacSign.fromBirthdate(birthdate)
    }

    /**
     * Get zodiac sign with additional information
     */
    fun getZodiacSignInfo(birthdate: LocalDate): ZodiacSignInfo {
        val sign = ZodiacSign.fromBirthdate(birthdate)
        return ZodiacSignInfo(
            sign = sign,
            element = sign.element,
            quality = sign.quality,
            rulingPlanet = sign.rulingPlanet,
            compatibleSigns = getCompatibleSigns(sign),
            traits = getZodiacTraits(sign)
        )
    }

    private fun getCompatibleSigns(sign: ZodiacSign): List<ZodiacSign> {
        return when (sign.element) {
            ZodiacElement.FIRE -> listOf(ZodiacSign.ARIES, ZodiacSign.LEO, ZodiacSign.SAGITTARIUS)
            ZodiacElement.EARTH -> listOf(ZodiacSign.TAURUS, ZodiacSign.VIRGO, ZodiacSign.CAPRICORN)
            ZodiacElement.AIR -> listOf(ZodiacSign.GEMINI, ZodiacSign.LIBRA, ZodiacSign.AQUARIUS)
            ZodiacElement.WATER -> listOf(ZodiacSign.CANCER, ZodiacSign.SCORPIO, ZodiacSign.PISCES)
        }.filter { it != sign }
    }

    private fun getZodiacTraits(sign: ZodiacSign): ZodiacTraits {
        return when (sign) {
            ZodiacSign.ARIES -> ZodiacTraits(
                strengths = listOf("Energetic", "Independent", "Courageous", "Leadership"),
                challenges = listOf("Impatient", "Impulsive", "Short-tempered")
            )
            ZodiacSign.TAURUS -> ZodiacTraits(
                strengths = listOf("Reliable", "Patient", "Practical", "Devoted"),
                challenges = listOf("Stubborn", "Possessive", "Uncompromising")
            )
            ZodiacSign.GEMINI -> ZodiacTraits(
                strengths = listOf("Adaptable", "Curious", "Quick-witted", "Communicative"),
                challenges = listOf("Inconsistent", "Indecisive", "Nervous")
            )
            ZodiacSign.CANCER -> ZodiacTraits(
                strengths = listOf("Intuitive", "Emotional", "Protective", "Sympathetic"),
                challenges = listOf("Moody", "Clingy", "Oversensitive")
            )
            ZodiacSign.LEO -> ZodiacTraits(
                strengths = listOf("Confident", "Generous", "Creative", "Warm-hearted"),
                challenges = listOf("Arrogant", "Stubborn", "Self-centered")
            )
            ZodiacSign.VIRGO -> ZodiacTraits(
                strengths = listOf("Analytical", "Practical", "Reliable", "Modest"),
                challenges = listOf("Overly critical", "Worry-prone", "Perfectionist")
            )
            ZodiacSign.LIBRA -> ZodiacTraits(
                strengths = listOf("Diplomatic", "Fair-minded", "Social", "Idealistic"),
                challenges = listOf("Indecisive", "Avoids confrontation", "Self-pity")
            )
            ZodiacSign.SCORPIO -> ZodiacTraits(
                strengths = listOf("Passionate", "Determined", "Brave", "Loyal"),
                challenges = listOf("Jealous", "Secretive", "Resentful")
            )
            ZodiacSign.SAGITTARIUS -> ZodiacTraits(
                strengths = listOf("Optimistic", "Freedom-loving", "Honest", "Intellectual"),
                challenges = listOf("Impatient", "Promises more than can deliver", "Tactless")
            )
            ZodiacSign.CAPRICORN -> ZodiacTraits(
                strengths = listOf("Ambitious", "Disciplined", "Patient", "Responsible"),
                challenges = listOf("Know-it-all", "Unforgiving", "Condescending")
            )
            ZodiacSign.AQUARIUS -> ZodiacTraits(
                strengths = listOf("Independent", "Humanitarian", "Original", "Intellectual"),
                challenges = listOf("Runs from emotion", "Temperamental", "Uncompromising")
            )
            ZodiacSign.PISCES -> ZodiacTraits(
                strengths = listOf("Compassionate", "Artistic", "Intuitive", "Gentle"),
                challenges = listOf("Fearful", "Overly trusting", "Sad", "Desire to escape")
            )
        }
    }
}

// Supporting data classes
data class ZodiacSignInfo(
    val sign: ZodiacSign,
    val element: ZodiacElement,
    val quality: ZodiacQuality,
    val rulingPlanet: String,
    val compatibleSigns: List<ZodiacSign>,
    val traits: ZodiacTraits
)

data class ZodiacTraits(
    val strengths: List<String>,
    val challenges: List<String>
)

// GetDailyHoroscopeUseCase.kt
package com.hadify.omnicast.feature.zodiac.domain.usecase

import com.hadify.omnicast.feature.zodiac.domain.model.HoroscopeReading
import com.hadify.omnicast.feature.zodiac.domain.model.ZodiacSign
import com.hadify.omnicast.feature.zodiac.domain.repository.ZodiacRepository
import com.hadify.omnicast.core.data.util.Resource
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

/**
 * Use case to get daily horoscope for a specific zodiac sign
 */
class GetDailyHoroscopeUseCase @Inject constructor(
    private val zodiacRepository: ZodiacRepository
) {

    /**
     * Get daily horoscope for zodiac sign and specific date
     */
    suspend operator fun invoke(
        zodiacSign: ZodiacSign,
        date: LocalDate = LocalDate.now()
    ): Flow<Resource<HoroscopeReading>> {
        return zodiacRepository.getDailyHoroscope(zodiacSign, date)
    }

    /**
     * Get today's horoscope for zodiac sign
     */
    suspend fun getTodaysHoroscope(zodiacSign: ZodiacSign): Flow<Resource<HoroscopeReading>> {
        return zodiacRepository.getDailyHoroscope(zodiacSign, LocalDate.now())
    }

    /**
     * Get horoscope for date range
     */
    suspend fun getHoroscopeRange(
        zodiacSign: ZodiacSign,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<Flow<Resource<HoroscopeReading>>> {
        val horoscopes = mutableListOf<Flow<Resource<HoroscopeReading>>>()
        var currentDate = startDate

        while (!currentDate.isAfter(endDate)) {
            horoscopes.add(zodiacRepository.getDailyHoroscope(zodiacSign, currentDate))
            currentDate = currentDate.plusDays(1)
        }

        return horoscopes
    }
}

// GetUserDailyHoroscopeUseCase.kt
package com.hadify.omnicast.feature.zodiac.domain.usecase

import com.hadify.omnicast.feature.zodiac.domain.model.HoroscopeReading
import com.hadify.omnicast.feature.zodiac.domain.repository.ZodiacRepository
import com.hadify.omnicast.core.data.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import javax.inject.Inject

/**
 * Use case to get daily horoscope for user based on their birthdate
 * This automatically determines the zodiac sign and fetches the horoscope
 */
class GetUserDailyHoroscopeUseCase @Inject constructor(
    private val zodiacRepository: ZodiacRepository,
    private val determineZodiacSignUseCase: DetermineZodiacSignUseCase
) {

    /**
     * Get user's daily horoscope based on their birthdate
     */
    suspend operator fun invoke(
        birthdate: LocalDate,
        date: LocalDate = LocalDate.now()
    ): Flow<Resource<HoroscopeReading>> {
        return try {
            val zodiacSign = determineZodiacSignUseCase(birthdate)
            zodiacRepository.getDailyHoroscope(zodiacSign, date)
        } catch (e: Exception) {
            flow { emit(Resource.Error("Failed to determine zodiac sign: ${e.message}")) }
        }
    }

    /**
     * Get today's horoscope for user
     */
    suspend fun getTodaysHoroscope(birthdate: LocalDate): Flow<Resource<HoroscopeReading>> {
        return invoke(birthdate, LocalDate.now())
    }

    /**
     * Get user's zodiac sign info
     */
    fun getUserZodiacInfo(birthdate: LocalDate): ZodiacSignInfo {
        return determineZodiacSignUseCase.getZodiacSignInfo(birthdate)
    }
}

// GetZodiacSignInfoUseCase.kt
package com.hadify.omnicast.feature.zodiac.domain.usecase

import com.hadify.omnicast.feature.zodiac.domain.model.ZodiacSign
import javax.inject.Inject

/**
 * Use case to get extended zodiac sign information
 */
class GetZodiacSignInfoUseCase @Inject constructor() {

    /**
     * Get comprehensive zodiac sign information
     */
    operator fun invoke(zodiacSign: ZodiacSign): ZodiacSignDetailInfo {
        return ZodiacSignDetailInfo(
            sign = zodiacSign,
            element = zodiacSign.element,
            quality = zodiacSign.quality,
            rulingPlanet = zodiacSign.rulingPlanet,
            symbol = zodiacSign.symbol,
            dateRange = zodiacSign.dateRange,
            description = getZodiacDescription(zodiacSign),
            strengths = getZodiacStrengths(zodiacSign),
            challenges = getZodiacChallenges(zodiacSign),
            luckyNumbers = getLuckyNumbers(zodiacSign),
            luckyColors = getLuckyColors(zodiacSign),
            compatibleSigns = getCompatibleSigns(zodiacSign),
            keywords = getKeywords(zodiacSign)
        )
    }

    private fun getZodiacDescription(sign: ZodiacSign): String {
        return when (sign) {
            ZodiacSign.ARIES -> "Dynamic, energetic pioneers who love to be first in everything they do."
            ZodiacSign.TAURUS -> "Practical, reliable, and determined individuals who value security and comfort."
            ZodiacSign.GEMINI -> "Curious, adaptable communicators who thrive on variety and intellectual stimulation."
            ZodiacSign.CANCER -> "Nurturing, intuitive, and emotional beings who value home and family above all."
            ZodiacSign.LEO -> "Confident, generous, and creative leaders who love to shine and inspire others."
            ZodiacSign.VIRGO -> "Analytical, practical perfectionists who serve others with dedication and precision."
            ZodiacSign.LIBRA -> "Diplomatic, fair-minded individuals who seek harmony and beauty in all aspects of life."
            ZodiacSign.SCORPIO -> "Intense, passionate, and mysterious souls who transform themselves and others."
            ZodiacSign.SAGITTARIUS -> "Optimistic, freedom-loving adventurers who seek truth and meaning through exploration."
            ZodiacSign.CAPRICORN -> "Ambitious, disciplined achievers who work steadily toward their goals with patience."
            ZodiacSign.AQUARIUS -> "Independent, innovative humanitarians who work for the betterment of society."
            ZodiacSign.PISCES -> "Compassionate, intuitive dreamers who navigate life through emotion and imagination."
        }
    }

    private fun getZodiacStrengths(sign: ZodiacSign): List<String> {
        return when (sign) {
            ZodiacSign.ARIES -> listOf("Leadership", "Courage", "Independence", "Energy", "Initiative")
            ZodiacSign.TAURUS -> listOf("Reliability", "Patience", "Loyalty", "Determination", "Practicality")
            ZodiacSign.GEMINI -> listOf("Versatility", "Intelligence", "Communication", "Curiosity", "Adaptability")
            ZodiacSign.CANCER -> listOf("Empathy", "Intuition", "Loyalty", "Protectiveness", "Imagination")
            ZodiacSign.LEO -> listOf("Confidence", "Generosity", "Creativity", "Leadership", "Warmth")
            ZodiacSign.VIRGO -> listOf("Attention to Detail", "Reliability", "Modesty", "Intelligence", "Practicality")
            ZodiacSign.LIBRA -> listOf("Diplomacy", "Fairness", "Social Skills", "Artistic Sense", "Idealism")
            ZodiacSign.SCORPIO -> listOf("Determination", "Passion", "Loyalty", "Resourcefulness", "Bravery")
            ZodiacSign.SAGITTARIUS -> listOf("Optimism", "Honesty", "Adventure", "Independence", "Humor")
            ZodiacSign.CAPRICORN -> listOf("Ambition", "Discipline", "Responsibility", "Patience", "Practicality")
            ZodiacSign.AQUARIUS -> listOf("Independence", "Originality", "Humanitarianism", "Intelligence", "Progressiveness")
            ZodiacSign.PISCES -> listOf("Compassion", "Intuition", "Creativity", "Gentleness", "Wisdom")
        }
    }

    private fun getZodiacChallenges(sign: ZodiacSign): List<String> {
        return when (sign) {
            ZodiacSign.ARIES -> listOf("Impatience", "Aggression", "Selfishness", "Impulsiveness")
            ZodiacSign.TAURUS -> listOf("Stubbornness", "Possessiveness", "Materialism", "Resistance to change")
            ZodiacSign.GEMINI -> listOf("Inconsistency", "Superficiality", "Restlessness", "Indecisiveness")
            ZodiacSign.CANCER -> listOf("Over-sensitivity", "Moodiness", "Clinginess", "Pessimism")
            ZodiacSign.LEO -> listOf("Arrogance", "Self-centeredness", "Stubbornness", "Inflexibility")
            ZodiacSign.VIRGO -> listOf("Over-criticism", "Worry", "Perfectionism", "Conservative nature")
            ZodiacSign.LIBRA -> listOf("Indecisiveness", "Superficiality", "Detachment", "Self-pity")
            ZodiacSign.SCORPIO -> listOf("Jealousy", "Secretiveness", "Resentfulness", "Controlling nature")
            ZodiacSign.SAGITTARIUS -> listOf("Over-confidence", "Carelessness", "Impatience", "Tactlessness")
            ZodiacSign.CAPRICORN -> listOf("Pessimism", "Fatalism", "Stubbornness", "Miserliness")
            ZodiacSign.AQUARIUS -> listOf("Detachment", "Rebelliousness", "Unpredictability", "Extremism")
            ZodiacSign.PISCES -> listOf("Over-sensitivity", "Escapism", "Idealism", "Secretiveness")
        }
    }

    private fun getLuckyNumbers(sign: ZodiacSign): List<Int> {
        return when (sign) {
            ZodiacSign.ARIES -> listOf(1, 8, 17)
            ZodiacSign.TAURUS -> listOf(2, 6, 9, 12, 24)
            ZodiacSign.GEMINI -> listOf(5, 7, 14, 23)
            ZodiacSign.CANCER -> listOf(2, 7, 11, 16, 20, 25)
            ZodiacSign.LEO -> listOf(1, 3, 10, 19)
            ZodiacSign.VIRGO -> listOf(3, 15, 20, 27)
            ZodiacSign.LIBRA -> listOf(4, 6, 13, 15, 24)
            ZodiacSign.SCORPIO -> listOf(8, 11, 18, 22)
            ZodiacSign.SAGITTARIUS -> listOf(3, 9, 15, 21, 22)
            ZodiacSign.CAPRICORN -> listOf(6, 9, 26, 8, 10)
            ZodiacSign.AQUARIUS -> listOf(4, 7, 11, 22, 29)
            ZodiacSign.PISCES -> listOf(3, 9, 12, 15, 18, 24)
        }
    }

    private fun getLuckyColors(sign: ZodiacSign): List<String> {
        return when (sign) {
            ZodiacSign.ARIES -> listOf("Red", "Orange", "Yellow")
            ZodiacSign.TAURUS -> listOf("Green", "Pink", "Blue")
            ZodiacSign.GEMINI -> listOf("Yellow", "Silver", "Green")
            ZodiacSign.CANCER -> listOf("White", "Silver", "Sea Green")
            ZodiacSign.LEO -> listOf("Gold", "Orange", "Red")
            ZodiacSign.VIRGO -> listOf("Navy Blue", "Grey", "Brown")
            ZodiacSign.LIBRA -> listOf("Pink", "Blue", "Green")
            ZodiacSign.SCORPIO -> listOf("Deep Red", "Black", "Maroon")
            ZodiacSign.SAGITTARIUS -> listOf("Purple", "Turquoise", "Orange")
            ZodiacSign.CAPRICORN -> listOf("Brown", "Black", "Grey")
            ZodiacSign.AQUARIUS -> listOf("Blue", "Silver", "Aqua")
            ZodiacSign.PISCES -> listOf("Sea Green", "Lavender", "Purple")
        }
    }

    private fun getCompatibleSigns(sign: ZodiacSign): List<ZodiacSign> {
        return when (sign) {
            ZodiacSign.ARIES -> listOf(ZodiacSign.LEO, ZodiacSign.SAGITTARIUS, ZodiacSign.GEMINI, ZodiacSign.AQUARIUS)
            ZodiacSign.TAURUS -> listOf(ZodiacSign.VIRGO, ZodiacSign.CAPRICORN, ZodiacSign.CANCER, ZodiacSign.PISCES)
            ZodiacSign.GEMINI -> listOf(ZodiacSign.LIBRA, ZodiacSign.AQUARIUS, ZodiacSign.ARIES, ZodiacSign.LEO)
            ZodiacSign.CANCER -> listOf(ZodiacSign.SCORPIO, ZodiacSign.PISCES, ZodiacSign.TAURUS, ZodiacSign.VIRGO)
            ZodiacSign.LEO -> listOf(ZodiacSign.ARIES, ZodiacSign.SAGITTARIUS, ZodiacSign.GEMINI, ZodiacSign.LIBRA)
            ZodiacSign.VIRGO -> listOf(ZodiacSign.TAURUS, ZodiacSign.CAPRICORN, ZodiacSign.CANCER, ZodiacSign.SCORPIO)
            ZodiacSign.LIBRA -> listOf(ZodiacSign.GEMINI, ZodiacSign.AQUARIUS, ZodiacSign.LEO, ZodiacSign.SAGITTARIUS)
            ZodiacSign.SCORPIO -> listOf(ZodiacSign.CANCER, ZodiacSign.PISCES, ZodiacSign.VIRGO, ZodiacSign.CAPRICORN)
            ZodiacSign.SAGITTARIUS -> listOf(ZodiacSign.ARIES, ZodiacSign.LEO, ZodiacSign.LIBRA, ZodiacSign.AQUARIUS)
            ZodiacSign.CAPRICORN -> listOf(ZodiacSign.TAURUS, ZodiacSign.VIRGO, ZodiacSign.SCORPIO, ZodiacSign.PISCES)
            ZodiacSign.AQUARIUS -> listOf(ZodiacSign.GEMINI, ZodiacSign.LIBRA, ZodiacSign.ARIES, ZodiacSign.SAGITTARIUS)
            ZodiacSign.PISCES -> listOf(ZodiacSign.CANCER, ZodiacSign.SCORPIO, ZodiacSign.TAURUS, ZodiacSign.CAPRICORN)
        }
    }

    private fun getKeywords(sign: ZodiacSign): List<String> {
        return when (sign) {
            ZodiacSign.ARIES -> listOf("Pioneer", "Leader", "Warrior", "Initiator", "Trailblazer")
            ZodiacSign.TAURUS -> listOf("Builder", "Preserver", "Sensualist", "Provider", "Stabilizer")
            ZodiacSign.GEMINI -> listOf("Communicator", "Messenger", "Thinker", "Adapter", "Networker")
            ZodiacSign.CANCER -> listOf("Nurturer", "Protector", "Caregiver", "Empath", "Healer")
            ZodiacSign.LEO -> listOf("Performer", "Creator", "Leader", "Entertainer", "Inspirer")
            ZodiacSign.VIRGO -> listOf("Analyzer", "Perfecter", "Server", "Helper", "Organizer")
            ZodiacSign.LIBRA -> listOf("Diplomat", "Peacemaker", "Artist", "Harmonizer", "Mediator")
            ZodiacSign.SCORPIO -> listOf("Transformer", "Investigator", "Healer", "Mystic", "Detective")
            ZodiacSign.SAGITTARIUS -> listOf("Explorer", "Philosopher", "Teacher", "Adventurer", "Seeker")
            ZodiacSign.CAPRICORN -> listOf("Achiever", "Builder", "Leader", "Strategist", "Master")
            ZodiacSign.AQUARIUS -> listOf("Innovator", "Humanitarian", "Rebel", "Visionary", "Revolutionary")
            ZodiacSign.PISCES -> listOf("Dreamer", "Mystic", "Artist", "Healer", "Intuitive")
        }
    }
}

// Supporting data class
data class ZodiacSignDetailInfo(
    val sign: ZodiacSign,
    val element: ZodiacElement,
    val quality: ZodiacQuality,
    val rulingPlanet: String,
    val symbol: String,
    val dateRange: String,
    val description: String,
    val strengths: List<String>,
    val challenges: List<String>,
    val luckyNumbers: List<Int>,
    val luckyColors: List<String>,
    val compatibleSigns: List<ZodiacSign>,
    val keywords: List<String>
)