package com.hadify.omnicast.feature.settings.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

/**
 * NotificationSettings component for managing notification preferences
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSettings(
    notificationsEnabled: Boolean,
    dailyReminderTime: String?,
    onNotificationsToggle: (Boolean) -> Unit,
    onReminderTimeChange: (String?) -> Unit,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Manage your notification preferences",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Main notifications toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Enable Notifications",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Receive daily horoscopes and insights",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Switch(
                checked = notificationsEnabled,
                onCheckedChange = onNotificationsToggle,
                enabled = !isLoading
            )
        }

        // Daily reminder time (only show if notifications are enabled)
        if (notificationsEnabled) {
            Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

            DailyReminderTimeSelector(
                currentTime = dailyReminderTime,
                onTimeSelected = onReminderTimeChange,
                enabled = !isLoading
            )
        }

        // Additional notification options (for future expansion)
        if (notificationsEnabled) {
            Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

            Text(
                text = "Notification Types",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            NotificationTypeToggle(
                title = "Daily Horoscope",
                description = "Your zodiac sign's daily prediction",
                enabled = true, // Always enabled for now
                onToggle = { /* TODO: Implement when we have more notification types */ },
                isLoading = isLoading
            )

            NotificationTypeToggle(
                title = "Biorhythm Peaks",
                description = "When your energy cycles reach peak levels",
                enabled = true, // Will be configurable later
                onToggle = { /* TODO: Implement */ },
                isLoading = isLoading
            )

            NotificationTypeToggle(
                title = "Special Events",
                description = "Full moon, equinox, and other cosmic events",
                enabled = true, // Will be configurable later
                onToggle = { /* TODO: Implement */ },
                isLoading = isLoading
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
                    text = "Updating notifications...",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DailyReminderTimeSelector(
    currentTime: String?,
    onTimeSelected: (String?) -> Unit,
    enabled: Boolean = true
) {
    var showTimePicker by remember { mutableStateOf(false) }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Daily Reminder Time",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = if (currentTime != null) {
                        "Reminder set for ${formatTime(currentTime)}"
                    } else {
                        "No reminder set"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { showTimePicker = true },
                    enabled = enabled
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "Set time",
                        tint = if (enabled) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        }
                    )
                }

                if (currentTime != null) {
                    TextButton(
                        onClick = { onTimeSelected(null) },
                        enabled = enabled
                    ) {
                        Text("Clear")
                    }
                }
            }
        }
    }

    if (showTimePicker) {
        TimePickerDialog(
            onTimeSelected = { hour, minute ->
                val timeString = String.format("%02d:%02d", hour, minute)
                onTimeSelected(timeString)
                showTimePicker = false
            },
            onDismiss = { showTimePicker = false }
        )
    }
}

@Composable
private fun NotificationTypeToggle(
    title: String,
    description: String,
    enabled: Boolean,
    onToggle: (Boolean) -> Unit,
    isLoading: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = if (enabled) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                }
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                    alpha = if (enabled) 1f else 0.6f
                )
            )
        }

        Switch(
            checked = enabled,
            onCheckedChange = onToggle,
            enabled = !isLoading,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerDialog(
    onTimeSelected: (hour: Int, minute: Int) -> Unit,
    onDismiss: () -> Unit
) {
    val timePickerState = rememberTimePickerState(
        initialHour = 9,
        initialMinute = 0,
        is24Hour = false
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Set Daily Reminder Time") },
        text = {
            TimePicker(state = timePickerState)
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onTimeSelected(timePickerState.hour, timePickerState.minute)
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

/**
 * Format time string for display
 */
private fun formatTime(timeString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val outputFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        val time = inputFormat.parse(timeString)
        outputFormat.format(time ?: Date())
    } catch (e: Exception) {
        timeString // Return original if parsing fails
    }
}