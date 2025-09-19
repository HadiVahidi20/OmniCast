package com.hadify.omnicast.feature.profile.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hadify.omnicast.core.ui.component.OmniCastAppBar
import com.hadify.omnicast.core.ui.component.OmniCastCard
import com.hadify.omnicast.core.ui.component.LoadingIndicator
import com.hadify.omnicast.feature.profile.ui.ProfileViewModel
import com.hadify.omnicast.feature.profile.ui.ProfileUiState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * ProfileScreen - User profile creation and editing
 * This is where users enter their critical information like birthdate
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Handle success message
    LaunchedEffect(uiState.showSuccess) {
        if (uiState.showSuccess) {
            // Success message will auto-clear after showing
            kotlinx.coroutines.delay(2000)
            viewModel.clearSuccess()
        }
    }

    Scaffold(
        topBar = {
            OmniCastAppBar(
                title = "Profile",
                showBackButton = true,
                showMenuButton = false,
                onBackClick = onNavigateBack
            )
        }
    ) { paddingValues ->

        if (uiState.isLoading) {
            LoadingIndicator(message = "Loading profile...")
        } else {
            ProfileContent(
                uiState = uiState,
                onNameChange = viewModel::updateName,
                onBirthdateChange = viewModel::updateBirthdate,
                onEmailChange = viewModel::updateEmail,
                onLocationChange = viewModel::updateLocation,
                onSaveClick = viewModel::saveProfile,
                onErrorDismiss = viewModel::clearError,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileContent(
    uiState: ProfileUiState,
    onNameChange: (String) -> Unit,
    onBirthdateChange: (LocalDate) -> Unit,
    onEmailChange: (String) -> Unit,
    onLocationChange: (String) -> Unit,
    onSaveClick: () -> Unit,
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

        // Welcome Message
        OmniCastCard {
            Column {
                Text(
                    text = if (uiState.user == null) "Create Your Profile" else "Edit Your Profile",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Your birthdate is essential for accurate predictions and readings. All other information helps personalize your experience.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Name Field
        OmniCastCard {
            Column {
                Text(
                    text = "Name *",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.name,
                    onValueChange = onNameChange,
                    placeholder = { Text("Enter your name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }

        // Birthdate Field - CRITICAL!
        OmniCastCard {
            Column {
                Text(
                    text = "Birthdate * (Required for predictions)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                BirthdatePicker(
                    selectedDate = uiState.birthdate,
                    onDateSelected = onBirthdateChange
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "This information is used for zodiac, biorhythm, and numerology calculations",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Email Field
        OmniCastCard {
            Column {
                Text(
                    text = "Email (Optional)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = onEmailChange,
                    placeholder = { Text("Enter your email") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }

        // Location Field
        OmniCastCard {
            Column {
                Text(
                    text = "Location (Optional)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.location,
                    onValueChange = onLocationChange,
                    placeholder = { Text("Enter your location") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }

        // Save Button
        Button(
            onClick = onSaveClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = !uiState.isSaving
        ) {
            if (uiState.isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Saving...")
            } else {
                Text(
                    text = if (uiState.user == null) "Create Profile" else "Update Profile",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        // Success Message
        if (uiState.showSuccess) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(
                    text = "✅ Profile saved successfully!",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
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
                        text = "❌ $error",
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BirthdatePicker(
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = selectedDate?.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) ?: "",
        onValueChange = { }, // Read-only
        readOnly = true,
        placeholder = { Text("Select your birthdate") },
        trailingIcon = {
            IconButton(onClick = { showDatePicker = true }) {
                Icon(Icons.Default.DateRange, contentDescription = "Select date")
            }
        },
        modifier = Modifier.fillMaxWidth()
    )

    if (showDatePicker) {
        CustomDatePickerDialog(
            onDateSelected = { dateMillis ->
                dateMillis?.let {
                    val localDate = java.time.Instant.ofEpochMilli(it)
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDate()
                    onDateSelected(localDate)
                }
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomDatePickerDialog(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
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
    ) {
        DatePicker(state = datePickerState)
    }
}