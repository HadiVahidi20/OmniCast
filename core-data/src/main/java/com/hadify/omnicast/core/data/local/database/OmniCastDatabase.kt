package com.hadify.omnicast.core.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.hadify.omnicast.core.data.local.converter.Converters
import com.hadify.omnicast.core.data.local.entity.PlaceholderEntity
import com.hadify.omnicast.core.data.local.dao.PlaceholderDao
import com.hadify.omnicast.core.data.local.entity.UserEntity
import com.hadify.omnicast.core.data.local.dao.UserDao
import com.hadify.omnicast.core.data.local.entity.ZodiacEntity
import com.hadify.omnicast.core.data.local.entity.WeeklyZodiacEntity
import com.hadify.omnicast.core.data.local.dao.ZodiacDao

/**
 * Main Room database for OmniCast app
 * All entities and DAOs properly imported from core-data
 */
@Database(
    entities = [
        PlaceholderEntity::class,  // Temporary - will be removed later
        UserEntity::class,         // User profile data
        ZodiacEntity::class,       // Daily horoscope readings
        WeeklyZodiacEntity::class  // Weekly horoscope readings
        // JournalEntity::class,   // Will be added by Developer 3
        // BiorhythmEntity::class, // Will be added by Developer 3
        // TarotEntity::class,     // Will be added by Developer 4
        // RuneEntity::class,      // Will be added by Developer 4
        // CoffeeEntity::class,    // Will be added by Developer 4
        // NumerologyEntity::class,// Will be added by Developer 5
        // IChingEntity::class,    // Will be added by Developer 5
        // AbjadEntity::class,     // Will be added by Developer 5
    ],
    version = 1, // ✅ FIXED: Changed from 2 to 1 for initial development
    exportSchema = false // Set to true in production for proper migrations
)
@TypeConverters(Converters::class)
abstract class OmniCastDatabase : RoomDatabase() {

    // Temporary placeholder DAO - will be removed once real DAOs are added
    abstract fun placeholderDao(): PlaceholderDao

    // User management DAO - critical for all other features
    abstract fun userDao(): UserDao

    // Zodiac feature DAO - first divination feature
    abstract fun zodiacDao(): ZodiacDao

    // Additional DAOs will be added here as features are implemented:
    // abstract fun journalDao(): JournalDao          // Developer 3
    // abstract fun biorhythmDao(): BiorhythmDao      // Developer 3
    // abstract fun overviewDao(): OverviewDao        // Developer 3
    // abstract fun tarotDao(): TarotDao              // Developer 4
    // abstract fun runeDao(): RuneDao                // Developer 4
    // abstract fun coffeeDao(): CoffeeDao            // Developer 4
    // abstract fun numerologyDao(): NumerologyDao    // Developer 5
    // abstract fun ichingDao(): IChingDao            // Developer 5
    // abstract fun abjadDao(): AbjadDao              // Developer 5

    companion object {
        const val DATABASE_NAME = "omnicast_database"

        /**
         * Create database instance
         * This method is provided for manual database creation if needed
         * Normally, the database is created through Dagger/Hilt in DataModule
         */
        fun create(context: Context): OmniCastDatabase {
            return Room.databaseBuilder(
                context,
                OmniCastDatabase::class.java,
                DATABASE_NAME
            )
                .fallbackToDestructiveMigration() // For development only - remove in production
                .build()
        }
    }
}