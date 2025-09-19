package com.hadify.omnicast.core.domain.usecase.settings

import com.hadify.omnicast.core.domain.model.AppSettings
import com.hadify.omnicast.core.domain.repository.SettingsRepository
import com.hadify.omnicast.core.domain.usecase.BaseUseCase
import javax.inject.Inject

/**
 * Use case for updating app settings
 * Takes new settings and persists them via the repository
 */
class UpdateAppSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) : BaseUseCase<AppSettings, Unit> {

    /**
     * Update app settings
     * @param params The new AppSettings to save
     */
    override suspend fun invoke(params: AppSettings): Unit {
        // Validate settings before saving
        require(params.isValid()) { "Invalid app settings provided" }

        // Update settings via repository
        settingsRepository.updateAppSettings(params)
    }
}