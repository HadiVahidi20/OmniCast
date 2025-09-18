package com.hadify.omnicast.core.domain.usecase.settings

import com.hadify.omnicast.core.domain.model.AppSettings
import com.hadify.omnicast.core.domain.repository.SettingsRepository
import com.hadify.omnicast.core.domain.usecase.BaseUseCase
import javax.inject.Inject

class UpdateAppSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) : BaseUseCase<AppSettings, Unit> {

    override suspend fun invoke(params: AppSettings) {
        settingsRepository.updateAppSettings(params)
    }
}