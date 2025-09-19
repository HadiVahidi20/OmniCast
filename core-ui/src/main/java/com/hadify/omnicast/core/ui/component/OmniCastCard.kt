package com.hadify.omnicast.core.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Base card component for OmniCast app
 * Provides consistent styling across all features
 * FINAL FIX: Uses two different Card implementations to correctly handle clickability
 * and prevent touch event interception for child components like TextFields.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OmniCastCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null, // onClick is optional
    content: @Composable ColumnScope.() -> Unit
) {
    val cardModifier = modifier.fillMaxWidth()
    val cardColors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    )

    // If onClick is provided, use the clickable version of the Card.
    if (onClick != null) {
        Card(
            modifier = cardModifier,
            onClick = onClick,
            enabled = true,
            colors = cardColors,
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp,
                pressedElevation = 8.dp,
                focusedElevation = 6.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                content = content
            )
        }
    } else {
        // If onClick is NOT provided, use the non-clickable version of the Card.
        // This version will not intercept touch events.
        Card(
            modifier = cardModifier,
            colors = cardColors,
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                content = content
            )
        }
    }
}