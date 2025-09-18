package com.hadify.omnicast.feature.settings.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hadify.omnicast.core.domain.model.ThemeMode
import com.hadify.omnicast.core.ui.component.LoadingIndicator
import com.hadify.omnicast.core.ui.component.OmniCastAppBar
import com.hadify.omnicast.core.ui.component.OmniCastCard
import com.hadify.omnicast.feature.settings.ui.component.LanguageSelector
import com.hadify.omnicast.feature.settings.ui.component.NotificationSettings
import com.hadify.omnicast.feature.settings.ui.component.PrivacySettings
import com.hadify.omnicast.feature.settings.ui.component.ThemeSelector

/**
 * SettingsScreen - App settings and preferences management
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Handle success message auto-clear
    LaunchedEffect(uiState.showSuccess) {
        if (uiState.showSuccess) {
            kotlinx.coroutines.delay(2000)
            viewModel.clearSuccess()
        }
    }

    Scaffold(
        topBar = {
            OmniCastAppBar(
                title = "Settings",
                showBackButton = true,
                showMenuButton = false,
                onBackClick = onNavigateBack
            )
        }
    ) { paddingValues ->

        if (uiState.isLoading) {
            LoadingIndicator(message = "Loading settings...")
        } else {
            SettingsContent(
                uiState = uiState,
                onThemeChange = viewModel::updateThemeMode,
                onLanguageChange = viewModel::updateLanguage,
                onNotificationsToggle = viewModel::toggleNotifications,
                onReminderTimeChange = viewModel::updateDailyReminderTime,
                onClearCache = viewModel::clearCache,
                onErrorDismiss = viewModel::clearError,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
private fun SettingsContent(
    uiState: SettingsUiState,
    onThemeChange: (ThemeMode) -> Unit,
    onLanguageChange: (String) -> Unit,
    onNotificationsToggle: (Boolean) -> Unit,
    onReminderTimeChange: (String?) -> Unit,
    onClearCache: () -> Unit,
    onErrorDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Welcome Section
        OmniCastCard {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "App Settings",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Customize your OmniCast experience",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Theme Settings
        OmniCastCard {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Palette,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "Theme & Appearance",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                ThemeSelector(
                    selectedTheme = uiState.selectedThemeMode,
                    onThemeSelected = onThemeChange,
                    isLoading = uiState.isSaving
                )
            }
        }

        // Language Settings
        OmniCastCard {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "Language",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                LanguageSelector(
                    selectedLanguage = uiState.selectedLanguage,
                    onLanguageSelected = onLanguageChange,
                    isLoading = uiState.isSaving
                )
            }
        }

        // Notification Settings
        OmniCastCard {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "Notifications",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                NotificationSettings(
                    notificationsEnabled = uiState.notificationsEnabled,
                    dailyReminderTime = uiState.dailyReminderTime,
                    onNotificationsToggle = onNotificationsToggle,
                    onReminderTimeChange = onReminderTimeChange,
                    isLoading = uiState.isSaving
                )
            }
        }

        // Privacy & Data Settings
        OmniCastCard {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "Privacy & Data",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                PrivacySettings(
                    onClearCache = onClearCache,
                    isLoading = uiState.isSaving
                )
            }
        }

        // App Information
        OmniCastCard {
            Column {
                Text(
                    text = "About OmniCast",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Version",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "1.0.0",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Build",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "2025.01.21",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Success Message
        if (uiState.showSuccess) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Settings updated successfully!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        // Error Message
        uiState.error?.let { error ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "⚠ $error",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(onClick = onErrorDismiss) {
                        Text("Dismiss")
                    }
                }
            }
        }
    }
}