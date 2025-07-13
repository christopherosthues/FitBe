package org.darthacheron.fitbe.health.sleep

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Popup
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.ic_access_time
import fitbe.composeapp.generated.resources.ic_date_range
import fitbe.composeapp.generated.resources.ic_edit_calendar
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.gestures.GestureConfig
import io.github.koalaplot.core.line.StairstepPlot
import io.github.koalaplot.core.style.LineStyle
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.xygraph.CategoryAxisModel
import io.github.koalaplot.core.xygraph.DoubleLinearAxisModel
import io.github.koalaplot.core.xygraph.Point
import io.github.koalaplot.core.xygraph.XYGraph
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.ceil
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class, ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SleepOverviewView(viewModel: SleepViewModel) {
    val sleeps by viewModel.sleeps.collectAsState()
    val viewType by viewModel.viewType.collectAsState()
    val startDate by viewModel.startDate.collectAsState()
    val endDate by viewModel.endDate.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()
        .padding(16.dp)) {
        Row {
            Button(onClick = { viewModel.setViewType(SleepViewType.WEEK) }) { Text("Week") }
            Button(onClick = { viewModel.setViewType(SleepViewType.MONTH) }) { Text("Month") }
            Button(onClick = { viewModel.setViewType(SleepViewType.YEAR) }) { Text("Year") }
        }
        Button(onClick = { showAddDialog = true }) { Text("Add Sleep") }
        Spacer(Modifier.height(8.dp))
        Row {
            Text("From: $startDate")
            Spacer(Modifier.width(8.dp))
            Text("To: $endDate")
        }
        Spacer(Modifier.height(8.dp))
        if (!sleeps.isEmpty()) {
            Plot(sleeps)
        }
    }
    if (showAddDialog) {
        DatePickerDocked()
//        AdvancedTimePickerExample(
//            onConfirm = {
//                showAddDialog = false
//            },
//            onDismiss = { showAddDialog = false }
//        )
//        SleepTrackingDialog(
//            onAdd = { h, m, d ->
//                viewModel.addSleep(h, m, d)
//                showAddDialog = false
//            },
//            onDismiss = { showAddDialog = false }
//        )
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

@OptIn(ExperimentalTime::class)
@Composable
fun SleepTrackingDialog(onAdd: (Int, Int, Instant) -> Unit, onDismiss: () -> Unit) {
    var hours by remember { mutableStateOf("") }
    var minutes by remember { mutableStateOf("") }
    var date by remember {
        mutableStateOf(
            Clock.System.now().toLocalDateTime(TimeZone.UTC).date.atStartOfDayIn(
                TimeZone.UTC
            )
        )
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Sleep") },
        text = {
            Column {
                OutlinedTextField(value = hours, onValueChange = { hours = it }, label = { Text("Hours") })
                OutlinedTextField(value = minutes, onValueChange = { minutes = it }, label = { Text("Minutes") })
                // Date picker placeholder
                Text("Date: $date")
            }
        },
        confirmButton = {
            Button(onClick = {
                onAdd(hours.toIntOrNull() ?: 0, minutes.toIntOrNull() ?: 0, date)
            }) { Text("Add") }
        },
        dismissButton = { Button(onClick = onDismiss) { Text("Cancel") } }
    )
}

@OptIn(ExperimentalTime::class, ExperimentalMaterial3Api::class)
@Composable
fun AdvancedTimePickerExample(
    onConfirm: (TimePickerState) -> Unit,
    onDismiss: () -> Unit,
) {

    val currentTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.hour,
        initialMinute = currentTime.minute,
        is24Hour = true,
    )

    /** Determines whether the time picker is dial or input */
    var showDial by remember { mutableStateOf(true) }

    /** The icon used for the icon button that switches from dial to input */
    val toggleIcon = if (showDial) {
        Res.drawable.ic_edit_calendar
    } else {
        Res.drawable.ic_access_time
    }

    AdvancedTimePickerDialog(
        onDismiss = { onDismiss() },
        onConfirm = { onConfirm(timePickerState) },
        toggle = {
            IconButton(onClick = { showDial = !showDial }) {
                Icon(
                    painter = painterResource(toggleIcon),
                    contentDescription = "Time picker type toggle",
                )
            }
        },
    ) {
        if (showDial) {
            TimePicker(
                state = timePickerState,
            )
        } else {
            TimeInput(
                state = timePickerState,
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



//@Composable
//fun SleepDialog(
//    initialSleepTime: LocalDateTime = LocalDateTime.now(),
//    initialWakeTime: LocalDateTime = LocalDateTime.now().plusHours(8),
//    onDismiss: () -> Unit,
//    onSave: (sleepTime: LocalDateTime, wakeTime: LocalDateTime, durationHours: Int, durationMinutes: Int) -> Unit
//) {
//    val context = LocalContext.current
//    var sleepTime by remember { mutableStateOf(initialSleepTime) }
//    var wakeTime by remember { mutableStateOf(initialWakeTime) }
//
//    var durationHours by remember { mutableStateOf("8") }
//    var durationMinutes by remember { mutableStateOf("0") }
//
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        title = { Text("Sleep Tracker") },
//        text = {
//            Column(modifier = Modifier.fillMaxWidth()) {
//                // Sleep Time
//                Text("Sleep Time")
//                DateTimeSelector(
//                    selectedDateTime = sleepTime,
//                    onDateTimeSelected = { sleepTime = it }
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // Wake Time
//                Text("Wake Time")
//                DateTimeSelector(
//                    selectedDateTime = wakeTime,
//                    onDateTimeSelected = { wakeTime = it }
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // Duration fields
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    OutlinedTextField(
//                        value = durationHours,
//                        onValueChange = { durationHours = it.filter { c -> c.isDigit() } },
//                        label = { Text("Hours") },
//                        modifier = Modifier.weight(1f),
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
//                    )
//
//                    Spacer(modifier = Modifier.width(8.dp))
//
//                    OutlinedTextField(
//                        value = durationMinutes,
//                        onValueChange = { durationMinutes = it.filter { c -> c.isDigit() } },
//                        label = { Text("Minutes") },
//                        modifier = Modifier.weight(1f),
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
//                    )
//                }
//            }
//        },
//        confirmButton = {
//            TextButton(onClick = {
//                val hours = durationHours.toIntOrNull() ?: 0
//                val minutes = durationMinutes.toIntOrNull() ?: 0
//                onSave(sleepTime, wakeTime, hours, minutes)
//            }) {
//                Text("Save")
//            }
//        },
//        dismissButton = {
//            TextButton(onClick = onDismiss) {
//                Text("Cancel")
//            }
//        }
//    )
//}
//
//@Composable
//fun DateTimeSelector(
//    selectedDateTime: LocalDateTime,
//    onDateTimeSelected: (LocalDateTime) -> Unit
//) {
//    val context = LocalContext.current
//    val calendar = Calendar.getInstance()
//
//    // DatePicker
//    val datePickerDialog = remember {
//        DatePickerDialog(
//            context,
//            { _, year, month, dayOfMonth ->
//                val newDate = selectedDateTime.withYear(year).withMonth(month + 1).withDayOfMonth(dayOfMonth)
//                onDateTimeSelected(newDate)
//            },
//            selectedDateTime.year,
//            selectedDateTime.monthValue - 1,
//            selectedDateTime.dayOfMonth
//        )
//    }
//
//    // TimePicker
//    val timePickerDialog = remember {
//        TimePickerDialog(
//            context,
//            { _, hour, minute ->
//                val newTime = selectedDateTime.withHour(hour).withMinute(minute)
//                onDateTimeSelected(newTime)
//            },
//            selectedDateTime.hour,
//            selectedDateTime.minute,
//            true
//        )
//    }
//
//    Row(
//        horizontalArrangement = Arrangement.SpaceBetween,
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        TextButton(onClick = { datePickerDialog.show() }) {
//            Text("Date: ${selectedDateTime.toLocalDate()}")
//        }
//
//        TextButton(onClick = { timePickerDialog.show() }) {
//            Text("Time: ${selectedDateTime.toLocalTime().truncatedTo(ChronoUnit.MINUTES)}")
//        }
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDocked() {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedDate,
            onValueChange = { },
            label = { Text("DOB") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = !showDatePicker }) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_date_range),
                        contentDescription = "Select date"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        )

        if (showDatePicker) {
            Popup(
                onDismissRequest = { showDatePicker = false },
                alignment = Alignment.TopStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 64.dp)
                        .shadow(elevation = 4.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false
                    )
                }
            }
        }
    }
}

@Composable
fun DatePickerFieldToModal(modifier: Modifier = Modifier) {
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var showModal by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = selectedDate?.let { convertMillisToDate(it) } ?: "",
        onValueChange = { },
        label = { Text("DOB") },
        placeholder = { Text("MM/DD/YYYY") },
        trailingIcon = {
            Icon(painterResource(Res.drawable.ic_date_range), contentDescription = "Select date")
        },
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(selectedDate) {
                awaitEachGesture {
                    // Modifier.clickable doesn't work for text fields, so we use Modifier.pointerInput
                    // in the Initial pass to observe events before the text field consumes them
                    // in the Main pass.
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            }
    )

    if (showModal) {
        DatePickerModal(
            onDateSelected = { selectedDate = it },
            onDismiss = { showModal = false }
        )
    }
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

fun convertMillisToDate(millis: Long): String {
//    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
//    return formatter.format(Date(millis))
    return ""
}