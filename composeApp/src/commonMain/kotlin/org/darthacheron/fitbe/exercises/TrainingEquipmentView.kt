package org.darthacheron.fitbe.exercises

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
            .padding(16.dp)
    ) {
        if (allEquipment.isEmpty()) {
            Text("No equipment found. Add some!")
            // TODO: Add a button or UI to add equipment
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 128.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(allEquipment.size, key = { it.toString() }) { equipmentIndex ->
                    TrainingEquipmentCard(allEquipment[equipmentIndex])
                }
            }
//            LazyColumn(modifier = Modifier.fillMaxSize()) {
//                items(allEquipment, key = { it.id.toString() }) { equipment ->
//                    TrainingEquipmentRow(equipment)
//                }
//            }
        }
    }
}

@Composable
fun TrainingEquipmentCard(equipment: EquipmentWithExercises) {
    // TODO: Expand this row to include edit/delete/reset buttons
    Text(
        text = equipment.getLocalizedName() + if (equipment.default) " (Default)" else "",
        modifier = Modifier.padding(vertical = 8.dp)
    )
}
