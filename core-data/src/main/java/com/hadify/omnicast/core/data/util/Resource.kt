package com.hadify.omnicast.core.data.util

/**
 * A generic wrapper class for handling network/database operations results
 * Used throughout the app for consistent error handling
 */
sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: String) : Resource<Nothing>()
    data object Loading : Resource<Nothing>()
}