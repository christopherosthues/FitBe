package org.darthacheron.fitbe.exercises.exercises

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.ic_add
// Assuming you will create a string resource for adding an exercise
// import fitbe.composeapp.generated.resources.exercise_add 
import org.darthacheron.fitbe.navigation.Screen // Assuming Screen.AddEditExercise(id) exists
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalUuidApi::class)
@Composable
fun ExercisesView(
    viewModel: ExercisesViewModel,
    navHostController: NavHostController
) {
    val allExercises by viewModel.allExercises.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (allExercises.isEmpty()) {
                Text("No exercises found. Add some!")
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 200.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(allExercises.size, key = { allExercises[it].id.toString() }) { exerciseIndex ->
                        val exercise = allExercises[exerciseIndex]
                        ExerciseCard(
                            exercise = exercise,
                            // Assuming you have a navigation route like Screen.AddEditExercise
                            onClick = { navHostController.navigate(Screen.AddEditExercise(exercise.id.toString())) }, 
                            contentDescription = "View or Edit ${exercise.name}"
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { navHostController.navigate(Screen.AddEditExercise(null)) }, // For adding a new exercise
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_add),
                // Replace with actual resource: stringResource(Res.string.exercise_add)
                contentDescription = "Add Exercise" 
            )
        }
    }
}

@Composable
fun ExerciseCard(
    exercise: Exercise,
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
//            .height(200.dp) // Height can be dynamic or fixed as per your design needs
//            .width(200.dp)  // Width can be dynamic or fixed
            .clickable(onClick = onClick)
            .semantics { this.contentDescription = contentDescription },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp) // Add some padding inside the card
        ) {
            Column {
                Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Muscles: ${exercise.targetMuscleGroups.joinToString { it.name }}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                // You can add more details from the 'exercise' object here, like 'guide'
                // Text(
                // text = exercise.guide,
                // style = MaterialTheme.typography.bodySmall
                // )
            }
        }
    }
}
