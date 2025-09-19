// HoroscopeCard.kt
package com.hadify.omnicast.feature.zodiac.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.hadify.omnicast.feature.zodiac.domain.model.HoroscopeReading
import com.hadify.omnicast.core.ui.component.OmniCastCard

/**
 * Card component for displaying horoscope reading with expandable details
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HoroscopeCard(
    horoscope: HoroscopeReading,
    modifier: Modifier = Modifier,
    isExpanded: Boolean = false,
    onExpandToggle: () -> Unit = {}
) {
    var internalExpanded by remember { mutableStateOf(isExpanded) }

    OmniCastCard(
        modifier = modifier.fillMaxWidth(),
        onClick = {
            internalExpanded = !internalExpanded
            onExpandToggle()
        }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header with zodiac sign and date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(flex = 1f) {
                    Text(
                        text = horoscope.zodiacSign.displayName,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = horoscope.date.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = horoscope.zodiacSign.symbol,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    IconButton(onClick = {
                        internalExpanded = !internalExpanded
                        onExpandToggle()
                    }) {
                        Icon(
                            imageVector = if (internalExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (internalExpanded) "Collapse" else "Expand"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // General reading (always visible)
            Text(
                text = horoscope.general,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = if (internalExpanded) Int.MAX_VALUE else 2,
                overflow = TextOverflow.Ellipsis
            )

            // Expandable detailed content
            AnimatedVisibility(
                visible = internalExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    // Love, Career, Health sections
                    HoroscopeSection(
                        icon = Icons.Default.Favorite,
                        title = "Love",
                        content = horoscope.love,
                        tint = MaterialTheme.colorScheme.error
                    )

                    HoroscopeSection(
                        icon = Icons.Default.Work,
                        title = "Career",
                        content = horoscope.career,
                        tint = MaterialTheme.colorScheme.primary
                    )

                    HoroscopeSection(
                        icon = Icons.Default.LocalHospital,
                        title = "Health",
                        content = horoscope.health,
                        tint = MaterialTheme.colorScheme.tertiary
                    )

                    // Lucky details
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        LuckyDetailChip(
                            label = "Number",
                            value = horoscope.luckyNumber.toString()
                        )

                        LuckyDetailChip(
                            label = "Color",
                            value = horoscope.luckyColor
                        )

                        LuckyDetailChip(
                            label = "Match",
                            value = horoscope.compatibility.displayName
                        )
                    }

                    // Mood and tags
                    if (horoscope.mood.isNotBlank()) {
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Mood: ${horoscope.mood}",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Section within horoscope card for love, career, health
 */
@Composable
private fun HoroscopeSection(
    icon: ImageVector,
    title: String,
    content: String,
    tint: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = tint
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = tint
            )
        }

        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 24.dp)
        )
    }
}

/**
 * Chip component for lucky number, color, compatibility
 */
@Composable
private fun LuckyDetailChip(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// ZodiacSignCard.kt
package com.hadify.omnicast.feature.zodiac.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hadify.omnicast.feature.zodiac.domain.model.ZodiacSign
import com.hadify.omnicast.core.ui.component.OmniCastCard

/**
 * Card displaying basic zodiac sign information
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZodiacSignCard(
    zodiacSign: ZodiacSign,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    OmniCastCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Zodiac symbol
            Text(
                text = zodiacSign.symbol,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(end = 16.dp)
            )

            // Sign information
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = zodiacSign.displayName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = zodiacSign.dateRange,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ElementChip(
                        text = zodiacSign.element.displayName,
                        color = getElementColor(zodiacSign.element)
                    )

                    ElementChip(
                        text = zodiacSign.quality.displayName,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}

/**
 * Small chip for displaying element and quality
 */
@Composable
private fun ElementChip(
    text: String,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = color.copy(alpha = 0.12f)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

/**
 * Get color for zodiac element
 */
@Composable
private fun getElementColor(element: com.hadify.omnicast.feature.zodiac.domain.model.ZodiacElement): androidx.compose.ui.graphics.Color {
    return when (element) {
        com.hadify.omnicast.feature.zodiac.domain.model.ZodiacElement.FIRE -> MaterialTheme.colorScheme.error
        com.hadify.omnicast.feature.zodiac.domain.model.ZodiacElement.EARTH -> MaterialTheme.colorScheme.tertiary
        com.hadify.omnicast.feature.zodiac.domain.model.ZodiacElement.AIR -> MaterialTheme.colorScheme.primary
        com.hadify.omnicast.feature.zodiac.domain.model.ZodiacElement.WATER -> MaterialTheme.colorScheme.secondary
    }
}

// ZodiacCompatibilityView.kt
package com.hadify.omnicast.feature.zodiac.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hadify.omnicast.feature.zodiac.domain.model.ZodiacSign
import com.hadify.omnicast.core.ui.component.OmniCastCard

/**
 * View showing zodiac compatibility information
 */
@Composable
fun ZodiacCompatibilityView(
    userSign: ZodiacSign,
    compatibleSigns: List<ZodiacSign>,
    modifier: Modifier = Modifier
) {
    OmniCastCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Compatibility for ${userSign.displayName}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(compatibleSigns) { sign ->
                    CompatibilityRow(
                        sign = sign,
                        compatibility = getCompatibilityScore(userSign, sign)
                    )
                }
            }
        }
    }
}

/**
 * Row showing compatibility with another sign
 */
@Composable
private fun CompatibilityRow(
    sign: ZodiacSign,
    compatibility: Float,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = sign.symbol,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(end = 12.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = sign.displayName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )

            LinearProgressIndicator(
                progress = compatibility,
                modifier = Modifier.fillMaxWidth(),
                color = when {
                    compatibility >= 0.8f -> MaterialTheme.colorScheme.primary
                    compatibility >= 0.6f -> MaterialTheme.colorScheme.secondary
                    else -> MaterialTheme.colorScheme.tertiary
                }
            )
        }

        Text(
            text = "${(compatibility * 100).toInt()}%",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

/**
 * Calculate compatibility score between two signs
 */
private fun getCompatibilityScore(sign1: ZodiacSign, sign2: ZodiacSign): Float {
    return when {
        sign1.element == sign2.element -> 0.9f // Same element
        isCompatibleElement(sign1.element, sign2.element) -> 0.7f // Compatible elements
        sign1.quality == sign2.quality -> 0.6f // Same quality
        else -> 0.4f // Default compatibility
    }
}

/**
 * Check if two elements are compatible
 */
private fun isCompatibleElement(
    element1: com.hadify.omnicast.feature.zodiac.domain.model.ZodiacElement,
    element2: com.hadify.omnicast.feature.zodiac.domain.model.ZodiacElement
): Boolean {
    return when (element1) {
        com.hadify.omnicast.feature.zodiac.domain.model.ZodiacElement.FIRE ->
            element2 == com.hadify.omnicast.feature.zodiac.domain.model.ZodiacElement.AIR
        com.hadify.omnicast.feature.zodiac.domain.model.ZodiacElement.AIR ->
            element2 == com.hadify.omnicast.feature.zodiac.domain.model.ZodiacElement.FIRE
        com.hadify.omnicast.feature.zodiac.domain.model.ZodiacElement.EARTH ->
            element2 == com.hadify.omnicast.feature.zodiac.domain.model.ZodiacElement.WATER
        com.hadify.omnicast.feature.zodiac.domain.model.ZodiacElement.WATER ->
            element2 == com.hadify.omnicast.feature.zodiac.domain.model.ZodiacElement.EARTH
    }
}