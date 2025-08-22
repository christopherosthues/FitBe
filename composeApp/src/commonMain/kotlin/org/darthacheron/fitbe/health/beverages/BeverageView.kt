package org.darthacheron.fitbe.health.beverages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.add_beverage
import org.darthacheron.fitbe.components.CircularWaveAnimationProgressIndicator
import org.darthacheron.fitbe.components.DropdownSelection
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun BeverageView(beverageViewModel: BeverageViewModel) {
    val beverages by beverageViewModel.todayIntake.collectAsState(initial = listOf())
    val todayProgress by beverageViewModel.todayProgress.collectAsState()

    var showDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        stickyHeader {
            Column(
                modifier = Modifier.padding(top = 100.dp, start = 16.dp, end = 16.dp, bottom = 16.dp).fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularWaveAnimationProgressIndicator(progress = { todayProgress.toFloat() })

                TextButton(
                    onClick = { showDialog = true },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.White,
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(text = stringResource(Res.string.add_beverage))
                }
            }
        }
        beverages.forEach {
            item {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                    Icon(
                        painterResource(it.unit.iconResource()),
                        contentDescription = null,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                        Text(text = it.localizedString())
                }
            }
        }
    }

    if (showDialog) {
        AddBeverageDialog(
            initialAmount = 0u,
            onSet = { amount, unit, beverage ->
                beverageViewModel.addBeverage(amount, unit, beverage)
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
}

@Composable
fun AddBeverageDialog(
    initialAmount: UInt?,
    onSet: (UInt, FluidUnit, String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedUnit by remember { mutableStateOf(FluidUnit.Milliliter) }
    var amountText by remember { mutableStateOf((initialAmount ?: "").toString()) }
    var beverage by remember { mutableStateOf("") }
    val unitOptions = FluidUnit.entries

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Set Daily Beverage Intake") },
        text = {
            Column {
                TextField(value = beverage, onValueChange = { beverage = it })

                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )

                Spacer(Modifier.height(8.dp))

                DropdownSelection(
                    initialState = false,
                    items = unitOptions,
                    title = "Choose a unit",
                    itemContent = { item, onClick ->
                        DropdownMenuItem(
                            text = {
                                Text(item.localizedString(amountText.toIntOrNull() ?: 0))
                            },
                            leadingIcon = { Icon(painterResource(item.iconResource()), contentDescription = null) },
                            onClick = onClick
                        )
                    },
                    itemToString = {
                        it.localizedString(amountText.toIntOrNull() ?: 0)
                    },
                    onItemSelected = {
                        selectedUnit = unitOptions[it]
                    }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onSet(amountText.toUInt(), selectedUnit, beverage)
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
