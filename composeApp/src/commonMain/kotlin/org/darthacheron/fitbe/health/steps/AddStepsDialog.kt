package org.darthacheron.fitbe.health.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.ic_date_range
import fitbe.composeapp.generated.resources.local_date_format
import fitbe.composeapp.generated.resources.steps_add_dialog_cancel
import fitbe.composeapp.generated.resources.steps_add_dialog_content_description_date
import fitbe.composeapp.generated.resources.steps_add_dialog_save
import fitbe.composeapp.generated.resources.steps_add_dialog_steps
import fitbe.composeapp.generated.resources.steps_add_dialog_title
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.components.date.DatePickerModal
import org.darthacheron.fitbe.health.components.format
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun AddStepsDialog(
    viewModel: AddStepsDialogViewModel,
    initialDate: Instant? = null,
    onDismiss: () -> Unit,
    onSave: (date: Instant, steps: UInt) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }

    if (initialDate != null) {
        viewModel.onDateChange(initialDate.toLocalDateTime(TimeZone.currentSystemDefault()).date)
    }

    AlertDialog(
        onDismissRequest = viewModel::dismissDialog,
        title = { Text(stringResource(Res.string.steps_add_dialog_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                TextButton(onClick = { showDatePicker = true }) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = uiState.date.format(stringResource(Res.string.local_date_format)))
                        Icon(
                            painter = painterResource(Res.drawable.ic_date_range),
                            contentDescription = stringResource(Res.string.steps_add_dialog_content_description_date)
                        )
                    }
                }

                if (showDatePicker) {
                    DatePickerModal(
                        onDateSelected = { millis ->
                            millis?.let {
                                val selectedDate =
                                    Instant.fromEpochMilliseconds(it).toLocalDateTime(TimeZone.UTC).date
                                viewModel.onDateChange(selectedDate)
                            }
                            showDatePicker = false
                        },
                        onDismiss = { showDatePicker = false },
                        initialSelectedDateMillis = uiState.date.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
                    )
                }

                TextField(
                    value = uiState.steps,
                    onValueChange = viewModel::onStepsChange,
                    label = { Text(text = stringResource(Res.string.steps_add_dialog_steps)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    isError = uiState.stepsError != null,
                    supportingText = {
                        uiState.stepsError?.let { Text(text = stringResource(it)) }
                    }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.dismissDialog()
                    onSave(
                        uiState.date.atStartOfDayIn(TimeZone.currentSystemDefault()),
                        uiState.steps.toUInt()
                    )
                },
                enabled = uiState.canSave
            ) {
                Text(text = stringResource(Res.string.steps_add_dialog_save))
            }
        },
        dismissButton = {
            TextButton(onClick = {
                viewModel.dismissDialog()
                onDismiss()
            }) {
                Text(text = stringResource(Res.string.steps_add_dialog_cancel))
            }
        }
    )
}