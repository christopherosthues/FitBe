package org.darthacheron.fitbe.health.sleep

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.components.DateRangePickerModal
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.ceil
import kotlinx.datetime.Clock
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlinx.datetime.Instant
import org.darthacheron.fitbe.components.DropdownSelection

@OptIn(ExperimentalTime::class, ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SleepOverviewView(modifier: Modifier, viewModel: SleepViewModel) {
    val sleeps by viewModel.sleeps.collectAsState()
    val viewType by viewModel.viewType.collectAsState()
    val startDate by viewModel.startDate.collectAsState()
    val endDate by viewModel.endDate.collectAsState()
    var showDateRangeDialog by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }
    val viewTypes = SleepViewType.entries

    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TextButton(
                onClick = { showDateRangeDialog = true },
            ) {
                Row {
                    Column {
                        Text(text =
                            startDate.toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()
                        )
                        Text(text =
                            endDate.toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()
                        )
                    }
                    Icon(
                        painterResource(Res.drawable.ic_date_range),
                        contentDescription = null,
                        modifier = Modifier.padding(horizontal = 8.dp).align(Alignment.CenterVertically)
                    )
                }
            }
            DropdownSelection(
                initialState = false,
                items = SleepViewType.entries,
                title = "Choose an option",
                itemContent = { item, onClick ->
                    DropdownMenuItem(
                        text = { Text(item.localizedString()) },
                        onClick = onClick
                    )
                },
                itemToString = {
                    it.localizedString()
                },
                onItemSelected = {
                    viewModel.setViewType(viewTypes[it])
                }
            )
        }
        Box(modifier = modifier.fillMaxSize()) {
            if (!sleeps.isEmpty()) {
                Plot(sleeps)
            }
            IconButton(
                onClick = {},
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(painter = painterResource(Res.drawable.ic_arrow_back), contentDescription = null)
            }
            IconButton(
                onClick = {},
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(painter = painterResource(Res.drawable.ic_arrow_forward), contentDescription = null)
            }
            FilledIconButton(
                onClick = { showAddDialog = true },
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                Icon(painter = painterResource(Res.drawable.ic_add), contentDescription = null)
            }
        }
    }

    if (showDateRangeDialog) {
        DateRangePickerModal(
            onDateRangeSelected = {
                if (it.first != null) {
                    viewModel.setStartDate(
                        Instant.fromEpochMilliseconds(it.first!!)
                    )
                }
                if (it.second != null) {
                    viewModel.setEndDate(
                        Instant.fromEpochMilliseconds(it.second!!)
                    )
                }
                showDateRangeDialog = false
            },
            onDismiss = { showDateRangeDialog = false }
        )
    }

    if (showAddDialog) {
        AddSleepDialog(
            onAdd = { start, end ->
//                 viewModel.addSleep(start, end)
                showAddDialog = false
            },
            onDismiss = { showAddDialog = false }
        )
    }
}

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
fun Plot(sleeps: List<Point<LocalDate, Double>>) {
    ChartLayout(
    ) {
        val dates = sleeps.map { it.x }
        XYGraph(
            xAxisModel = CategoryAxisModel(dates),
            yAxisModel = DoubleLinearAxisModel(range = 0.0..sleeps.maxOf { ceil(it.y) }, minorTickCount = 1),
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

@Composable
fun AdvancedTimePickerDialog(
    title: String = "Select Time",
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    toggle: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            shape = MaterialTheme.shapes.large,
            tonalElevation = 6.dp,
            modifier =
                Modifier
                    .width(IntrinsicSize.Min)
                    .height(IntrinsicSize.Min)
                    .background(
                        shape = MaterialTheme.shapes.large,
                        color = MaterialTheme.colorScheme.surface
                    ),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    toggle()
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    TextButton(onClick = onConfirm) { Text("OK") }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun AddSleepDialog(
    onAdd: (start: LocalDateTime, end: LocalDateTime) -> Unit,
    onDismiss: () -> Unit
) {
    var startDateTime by remember { mutableStateOf(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())) }
    var endDateTime by remember { mutableStateOf(Clock.System.now().plus(value = 8, unit = DateTimeUnit.HOUR).toLocalDateTime(TimeZone.currentSystemDefault())) }

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
                                Icon(painterResource(Res.drawable.ic_date_range), contentDescription = null)
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(8.dp))
                    OutlinedTextField(
                        value = "${startDateTime.hour.toString().padStart(2, '0')}:${startDateTime.minute.toString().padStart(2, '0')}",
                        onValueChange = {},
                        label = { Text("Start Time") },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { showStartTimePicker = true }) {
                                Icon(painterResource(Res.drawable.ic_access_time), contentDescription = null)
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
                                Icon(painterResource(Res.drawable.ic_date_range), contentDescription = null)
                            }
                        },
                        modifier = Modifier.padding(top = 16.dp).weight(1f)
                    )
                    Spacer(Modifier.width(8.dp))
                    OutlinedTextField(
                        value = "${endDateTime.hour.toString().padStart(2, '0')}:${endDateTime.minute.toString().padStart(2, '0')}",
                        onValueChange = {},
                        label = { Text("End Time") },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { showEndTimePicker = true }) {
                                Icon(painterResource(Res.drawable.ic_access_time), contentDescription = null)
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
            TextButton(onClick = { onAdd(startDateTime, endDateTime) }) { Text("Add") }
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
                    val date = Instant.fromEpochMilliseconds(it).toLocalDateTime(TimeZone.currentSystemDefault()).date
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
                    val date = Instant.fromEpochMilliseconds(it).toLocalDateTime(TimeZone.currentSystemDefault()).date
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    initialHour: Int,
    initialMinute: Int,
    onTimeSelected: (Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = true
    )
    AdvancedTimePickerDialog(
        onDismiss = onDismiss,
        onConfirm = { onTimeSelected(timePickerState.hour, timePickerState.minute) }
    ) {
        TimePicker(state = timePickerState)
    }
}
