package org.darthacheron.fitbe.health.sleep

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.ic_access_time
import fitbe.composeapp.generated.resources.ic_add
import fitbe.composeapp.generated.resources.ic_arrow_back
import fitbe.composeapp.generated.resources.ic_arrow_forward
import fitbe.composeapp.generated.resources.ic_date_range
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.gestures.GestureConfig
import io.github.koalaplot.core.line.StairstepPlot
import io.github.koalaplot.core.style.LineStyle
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.xygraph.CategoryAxisModel
import io.github.koalaplot.core.xygraph.DoubleLinearAxisModel
import io.github.koalaplot.core.xygraph.Point
import io.github.koalaplot.core.xygraph.XYGraph
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.components.date.DatePickerModal
import org.darthacheron.fitbe.components.date.DateRange
import org.darthacheron.fitbe.components.date.DateRangePickerModal
import org.darthacheron.fitbe.components.date.DateUnit
import org.darthacheron.fitbe.components.date.TimePickerDialog
import org.darthacheron.fitbe.health.beverages.DateRangeControl
import org.darthacheron.fitbe.health.steps.StepsViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.ceil
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class, ExperimentalMaterial3Api::class)
@Composable
fun SleepOverviewView(
    viewModel: SleepViewModel,
) {
    LaunchedEffect(Unit) {
        viewModel.updateTopBarConfig()
    }
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    uiState.errorMessage?.let {
        val message = stringResource(it)
        LaunchedEffect(it, message) {
            scope.launch {
                snackbarHostState.showSnackbar(message)
                viewModel.clearErrorMessage()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            PlotSleeps(
                Modifier.padding(bottom = 64.dp),
                uiState.sleeps,
                dateRange,
                uiState.dates, // Changed here
                maxSleeps,
                false,
                targetSleeps,
            )
        }

        IconButton(
            onClick = { viewModel.movePast() },
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_arrow_back),
                contentDescription = null
            )
        }

        IconButton(
            onClick = { viewModel.moveFuture() },
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
                uiState.dateRange,
                viewModel
            )
        }

        FilledIconButton(
            onClick = { showAddDialog = true },
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Icon(painter = painterResource(Res.drawable.ic_add), contentDescription = null)
        }
    }

    if (showAddDialog) {
        AddSleepDialog(
            onAdd = { start, end ->
//                 viewModel.addSleep(start.toUInt(), end.toUInt(), )
                showAddDialog = false
            },
            onDismiss = { showAddDialog = false }
        )
    }
}

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
fun PlotSleeps(sleeps: List<Point<LocalDate, Double>>) {
    ChartLayout(
    ) {
        val dates = sleeps.map { it.x }
        XYGraph(
            xAxisModel = CategoryAxisModel(dates),
            yAxisModel = DoubleLinearAxisModel(
                range = 0.0..sleeps.maxOf { ceil(it.y) },
                minorTickCount = 1
            ),
            xAxisLabels = {
                Text(
                    it.toString(),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 2.dp).graphicsLayer {
                        rotationZ = -75f
                    },
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
            },
            xAxisTitle = { null },
            gestureConfig = GestureConfig(zoomXEnabled = true, zoomYEnabled = true),
            yAxisLabels = {
                Text(
                    "${it.toInt()}h",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.absolutePadding(right = 2.dp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
            },
        ) {
            StairstepPlot(
                data = sleeps,
                lineStyle = LineStyle(
                    brush = SolidColor(Color.Black),
                    strokeWidth = 2.dp
                )
            )
        }
    }
}

// TODO: This is a common control. Extract from all health overviews
@Composable
private fun DateRangeControl(
    dateRange: DateRange,
    sleepsViewModel: SleepViewModel,
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
                    sleepsViewModel.setRange(
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


@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun AddSleepDialog(
    onAdd: (hours: Long, minutes: Long) -> Unit,
    onDismiss: () -> Unit
) {
    var startDateTime by remember {
        mutableStateOf(
            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        )
    }
    var endDateTime by remember {
        mutableStateOf(
            Clock.System.now().plus(value = 8, unit = DateTimeUnit.HOUR)
                .toLocalDateTime(TimeZone.currentSystemDefault())
        )
    }

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    val duration = remember(startDateTime, endDateTime) {
        val diff = endDateTime.toInstant(TimeZone.currentSystemDefault())
            .minus(startDateTime.toInstant(TimeZone.currentSystemDefault()))
        if (diff.isNegative()) Duration.ZERO else diff
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Sleep") },
        text = {
            Column {
                // Start Date & Time
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = startDateTime.date.toString(),
                        onValueChange = {},
                        label = { Text("Start Date") },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { showStartDatePicker = true }) {
                                Icon(
                                    painterResource(Res.drawable.ic_date_range),
                                    contentDescription = null
                                )
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(8.dp))
                    OutlinedTextField(
                        value = "${
                            startDateTime.hour.toString().padStart(2, '0')
                        }:${startDateTime.minute.toString().padStart(2, '0')}",
                        onValueChange = {},
                        label = { Text("Start Time") },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { showStartTimePicker = true }) {
                                Icon(
                                    painterResource(Res.drawable.ic_access_time),
                                    contentDescription = null
                                )
                            }
                        },
                        modifier = Modifier.weight(0.75f)
                    )
                }

                // End Date & Time
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = endDateTime.date.toString(),
                        onValueChange = {},
                        label = { Text("End Date") },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { showEndDatePicker = true }) {
                                Icon(
                                    painterResource(Res.drawable.ic_date_range),
                                    contentDescription = null
                                )
                            }
                        },
                        modifier = Modifier.padding(top = 16.dp).weight(1f)
                    )
                    Spacer(Modifier.width(8.dp))
                    OutlinedTextField(
                        value = "${
                            endDateTime.hour.toString().padStart(2, '0')
                        }:${endDateTime.minute.toString().padStart(2, '0')}",
                        onValueChange = {},
                        label = { Text("End Time") },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { showEndTimePicker = true }) {
                                Icon(
                                    painterResource(Res.drawable.ic_access_time),
                                    contentDescription = null
                                )
                            }
                        },
                        modifier = Modifier.padding(top = 16.dp).weight(0.75f)
                    )
                }

                // Duration
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = formatDuration(duration),
                    onValueChange = {},
                    label = { Text("Sleeping Time") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val hours = duration.toLong(DurationUnit.HOURS)
                val minutes = (duration.inWholeMinutes % 60)
                onAdd(hours, minutes)
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )

    // Date/Time pickers
    if (showStartDatePicker) {
        DatePickerModal(
            onDateSelected = { millis ->
                millis?.let {
                    val date = Instant.fromEpochMilliseconds(it)
                        .toLocalDateTime(TimeZone.currentSystemDefault()).date
                    startDateTime = LocalDateTime(date, startDateTime.time)
                }
                showStartDatePicker = false
            },
            onDismiss = { showStartDatePicker = false }
        )
    }
    if (showStartTimePicker) {
        TimePickerDialog(
            initialHour = startDateTime.hour,
            initialMinute = startDateTime.minute,
            onTimeSelected = { hour, minute ->
                startDateTime = LocalDateTime(startDateTime.date, LocalTime(hour, minute))
                showStartTimePicker = false
            },
            onDismiss = { showStartTimePicker = false }
        )
    }
    if (showEndDatePicker) {
        DatePickerModal(
            onDateSelected = { millis ->
                millis?.let {
                    val date = Instant.fromEpochMilliseconds(it)
                        .toLocalDateTime(TimeZone.currentSystemDefault()).date
                    endDateTime = LocalDateTime(date, endDateTime.time)
                }
                showEndDatePicker = false
            },
            onDismiss = { showEndDatePicker = false }
        )
    }
    if (showEndTimePicker) {
        TimePickerDialog(
            initialHour = endDateTime.hour,
            initialMinute = endDateTime.minute,
            onTimeSelected = { hour, minute ->
                endDateTime = LocalDateTime(endDateTime.date, LocalTime(hour, minute))
                showEndTimePicker = false
            },
            onDismiss = { showEndTimePicker = false }
        )
    }
}

// Helper to format duration as "Xh Ym"
fun formatDuration(duration: Duration): String {
    val hours = duration.toLong(DurationUnit.HOURS)
    val minutes = (duration.inWholeMinutes % 60)
    return "${hours}h ${minutes}m"
}
