package org.darthacheron.fitbe.health.weight


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.ui.tooling.preview.Preview

// StackedAreaChart

@Preview
@Composable
fun WeightOverviewView(bodyWeightOverviewViewModel: WeightOverviewViewModel) {
    val chartData by remember { derivedStateOf { bodyWeightOverviewViewModel.chartData } }

    Column {
        Row {
            DropdownMenuWithGroupingSelector(
                selected = bodyWeightOverviewViewModel.selectedGrouping,
                onSelected = {
                    bodyWeightOverviewViewModel.selectedGrouping = it
                    bodyWeightOverviewViewModel.loadChartData()
                }
            )
            Spacer(Modifier.weight(1f))
            IconButton(onClick = { bodyWeightOverviewViewModel.goBack() }) { Icon(Icons.Default.ArrowBack, null) }
            IconButton(onClick = { bodyWeightOverviewViewModel.goForward() }) { Icon(Icons.Default.ArrowForward, null) }
        }

        StackedAreaChart(data = chartData)

        FloatingActionButton(
            onClick = { showAddEntryDialog = true },
            modifier = Modifier.align(Alignment.End).padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Entry")
        }
    }

    if (showAddEntryDialog) {
        AddWeightEntryDialog(
            onDismiss = { showAddEntryDialog = false },
            onSave = { bodyWeightOverviewViewModel.loadChartData() }
        )
    }
}

@Composable
fun AddWeightEntryDialog(onDismiss: () -> Unit, onSave: suspend () -> Unit) {
    var weight by remember { mutableStateOf("") }
    var bodyFat by remember { mutableStateOf("") }
    var muscle by remember { mutableStateOf("") }
    var bone by remember { mutableStateOf("") }
    var water by remember { mutableStateOf("") }
    val today = LocalDate.now()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Weight Entry") },
        confirmButton = {
            Button(onClick = {
                val total = weight.toDoubleOrNull() ?: return@Button
                val partsSum = listOfNotNull(
                    bodyFat.toDoubleOrNull(),
                    muscle.toDoubleOrNull(),
                    bone.toDoubleOrNull(),
                    water.toDoubleOrNull()
                ).sum()

                if (partsSum <= total) {
                    // Save
                }
            }) { Text("Save") }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
        },
        text = {
            Column {
                TextField(value = weight, onValueChange = { weight = it }, label = { Text("Total Weight (kg)") })
                TextField(value = bodyFat, onValueChange = { bodyFat = it }, label = { Text("Body Fat (%)") })
                TextField(value = muscle, onValueChange = { muscle = it }, label = { Text("Muscle Mass (kg)") })
                TextField(value = bone, onValueChange = { bone = it }, label = { Text("Bone Mass (kg)") })
                TextField(value = water, onValueChange = { water = it }, label = { Text("Body Water (%)") })
            }
        }
    )
}
