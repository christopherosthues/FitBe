package org.darthacheron.fitbe.workouts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.card_title_content_description_exercises_overview
import fitbe.composeapp.generated.resources.card_title_content_description_training_equipment_overview
import fitbe.composeapp.generated.resources.card_title_content_description_workouts_overview
import fitbe.composeapp.generated.resources.card_title_exercises_overview
import fitbe.composeapp.generated.resources.card_title_training_equipment_overview
import fitbe.composeapp.generated.resources.card_title_workouts_overview
import fitbe.composeapp.generated.resources.ic_exercises
import fitbe.composeapp.generated.resources.ic_training_equipment
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ExercisesDashboardView(
    exercisesDashboardViewModel: ExercisesDashboardViewModel,
) {
    LaunchedEffect(Unit) {
        exercisesDashboardViewModel.updateTopBarConfig()
    }
    LazyVerticalGrid(
        columns = GridCells.Adaptive(300.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            DashboardCard(
                title = stringResource(Res.string.card_title_workouts_overview),
                imagePainter = painterResource(Res.drawable.ic_exercises),
                onClick = { exercisesDashboardViewModel.navigateToWorkouts() },
                contentDescription = stringResource(Res.string.card_title_content_description_workouts_overview)
            )
        }
        item {
            DashboardCard(
                title = stringResource(Res.string.card_title_exercises_overview),
                imagePainter = painterResource(Res.drawable.ic_exercises),
                onClick = { exercisesDashboardViewModel.navigateToExercises() },
                contentDescription = stringResource(Res.string.card_title_content_description_exercises_overview)
            )
        }
        item {
            DashboardCard(
                title = stringResource(Res.string.card_title_training_equipment_overview),
                imagePainter = painterResource(Res.drawable.ic_training_equipment),
                onClick = { exercisesDashboardViewModel.navigateToEquipment() },
                contentDescription = stringResource(Res.string.card_title_content_description_training_equipment_overview)
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
                contentDescription = null,
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
