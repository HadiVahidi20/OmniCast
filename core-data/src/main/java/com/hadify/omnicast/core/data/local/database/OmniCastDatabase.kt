// OmniCastDatabase.kt
package com.hadify.omnicast.core.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.hadify.omnicast.core.data.local.converter.Converters
import com.hadify.omnicast.core.data.local.dao.ZodiacDao
import com.hadify.omnicast.core.data.local.entity.ZodiacEntity
import com.hadify.omnicast.core.data.local.entity.WeeklyZodiacEntity

/**
 * Main Room database for OmniCast app
 * Contains all entities and provides access to DAOs
 */
@Database(
    entities = [
        ZodiacEntity::class,
        WeeklyZodiacEntity::class
        // Add other entities here as they're implemented:
        // UserEntity::class,
        // JournalEntryEntity::class,
        // BiorhythmEntity::class,
        // etc.
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class OmniCastDatabase : RoomDatabase() {

    // DAOs
    abstract fun zodiacDao(): ZodiacDao
    // Add other DAOs here:
    // abstract fun userDao(): UserDao
    // abstract fun journalDao(): JournalDao
    // abstract fun biorhythmDao(): BiorhythmDao

    companion object {
        const val DATABASE_NAME = "omnicast_database"
    }
}

// DatabaseModule.kt
package com.hadify.omnicast.core.data.di

import android.content.Context
import androidx.room.Room
import com.hadify.omnicast.core.data.local.database.OmniCastDatabase
import com.hadify.omnicast.core.data.local.dao.ZodiacDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dependency injection module for core database components
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Provide Room database instance
     */
    @Provides
    @Singleton
    fun provideOmniCastDatabase(@ApplicationContext context: Context): OmniCastDatabase {
        return Room.databaseBuilder(
            context,
            OmniCastDatabase::class.java,
            OmniCastDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration() // For development - remove in production
            .build()
    }

    /**
     * Provide ZodiacDao
     */
    @Provides
    fun provideZodiacDao(database: OmniCastDatabase): ZodiacDao {
        return database.zodiacDao()
    }

    // Add other DAOs as they're implemented:
    // @Provides
    // fun provideUserDao(database: OmniCastDatabase): UserDao {
    //     return database.userDao()
    // }
}

// DataModule.kt
package com.hadify.omnicast.core.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.hadify.omnicast.core.data.source.ContentLoader
import com.hadify.omnicast.core.data.source.AssetManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dependency injection module for core data components
 */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    /**
     * Provide ContentLoader for loading JSON assets
     */
    @Provides
    @Singleton
    fun provideContentLoader(@ApplicationContext context: Context): ContentLoader {
        return ContentLoader(context)
    }

    /**
     * Provide AssetManager for enhanced asset handling
     */
    @Provides
    @Singleton
    fun provideAssetManager(@ApplicationContext context: Context): AssetManager {
        return AssetManager(context)
    }

    /**
     * Provide DataStore for preferences
     */
    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }
}

// DataStore extension
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "omnicast_preferences")

// UserPreferences.kt (For future use)
package com.hadify.omnicast.core.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages user preferences using DataStore
 */
@Singleton
class UserPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    companion object {
        // Theme preferences
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val DYNAMIC_THEME_ENABLED = booleanPreferencesKey("dynamic_theme_enabled")
        val ZODIAC_THEME_ENABLED = booleanPreferencesKey("zodiac_theme_enabled")

        // User data preferences
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_BIRTHDATE = stringPreferencesKey("user_birthdate")
        val USER_LOCATION = stringPreferencesKey("user_location")

        // App preferences
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val DAILY_REMINDER_TIME = stringPreferencesKey("daily_reminder_time")
        val FIRST_LAUNCH = booleanPreferencesKey("first_launch")
        val LAST_UPDATE_CHECK = longPreferencesKey("last_update_check")
    }

    // Theme preferences
    val themeMode: Flow<String> = dataStore.data.map { preferences ->
        preferences[THEME_MODE] ?: "system"
    }

    val isDynamicThemeEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[DYNAMIC_THEME_ENABLED] ?: true
    }

    val isZodiacThemeEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[ZODIAC_THEME_ENABLED] ?: false
    }

    // User data preferences
    val userName: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USER_NAME]
    }

    val userBirthdate: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USER_BIRTHDATE]
    }

    val userLocation: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USER_LOCATION]
    }

    // App preferences
    val areNotificationsEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[NOTIFICATIONS_ENABLED] ?: true
    }

    val dailyReminderTime: Flow<String> = dataStore.data.map { preferences ->
        preferences[DAILY_REMINDER_TIME] ?: "09:00"
    }

    val isFirstLaunch: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[FIRST_LAUNCH] ?: true
    }

    // Update methods
    suspend fun updateThemeMode(themeMode: String) {
        dataStore.edit { preferences ->
            preferences[THEME_MODE] = themeMode
        }
    }

    suspend fun updateDynamicThemeEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[DYNAMIC_THEME_ENABLED] = enabled
        }
    }

    suspend fun updateZodiacThemeEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[ZODIAC_THEME_ENABLED] = enabled
        }
    }

    suspend fun updateUserName(name: String) {
        dataStore.edit { preferences ->
            preferences[USER_NAME] = name
        }
    }

    suspend fun updateUserBirthdate(birthdate: String) {
        dataStore.edit { preferences ->
            preferences[USER_BIRTHDATE] = birthdate
        }
    }

    suspend fun updateUserLocation(location: String) {
        dataStore.edit { preferences ->
            preferences[USER_LOCATION] = location
        }
    }

    suspend fun updateNotificationsEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[NOTIFICATIONS_ENABLED] = enabled
        }
    }

    suspend fun updateDailyReminderTime(time: String) {
        dataStore.edit { preferences ->
            preferences[DAILY_REMINDER_TIME] = time
        }
    }

    suspend fun setFirstLaunchCompleted() {
        dataStore.edit { preferences ->
            preferences[FIRST_LAUNCH] = false
        }
    }

    suspend fun updateLastUpdateCheck(timestamp: Long) {
        dataStore.edit { preferences ->
            preferences[LAST_UPDATE_CHECK] = timestamp
        }
    }

    suspend fun clearAllPreferences() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}