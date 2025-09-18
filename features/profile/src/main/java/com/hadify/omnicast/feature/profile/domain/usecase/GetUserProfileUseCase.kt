package com.hadify.omnicast.feature.profile.domain.usecase

import com.hadify.omnicast.feature.profile.domain.model.User
import com.hadify.omnicast.feature.profile.domain.repository.UserRepository
import com.hadify.omnicast.core.data.util.Resource
import com.hadify.omnicast.core.domain.usecase.NoParamsFlowUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to get the current user profile
 * This is what the UI will call to get user data
 */
class GetUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) : NoParamsFlowUseCase<Resource<User?>> {

    /**
     * Get current user profile
     * Returns Flow so UI can react to changes automatically
     */
    override fun invoke(): Flow<Resource<User?>> {
        return userRepository.getCurrentUser()
    }
}