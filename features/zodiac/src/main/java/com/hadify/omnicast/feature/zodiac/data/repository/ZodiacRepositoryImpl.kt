// ZodiacRepositoryImpl.kt - Complete implementation with missing functions
package com.hadify.omnicast.feature.zodiac.data.repository

import com.hadify.omnicast.feature.zodiac.domain.model.HoroscopeReading
import com.hadify.omnicast.feature.zodiac.domain.model.WeeklyHoroscopeReading
import com.hadify.omnicast.feature.zodiac.domain.model.ZodiacSign
import com.hadify.omnicast.feature.zodiac.domain.repository.ZodiacRepository
import com.hadify.omnicast.core.data.local.dao.ZodiacDao
import com.hadify.omnicast.core.data.local.entity.ZodiacEntity
import com.hadify.omnicast.core.data.local.entity.WeeklyZodiacEntity
import com.hadify.omnicast.core.data.source.ContentLoader
import com.hadify.omnicast.core.data.model.ContentType
import com.hadify.omnicast.core.data.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.WeekFields
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Complete implementation of ZodiacRepository
 * Handles loading zodiac content from JSON assets and caching in database
 */
@Singleton
class ZodiacRepositoryImpl @Inject constructor(
    private val zodiacDao: ZodiacDao,
    private val contentLoader: ContentLoader
) : ZodiacRepository {

    companion object {
        private const val CACHE_DAYS = 30
    }

    /**
     * Get daily horoscope for a specific zodiac sign and date
     */
    override suspend fun getDailyHoroscope(
        zodiacSign: ZodiacSign,
        date: LocalDate
    ): Flow<Resource<HoroscopeReading>> = flow {
        emit(Resource.Loading)

        try {
            // 1. Check cache first
            val cachedHoroscope = zodiacDao.getHoroscopeForDate(zodiacSign.name, date)
            if (cachedHoroscope != null) {
                emit(Resource.Success(cachedHoroscope.toDomain()))
                return@flow
            }

            // 2. Load from JSON and generate horoscope
            val horoscopeReading = generateDailyHoroscope(zodiacSign, date)

            // 3. Cache the result
            zodiacDao.insertHoroscope(horoscopeReading.toEntity())

            emit(Resource.Success(horoscopeReading))

        } catch (e: Exception) {
            emit(Resource.Error("Failed to load daily horoscope: ${e.message}"))
        }
    }

    /**
     * Get weekly horoscope for a specific zodiac sign
     */
    override suspend fun getWeeklyHoroscope(
        zodiacSign: ZodiacSign,
        startDate: LocalDate
    ): Flow<Resource<WeeklyHoroscopeReading>> = flow {
        emit(Resource.Loading)

        try {
            // Calculate week start (Monday)
            val weekFields = WeekFields.of(Locale.getDefault())
            val weekStart = startDate.with(weekFields.dayOfWeek(), 1)

            // Check cache first
            val cachedWeekly = zodiacDao.getWeeklyHoroscope(zodiacSign.name, weekStart)
            if (cachedWeekly != null) {
                emit(Resource.Success(cachedWeekly.toDomain()))
                return@flow
            }

            // Generate weekly horoscope
            val weeklyReading = generateWeeklyHoroscope(zodiacSign, weekStart)

            // Cache the result
            zodiacDao.insertWeeklyHoroscope(weeklyReading.toEntity())

            emit(Resource.Success(weeklyReading))

        } catch (e: Exception) {
            emit(Resource.Error("Failed to load weekly horoscope: ${e.message}"))
        }
    }

    /**
     * Get horoscope reading by ID
     */
    override suspend fun getHoroscopeById(id: String): Resource<HoroscopeReading?> {
        return try {
            val entity = zodiacDao.getHoroscopeById(id)
            Resource.Success(entity?.toDomain())
        } catch (e: Exception) {
            Resource.Error("Failed to get horoscope by ID: ${e.message}")
        }
    }

    /**
     * Get compatibility analysis between two zodiac signs
     */
    override suspend fun getCompatibilityReading(
        sign1: ZodiacSign,
        sign2: ZodiacSign
    ): Resource<String> {
        return try {
            // Load zodiac data to get compatibility info
            val zodiacData = loadZodiacData()
            val sign1Data = zodiacData.signs.find { it.id == sign1.name.lowercase() }
            val sign2Data = zodiacData.signs.find { it.id == sign2.name.lowercase() }

            val compatibility = when {
                sign1Data?.compatibleSigns?.contains(sign2.name.lowercase()) == true ->
                    "High compatibility! ${sign1.displayName} and ${sign2.displayName} share excellent harmony."
                sign1.element == sign2.element ->
                    "Good compatibility through shared ${sign1.element.displayName} energy."
                else ->
                    "Moderate compatibility. ${sign1.displayName} and ${sign2.displayName} can learn from each other's differences."
            }

            Resource.Success(compatibility)
        } catch (e: Exception) {
            Resource.Error("Failed to analyze compatibility: ${e.message}")
        }
    }

    /**
     * Cache horoscope reading locally
     */
    override suspend fun cacheHoroscope(reading: HoroscopeReading): Resource<Unit> {
        return try {
            zodiacDao.insertHoroscope(reading.toEntity())
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Failed to cache horoscope: ${e.message}")
        }
    }

    /**
     * Get cached horoscopes for a zodiac sign
     */
    override fun getCachedHoroscopes(zodiacSign: ZodiacSign): Flow<List<HoroscopeReading>> {
        return zodiacDao.getCachedHoroscopes(zodiacSign.name).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    /**
     * Clear old cached horoscopes
     */
    override suspend fun clearOldCache(daysToKeep: Int): Resource<Unit> {
        return try {
            val cutoffDate = LocalDate.now().minusDays(daysToKeep.toLong())
            zodiacDao.deleteOldHoroscopes(cutoffDate)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Failed to clear old cache: ${e.message}")
        }
    }

    /**
     * Check if fresh horoscope data is available for today
     */
    override suspend fun hasTodaysHoroscope(zodiacSign: ZodiacSign): Boolean {
        return try {
            zodiacDao.hasHoroscopeForDate(zodiacSign.name, LocalDate.now())
        } catch (e: Exception) {
            false
        }
    }

    // Private helper methods

    /**
     * Generate daily horoscope reading from JSON data
     */
    private suspend fun generateDailyHoroscope(
        zodiacSign: ZodiacSign,
        date: LocalDate
    ): HoroscopeReading {
        val zodiacData = loadZodiacData()
        val signData = zodiacData.signs.find { it.id == zodiacSign.name.lowercase() }
            ?: throw IllegalArgumentException("Sign data not found for ${zodiacSign.name}")

        // Use date as seed for consistent daily predictions
        val seed = (date.toEpochDay() % signData.predictions.daily.size).toInt()
        val selectedPrediction = signData.predictions.daily[seed.coerceAtLeast(0)]

        return HoroscopeReading(
            id = "${zodiacSign.name.lowercase()}_${date}",
            zodiacSign = zodiacSign,
            date = date,
            general = selectedPrediction.general,
            love = selectedPrediction.love,
            career = selectedPrediction.career,
            health = selectedPrediction.health,
            luckyNumber = selectedPrediction.luckyNumber,
            luckyColor = selectedPrediction.luckyColor,
            compatibility = ZodiacSign.valueOf(selectedPrediction.compatibility.uppercase()),
            mood = selectedPrediction.mood,
            tags = selectedPrediction.tags,
            intensity = 0.5f + (seed * 0.1f) // Vary intensity based on seed
        )
    }

    /**
     * Generate weekly horoscope reading
     */
    private suspend fun generateWeeklyHoroscope(
        zodiacSign: ZodiacSign,
        weekStartDate: LocalDate
    ): WeeklyHoroscopeReading {
        val zodiacData = loadZodiacData()
        val signData = zodiacData.signs.find { it.id == zodiacSign.name.lowercase() }
            ?: throw IllegalArgumentException("Sign data not found for ${zodiacSign.name}")

        // Use week start date as seed for consistent weekly predictions
        val weekOfYear = weekStartDate.dayOfYear / 7
        val selectedWeekly = if (signData.predictions.weekly.isNotEmpty()) {
            signData.predictions.weekly[weekOfYear % signData.predictions.weekly.size]
        } else {
            // Generate from daily predictions if no weekly data
            null
        }

        return WeeklyHoroscopeReading(
            id = "${zodiacSign.name.lowercase()}_week_${weekStartDate}",
            zodiacSign = zodiacSign,
            weekStartDate = weekStartDate,
            weekEndDate = weekStartDate.plusDays(6),
            general = selectedWeekly?.general ?: "This week brings opportunities for growth and self-reflection.",
            love = selectedWeekly?.love ?: "Relationships benefit from open communication and understanding.",
            career = selectedWeekly?.career ?: "Professional matters require patience and strategic thinking.",
            health = selectedWeekly?.health ?: "Focus on balance between activity and rest for optimal well-being.",
            luckyDays = selectedWeekly?.luckyDays ?: listOf("Tuesday", "Friday"),
            challengingDays = selectedWeekly?.challengingDays ?: listOf("Wednesday"),
            overallTrend = selectedWeekly?.overallTrend ?: "Positive",
            tags = selectedWeekly?.tags ?: listOf("growth", "balance", "opportunity")
        )
    }

    /**
     * Load zodiac data from JSON assets
     */
    private suspend fun loadZodiacData(): ZodiacDataModel {
        val jsonResult = contentLoader.loadRawContent(ContentType.ZODIAC)

        return when (jsonResult) {
            is Resource.Success -> {
                try {
                    Json.decodeFromString(ZodiacDataModel.serializer(), jsonResult.data)
                } catch (e: Exception) {
                    throw Exception("Failed to parse zodiac JSON: ${e.message}")
                }
            }
            is Resource.Error -> {
                throw Exception("Failed to load zodiac JSON: ${jsonResult.message}")
            }
            is Resource.Loading -> {
                throw Exception("Unexpected loading state")
            }
        }
    }

    // Extension functions for entity-domain conversion

    private fun HoroscopeReading.toEntity(): ZodiacEntity {
        return ZodiacEntity(
            id = id,
            zodiacSignName = zodiacSign.name,
            date = date,
            general = general,
            love = love,
            career = career,
            health = health,
            luckyNumber = luckyNumber,
            luckyColor = luckyColor,
            compatibilitySignName = compatibility.name,
            mood = mood,
            tags = tags,
            intensity = intensity,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
    }

    private fun ZodiacEntity.toDomain(): HoroscopeReading {
        return HoroscopeReading(
            id = id,
            zodiacSign = ZodiacSign.valueOf(zodiacSignName.uppercase()),
            date = date,
            general = general,
            love = love,
            career = career,
            health = health,
            luckyNumber = luckyNumber,
            luckyColor = luckyColor,
            compatibility = ZodiacSign.valueOf(compatibilitySignName.uppercase()),
            mood = mood,
            tags = tags,
            intensity = intensity
        )
    }

    private fun WeeklyHoroscopeReading.toEntity(): WeeklyZodiacEntity {
        return WeeklyZodiacEntity(
            id = id,
            zodiacSignName = zodiacSign.name,
            weekStartDate = weekStartDate,
            weekEndDate = weekEndDate,
            general = general,
            love = love,
            career = career,
            health = health,
            luckyDays = luckyDays,
            challengingDays = challengingDays,
            overallTrend = overallTrend,
            tags = tags,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
    }

    private fun WeeklyZodiacEntity.toDomain(): WeeklyHoroscopeReading {
        return WeeklyHoroscopeReading(
            id = id,
            zodiacSign = ZodiacSign.valueOf(zodiacSignName.uppercase()),
            weekStartDate = weekStartDate,
            weekEndDate = weekEndDate,
            general = general,
            love = love,
            career = career,
            health = health,
            luckyDays = luckyDays,
            challengingDays = challengingDays,
            overallTrend = overallTrend,
            tags = tags
        )
    }
}

// Data models for JSON parsing (these should match the JSON structure)

@Serializable
data class ZodiacDataModel(
    val metadata: ContentMetadata,
    val signs: List<ZodiacSignData>
)

@Serializable
data class ContentMetadata(
    val version: String,
    val lastUpdated: String,
    val language: String,
    val itemCount: Int
)

@Serializable
data class ZodiacSignData(
    val id: String,
    val nameKey: String,
    val dateRange: String,
    val element: String,
    val symbol: String,
    val rulingPlanet: String,
    val luckyNumbers: List<Int>,
    val luckyColors: List<String>,
    val compatibleSigns: List<String>,
    val qualities: List<String>,
    val polarity: String,
    val description: String,
    val strengths: List<String>,
    val weaknesses: List<String>,
    val predictions: PredictionsData
)

@Serializable
data class PredictionsData(
    val daily: List<DailyPrediction>,
    val weekly: List<WeeklyPrediction> = emptyList()
)

@Serializable
data class DailyPrediction(
    val id: String,
    val seed: Int,
    val general: String,
    val love: String,
    val career: String,
    val health: String,
    val luckyNumber: Int,
    val luckyColor: String,
    val compatibility: String,
    val mood: String,
    val tags: List<String>
)

@Serializable
data class WeeklyPrediction(
    val id: String,
    val weekNumber: Int,
    val startDate: String,
    val endDate: String,
    val general: String,
    val love: String,
    val career: String,
    val health: String,
    val luckyDays: List<String>,
    val challengingDays: List<String>,
    val overallTrend: String,
    val tags: List<String>
)