package com.hadify.omnicast.core.domain.usecase

import kotlinx.coroutines.flow.Flow

/**
 * Base interface for all use cases - marker interface
 */
interface UseCase

/**
 * Base use case for operations that require parameters and return a single result
 * This is the main interface used by existing code
 */
interface BaseUseCase<in P, out T> : UseCase {
    suspend operator fun invoke(params: P): T
}

/**
 * Base use case for operations that don't require parameters and return a single result
 */
interface NoParamsUseCase<out T> : UseCase {
    suspend operator fun invoke(): T
}

/**
 * Base use case for operations that don't require parameters and return a Flow
 */
interface NoParamsFlowUseCase<out T> : UseCase {
    operator fun invoke(): Flow<T>
}

/**
 * Base use case for operations that require parameters and return a Flow
 */
interface FlowUseCase<in P, out T> : UseCase {
    operator fun invoke(params: P): Flow<T>
}

/**
 * Use case for operations that don't return anything (Unit)
 */
interface NoReturnUseCase<in P> : UseCase {
    suspend operator fun invoke(params: P)
}

/**
 * Use case for operations that don't require parameters and don't return anything
 */
interface SimpleUseCase : UseCase {
    suspend operator fun invoke()
}

/**
 * Parameters wrapper for use cases that need multiple parameters
 */
interface UseCaseParams

/**
 * Empty parameters for use cases that don't need any parameters
 */
object NoParams : UseCaseParams

/**
 * Common parameter classes for typical operations
 */

/**
 * Parameters for use cases that work with user ID
 */
data class UserIdParams(val userId: String) : UseCaseParams

/**
 * Parameters for use cases that work with dates
 */
data class DateParams(val date: java.time.LocalDate) : UseCaseParams

/**
 * Parameters for use cases that work with date ranges
 */
data class DateRangeParams(
    val startDate: java.time.LocalDate,
    val endDate: java.time.LocalDate
) : UseCaseParams

/**
 * Parameters for pagination
 */
data class PaginationParams(
    val page: Int,
    val pageSize: Int = 20
) : UseCaseParams