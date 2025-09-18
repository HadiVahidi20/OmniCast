package com.hadify.omnicast.core.domain.model

/**
 * Domain layer result wrapper
 * Similar to the data layer Resource but for domain operations
 */
sealed class DomainResult<out T> {
    data class Success<T>(val data: T) : DomainResult<T>()
    data class Error(val exception: Throwable, val message: String? = null) : DomainResult<Nothing>()
    data object Loading : DomainResult<Nothing>()
}

/**
 * Extension functions for easier result handling
 */
inline fun <T> DomainResult<T>.onSuccess(action: (T) -> Unit): DomainResult<T> {
    if (this is DomainResult.Success) action(data)
    return this
}

inline fun <T> DomainResult<T>.onError(action: (Throwable, String?) -> Unit): DomainResult<T> {
    if (this is DomainResult.Error) action(exception, message)
    return this
}

inline fun <T> DomainResult<T>.onLoading(action: () -> Unit): DomainResult<T> {
    if (this is DomainResult.Loading) action()
    return this
}