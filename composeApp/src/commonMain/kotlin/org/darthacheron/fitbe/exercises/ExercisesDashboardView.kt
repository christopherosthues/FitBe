package org.darthacheron.fitbe.exercises

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import fitbe.composeapp.generated.resources.card_title_exercises_overview
import fitbe.composeapp.generated.resources.card_title_training_equipment
import fitbe.composeapp.generated.resources.ic_launcher
import org.darthacheron.fitbe.navigation.Screen
// Import your placeholder drawable resources here once they are added
// For example:
// import fitbe.composeapp.generated.resources.ic_exercises_overview_card
// import fitbe.composeapp.generated.resources.ic_manage_equipment_card
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ExercisesDashboardView(
    navHostController: NavHostController
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            DashboardCard(
                title = stringResource(Res.string.card_title_exercises_overview),
                // Replace with actual painterResource(Res.drawable.ic_exercises_overview_card)
                // after adding the drawable
                imagePainter = painterResource(Res.drawable.ic_launcher), // Placeholder
                onClick = { navHostController.navigate(Screen.Exercises) },
                contentDescription = "Navigate to Exercises Overview"
            )
        }
        item {
            DashboardCard(
                title = stringResource(Res.string.card_title_training_equipment),
                // Replace with actual painterResource(Res.drawable.ic_manage_equipment_card)
                // after adding the drawable
                imagePainter = painterResource(Res.drawable.ic_launcher), // Placeholder
                onClick = { navHostController.navigate(Screen.TrainingEquipment) },
                contentDescription = "Navigate to Manage Training Equipment"
            )
        }
    }
}

@Composable
private fun DashboardCard(
    title: String,
    imagePainter: Painter,
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .height(200.dp)
            .clickable(onClick = onClick)
            .semantics { this.contentDescription = contentDescription },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = imagePainter,
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
                        .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp, topStart = 0.dp, topEnd = 0.dp))
                        .background(Color.Black.copy(alpha = 0.6f))
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                }
            }
        }
    }
}
