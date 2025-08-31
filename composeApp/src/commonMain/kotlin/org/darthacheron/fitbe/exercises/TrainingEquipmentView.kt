package org.darthacheron.fitbe.exercises

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.ic_launcher
import fitbe.composeapp.generated.resources.ic_training_equipment
import org.darthacheron.fitbe.navigation.Screen
import org.jetbrains.compose.resources.painterResource
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalUuidApi::class)
@Composable
fun TrainingEquipmentView(
    viewModel: TrainingEquipmentViewModel,
    navHostController: NavHostController
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
                columns = GridCells.Adaptive(minSize = 200.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(allEquipment.size, key = { it.toString() }) { equipmentIndex ->
                    TrainingEquipmentCard(equipment = allEquipment[equipmentIndex],
                        onClick = { navHostController.navigate(Screen.TrainingEquipment) },
                        contentDescription = "Navigate to Manage Training Equipment")
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
fun TrainingEquipmentCard(
    equipment: EquipmentWithExercises,
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    // TODO: Expand this row to include edit/delete/reset buttons
    val imageResource = equipment.getLocalizedImage()
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .height(200.dp)
            .width(200.dp)
            .clickable(onClick = onClick)
            .semantics { this.contentDescription = contentDescription },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = if (imageResource != null) painterResource(imageResource) else painterResource(Res.drawable.ic_launcher),
                contentDescription = null, // Decorative image, title provides context
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(
                                bottomStart = 16.dp,
                                bottomEnd = 16.dp,
                                topStart = 0.dp,
                                topEnd = 0.dp
                            )
                        )
                        .background(Color.Black.copy(alpha = 0.6f))
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = equipment.getLocalizedName() + if (equipment.default) " (Default)" else "",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}
