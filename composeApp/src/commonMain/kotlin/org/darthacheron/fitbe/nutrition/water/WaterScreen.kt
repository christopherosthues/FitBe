package org.darthacheron.fitbe.nutrition.water

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import fitbe.composeapp.generated.resources.ic_add
import org.jetbrains.compose.resources.painterResource

@Composable
fun WaterScreen(viewModel: WaterIntakeViewModel) {
    val intake by viewModel.todayIntake.collectAsState()
    val total = intake?.amount ?: 0
    val goal = 2000

    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween // Pushes content to top & button to bottom
    ) {
        Column {
            Text(
                text = "Today's Intake: $total ml / $goal ml",
                style = MaterialTheme.typography.headlineSmall
            )

            LinearProgressIndicator(
                progress = { total / goal.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .padding(vertical = 16.dp)
            )
        }

        IconButton(
            content = {
                Icon(
                    painter = painterResource(Res.drawable.ic_add),
                    contentDescription = "Add Water",
                    tint = Color.White
                )
            },
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 16.dp)
                .background(MaterialTheme.colorScheme.primary)
        )
    }

    if (showDialog) {
        AddWaterDialog(
            initialAmount = 0,
            onSet = { amountMl ->
                viewModel.setIntake(intake, amountMl)
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
}

@Composable
fun AddWaterDialog(
    initialAmount: Int?,
    onSet: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedUnit by remember { mutableStateOf("ml") }
    var amountText by remember { mutableStateOf((initialAmount ?: "").toString()) }

    val unitOptions = listOf(
        "ml", "cl", "dl", "l",
        "cup (250ml)", "small glass (150ml)",
        "normal glass (200ml)", "large glass (300ml)"
    )

    fun convertToMl(input: String, unit: String): Int? {
        val value = input.toFloatOrNull() ?: return null
        return when (unit) {
            "ml" -> value.toInt()
            "cl" -> (value * 10).toInt()
            "dl" -> (value * 100).toInt()
            "l" -> (value * 1000).toInt()
            "cup (250ml)" -> (value * 250).toInt()
            "small glass (150ml)" -> (value * 150).toInt()
            "normal glass (200ml)" -> (value * 200).toInt()
            "large glass (300ml)" -> (value * 300).toInt()
            else -> null
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Set Daily Water Intake") },
        text = {
            Column {
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )

                Spacer(Modifier.height(8.dp))

                DropdownMenuWithSelected(
                    options = unitOptions,
                    selectedOption = selectedUnit,
                    onOptionSelected = { selectedUnit = it }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                convertToMl(amountText, selectedUnit)?.let(onSet)
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

@Composable
fun DropdownMenuWithSelected(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.fillMaxWidth().clickable { expanded = true },
            label = { Text("Unit") }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}