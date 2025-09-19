package com.hadify.omnicast.core.domain.usecase.settings

import com.hadify.omnicast.core.domain.model.AppSettings
import com.hadify.omnicast.core.domain.repository.SettingsRepository
import com.hadify.omnicast.core.domain.usecase.NoParamsFlowUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for getting current app settings
 * Returns a Flow so UI can react to settings changes automatically
 */
class GetAppSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) : NoParamsFlowUseCase<AppSettings> {

    /**
     * Get current app settings as a Flow
     * @return Flow of current AppSettings that updates when settings change
     */
    override fun invoke(): Flow<AppSettings> {
        return settingsRepository.getAppSettings()
    }
}