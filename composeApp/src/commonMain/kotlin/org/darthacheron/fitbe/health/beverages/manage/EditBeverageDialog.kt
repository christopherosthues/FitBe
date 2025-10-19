package org.darthacheron.fitbe.health.beverages.manage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.beverages_add_dialog_amount
import fitbe.composeapp.generated.resources.beverages_add_dialog_cancel
import fitbe.composeapp.generated.resources.beverages_add_dialog_content_description_date
import fitbe.composeapp.generated.resources.beverages_add_dialog_content_description_time
import fitbe.composeapp.generated.resources.beverages_add_dialog_date
import fitbe.composeapp.generated.resources.beverages_add_dialog_name
import fitbe.composeapp.generated.resources.beverages_add_dialog_save
import fitbe.composeapp.generated.resources.beverages_add_dialog_time
import fitbe.composeapp.generated.resources.beverages_add_dialog_title
import fitbe.composeapp.generated.resources.beverages_add_dialog_unit
import fitbe.composeapp.generated.resources.ic_access_time
import fitbe.composeapp.generated.resources.ic_date_range
import fitbe.composeapp.generated.resources.local_date_format
import fitbe.composeapp.generated.resources.local_time_format
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.components.date.DatePickerModal
import org.darthacheron.fitbe.components.date.TimePickerDialog
import org.darthacheron.fitbe.health.beverages.FluidUnit
import org.darthacheron.fitbe.health.components.format
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalMaterial3Api::class, ExperimentalUuidApi::class)
@Composable
fun EditBeverageDialog(
    viewModel: EditBeverageDialogViewModel = koinViewModel(),
    id: Uuid,
    onDismiss: () -> Any,
    onSave: (Uuid, Double, String, FluidUnit, Instant) -> Any
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.init(id)
    }

    AlertDialog(
        onDismissRequest = viewModel::dismissDialog,
        title = { Text(stringResource(Res.string.beverages_add_dialog_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = uiState.dateTime.date.format(stringResource(Res.string.local_date_format)),
                        onValueChange = {},
                        label = { Text(text = stringResource(Res.string.beverages_add_dialog_date)) },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { showDatePicker = true }) {
                                Icon(
                                    painterResource(Res.drawable.ic_date_range),
                                    contentDescription =
                                        stringResource(Res.string.beverages_add_dialog_content_description_date)
                                )
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(8.dp))
                    OutlinedTextField(
                        value = uiState.dateTime.time.format(stringResource(Res.string.local_time_format)),
                        onValueChange = {},
                        label = { Text(text = stringResource(Res.string.beverages_add_dialog_time)) },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { showTimePicker = true }) {
                                Icon(
                                    painterResource(Res.drawable.ic_access_time),
                                    contentDescription =
                                        stringResource(Res.string.beverages_add_dialog_content_description_time)
                                )
                            }
                        },
                        modifier = Modifier.weight(0.75f)
                    )
                }

                if (showDatePicker) {
                    DatePickerModal(
                        onDateSelected = { millis ->
                            millis?.let {
                                val selectedDate =
                                    Instant
                                        .fromEpochMilliseconds(it)
                                        .toLocalDateTime(TimeZone.currentSystemDefault())
                                        .date
                                viewModel.onDateChange(selectedDate)
                            }
                            showDatePicker = false
                        },
                        onDismiss = { showDatePicker = false },
                        initialSelectedDateMillis = uiState.dateTime.date.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
                    )
                }

                if (showTimePicker) {
                    TimePickerDialog(
                        initialHour = uiState.dateTime.hour,
                        initialMinute = uiState.dateTime.minute,
                        onTimeSelected = { hour, minute ->
                            viewModel.onTimeChange(LocalTime(hour, minute))
                            showTimePicker = false
                        },
                        onDismiss = { showTimePicker = false },
                    )
                }

                OutlinedTextField(
                    value = uiState.amount,
                    onValueChange = viewModel::onAmountChange,
                    label = { Text(text = stringResource(Res.string.beverages_add_dialog_amount)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    isError = uiState.amountError != null,
                    supportingText = {
                        uiState.amountError?.let { Text(text = stringResource(it)) }
                    }
                )
                OutlinedTextField(
                    value = uiState.beverageName,
                    onValueChange = viewModel::onNameChange,
                    label = { Text(text = stringResource(Res.string.beverages_add_dialog_name)) },
                    modifier = Modifier.fillMaxWidth(),
                    isError = uiState.beverageNameError != null,
                    supportingText = {
                        uiState.beverageNameError?.let { Text(text = stringResource(it)) }
                    }
                )

                var unitDropdownExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = unitDropdownExpanded,
                    onExpandedChange = { unitDropdownExpanded = !unitDropdownExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = uiState.selectedUnit.localizedString(uiState.amount.toDoubleOrNull() ?: 0.0),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(text = stringResource(Res.string.beverages_add_dialog_unit)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = unitDropdownExpanded) },
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = unitDropdownExpanded,
                        onDismissRequest = { unitDropdownExpanded = false }
                    ) {
                        viewModel.allFluidUnits.forEach { unit ->
                            DropdownMenuItem(
                                text = { Text(text = unit.localizedString(uiState.amount.toDoubleOrNull() ?: 0.0)) },
                                onClick = {
                                    viewModel.onUnitChange(unit)
                                    unitDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.dismissDialog()
                    onSave(
                        uiState.id,
                        uiState.amount.toDouble(),
                        uiState.beverageName,
                        uiState.selectedUnit,
                        uiState.dateTime.toInstant(TimeZone.currentSystemDefault())
                    )
                },
                enabled = uiState.canSave
            ) {
                Text(text = stringResource(Res.string.beverages_add_dialog_save))
            }
        },
        dismissButton = {
            TextButton(onClick = {
                viewModel.dismissDialog()
                onDismiss()
            }) {
                Text(text = stringResource(Res.string.beverages_add_dialog_cancel))
            }
        }
    )
}