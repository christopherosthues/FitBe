package org.darthacheron.fitbe.health.steps

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.ic_date_range
import fitbe.composeapp.generated.resources.steps_add_steps_count
import fitbe.composeapp.generated.resources.steps_add_steps_date
import fitbe.composeapp.generated.resources.steps_add_steps_title
import fitbe.composeapp.generated.resources.steps_cancel
import fitbe.composeapp.generated.resources.steps_error_count
import fitbe.composeapp.generated.resources.steps_save
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.components.date.DatePickerModal
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun AddStepsDialog(
    viewModel: AddStepsDialogViewModel,
    onDismiss: () -> Unit,
    onSave: (date: LocalDate, steps: UInt) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var steps by remember { mutableStateOf("0") }
    var date by remember { mutableStateOf(Clock.System.now().toLocalDateTime(TimeZone.UTC).date) }
    var showDateDialog by remember { mutableStateOf(false) }
    var stepsError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.steps_add_steps_title)) },
        confirmButton = {
            TextButton(
                onClick = {
                    val stepsValue = steps.toUIntOrNull()
                    if (stepsValue != null && stepsValue <= 100_000U) {
                        onSave(date, stepsValue)
                    }
                }
            ) {
                Text(stringResource(Res.string.steps_save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.steps_cancel))
            }
        },
        text = {
            Column {
                OutlinedTextField(
                    value = date.toString(),
                    onValueChange = {},
                    label = { Text(stringResource(Res.string.steps_add_steps_date)) },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showDateDialog = true }) {
                            Icon(
                                painterResource(Res.drawable.ic_date_range),
                                contentDescription = null
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = steps,
                    onValueChange = {
                        steps = it
                        stepsError = it.toUIntOrNull() == null ||
                                   (it.toUIntOrNull() ?: 0U) > 100_000U
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text(stringResource(Res.string.steps_add_steps_count)) },
                    isError = stepsError,
                    supportingText = {
                        if (stepsError) {
                            Text(stringResource(Res.string.steps_error_count))
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )

    if (showDateDialog) {
        DatePickerModal(
            onDateSelected = { millis ->
                millis?.let {
                    date = Instant.fromEpochMilliseconds(it).toLocalDateTime(TimeZone.UTC).date
                }
                showDateDialog = false
            },
            onDismiss = { showDateDialog = false }
        )
    }
}

