package org.darthacheron.fitbe.health.beverages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
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
import fitbe.composeapp.generated.resources.beverages_overview_add_beverage_title
import fitbe.composeapp.generated.resources.beverages_overview_beverage_amount
import fitbe.composeapp.generated.resources.beverages_overview_beverage_name
import fitbe.composeapp.generated.resources.beverages_overview_beverage_unit
import fitbe.composeapp.generated.resources.beverages_overview_cancel
import fitbe.composeapp.generated.resources.beverages_overview_save
import fitbe.composeapp.generated.resources.ic_date_range
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.components.date.DatePickerModal
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBeverageDialog(
    viewModel: AddBeverageDialogViewModel,
    onDismiss: () -> Any,
    onSave: (Double, String, FluidUnit, Instant) -> Any
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = viewModel::dismissAddBeverageDialog,
        title = { Text(stringResource(Res.string.beverages_overview_add_beverage_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                TextButton(onClick = { showDatePicker = true }) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = "${uiState.selectedDateForDialog}")
                        Icon(
                            painter = painterResource(Res.drawable.ic_date_range),
                            contentDescription = null
                        )
                    }
                }

                if (showDatePicker) {
                    DatePickerModal(
                        onDateSelected = { millis ->
                            millis?.let {
                                val selectedDate =
                                    Instant.fromEpochMilliseconds(it).toLocalDateTime(TimeZone.UTC).date
                                viewModel.onDialogDateChange(selectedDate)
                            }
                            showDatePicker = false
                        },
                        onDismiss = { showDatePicker = false },
                        initialSelectedDateMillis = uiState.selectedDateForDialog.atStartOfDayIn(
                            TimeZone.UTC).toEpochMilliseconds()
                    )
                }

                TextField(
                    value = uiState.amount,
                    onValueChange = viewModel::onDialogAmountChange,
                    label = { Text(stringResource(Res.string.beverages_overview_beverage_amount)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    isError = uiState.amountError != null,
                    supportingText = {
                        uiState.amountError?.let { Text(stringResource(it)) }
                    }
                )
                TextField(
                    value = uiState.beverageName,
                    onValueChange = viewModel::onDialogBeverageNameChange,
                    label = { Text(stringResource(Res.string.beverages_overview_beverage_name)) },
                    modifier = Modifier.fillMaxWidth(),
                    isError = uiState.beverageNameError != null,
                    supportingText = {
                        uiState.beverageNameError?.let { Text(stringResource(it)) }
                    }
                )

                var unitDropdownExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = unitDropdownExpanded,
                    onExpandedChange = { unitDropdownExpanded = !unitDropdownExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
                        value = uiState.selectedUnit.localizedString(uiState.amount.toDoubleOrNull() ?: 0.0),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(Res.string.beverages_overview_beverage_unit)) },
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
                                    viewModel.onDialogUnitChange(unit)
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
                    viewModel.dismissAddBeverageDialog()
                    onSave(
                        uiState.amount.toDouble(),
                        uiState.beverageName,
                        uiState.selectedUnit,
                        uiState.selectedDateForDialog.atStartOfDayIn(TimeZone.UTC)
                    )
                },
                enabled = uiState.canSave
            ) {
                Text(stringResource(Res.string.beverages_overview_save))
            }
        },
        dismissButton = {
            TextButton(onClick = {
                viewModel.dismissAddBeverageDialog()
                onDismiss()
            }) {
                Text(stringResource(Res.string.beverages_overview_cancel))
            }
        }
    )
}