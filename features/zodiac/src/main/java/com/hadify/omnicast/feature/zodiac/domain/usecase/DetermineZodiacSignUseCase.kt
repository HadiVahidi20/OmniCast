package com.hadify.omnicast.feature.zodiac.domain.usecase

import com.hadify.omnicast.feature.zodiac.domain.model.ZodiacSign
import com.hadify.omnicast.core.domain.usecase.BaseUseCase
import java.time.LocalDate
import javax.inject.Inject

/**
 * Use case to determine zodiac sign from birthdate
 * This is critical for other developers who need zodiac calculations
 */
class DetermineZodiacSignUseCase @Inject constructor() : BaseUseCase<LocalDate, ZodiacSign> {

    /**
     * Determine zodiac sign from birthdate
     * @param params User's birthdate
     * @return Corresponding zodiac sign
     */
    override suspend fun invoke(params: LocalDate): ZodiacSign {
        return ZodiacSign.fromBirthdate(params)
    }
}

/**
 * Parameters for zodiac sign determination with additional context
 */
data class ZodiacSignParams(
    val birthdate: LocalDate,
    val includeCompatibility: Boolean = false,
    val includeLuckyNumbers: Boolean = false
)

/**
 * Extended use case that provides more zodiac information
 */
class GetZodiacSignInfoUseCase @Inject constructor() : BaseUseCase<ZodiacSignParams, ZodiacSignInfo> {

    override suspend fun invoke(params: ZodiacSignParams): ZodiacSignInfo {
        val zodiacSign = ZodiacSign.fromBirthdate(params.birthdate)

        return ZodiacSignInfo(
            sign = zodiacSign,
            compatibility = if (params.includeCompatibility) zodiacSign.compatibleSigns else emptyList(),
            luckyNumbers = if (params.includeLuckyNumbers) zodiacSign.luckyNumbers else emptyList(),
            element = zodiacSign.element,
            quality = zodiacSign.quality,
            rulingPlanet = zodiacSign.rulingPlanet,
            symbol = zodiacSign.symbol,
            luckyColors = zodiacSign.luckyColors
        )
    }
}

/**
 * Extended zodiac sign information
 */
data class ZodiacSignInfo(
    val sign: ZodiacSign,
    val compatibility: List<String>,
    val luckyNumbers: List<Int>,
    val element: com.hadify.omnicast.feature.zodiac.domain.model.ZodiacElement,
    val quality: com.hadify.omnicast.feature.zodiac.domain.model.ZodiacQuality,
    val rulingPlanet: String,
    val symbol: String,
    val luckyColors: List<String>
)