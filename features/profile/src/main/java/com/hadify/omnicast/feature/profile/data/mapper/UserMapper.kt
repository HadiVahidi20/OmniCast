package com.hadify.omnicast.feature.profile.data.mapper

import com.hadify.omnicast.core.data.local.entity.UserEntity
import com.hadify.omnicast.feature.profile.domain.model.User

fun UserEntity.toDomain(): User {
    return User(
        id = this.id,
        name = this.name,
        birthdate = this.birthdate,
        email = this.email,
        profilePicture = this.profilePicture,
        location = this.location,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = this.id,
        name = this.name,
        birthdate = this.birthdate,
        email = this.email,
        profilePicture = this.profilePicture,
        location = this.location,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}