package com.hadify.omnicast.core.domain.usecase

import kotlinx.coroutines.flow.Flow

/**
 * Base interface for all use cases in the app
 * Provides consistent structure for business logic operations
 */
interface BaseUseCase<in Params, out Result> {
    suspend operator fun invoke(params: Params): Result
}

/**
 * Base interface for use cases that return Flow (reactive data)
 */
interface FlowUseCase<in Params, out Result> {
    operator fun invoke(params: Params): Flow<Result>
}

/**
 * Base interface for use cases with no parameters
 */
interface NoParamsUseCase<out Result> {
    suspend operator fun invoke(): Result
}

/**
 * Base interface for use cases that return Flow with no parameters
 */
interface NoParamsFlowUseCase<out Result> {
    operator fun invoke(): Flow<Result>
}