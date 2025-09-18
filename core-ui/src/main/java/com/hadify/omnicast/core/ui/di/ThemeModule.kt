package com.hadify.omnicast.core.ui.di

import com.hadify.omnicast.core.domain.repository.SettingsRepository
import com.hadify.omnicast.core.ui.theme.ThemeManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dependency injection module for theming system
 */
@Module
@InstallIn(SingletonComponent::class)
object ThemeModule {

    @Provides
    @Singleton
    fun provideThemeManager(
        settingsRepository: SettingsRepository
    ): ThemeManager {
        return ThemeManager(settingsRepository)
    }
}