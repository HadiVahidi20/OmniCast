package com.hadify.omnicast.core.data.source

import android.content.Context
import com.hadify.omnicast.core.data.model.ContentType
import com.hadify.omnicast.core.data.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation for loading content from Android assets
 */
@Singleton
class AssetDataSourceImpl @Inject constructor(
    private val context: Context
) : AssetDataSource {

    override suspend fun loadRawContent(
        contentType: ContentType,
        language: String
    ): Resource<String> = withContext(Dispatchers.IO) {
        try {
            val path = "content/$language/${contentType.fileName}"
            val content = context.assets.open(path).bufferedReader().use { it.readText() }
            Resource.Success(content)
        } catch (e: IOException) {
            // Try fallback to English if language-specific content not found
            if (language != "en") {
                loadRawContent(contentType, "en")
            } else {
                Resource.Error("Failed to load content: ${e.message}")
            }
        } catch (e: Exception) {
            Resource.Error("Unexpected error loading content: ${e.message}")
        }
    }

    override suspend fun contentExists(
        contentType: ContentType,
        language: String
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val path = "content/$language/${contentType.fileName}"
            context.assets.open(path).close()
            true
        } catch (e: IOException) {
            false
        }
    }

    override suspend fun getAvailableLanguages(contentType: ContentType): List<String> =
        withContext(Dispatchers.IO) {
            try {
                val contentDir = context.assets.list("content") ?: emptyArray()
                contentDir.filter { language ->
                    contentExists(contentType, language)
                }
            } catch (e: Exception) {
                listOf("en") // Default fallback
            }
        }
}