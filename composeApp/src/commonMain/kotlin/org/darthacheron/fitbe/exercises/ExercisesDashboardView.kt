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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DashboardCard(
            title = stringResource(Res.string.card_title_exercises_overview),
            // Replace with actual painterResource(Res.drawable.ic_exercises_overview_card)
            // after adding the drawable
            imagePainter = painterResource(Res.drawable.ic_launcher), // Placeholder
            onClick = { navHostController.navigate(Screen.Exercises) },
            contentDescription = "Navigate to Exercises Overview" // Content description for the Card's click action
        )

        DashboardCard(
            title = stringResource(Res.string.card_title_training_equipment),
            // Replace with actual painterResource(Res.drawable.ic_manage_equipment_card)
            // after adding the drawable
            imagePainter = painterResource(Res.drawable.ic_launcher), // Placeholder
            onClick = { navHostController.navigate(Screen.TrainingEquipment) },
            contentDescription = "Navigate to Manage Training Equipment" // Content description for the Card's click action
        )
    }
}

@Composable
private fun DashboardCard(
    title: String,
    imagePainter: Painter,
    onClick: () -> Unit,
    contentDescription: String, // This describes the action of clicking the card
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
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
                contentScale = ContentScale.Crop // Crop to fill the bounds
            )
            Box( // This Box is aligned to BottomCenter and has overall padding for the text element
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp) // Outer padding for the whole text element from card edges
            ) {
                Box( // This Box provides the semi-transparent background for the Text
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp)) // Clip the background to have rounded corners
                        .background(Color.Black.copy(alpha = 0.6f))
                        .padding(horizontal = 12.dp, vertical = 8.dp) // Padding *inside* the background, around the text
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
