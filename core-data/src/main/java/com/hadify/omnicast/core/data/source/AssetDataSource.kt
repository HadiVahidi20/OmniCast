package com.hadify.omnicast.core.data.source

import com.hadify.omnicast.core.data.model.ContentType
import com.hadify.omnicast.core.data.util.Resource

/**
 * Interface for loading content from app assets
 */
interface AssetDataSource {

    /**
     * Load raw JSON content as string
     */
    suspend fun loadRawContent(contentType: ContentType, language: String = "en"): Resource<String>

    /**
     * Check if content exists for given type and language
     */
    suspend fun contentExists(contentType: ContentType, language: String = "en"): Boolean

    /**
     * Get available languages for a content type
     */
    suspend fun getAvailableLanguages(contentType: ContentType): List<String>
}