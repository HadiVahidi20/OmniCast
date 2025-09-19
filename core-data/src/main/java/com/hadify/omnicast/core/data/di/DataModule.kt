package com.hadify.omnicast.core.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.hadify.omnicast.core.data.local.database.OmniCastDatabase
import com.hadify.omnicast.core.data.local.dao.UserDao
import com.hadify.omnicast.core.data.local.dao.ZodiacDao
import com.hadify.omnicast.core.data.source.AssetDataSource
import com.hadify.omnicast.core.data.source.ContentLoader
import com.hadify.omnicast.core.data.repository.SettingsRepositoryImpl
import com.hadify.omnicast.core.domain.repository.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// DataStore extension
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

/**
 * Dependency injection module for core data components
 * Provides database, DAOs, DataStore, and repositories
 */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    /**
     * Provides the main Room database instance with proper configuration
     */
    @Provides
    @Singleton
    fun provideOmniCastDatabase(
        @ApplicationContext context: Context
    ): OmniCastDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            OmniCastDatabase::class.java,
            OmniCastDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration() // For development - remove in production
            .build()
    }

    /**
     * Provides UserDao from the database
     */
    @Provides
    fun provideUserDao(database: OmniCastDatabase): UserDao {
        return database.userDao()
    }

    /**
     * Provides ZodiacDao from the database - CRITICAL for zodiac features!
     */
    @Provides
    fun provideZodiacDao(database: OmniCastDatabase): ZodiacDao {
        return database.zodiacDao()
    }

    /**
     * Provides DataStore for storing user preferences
     */
    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    /**
     * Provides AssetDataSource for loading JSON content from assets
     */
    @Provides
    @Singleton
    fun provideAssetDataSource(@ApplicationContext context: Context): AssetDataSource {
        return AssetDataSourceImpl(context)
    }

    /**
     * Provides ContentLoader for parsing and caching JSON content
     */
    @Provides
    @Singleton
    fun provideContentLoader(assetDataSource: AssetDataSource): ContentLoader {
        return ContentLoader(assetDataSource)
    }

    /**
     * Provides SettingsRepository implementation
     */
    @Provides
    @Singleton
    fun provideSettingsRepository(dataStore: DataStore<Preferences>): SettingsRepository {
        return UserPreferences(dataStore)
    }
}