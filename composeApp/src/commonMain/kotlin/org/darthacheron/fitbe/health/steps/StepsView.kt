package org.darthacheron.fitbe.health.steps

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
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun StepsView(
    stepsViewModel: StepsViewModel,
) {
    LaunchedEffect(Unit) {
        stepsViewModel.updateTopBarConfig()
    }
    val uiState by stepsViewModel.uiState.collectAsState()
    val dateRange by stepsViewModel.dateRangeFlow.collectAsState()
    val targetSteps by stepsViewModel.targetSteps.collectAsState()
    val maxSteps by stepsViewModel.maxSteps.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    uiState.errorMessage?.let {
        val message = stringResource(it)
        LaunchedEffect(it, message) {
            scope.launch {
                snackbarHostState.showSnackbar(message)
                stepsViewModel.clearErrorMessage()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            PlotSteps(
                Modifier.padding(bottom = 64.dp),
                uiState.steps,
                dateRange,
                uiState.dates,
                maxSteps,
                false,
                targetSteps,
            )

            IconButton(
                onClick = { stepsViewModel.movePast() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_back),
                    contentDescription = null
                )
            }

            IconButton(
                onClick = { stepsViewModel.moveFuture() },
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
                    stepsViewModel
                )

                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(16.dp),
                ) {
                    Icon(painter = painterResource(Res.drawable.ic_add), contentDescription = null)
                }
            }
        }
    }

    if (showAddDialog) {
        AddStepsDialog(
            onSave = { date, steps ->
                stepsViewModel.addSteps(date, steps)
                showAddDialog = false
            },
            onDismiss = { showAddDialog = false }
        )
    }
}

@Composable
private fun DateRangeControl(
    dateRange: DateRange,
    stepsViewModel: StepsViewModel,
) {
    var showDateRangeDialog by remember { mutableStateOf(false) }

    TextButton(
        modifier = Modifier.padding(16.dp),
        onClick = { showDateRangeDialog = true },
    ) {
        Row {
            Column {
                Text(
                    text = dateRange.startDate.toLocalDateTime(TimeZone.UTC).date.toString()
                )
                Text(
                    text = dateRange.endDate.toLocalDateTime(TimeZone.UTC).date.toString()
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
                    stepsViewModel.setRange(
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
