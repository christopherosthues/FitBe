package org.darthacheron.fitbe.workouts.workouts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.painterResource
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.ic_access_time
import fitbe.composeapp.generated.resources.ic_add
import fitbe.composeapp.generated.resources.workout_overview_content_description_start_new_workout_fab
import fitbe.composeapp.generated.resources.workout_overview_content_description_schedule_new_workout_fab // New resource
import fitbe.composeapp.generated.resources.workout_overview_empty
import fitbe.composeapp.generated.resources.workout_overview_scheduled_header // New resource
import fitbe.composeapp.generated.resources.workout_overview_past_header // New resource
import kotlin.uuid.ExperimentalUuidApi
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalUuidApi::class)
@Composable
fun PerformedWorkoutsOverviewView(
    viewModel: PerformedWorkoutsOverviewViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.updateTopBarConfig()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp) // Main padding for the screen content
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
            uiState.scheduledSessions.isEmpty() && uiState.pastSessions.isEmpty() -> {
                Text(
                    text = stringResource(Res.string.workout_overview_empty),
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 140.dp), // Increased padding for two FABs
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (uiState.scheduledSessions.isNotEmpty()) {
                        item {
                            Text(
                                text = stringResource(Res.string.workout_overview_scheduled_header),
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        items(uiState.scheduledSessions, key = { "scheduled-${it.id}" }) { workoutSession ->
                            WorkoutSessionItem(
                                workoutSession = workoutSession,
                                onClick = { viewModel.onWorkoutSessionClicked(workoutSession) }
                            )
                        }
                    }

                    if (uiState.pastSessions.isNotEmpty()) {
                        item {
                            Text(
                                text = stringResource(Res.string.workout_overview_past_header),
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        items(uiState.pastSessions, key = { "past-${it.id}" }) { workoutSession ->
                            WorkoutSessionItem(
                                workoutSession = workoutSession,
                                onClick = { viewModel.onWorkoutSessionClicked(workoutSession) }
                            )
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp), // Align with parent box padding logic if needed
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FloatingActionButton(
                onClick = { viewModel.onScheduleNewWorkoutClicked() },
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_access_time), // TODO: create ic_schedule
                    contentDescription = stringResource(Res.string.workout_overview_content_description_schedule_new_workout_fab)
                )
            }
            FloatingActionButton(
                onClick = { viewModel.onStartNewWorkoutClicked() },
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_add),
                    contentDescription = stringResource(Res.string.workout_overview_content_description_start_new_workout_fab)
                )
            }
        }
    }
}

@OptIn(ExperimentalUuidApi::class)
@Composable
fun WorkoutSessionItem(
    workoutSession: WorkoutExecutionSession, // Updated type
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
                text = workoutSession.name,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))

            if (workoutSession.scheduledTimestamp != null && workoutSession.startTimestamp == null) {
                // Scheduled workout
                val scheduledDateTime = workoutSession.scheduledTimestamp.toLocalDateTime(TimeZone.currentSystemDefault())
                Text(
                    text = "Scheduled: ${scheduledDateTime.date} at ${scheduledDateTime.time.hour.toString().padStart(2, '0')}:${scheduledDateTime.time.minute.toString().padStart(2, '0')}",
                    style = MaterialTheme.typography.bodySmall
                )
            } else if (workoutSession.startTimestamp != null) {
                // Past or In-progress workout
                val startDateTime = workoutSession.startTimestamp.toLocalDateTime(TimeZone.currentSystemDefault())
                Text(
                    text = "Started: ${startDateTime.date} at ${startDateTime.time.hour.toString().padStart(2, '0')}:${startDateTime.time.minute.toString().padStart(2, '0')}",
                    style = MaterialTheme.typography.bodySmall
                )
                workoutSession.endTimestamp?.let {
                    val durationMinutes = (it.epochSeconds - workoutSession.startTimestamp.epochSeconds) / 60
                    Text(
                        text = "Duration: $durationMinutes mins",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            } else {
                // Fallback, should ideally not happen if logic is correct
                 Text(
                    text = "Status: Unknown",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // You can add more details here, e.g., number of exercises, if available
            if (workoutSession.performedSets.isNotEmpty()){
                 Text(
                    text = "Exercises: ${workoutSession.performedSets.distinctBy { it.exerciseId }.size}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
