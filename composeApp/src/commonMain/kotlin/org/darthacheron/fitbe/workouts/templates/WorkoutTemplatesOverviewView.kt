package org.darthacheron.fitbe.workouts.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.exercise_content_description_add // TODO: Consider a more specific string like "add_workout_template"
import fitbe.composeapp.generated.resources.exercise_content_description_card
import fitbe.composeapp.generated.resources.exercise_content_description_card_add_favorite
import fitbe.composeapp.generated.resources.exercise_content_description_card_remove_favorite
import fitbe.composeapp.generated.resources.exercise_content_description_default_exercise
import fitbe.composeapp.generated.resources.ic_add
import fitbe.composeapp.generated.resources.ic_favorite
import fitbe.composeapp.generated.resources.ic_favorite_border
import fitbe.composeapp.generated.resources.ic_launcher
import org.darthacheron.fitbe.components.ImageWithDefault
import org.darthacheron.fitbe.workouts.exercises.Exercise
import org.darthacheron.fitbe.workouts.exercises.getExerciseImage
import org.darthacheron.fitbe.workouts.exercises.getExerciseName
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

// New dummy data for Workout Templates
@OptIn(ExperimentalUuidApi::class)
data class DummyWorkoutTemplate(
    val id: Uuid,
    val name: String,
    val description: String,
    val type: String, // e.g., "Strength", "Cardio", "Mixed"
    val exercises: List<String>, // Just names for simplicity for now
    val imageName: String? = null // Placeholder for a potential image
)

@OptIn(ExperimentalUuidApi::class)
private val dummyWorkoutTemplates = listOf(
    DummyWorkoutTemplate(
        id = Uuid.random(),
        name = "Morning Energizer",
        description = "A quick routine to start your day.",
        type = "Mixed",
        exercises = listOf("Jumping Jacks - 30 sec", "Push Ups - 10 reps", "Squats - 12 reps", "Plank - 45 sec")
    ),
    DummyWorkoutTemplate(
        id = Uuid.random(),
        name = "Upper Body Strength",
        description = "Focus on building upper body muscles.",
        type = "Strength",
        exercises = listOf("Bench Press - 3 sets", "Overhead Press - 3 sets", "Pull Ups - 3 sets", "Bicep Curls - 3 sets")
    ),
    DummyWorkoutTemplate(
        id = Uuid.random(),
        name = "Cardio Blast",
        description = "Get your heart rate up!",
        type = "Cardio",
        exercises = listOf("Running - 20 min", "Cycling - 20 min"),
        imageName = "ic_launcher" // Example using existing dummy image resource name (ensure it exists or remove)
    ),
    DummyWorkoutTemplate(
        id = Uuid.random(),
        name = "Full Body Workout",
        description = "A comprehensive full body routine.",
        type = "Strength",
        exercises = listOf("Squats - 3 sets", "Deadlifts - 1 set", "Bench Press - 3 sets", "Rows - 3 sets", "Overhead Press - 3 sets")
    )
)

@OptIn(ExperimentalUuidApi::class)
@Composable
fun WorkoutTemplatesOverviewView(workoutTemplatesOverviewViewModel: WorkoutTemplatesOverviewViewModel) {
    LaunchedEffect(Unit) {
        workoutTemplatesOverviewViewModel.updateTopBarConfig() // Assumes ViewModel still handles top bar title
    }
    // val allExercises by workoutTemplatesViewModel.allExercises.collectAsState() // Using dummyWorkoutTemplates instead
    // val favoriteExerciseIds by workoutTemplatesViewModel.favoriteExerciseIds.collectAsState() // Not directly applicable to template list

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (dummyWorkoutTemplates.isEmpty()) {
                Text(text = "No workout templates found. Add some!") // Placeholder text
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 200.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(dummyWorkoutTemplates.size, key = { dummyWorkoutTemplates[it].id }) { templateIndex ->
                        val template = dummyWorkoutTemplates[templateIndex]
                        WorkoutTemplateCard(
                            template = template,
                            onClick = { workoutTemplatesOverviewViewModel.navigateToWorkoutTemplateDetail(template.id) },
                            contentDescription = "Workout template: ${template.name}" // TODO:
//                            contentDescription = stringResource(
//                                Res.string.workout_template_content_description_card,
//                                getExerciseName(exercise.name, exercise.default)
//                            )
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { workoutTemplatesOverviewViewModel.navigateToWorkoutTemplateDetail(null) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_add),
                contentDescription = stringResource(Res.string.exercise_content_description_add) // TODO: Change to "Add workout template"
            )
        }
    }
}

@Composable
fun WorkoutTemplateCard(
    template: DummyWorkoutTemplate,
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .height(200.dp)
            .width(200.dp)
            .clickable(onClick = onClick)
            .semantics { this.contentDescription = contentDescription },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            ImageWithDefault(
                imageUri = null,
                imageResource = Res.drawable.ic_launcher,
                default = true,
                contentDescription = null,
                defaultContentDescription = stringResource(Res.string.exercise_content_description_default_exercise),
                modifier = Modifier.fillMaxSize()
            )

//            IconButton(
//                onClick = onToggleFavorite,
//                modifier = Modifier.align(Alignment.TopEnd).padding(4.dp)
//            ) {
//                Icon(
//                    painter = painterResource(if (isFavorite) Res.drawable.ic_favorite else Res.drawable.ic_favorite_border),
//                    contentDescription = stringResource(if (isFavorite) Res.string.exercise_content_description_card_remove_favorite else Res.string.exercise_content_description_card_add_favorite),
//                    tint = MaterialTheme.colorScheme.primary
//                )
//            }

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
                    Column() {
                        // Optional: Image for the template - assuming ic_launcher is a valid drawable resource key
                        // if (template.imageName == "ic_launcher") { // Simplified check for dummy data
                        //     Icon(painterResource(Res.drawable.ic_launcher), contentDescription = "${template.name} image", modifier = Modifier.height(80.dp).padding(bottom = 8.dp))
                        // }
                        Text(
                            text = template.name,
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        val chipColors = SuggestionChipDefaults.suggestionChipColors()
                        SuggestionChip(
                            onClick = { },
                            label = { Text(text = "${template.exercises.size} exercises") },
                            enabled = false,
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                disabledContainerColor = chipColors.containerColor,
                                disabledLabelColor = chipColors.labelColor,
                            ),
                            modifier = Modifier.height(24.dp)
                        )
                    }
                }
            }
        }
    }
}
