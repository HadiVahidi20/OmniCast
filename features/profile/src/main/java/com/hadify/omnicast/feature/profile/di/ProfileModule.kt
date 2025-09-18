package com.hadify.omnicast.feature.profile.di

import com.hadify.omnicast.feature.profile.domain.repository.UserRepository
import com.hadify.omnicast.feature.profile.data.repository.UserRepositoryImpl
import com.hadify.omnicast.feature.profile.domain.usecase.GetUserProfileUseCase
import com.hadify.omnicast.feature.profile.domain.usecase.SaveUserUseCase
import com.hadify.omnicast.core.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dependency injection module for Profile feature
 * This tells Hilt how to provide UserRepository and use cases
 * UserDao is provided by DataModule in core-data
 */
@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {

    /**
     * Provide UserRepository implementation
     * UserDao is automatically injected from DataModule
     */
    @Provides
    @Singleton
    fun provideUserRepository(
        userDao: UserDao
    ): UserRepository {
        return UserRepositoryImpl(userDao)
    }

    /**
     * Provide GetUserProfileUseCase
     */
    @Provides
    fun provideGetUserProfileUseCase(
        userRepository: UserRepository
    ): GetUserProfileUseCase {
        return GetUserProfileUseCase(userRepository)
    }

    /**
     * Provide SaveUserUseCase
     */
    @Provides
    fun provideSaveUserUseCase(
        userRepository: UserRepository
    ): SaveUserUseCase {
        return SaveUserUseCase(userRepository)
    }
}