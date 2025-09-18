package com.hadify.omnicast.feature.zodiac.di

import dagger.hilt.android.qualifiers.ApplicationContext
import com.hadify.omnicast.feature.zodiac.domain.repository.ZodiacRepository
import com.hadify.omnicast.feature.zodiac.data.repository.ZodiacRepositoryImpl
import com.hadify.omnicast.feature.zodiac.domain.usecase.GetDailyHoroscopeUseCase
import com.hadify.omnicast.feature.zodiac.domain.usecase.GetUserDailyHoroscopeUseCase
import com.hadify.omnicast.feature.zodiac.domain.usecase.DetermineZodiacSignUseCase
import com.hadify.omnicast.feature.zodiac.domain.usecase.GetZodiacSignInfoUseCase
import com.hadify.omnicast.core.data.local.dao.ZodiacDao
import com.hadify.omnicast.core.data.source.ContentLoader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dependency injection module for Zodiac feature
 * Provides repository, use cases, and all zodiac-related dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object ZodiacModule {

    /**
     * Provide ZodiacRepository implementation
     * This connects the domain interface to the data implementation
     */
    @Provides
    @Singleton
    fun provideZodiacRepository(
        zodiacDao: ZodiacDao,
        contentLoader: ContentLoader
    ): ZodiacRepository {
        return ZodiacRepositoryImpl(zodiacDao, contentLoader)
    }

    /**
     * Provide GetDailyHoroscopeUseCase
     */
    @Provides
    fun provideGetDailyHoroscopeUseCase(
        zodiacRepository: ZodiacRepository
    ): GetDailyHoroscopeUseCase {
        return GetDailyHoroscopeUseCase(zodiacRepository)
    }

    /**
     * Provide GetUserDailyHoroscopeUseCase
     * This one takes a birthdate and determines the zodiac sign automatically
     */
    @Provides
    fun provideGetUserDailyHoroscopeUseCase(
        zodiacRepository: ZodiacRepository,
        determineZodiacSignUseCase: DetermineZodiacSignUseCase
    ): GetUserDailyHoroscopeUseCase {
        return GetUserDailyHoroscopeUseCase(zodiacRepository, determineZodiacSignUseCase)
    }

    /**
     * Provide DetermineZodiacSignUseCase
     * Critical for other developers - converts birthdate to zodiac sign
     */
    @Provides
    fun provideDetermineZodiacSignUseCase(): DetermineZodiacSignUseCase {
        return DetermineZodiacSignUseCase()
    }

    /**
     * Provide GetZodiacSignInfoUseCase
     * Provides extended zodiac information
     */
    @Provides
    fun provideGetZodiacSignInfoUseCase(): GetZodiacSignInfoUseCase {
        return GetZodiacSignInfoUseCase()
    }
}