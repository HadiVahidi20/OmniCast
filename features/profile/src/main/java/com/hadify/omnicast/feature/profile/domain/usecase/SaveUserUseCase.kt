package com.hadify.omnicast.feature.profile.domain.usecase

import com.hadify.omnicast.feature.profile.domain.model.User
import com.hadify.omnicast.feature.profile.domain.repository.UserRepository
import com.hadify.omnicast.core.data.util.Resource
import com.hadify.omnicast.core.domain.usecase.BaseUseCase
import javax.inject.Inject

/**
 * Use case to save/update user profile
 * This handles the business logic for saving user data
 */
class SaveUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) : BaseUseCase<User, Resource<User>> {

    /**
     * Save or update user profile
     * Includes validation logic
     */
    override suspend fun invoke(params: User): Resource<User> {
        // Business logic: Validate user data
        if (params.name.isBlank()) {
            return Resource.Error("Name cannot be empty")
        }

        // Call repository to save
        return userRepository.saveUser(params)
    }
}