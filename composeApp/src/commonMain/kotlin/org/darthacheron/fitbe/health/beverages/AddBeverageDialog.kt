package org.darthacheron.fitbe.health.beverages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
//import fitbe.composeapp.generated.resources.add_beverage_title
//import fitbe.composeapp.generated.resources.beverage_amount
//import fitbe.composeapp.generated.resources.beverage_name
//import fitbe.composeapp.generated.resources.beverage_unit
//import fitbe.composeapp.generated.resources.cancel
//import fitbe.composeapp.generated.resources.save
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBeverageDialog(
    date: LocalDate,
    amount: String,
    beverageName: String,
    selectedUnit: FluidUnit,
    allUnits: List<FluidUnit>,
    onAmountChange: (String) -> Unit,
    onBeverageNameChange: (String) -> Unit,
    onUnitChange: (FluidUnit) -> Unit,
    onDismissRequest: () -> Unit,
    onSaveRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(/*stringResource(Res.string.add_beverage_title))*/ "Title") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Date: $date") // Displaying the selected date

                TextField(
                    value = amount,
                    onValueChange = onAmountChange,
                    label = { Text(/*stringResource(Res.string.beverage_amount)*/ "Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = beverageName,
                    onValueChange = onBeverageNameChange,
                    label = { Text(/*stringResource(Res.string.beverage_name)*/ "Beverage Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                var unitDropdownExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = unitDropdownExpanded,
                    onExpandedChange = { unitDropdownExpanded = !unitDropdownExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
                        value = selectedUnit.name, // Consider localizing unit names if needed
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(/*stringResource(Res.string.beverage_unit)*/ "Beverage Unit") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = unitDropdownExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = unitDropdownExpanded,
                        onDismissRequest = { unitDropdownExpanded = false }
                    ) {
                        allUnits.forEach { unit ->
                            DropdownMenuItem(
                                text = { Text(unit.name) }, // Consider localizing
                                onClick = {
                                    onUnitChange(unit)
                                    unitDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onSaveRequest) {
                Text(/*stringResource(Res.string.save)*/ "Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(/*stringResource(Res.string.cancel)*/ "Cancel")
            }
        }
    )
}
