package org.darthacheron.fitbe.workouts.workouts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
// Removed: import androidx.compose.material.icons.Icons
// Removed: import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.painterResource // Added
import fitbe.composeapp.generated.resources.Res // Added/Ensured
import fitbe.composeapp.generated.resources.ic_add
import fitbe.composeapp.generated.resources.workout_overview_content_description_start_new_workout_fab
import fitbe.composeapp.generated.resources.workout_overview_empty
import fitbe.composeapp.generated.resources.workout_overview_error_loading_workouts
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Composable
fun WorkoutOverviewView(
    viewModel: WorkoutOverviewViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.updateTopBarConfig()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            // Assuming the parent Scaffold provides necessary overall padding.
            // This inner padding is for the content within this screen.
            .padding(16.dp)
    ) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            uiState.error != null -> {
                Text(
                    text = stringResource(uiState.error!!),
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }
            uiState.workoutSessions.isEmpty() -> {
                Text(
                    text = stringResource(Res.string.workout_overview_empty),
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    // Add padding to the bottom to ensure the FAB doesn't overlap the last item.
                    // FAB height (56dp) + some margin (16dp) = 72dp. Adjust as needed.
                    contentPadding = PaddingValues(bottom = 72.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.workoutSessions, key = { it.id }) { workoutSession ->
                        WorkoutSessionItem(
                            workoutSession = workoutSession,
                            onClick = { viewModel.onWorkoutSessionClicked(workoutSession.id) }
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { viewModel.onStartNewWorkoutClicked() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_add),
                contentDescription = stringResource(Res.string.workout_overview_content_description_start_new_workout_fab)
            )
        }
    }
}

@Composable
fun WorkoutSessionItem(
    workoutSession: WorkoutSession,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = workoutSession.name ?: "Workout on ${workoutSession.startTimestamp}",
                style = MaterialTheme.typography.titleMedium
            )
            // TODO: Add more details like date, duration, number of exercises etc.
            // Example:
            // Text(text = "Date: ${workoutSession.startTime.toString()}", style = MaterialTheme.typography.bodySmall)
            // if (workoutSession.endTime != null) {
            //     val duration = workoutSession.endTime.epochSeconds - workoutSession.startTime.epochSeconds
            //     Text(text = "Duration: ${duration / 60} mins", style = MaterialTheme.typography.bodySmall)
            // }
        }
    }
}
