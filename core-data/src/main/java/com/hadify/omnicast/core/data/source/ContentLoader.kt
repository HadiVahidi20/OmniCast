package com.hadify.omnicast.core.data.source

import com.hadify.omnicast.core.data.util.Resource
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

/**
 * ContentLoader handles parsing and caching of JSON content from assets
 * Provides a consistent interface for loading different types of divination content
 */
@Singleton
class ContentLoader @Inject constructor(
    private val assetDataSource: AssetDataSource
) {

    // In-memory cache for parsed content
    private val contentCache = mutableMapOf<String, JSONObject>()
    private val cacheMutex = Mutex()

    /**
     * Load and parse JSON content from assets with caching
     */
    suspend fun loadContent(fileName: String, locale: String = "en"): Resource<JSONObject> {
        val cacheKey = "${locale}_$fileName"

        return cacheMutex.withLock {
            // Check cache first
            contentCache[cacheKey]?.let { cachedContent ->
                return@withLock Resource.success(cachedContent)
            }

            // Load from assets if not cached
            when (val result = assetDataSource.loadJsonFromAssetsWithLocale(fileName, locale)) {
                is Resource.Success -> {
                    try {
                        val jsonObject = JSONObject(result.data)
                        contentCache[cacheKey] = jsonObject
                        Resource.success(jsonObject)
                    } catch (e: Exception) {
                        Resource.error(e, "Failed to parse JSON for $fileName")
                    }
                }
                is Resource.Error -> Resource.error(result.exception, result.message)
                is Resource.Loading -> Resource.loading()
            }
        }
    }

    /**
     * Load zodiac content specifically
     */
    suspend fun loadZodiacContent(locale: String = "en"): Resource<JSONObject> {
        return loadContent("zodiac.json", locale)
    }

    /**
     * Load tarot content specifically
     */
    suspend fun loadTarotContent(locale: String = "en"): Resource<JSONObject> {
        return loadContent("tarot.json", locale)
    }

    /**
     * Load numerology content specifically
     */
    suspend fun loadNumerologyContent(locale: String = "en"): Resource<JSONObject> {
        return loadContent("numerology.json", locale)
    }

    /**
     * Load biorhythm content specifically
     */
    suspend fun loadBiorhythmContent(locale: String = "en"): Resource<JSONObject> {
        return loadContent("biorhythm.json", locale)
    }

    /**
     * Load I Ching content specifically
     */
    suspend fun loadIChingContent(locale: String = "en"): Resource<JSONObject> {
        return loadContent("iching.json", locale)
    }

    /**
     * Load rune content specifically
     */
    suspend fun loadRuneContent(locale: String = "en"): Resource<JSONObject> {
        return loadContent("runes.json", locale)
    }

    /**
     * Load coffee reading content specifically
     */
    suspend fun loadCoffeeContent(locale: String = "en"): Resource<JSONObject> {
        return loadContent("coffee.json", locale)
    }

    /**
     * Load Abjad content specifically
     */
    suspend fun loadAbjadContent(locale: String = "en"): Resource<JSONObject> {
        return loadContent("abjad.json", locale)
    }

    /**
     * Clear content cache (useful for locale changes or memory management)
     */
    suspend fun clearCache() {
        cacheMutex.withLock {
            contentCache.clear()
        }
    }

    /**
     * Get cache size for monitoring
     */
    suspend fun getCacheSize(): Int {
        return cacheMutex.withLock {
            contentCache.size
        }
    }

    /**
     * Utility function to extract a specific zodiac sign from zodiac content
     */
    suspend fun getZodiacSignData(signName: String, locale: String = "en"): Resource<JSONObject> {
        return when (val zodiacContent = loadZodiacContent(locale)) {
            is Resource.Success -> {
                try {
                    val signsArray = zodiacContent.data.getJSONArray("signs")
                    for (i in 0 until signsArray.length()) {
                        val signData = signsArray.getJSONObject(i)
                        if (signData.getString("id").equals(signName, ignoreCase = true)) {
                            return Resource.success(signData)
                        }
                    }
                    Resource.error("Zodiac sign '$signName' not found")
                } catch (e: Exception) {
                    Resource.error(e, "Failed to extract zodiac sign data")
                }
            }
            is Resource.Error -> Resource.error(zodiacContent.exception, zodiacContent.message)
            is Resource.Loading -> Resource.loading()
        }
    }
}