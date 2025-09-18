package com.hadify.omnicast.feature.settings.di

import com.hadify.omnicast.core.domain.repository.SettingsRepository
import com.hadify.omnicast.core.domain.usecase.settings.GetAppSettingsUseCase
import com.hadify.omnicast.core.domain.usecase.settings.UpdateAppSettingsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Dependency injection module for Settings feature
 * Provides use cases for settings management
 */
@Module
@InstallIn(SingletonComponent::class)
object SettingsModule {

    /**
     * Provide GetAppSettingsUseCase
     * SettingsRepository is provided by DataModule in core-data
     */
    @Provides
    fun provideGetAppSettingsUseCase(
        settingsRepository: SettingsRepository
    ): GetAppSettingsUseCase {
        return GetAppSettingsUseCase(settingsRepository)
    }

    /**
     * Provide UpdateAppSettingsUseCase
     */
    @Provides
    fun provideUpdateAppSettingsUseCase(
        settingsRepository: SettingsRepository
    ): UpdateAppSettingsUseCase {
        return UpdateAppSettingsUseCase(settingsRepository)
    }
}