package org.darthacheron.fitbe.health.sleep.manage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import fitbe.composeapp.generated.resources.local_date_format
import fitbe.composeapp.generated.resources.local_time_format
import fitbe.composeapp.generated.resources.sleep_add_dialog_cancel
import fitbe.composeapp.generated.resources.sleep_add_dialog_content_description_end_date
import fitbe.composeapp.generated.resources.sleep_add_dialog_content_description_end_time
import fitbe.composeapp.generated.resources.sleep_add_dialog_content_description_start_date
import fitbe.composeapp.generated.resources.sleep_add_dialog_content_description_start_time
import fitbe.composeapp.generated.resources.sleep_add_dialog_duration
import fitbe.composeapp.generated.resources.sleep_add_dialog_end_date
import fitbe.composeapp.generated.resources.sleep_add_dialog_end_time
import fitbe.composeapp.generated.resources.sleep_add_dialog_save
import fitbe.composeapp.generated.resources.sleep_add_dialog_start_date
import fitbe.composeapp.generated.resources.sleep_add_dialog_start_time
import fitbe.composeapp.generated.resources.sleep_add_dialog_title
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.components.date.DatePickerModal
import org.darthacheron.fitbe.components.date.TimePickerDialog
import org.darthacheron.fitbe.health.components.format
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
@Composable
fun EditSleepDialog(
    viewModel: EditSleepDialogViewModel = koinViewModel(),
    id: Uuid,
    onSave: (id: Uuid, start: Instant, end: Instant) -> Any,
    onDismiss: () -> Any
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.init(id)
    }

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    val duration =
        remember(uiState.startDateTime, uiState.endDateTime) {
            val diff =
                uiState.endDateTime
                    .toInstant(TimeZone.currentSystemDefault())
                    .minus(uiState.startDateTime.toInstant(TimeZone.currentSystemDefault()))
            if (diff.isNegative()) Duration.ZERO else diff
        }

    AlertDialog(
        onDismissRequest = viewModel::dismissDialog,
        title = { Text(text = stringResource(Res.string.sleep_add_dialog_title)) },
        text = {
            Column {
                // Start Date & Time
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = uiState.startDateTime.date.format(stringResource(Res.string.local_date_format)),
                            onValueChange = {},
                            label = { Text(text = stringResource(Res.string.sleep_add_dialog_start_date)) },
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = { showStartDatePicker = true }) {
                                    Icon(
                                        painterResource(Res.drawable.ic_date_range),
                                        contentDescription =
                                            stringResource(Res.string.sleep_add_dialog_content_description_start_date)
                                    )
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(Modifier.width(8.dp))
                        OutlinedTextField(
                            value = uiState.startDateTime.time.format(stringResource(Res.string.local_time_format)),
                            onValueChange = {},
                            label = { Text(text = stringResource(Res.string.sleep_add_dialog_start_time)) },
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = { showStartTimePicker = true }) {
                                    Icon(
                                        painterResource(Res.drawable.ic_access_time),
                                        contentDescription =
                                            stringResource(Res.string.sleep_add_dialog_content_description_start_time)
                                    )
                                }
                            },
                            modifier = Modifier.weight(0.75f)
                        )
                    }
                    if (uiState.startDateTimeError != null) {
                        uiState.startDateTimeError?.let {
                            Text(text = stringResource(it), color = MaterialTheme.colorScheme.error)
                        }
                    }
                }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = uiState.endDateTime.date.format(stringResource(Res.string.local_date_format)),
                            onValueChange = {},
                            label = { Text(text = stringResource(Res.string.sleep_add_dialog_end_date)) },
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = { showEndDatePicker = true }) {
                                    Icon(
                                        painterResource(Res.drawable.ic_date_range),
                                        contentDescription =
                                            stringResource(Res.string.sleep_add_dialog_content_description_end_date)
                                    )
                                }
                            },
                            modifier = Modifier.padding(top = 16.dp).weight(1f)
                        )
                        Spacer(Modifier.width(8.dp))
                        OutlinedTextField(
                            value = uiState.endDateTime.time.format(stringResource(Res.string.local_time_format)),
                            onValueChange = {},
                            label = { Text(text = stringResource(Res.string.sleep_add_dialog_end_time)) },
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = { showEndTimePicker = true }) {
                                    Icon(
                                        painterResource(Res.drawable.ic_access_time),
                                        contentDescription =
                                            stringResource(Res.string.sleep_add_dialog_content_description_end_time)
                                    )
                                }
                            },
                            modifier = Modifier.padding(top = 16.dp).weight(0.75f)
                        )
                    }
                    if (uiState.endDateTimeError != null) {
                        uiState.endDateTimeError?.let {
                            Text(text = stringResource(it), color = MaterialTheme.colorScheme.error)
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = formatDuration(duration),
                    onValueChange = {},
                    label = { Text(text = stringResource(Res.string.sleep_add_dialog_duration)) },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.dismissDialog()
                    onSave(
                        uiState.id,
                        uiState.startDateTime.toInstant(TimeZone.currentSystemDefault()),
                        uiState.endDateTime.toInstant(TimeZone.currentSystemDefault())
                    )
                },
                enabled = uiState.canSave
            ) {
                Text(text = stringResource(Res.string.sleep_add_dialog_save))
            }
        },
        dismissButton = {
            TextButton(onClick = {
                viewModel.dismissDialog()
                onDismiss()
            }) {
                Text(text = stringResource(Res.string.sleep_add_dialog_cancel))
            }
        }
    )

    if (showStartDatePicker) {
        DatePickerModal(
            onDateSelected = { millis ->
                millis?.let {
                    val date =
                        Instant
                            .fromEpochMilliseconds(it)
                            .toLocalDateTime(TimeZone.currentSystemDefault())
                            .date
                    viewModel.onStartDateChange(date)
                }
                showStartDatePicker = false
            },
            onDismiss = { showStartDatePicker = false }
        )
    }
    if (showStartTimePicker) {
        TimePickerDialog(
            initialHour = uiState.startDateTime.hour,
            initialMinute = uiState.startDateTime.minute,
            onTimeSelected = { hour, minute ->
                viewModel.onStartTimeChange(LocalTime(hour, minute))
                showStartTimePicker = false
            },
            onDismiss = { showStartTimePicker = false }
        )
    }
    if (showEndDatePicker) {
        DatePickerModal(
            onDateSelected = { millis ->
                millis?.let {
                    val date =
                        Instant
                            .fromEpochMilliseconds(it)
                            .toLocalDateTime(TimeZone.currentSystemDefault())
                            .date
                    viewModel.onEndDateChange(date)
                }
                showEndDatePicker = false
            },
            onDismiss = { showEndDatePicker = false }
        )
    }
    if (showEndTimePicker) {
        TimePickerDialog(
            initialHour = uiState.endDateTime.hour,
            initialMinute = uiState.endDateTime.minute,
            onTimeSelected = { hour, minute ->
                viewModel.onEndTimeChange(LocalTime(hour, minute))
                showEndTimePicker = false
            },
            onDismiss = { showEndTimePicker = false }
        )
    }
}