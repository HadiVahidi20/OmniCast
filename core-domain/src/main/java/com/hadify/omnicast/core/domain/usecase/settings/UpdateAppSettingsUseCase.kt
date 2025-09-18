package com.hadify.omnicast.core.domain.usecase.settings

import com.hadify.omnicast.core.domain.model.AppSettings
import com.hadify.omnicast.core.domain.usecase.BaseUseCase
import com.hadify.omnicast.core.domain.repository.SettingsRepository
import javax.inject.Inject

/**
 * Use case to update app settings
 */
class UpdateAppSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) : BaseUseCase<AppSettings, Unit> {

    override suspend fun invoke(params: AppSettings) {
        settingsRepository.updateAppSettings(params)
    }
}