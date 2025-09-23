package org.darthacheron.fitbe.health.weight


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.ic_add
import fitbe.composeapp.generated.resources.ic_arrow_back
import fitbe.composeapp.generated.resources.ic_arrow_forward
import fitbe.composeapp.generated.resources.ic_date_range
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.components.date.DateRange
import org.darthacheron.fitbe.components.date.DateRangePickerModal
import org.darthacheron.fitbe.settings.Settings
import org.darthacheron.fitbe.settings.SettingsRepository
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Composable
fun WeightOverviewView(
    bodyWeightOverviewViewModel: WeightOverviewViewModel,
    settingsRepository: SettingsRepository, // This might not be needed directly if settings come from uiState or a specific flow in ViewModel
) {
    LaunchedEffect(Unit) {
        bodyWeightOverviewViewModel.updateTopBarConfig()
    }
    val uiState by bodyWeightOverviewViewModel.uiState.collectAsState()
    val maxBodyWeight by bodyWeightOverviewViewModel.maxWeight.collectAsState()
    val settings by settingsRepository.getSettingsFlow().collectAsState(Settings()) // Consider if settings should come from uiState
    val dateRange by bodyWeightOverviewViewModel.dateRangeFlow.collectAsState()
    val targetWeight by bodyWeightOverviewViewModel.targetWeight.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    uiState.errorMessage?.let {
        val message = stringResource(it)
        LaunchedEffect(it, message) {
            scope.launch {
                snackbarHostState.showSnackbar(message)
                bodyWeightOverviewViewModel.clearErrorMessage()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            PlotBodyWeights(
                Modifier.padding(bottom = 64.dp),
                uiState.bodyWeights,
                dateRange,
                uiState.dates,
                settings, // Pass settings directly
                maxBodyWeight,
                false,
                targetWeight,
            )

            IconButton(
                onClick = { bodyWeightOverviewViewModel.movePast() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_back),
                    contentDescription = null
                )
            }
            IconButton(
                onClick = { bodyWeightOverviewViewModel.moveFuture() },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_forward),
                    contentDescription = null
                )
            }
            Row(
                modifier = Modifier.align(Alignment.BottomEnd).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                DateRangeControl(
                    dateRange,
                    bodyWeightOverviewViewModel
                )

                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(16.dp),
                ) {
                    Icon(painter = painterResource(Res.drawable.ic_add), contentDescription = null)
                }
            }
        }
    }

    if (showAddDialog) {
        AddBodyWeightDialog(
            settings = settings, // Pass settings to the dialog
            onSave = { date,
                       weightInKg,
                       bodyFatPercentage,
                       muscleMassInKg,
                       boneMassInKg,
                       bodyWaterInPercentage ->
                bodyWeightOverviewViewModel.addBodyWeight(
                    date,
                    weightInKg,
                    bodyFatPercentage,
                    muscleMassInKg,
                    boneMassInKg,
                    bodyWaterInPercentage
                )
                showAddDialog = false
            },
            onDismiss = { showAddDialog = false }
        )
    }
}

@Composable
private fun DateRangeControl(
    dateRange: DateRange,
    bodyWeightOverviewViewModel: WeightOverviewViewModel,
) {
    var showDateRangeDialog by remember { mutableStateOf(false) }
    TextButton(
        modifier = Modifier.padding(16.dp),
        onClick = { showDateRangeDialog = true },
    ) {
        Row {
            Column {
                Text(
                    text =
                        dateRange.startDate.toLocalDateTime(TimeZone.UTC).date.toString()
                )
                Text(
                    text =
                        dateRange.endDate.toLocalDateTime(TimeZone.UTC).date.toString()
                )
            }
            Icon(
                painterResource(Res.drawable.ic_date_range),
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterVertically).padding(horizontal = 8.dp)
            )
        }
    }

    if (showDateRangeDialog) {
        DateRangePickerModal(
            onDateRangeSelected = { newDateRange, selectedDateUnit ->
                if (newDateRange.first != null && newDateRange.second != null) {
                    bodyWeightOverviewViewModel.setRange(
                        Instant.fromEpochMilliseconds(newDateRange.first!!),
                        Instant.fromEpochMilliseconds(newDateRange.second!!),
                        selectedDateUnit
                    )
                }
                showDateRangeDialog = false
            },
            onDismiss = { showDateRangeDialog = false }
        )
    }
}
