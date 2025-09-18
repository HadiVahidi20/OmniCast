package com.hadify.omnicast.core.domain.usecase.settings

import com.hadify.omnicast.core.domain.model.AppSettings
import com.hadify.omnicast.core.domain.repository.SettingsRepository
import com.hadify.omnicast.core.domain.usecase.NoParamsFlowUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAppSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) : NoParamsFlowUseCase<AppSettings> {

    override fun invoke(): Flow<AppSettings> {
        return settingsRepository.getAppSettings()
    }
}