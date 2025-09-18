package com.hadify.omnicast.core.data.source

import com.hadify.omnicast.core.data.model.ContentType
import com.hadify.omnicast.core.data.util.Resource
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Helper class for loading and parsing JSON content from assets
 * Provides JSON parsing functionality without reified types
 */
@Singleton
class ContentLoader @Inject constructor(
    private val assetDataSource: AssetDataSource
) {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    /**
     * Load raw JSON content as string
     */
    suspend fun loadRawContent(
        contentType: ContentType,
        language: String = "en"
    ): Resource<String> {
        return assetDataSource.loadRawContent(contentType, language)
    }

    /**
     * Parse JSON string to specific type
     * Each feature can call this with their specific data classes
     */
    fun <T> parseJson(jsonString: String, deserializer: kotlinx.serialization.DeserializationStrategy<T>): Resource<T> {
        return try {
            val parsed = json.decodeFromString(deserializer, jsonString)
            Resource.Success(parsed)
        } catch (e: Exception) {
            Resource.Error("Failed to parse JSON: ${e.message}")
        }
    }
}