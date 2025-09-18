package com.hadify.omnicast.feature.settings.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * PrivacySettings component for managing privacy and data preferences
 */
@Composable
fun PrivacySettings(
    onClearCache: () -> Unit,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    var showClearCacheDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Manage your data and privacy settings",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Data Management Section
        Text(
            text = "Data Management",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )

        PrivacyActionItem(
            icon = Icons.Default.DeleteSweep,
            title = "Clear Cache",
            description = "Remove cached horoscopes and temporary data",
            onClick = { showClearCacheDialog = true },
            enabled = !isLoading,
            buttonText = "Clear",
            isDestructive = true
        )

        PrivacyActionItem(
            icon = Icons.Default.CloudOff,
            title = "Offline Mode",
            description = "App works fully offline - no data sent to servers",
            onClick = { /* No action - informational */ },
            enabled = false,
            buttonText = "Active",
            showButton = false
        )

        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

        // Privacy Information Section
        Text(
            text = "Privacy Information",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )

        PrivacyActionItem(
            icon = Icons.Default.Security,
            title = "Data Storage",
            description = "All your data is stored locally on your device",
            onClick = { /* No action - informational */ },
            enabled = false,
            showButton = false
        )

        PrivacyActionItem(
            icon = Icons.Default.Info,
            title = "Privacy Policy",
            description = "Learn how we protect your personal information",
            onClick = { /* TODO: Open privacy policy */ },
            enabled = true,
            buttonText = "View"
        )

        // Future Features Section (placeholder)
        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

        Text(
            text = "Cloud Features (Coming Soon)",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        PrivacyActionItem(
            icon = Icons.Default.CloudOff,
            title = "Cloud Backup",
            description = "Optional backup of your journal and preferences",
            onClick = { /* TODO: Implement when Firebase is ready */ },
            enabled = false,
            buttonText = "Enable",
            showButton = false
        )

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
                    text = "Processing...",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

    // Clear Cache Confirmation Dialog
    if (showClearCacheDialog) {
        AlertDialog(
            onDismissRequest = { showClearCacheDialog = false },
            title = { Text("Clear Cache") },
            text = {
                Text(
                    text = "This will remove all cached horoscope data and temporary files. Your profile and journal entries will not be affected.\n\nAre you sure you want to continue?",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onClearCache()
                        showClearCacheDialog = false
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Clear Cache")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearCacheDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun PrivacyActionItem(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    buttonText: String? = null,
    showButton: Boolean = buttonText != null,
    isDestructive: Boolean = false,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = when {
                    !enabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    isDestructive -> MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                }
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
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
                        alpha = if (enabled) 0.8f else 0.5f
                    )
                )
            }
        }

        if (showButton && buttonText != null) {
            if (isDestructive) {
                OutlinedButton(
                    onClick = onClick,
                    enabled = enabled,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
                        ).brush
                    )
                ) {
                    Text(buttonText)
                }
            } else {
                OutlinedButton(
                    onClick = onClick,
                    enabled = enabled
                ) {
                    Text(buttonText)
                }
            }
        }
    }
}