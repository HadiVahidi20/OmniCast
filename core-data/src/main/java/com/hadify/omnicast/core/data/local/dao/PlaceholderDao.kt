package com.hadify.omnicast.core.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.hadify.omnicast.core.data.local.entity.PlaceholderEntity

/**
 * Temporary placeholder DAO
 * Will be removed once real DAOs are added by other developers
 */
@Dao
interface PlaceholderDao {

    @Query("SELECT * FROM placeholder LIMIT 1")
    suspend fun getPlaceholder(): PlaceholderEntity?
}