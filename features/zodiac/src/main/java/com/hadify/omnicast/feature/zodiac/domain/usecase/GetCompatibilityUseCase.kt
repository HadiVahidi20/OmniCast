package com.hadify.omnicast.feature.zodiac.domain.usecase

import com.hadify.omnicast.feature.zodiac.domain.model.ZodiacCompatibility
import com.hadify.omnicast.feature.zodiac.domain.model.ZodiacSign
import com.hadify.omnicast.feature.zodiac.domain.repository.ZodiacRepository
import javax.inject.Inject

class GetCompatibilityUseCase @Inject constructor(
    private val zodiacRepository: ZodiacRepository
) {
    suspend operator fun invoke(sign1: ZodiacSign, sign2: ZodiacSign): Result<ZodiacCompatibility> {
        return zodiacRepository.getZodiacCompatibility(sign1, sign2)
    }
}