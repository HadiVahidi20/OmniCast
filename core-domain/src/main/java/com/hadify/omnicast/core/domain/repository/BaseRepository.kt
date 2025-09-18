package com.hadify.omnicast.core.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Base repository interface with common CRUD operations
 * Can be extended by feature-specific repositories
 */
interface BaseRepository<T, ID> {
    suspend fun insert(item: T): Long
    suspend fun update(item: T)
    suspend fun delete(item: T)
    suspend fun getById(id: ID): T?
    fun getAll(): Flow<List<T>>
}

/**
 * Base read-only repository for data that doesn't need modification
 */
interface ReadOnlyRepository<T, ID> {
    suspend fun getById(id: ID): T?
    fun getAll(): Flow<List<T>>
}