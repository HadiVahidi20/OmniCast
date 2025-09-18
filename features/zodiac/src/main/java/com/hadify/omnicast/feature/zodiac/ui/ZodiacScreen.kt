package com.hadify.omnicast.feature.zodiac.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hadify.omnicast.core.ui.component.ErrorView
import com.hadify.omnicast.core.ui.component.LoadingIndicator
import com.hadify.omnicast.core.ui.component.OmniCastAppBar
import com.hadify.omnicast.core.ui.component.OmniCastCard
import com.hadify.omnicast.feature.zodiac.domain.model.HoroscopeReading
import com.hadify.omnicast.feature.zodiac.domain.model.ZodiacSign
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * ZodiacScreen - Displays user's zodiac sign and daily horoscope
 * This is the first fully functional divination feature!
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZodiacScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    viewModel: ZodiacViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            OmniCastAppBar(
                title = "Zodiac",
                showBackButton = true,
                showMenuButton = false,
                onBackClick = onNavigateBack
            )
        }
    ) { paddingValues ->

        when {
            uiState.isLoading -> {
                LoadingIndicator(message = "Loading your zodiac information...")
            }

            !uiState.hasBirthdate -> {
                NoBirthdateView(
                    onNavigateToProfile = onNavigateToProfile,
                    modifier = Modifier.padding(paddingValues)
                )
            }

            uiState.error != null -> {
                ErrorView(
                    message = uiState.error!!,
                    onRetry = viewModel::retry,
                    modifier = Modifier.padding(paddingValues)
                )
            }

            else -> {
                ZodiacContent(
                    uiState = uiState,
                    onDateSelected = viewModel::loadHoroscopeForDate,
                    onRetry = viewModel::retry,
                    viewModel = viewModel,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
private fun NoBirthdateView(
    onNavigateToProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Set Your Birthdate",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "To see your zodiac horoscope, please set your birthdate in your profile first.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onNavigateToProfile,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go to Profile")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun ZodiacContent(
    uiState: ZodiacUiState,
    onDateSelected: (LocalDate) -> Unit,
    onRetry: () -> Unit,
    viewModel: ZodiacViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Zodiac Sign Overview Card
        uiState.userZodiacSign?.let { zodiacSign ->
            ZodiacSignCard(
                zodiacSign = zodiacSign,
                signInfo = viewModel.getZodiacSignInfo(zodiacSign)
            )
        }

        // Date Selection Card
        DateSelectionCard(
            selectedDate = uiState.selectedDate,
            onDateSelected = onDateSelected
        )

        // Daily Horoscope Content
        if (uiState.isLoadingHoroscope) {
            Card {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        } else if (uiState.dailyHoroscope != null) {
            HoroscopeContent(horoscope = uiState.dailyHoroscope)
        }
    }
}

@Composable
private fun ZodiacSignCard(
    zodiacSign: ZodiacSign,
    signInfo: ZodiacSignDisplayInfo
) {
    OmniCastCard {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Zodiac Sign Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = zodiacSign.symbol.first().toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = signInfo.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = signInfo.dateRange,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${signInfo.element} • ${signInfo.symbol}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Zodiac Sign Details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ZodiacDetailItem(
                    label = "Element",
                    value = signInfo.element,
                    modifier = Modifier.weight(1f)
                )
                ZodiacDetailItem(
                    label = "Planet",
                    value = signInfo.rulingPlanet,
                    modifier = Modifier.weight(1f)
                )
                ZodiacDetailItem(
                    label = "Lucky #s",
                    value = signInfo.luckyNumbers.take(3).joinToString(", "),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ZodiacDetailItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateSelectionCard(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }

    OmniCastCard(
        onClick = { showDatePicker = true }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "Selected Date",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = selectedDate.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                if (selectedDate == LocalDate.now()) {
                    Text(
                        text = "Today",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "TAP TO CHANGE",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate.toEpochDay() * 24 * 60 * 60 * 1000
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val newDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            onDateSelected(newDate)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
private fun HoroscopeContent(
    horoscope: HoroscopeReading
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // General Reading
        HoroscopeSection(
            title = "General",
            content = horoscope.general,
            icon = Icons.Default.Star,
            iconColor = MaterialTheme.colorScheme.primary
        )

        // Love Reading
        HoroscopeSection(
            title = "Love & Relationships",
            content = horoscope.love,
            icon = Icons.Default.Favorite,
            iconColor = Color(0xFFE91E63)
        )

        // Career Reading
        HoroscopeSection(
            title = "Career & Finance",
            content = horoscope.career,
            icon = Icons.Default.Star,
            iconColor = Color(0xFF4CAF50)
        )

        // Health Reading
        HoroscopeSection(
            title = "Health & Wellness",
            content = horoscope.health,
            icon = Icons.Default.Person,
            iconColor = Color(0xFFFF9800)
        )

        // Lucky Info
        LuckyInfoCard(horoscope = horoscope)
    }
}

@Composable
private fun HoroscopeSection(
    title: String,
    content: String,
    icon: ImageVector,
    iconColor: Color
) {
    OmniCastCard {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = iconColor
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.2
                )
            }
        }
    }
}

@Composable
private fun LuckyInfoCard(
    horoscope: HoroscopeReading
) {
    OmniCastCard {
        Column {
            Text(
                text = "Today's Lucky Elements",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                LuckyItem(
                    label = "Number",
                    value = horoscope.luckyNumber.toString(),
                    modifier = Modifier.weight(1f)
                )
                LuckyItem(
                    label = "Color",
                    value = horoscope.luckyColor,
                    modifier = Modifier.weight(1f)
                )
                LuckyItem(
                    label = "Mood",
                    value = horoscope.mood.capitalize(),
                    modifier = Modifier.weight(1f)
                )
                LuckyItem(
                    label = "Compatible",
                    value = horoscope.compatibility.displayName,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun LuckyItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
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
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}