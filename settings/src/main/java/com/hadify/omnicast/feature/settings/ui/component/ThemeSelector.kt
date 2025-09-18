package com.hadify.omnicast.feature.settings.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hadify.omnicast.core.domain.model.ThemeMode

/**
 * ThemeSelector component for choosing app theme
 */
@Composable
fun ThemeSelector(
    selectedTheme: ThemeMode,
    onThemeSelected: (ThemeMode) -> Unit,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .selectableGroup(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Text(
            text = "Choose your preferred theme",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Theme options
        val themeOptions = listOf(
            ThemeOption(
                mode = ThemeMode.SYSTEM,
                title = "System Default",
                description = "Follows your device's light/dark theme setting"
            ),
            ThemeOption(
                mode = ThemeMode.LIGHT,
                title = "Light Theme",
                description = "Always use light colors"
            ),
            ThemeOption(
                mode = ThemeMode.DARK,
                title = "Dark Theme",
                description = "Always use dark colors"
            ),
            ThemeOption(
                mode = ThemeMode.ZODIAC_BASED,
                title = "Zodiac Theme",
                description = "Dynamic colors based on your zodiac sign"
            ),
            ThemeOption(
                mode = ThemeMode.BIORHYTHM_BASED,
                title = "Biorhythm Theme",
                description = "Colors that adapt to your energy levels"
            )
        )

        themeOptions.forEach { option ->
            ThemeOptionRow(
                option = option,
                isSelected = selectedTheme == option.mode,
                onSelected = { onThemeSelected(option.mode) },
                enabled = !isLoading
            )
        }

        if (selectedTheme == ThemeMode.ZODIAC_BASED) {
            Text(
                text = "💡 Tip: Set your birthdate in Profile to enable zodiac themes",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 32.dp, top = 4.dp)
            )
        }

        if (selectedTheme == ThemeMode.BIORHYTHM_BASED) {
            Text(
                text = "💡 Tip: Biorhythm themes change based on your calculated energy cycles",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 32.dp, top = 4.dp)
            )
        }

        if (isLoading) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Applying theme...",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ThemeOptionRow(
    option: ThemeOption,
    isSelected: Boolean,
    onSelected: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .selectable(
                selected = isSelected,
                enabled = enabled,
                onClick = onSelected,
                role = Role.RadioButton
            )
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = null, // Handled by selectable modifier
            enabled = enabled
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = option.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = if (enabled) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                }
            )

            Text(
                text = option.description,
                style = MaterialTheme.typography.bodySmall,
                color = if (enabled) {
                    MaterialTheme.colorScheme.onSurfaceVariant
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                }
            )
        }
    }
}

/**
 * Data class representing a theme option
 */
private data class ThemeOption(
    val mode: ThemeMode,
    val title: String,
    val description: String
)