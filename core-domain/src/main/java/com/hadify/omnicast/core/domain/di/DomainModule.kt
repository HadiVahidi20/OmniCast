package com.hadify.omnicast.core.domain.di

import com.hadify.omnicast.core.domain.repository.SettingsRepository
import com.hadify.omnicast.core.domain.usecase.settings.GetAppSettingsUseCase
import com.hadify.omnicast.core.domain.usecase.settings.UpdateAppSettingsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Dependency injection module for core domain components
 */
@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    fun provideGetAppSettingsUseCase(
        settingsRepository: SettingsRepository
    ): GetAppSettingsUseCase {
        return GetAppSettingsUseCase(settingsRepository)
    }

    @Provides
    fun provideUpdateAppSettingsUseCase(
        settingsRepository: SettingsRepository
    ): UpdateAppSettingsUseCase {
        return UpdateAppSettingsUseCase(settingsRepository)
    }
}