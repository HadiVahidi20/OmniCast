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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.WeekFields
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of ZodiacRepository
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
     * Generate daily horoscope reading
     */
    private suspend fun generateDailyHoroscope(
        zodiacSign: ZodiacSign,
        date: LocalDate
    ): HoroscopeReading {
        val zodiacData = loadZodiacData()
        val signData = zodiacData.signs.find { it.id == zodiacSign.name.lowercase() }
            ?: throw IllegalArgumentException("Zodiac sign data not found")

        // Use date as seed for deterministic but varied readings
        val seed = (date.toEpochDay() + zodiacSign.ordinal).toInt()
        val random = Random(seed.toLong())

        // Select predictions based on seed
        val predictions = signData.predictions.daily
        val selectedPrediction = predictions[seed % predictions.size]

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
            intensity = 0.5f + (random.nextFloat() * 0.5f) // 0.5 to 1.0
        )
    }

    /**
     * Generate weekly horoscope reading
     */
    private suspend fun generateWeeklyHoroscope(
        zodiacSign: ZodiacSign,
        weekStart: LocalDate
    ): WeeklyHoroscopeReading {
        val zodiacData = loadZodiacData()
        val signData = zodiacData.signs.find { it.id == zodiacSign.name.lowercase() }
            ?: throw IllegalArgumentException("Zodiac sign data not found")

        val weekEnd = weekStart.plusDays(6)
        val seed = (weekStart.toEpochDay() / 7 + zodiacSign.ordinal).toInt()

        // Use weekly predictions if available, otherwise generate from daily
        val weeklyPredictions = signData.predictions.weekly
        val selectedWeekly = if (weeklyPredictions.isNotEmpty()) {
            weeklyPredictions[seed % weeklyPredictions.size]
        } else {
            // Generate weekly from daily data
            null
        }

        return WeeklyHoroscopeReading(
            id = "${zodiacSign.name.lowercase()}_week_${weekStart}",
            zodiacSign = zodiacSign,
            weekStartDate = weekStart,
            weekEndDate = weekEnd,
            general = selectedWeekly?.general ?: "This week brings opportunities for growth and new perspectives.",
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
        // Step 1: Load the raw JSON content as a string
        val jsonResult = contentLoader.loadRawContent(ContentType.ZODIAC)

        val jsonString = when(jsonResult) {
            is Resource.Success -> jsonResult.data
            is Resource.Error -> throw Exception("Failed to load zodiac JSON: ${jsonResult.message}")
            is Resource.Loading -> throw Exception("Unexpected loading state")
        }

        // Step 2: Use the configured parser from ContentLoader
        val parsedResult = contentLoader.parseJson(jsonString, ZodiacDataModel.serializer())

        return when(parsedResult) {
            is Resource.Success -> parsedResult.data
            is Resource.Error -> throw Exception("Failed to parse zodiac JSON: ${parsedResult.message}")
            is Resource.Loading -> throw Exception("Unexpected loading state during parsing")
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
            zodiacSign = ZodiacSign.valueOf(zodiacSignName),
            date = date,
            general = general,
            love = love,
            career = career,
            health = health,
            luckyNumber = luckyNumber,
            luckyColor = luckyColor,
            compatibility = ZodiacSign.valueOf(compatibilitySignName),
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
            zodiacSign = ZodiacSign.valueOf(zodiacSignName),
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

// Data models for JSON parsing

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
    val hasImage: Boolean? = null, // Make optional to handle absence
    val imageFileName: String? = null, // Make optional
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