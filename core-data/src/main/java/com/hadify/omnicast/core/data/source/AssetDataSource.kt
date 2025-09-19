package com.hadify.omnicast.core.data.source

import android.content.Context
import com.hadify.omnicast.core.data.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

/**
 * Interface for loading content from app assets
 */
interface AssetDataSource {
    suspend fun loadJsonFromAssets(fileName: String): Resource<String>
    suspend fun loadJsonFromAssetsWithLocale(fileName: String, locale: String): Resource<String>
    fun getAvailableLocales(): List<String>
}

/**
 * Implementation of AssetDataSource for loading JSON content from Android assets
 */
class AssetDataSourceImpl @Inject constructor(
    private val context: Context
) : AssetDataSource {

    companion object {
        private const val CONTENT_FOLDER = "content"
        private const val DEFAULT_LOCALE = "en"
    }

    /**
     * Load JSON content from assets folder
     */
    override suspend fun loadJsonFromAssets(fileName: String): Resource<String> {
        return loadJsonFromAssetsWithLocale(fileName, DEFAULT_LOCALE)
    }

    /**
     * Load JSON content from assets folder with specific locale
     */
    override suspend fun loadJsonFromAssetsWithLocale(
        fileName: String,
        locale: String
    ): Resource<String> = withContext(Dispatchers.IO) {
        try {
            val fullPath = "$CONTENT_FOLDER/$locale/$fileName"
            val inputStream = context.assets.open(fullPath)
            val content = inputStream.bufferedReader().use { it.readText() }
            inputStream.close()

            Resource.success(content)
        } catch (e: IOException) {
            // Try fallback to default locale if specific locale fails
            if (locale != DEFAULT_LOCALE) {
                try {
                    val fallbackPath = "$CONTENT_FOLDER/$DEFAULT_LOCALE/$fileName"
                    val inputStream = context.assets.open(fallbackPath)
                    val content = inputStream.bufferedReader().use { it.readText() }
                    inputStream.close()

                    Resource.success(content)
                } catch (fallbackException: IOException) {
                    Resource.error(
                        fallbackException,
                        "Failed to load $fileName for locale $locale and fallback $DEFAULT_LOCALE"
                    )
                }
            } else {
                Resource.error(e, "Failed to load $fileName from assets")
            }
        } catch (e: Exception) {
            Resource.error(e, "Unexpected error loading $fileName: ${e.message}")
        }
    }

    /**
     * Get list of available locales by checking content folder structure
     */
    override fun getAvailableLocales(): List<String> {
        return try {
            val contentFolders = context.assets.list(CONTENT_FOLDER) ?: emptyArray()
            contentFolders.toList().sorted()
        } catch (e: IOException) {
            listOf(DEFAULT_LOCALE) // Return default locale if we can't read folder structure
        }
    }
}