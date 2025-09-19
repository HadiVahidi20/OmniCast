package com.hadify.omnicast.core.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hadify.omnicast.core.data.local.converter.TypeConverters as OmniCastTypeConverters
import com.hadify.omnicast.core.data.local.dao.UserDao
import com.hadify.omnicast.core.data.local.dao.ZodiacDao
import com.hadify.omnicast.core.data.local.entity.UserEntity
import com.hadify.omnicast.core.data.local.entity.ZodiacEntity
import com.hadify.omnicast.core.data.local.entity.WeeklyZodiacEntity

/**
 * Main Room database for OmniCast application
 * Contains all entities and DAOs needed for offline storage
 */
@Database(
    entities = [
        UserEntity::class,
        ZodiacEntity::class,
        WeeklyZodiacEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(OmniCastTypeConverters::class)
abstract class OmniCastDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun zodiacDao(): ZodiacDao

    companion object {
        const val DATABASE_NAME = "omnicast_db"
    }
}