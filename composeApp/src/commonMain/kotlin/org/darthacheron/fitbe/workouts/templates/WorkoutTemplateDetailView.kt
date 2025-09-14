package org.darthacheron.fitbe.workouts.templates

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.ic_launcher
import org.jetbrains.compose.resources.painterResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

// Dummy data structures
private data class ExerciseSetInfo(val description: String)
private data class ExerciseInfo(val name: String, val imageName: String, val sets: List<ExerciseSetInfo>)

// Dummy data for the tabs
private val warmUpExercisesList = listOf(
    ExerciseInfo("Jumping Jacks", "ic_launcher", listOf(ExerciseSetInfo("30 sec"), ExerciseSetInfo("30 sec"), ExerciseSetInfo("25 sec"))),
    ExerciseInfo("Arm Circles", "ic_launcher", listOf(ExerciseSetInfo("15 reps"), ExerciseSetInfo("15 reps"))),
    ExerciseInfo("High Knees", "ic_launcher", listOf(ExerciseSetInfo("45 sec")))
)

private val mainWorkoutExercisesList = listOf(
    ExerciseInfo("Push Ups", "ic_launcher", listOf(ExerciseSetInfo("10 reps"), ExerciseSetInfo("10 reps"), ExerciseSetInfo("8 reps"))),
    ExerciseInfo("Squats", "ic_launcher", listOf(ExerciseSetInfo("12 reps"), ExerciseSetInfo("12 reps @ 20kg"), ExerciseSetInfo("10 reps @ 25kg"))),
    ExerciseInfo("Plank", "ic_launcher", listOf(ExerciseSetInfo("45 sec"), ExerciseSetInfo("60 sec"))),
    ExerciseInfo("Bicep Curls", "ic_launcher", listOf(ExerciseSetInfo("10 reps"), ExerciseSetInfo("10 reps"), ExerciseSetInfo("8 reps")))
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalUuidApi::class)
@Composable
fun WorkoutTemplateDetailView(
    workoutTemplateId: Uuid?,
    workoutTemplateDetailViewModel: WorkoutTemplateDetailViewModel
) {
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 72.dp) // Space for the bottom button
        ) {
            // Top static info
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Icon(
                    painter = painterResource(Res.drawable.ic_launcher),
                    contentDescription = "Workout Image",
                    modifier = Modifier
                        .size(100.dp) // Adjusted size
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Workout Name", // Dummy name
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(4.dp))
                AssistChip(
                    onClick = { /* Non-functional for this dummy view */ },
                    label = { Text("Mixed exercises") }, // Dummy info
                    enabled = false, // Keep it non-interactive
                    colors = AssistChipDefaults.assistChipColors(
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            Spacer(modifier = Modifier.height(16.dp)) // Space before tabs

            // Tabs and their content
            var selectedTabIndex by remember { mutableStateOf(0) }
            val tabs = listOf("Warm Up", "Workouts") // Dummy tab titles

            PrimaryTabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth()
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }

            // Tab Content
            val currentExercises = when (selectedTabIndex) {
                0 -> warmUpExercisesList
                1 -> mainWorkoutExercisesList
                else -> emptyList()
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp), // Add padding above the list
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                currentExercises.forEach { exercise ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp)) { // Horizontal padding for items
                        ExerciseItemCard(exercise = exercise)
                    }
                }
            }
        }

        TextButton(
            onClick = { /* TODO: Implement start workout action */ },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Text("Start Workout")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExerciseItemCard(exercise: ExerciseInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_launcher), // Using placeholder
                contentDescription = "${exercise.name} image",
                modifier = Modifier.size(56.dp) // Slightly smaller image
            )
            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = exercise.name, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(exercise.sets) { setInfo ->
                        AssistChip(
                            onClick = { /* Non-interactive */ },
                            label = { Text(setInfo.description) }
                            // Optionally, style chips to be smaller or distinct
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))
            IconButton(
                onClick = { /* TODO: Implement drag-to-reorder action */ }
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_launcher), // Using placeholder
                    contentDescription = "Drag item ${exercise.name}",
                )
            }
        }
    }
}
