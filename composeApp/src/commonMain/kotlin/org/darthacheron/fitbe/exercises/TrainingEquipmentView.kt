package org.darthacheron.fitbe.exercises

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalUuidApi::class)
@Composable
fun TrainingEquipmentView(
    viewModel: TrainingEquipmentViewModel
) {
    val allEquipment by viewModel.allEquipment.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp) // Removed paddingValues, kept the original 16.dp padding
    ) {
        if (allEquipment.isEmpty()) {
            Text("No equipment found. Add some!")
            // TODO: Add a button or UI to add equipment
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(allEquipment, key = { it.id.toString() }) { equipment ->
                    TrainingEquipmentRow(equipment)
                }
            }
        }
    }
}

@Composable
fun TrainingEquipmentRow(equipment: TrainingEquipmentEntity) {
    // TODO: Expand this row to include edit/delete/reset buttons
    Text(
        text = equipment.name + if (equipment.default) " (Default)" else "",
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

// TODO: You might need a way to get the ViewModel instance,
// for example, using Koin:
// import org.koin.compose.viewmodel.koinViewModel
// viewModel: TrainingEquipmentViewModel = koinViewModel()
// or by passing it as a parameter from your navigation graph.
