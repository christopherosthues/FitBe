package org.darthacheron.fitbe.health.sleep

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.ic_access_time
import fitbe.composeapp.generated.resources.ic_date_range
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.components.date.DatePickerModal
import org.darthacheron.fitbe.components.date.TimePickerDialog
import org.jetbrains.compose.resources.painterResource
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime

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
