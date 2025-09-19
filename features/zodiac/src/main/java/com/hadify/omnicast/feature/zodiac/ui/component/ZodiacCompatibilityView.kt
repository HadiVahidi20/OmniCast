package com.hadify.omnicast.feature.zodiac.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.hadify.omnicast.feature.zodiac.domain.model.ZodiacCompatibility

@Composable
fun ZodiacCompatibilityView(compatibility: ZodiacCompatibility) {
    Column {
        Text(text = "Compatibility between ${compatibility.sign1} and ${compatibility.sign2}")
        Text(text = "Score: ${compatibility.compatibilityScore}")
        Text(text = "Description: ${compatibility.description}")
    }
}