package com.hadify.omnicast.core.data.util

/**
 * Generic Resource wrapper for handling API/Database responses
 * Provides a consistent way to handle loading, success, and error states
 */
sealed class Resource<out T> {

    /**
     * Loading state - operation is in progress
     */
    data object Loading : Resource<Nothing>()

    /**
     * Success state - operation completed successfully with data
     */
    data class Success<T>(val data: T) : Resource<T>()

    /**
     * Error state - operation failed with error information
     */
    data class Error(
        val exception: Throwable,
        val message: String = exception.message ?: "Unknown error occurred"
    ) : Resource<Nothing>()

    companion object {
        /**
         * Create a loading resource
         */
        fun <T> loading(): Resource<T> = Loading

        /**
         * Create a success resource with data
         */
        fun <T> success(data: T): Resource<T> = Success(data)

        /**
         * Create an error resource with exception
         */
        fun <T> error(exception: Throwable): Resource<T> = Error(exception)

        /**
         * Create an error resource with message
         */
        fun <T> error(message: String): Resource<T> = Error(Exception(message), message)

        /**
         * Create an error resource with exception and custom message
         */
        fun <T> error(exception: Throwable, message: String): Resource<T> = Error(exception, message)
    }
}

/**
 * Extension function to check if resource is loading
 */
fun <T> Resource<T>.isLoading(): Boolean = this is Resource.Loading

/**
 * Extension function to check if resource is success
 */
fun <T> Resource<T>.isSuccess(): Boolean = this is Resource.Success

/**
 * Extension function to check if resource is error
 */
fun <T> Resource<T>.isError(): Boolean = this is Resource.Error

/**
 * Extension function to get data if success, null otherwise
 */
fun <T> Resource<T>.getDataOrNull(): T? = when (this) {
    is Resource.Success -> data
    else -> null
}

/**
 * Extension function to get error message if error, null otherwise
 */
fun <T> Resource<T>.getErrorMessage(): String? = when (this) {
    is Resource.Error -> message
    else -> null
}

/**
 * Extension function to handle resource states
 */
inline fun <T> Resource<T>.onSuccess(action: (T) -> Unit): Resource<T> {
    if (this is Resource.Success) action(data)
    return this
}

/**
 * Extension function to handle error state
 */
inline fun <T> Resource<T>.onError(action: (String) -> Unit): Resource<T> {
    if (this is Resource.Error) action(message)
    return this
}

/**
 * Extension function to handle loading state
 */
inline fun <T> Resource<T>.onLoading(action: () -> Unit): Resource<T> {
    if (this is Resource.Loading) action()
    return this
}