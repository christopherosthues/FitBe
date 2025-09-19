package org.darthacheron.fitbe.workouts.templates

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.ic_photo_library // Added
import fitbe.composeapp.generated.resources.ic_remove // Added
// TODO: Add specific string resources for workout template images if needed
import fitbe.composeapp.generated.resources.exercise_detail_content_description_default_equipment // Placeholder
import fitbe.composeapp.generated.resources.exercise_detail_content_description_edit
import fitbe.composeapp.generated.resources.exercise_detail_content_description_image // Placeholder
import fitbe.composeapp.generated.resources.exercise_detail_content_description_remove_image // Placeholder
import fitbe.composeapp.generated.resources.exercise_detail_content_description_select_image // Placeholder
import fitbe.composeapp.generated.resources.ic_arrow_forward
import io.github.vinceglb.filekit.absolutePath
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import org.darthacheron.fitbe.components.ImagePlaceholder // Added
import org.darthacheron.fitbe.components.ImageWithDefault
import org.darthacheron.fitbe.workouts.exercises.getExerciseImage
import org.darthacheron.fitbe.workouts.exercises.getExerciseName
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalMaterial3Api::class, ExperimentalUuidApi::class)
@Composable
fun WorkoutTemplateDetailView(
    workoutTemplateId: Uuid?,
    viewModel: WorkoutTemplateDetailViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    val galleryLauncher = rememberFilePickerLauncher(
        type = FileKitType.Image,
        mode = FileKitMode.Single,
        onResult = {
            if (it != null && uiState.isEditing) {
                viewModel.onImageUriChange(it.absolutePath())
            }
        }
    )

    LaunchedEffect(workoutTemplateId) {
        viewModel.loadWorkoutTemplate(workoutTemplateId)
        viewModel.updateTopBarConfig()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (uiState.error != null) {
            Text(
                text = stringResource(uiState.error!!),
                modifier = Modifier.align(Alignment.Center).padding(16.dp),
                color = MaterialTheme.colorScheme.error
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(bottom = 72.dp, start = 16.dp, end = 16.dp, top = 16.dp)
            ) {
                // Top static info
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box {
                        if (uiState.imageUri != null) {
                            ImageWithDefault(
                                imageUri = uiState.imageUri,
                                imageResource = getWorkoutImage(uiState.imageUri, uiState.default),
                                default = uiState.default,
                                contentDescription = stringResource(Res.string.exercise_detail_content_description_image), // TODO: Change to workout_template_content_description_image
                                defaultContentDescription = stringResource(Res.string.exercise_detail_content_description_default_equipment), // TODO: Change to workout_template_content_description_default_image
                                modifier = Modifier.size(150.dp) // Adjusted size for template image
                            )
                            if (uiState.isEditing) {
                                IconButton(
                                    onClick = { viewModel.onImageUriChange(null) },
                                    modifier = Modifier.align(Alignment.TopEnd),
                                    enabled = uiState.isEditing
                                ) {
                                    Icon(
                                        painter = painterResource(Res.drawable.ic_remove),
                                        contentDescription = stringResource(Res.string.exercise_detail_content_description_remove_image) // TODO: Change to workout_template_content_description_remove_image
                                    )
                                }
                            }
                        } else {
                            ImagePlaceholder(
                                isEditing = uiState.isEditing,
                                default = uiState.default, // You might want a specific placeholder for templates
                                contentDescription = stringResource(Res.string.exercise_detail_content_description_default_equipment), // TODO: Change to workout_template_content_description_default_image
                            )
                        }
                        if (uiState.isEditing) {
                            IconButton(
                                onClick = { galleryLauncher.launch() },
                                modifier = Modifier.align(Alignment.BottomEnd), // Changed to BottomEnd for better placement
                                enabled = uiState.isEditing
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_photo_library),
                                    contentDescription = stringResource(Res.string.exercise_detail_content_description_select_image) // TODO: Change to workout_template_content_description_select_image
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = getWorkoutName(uiState.name, uiState.default),
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    if (uiState.description != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = uiState.description!!,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    // TODO: Determine what this chip should represent based on actual data
                    AssistChip(
                        onClick = { /* Non-functional for this dummy view */ },
                        label = { Text("Workout Type/Focus") }, // Dummy info
                        enabled = false,
                        colors = AssistChipDefaults.assistChipColors(
                            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                var selectedTabIndex by remember { mutableStateOf(0) }
                val tabs = listOf("Warmup", "Exercises")

                PrimaryTabRow(
                    selectedTabIndex = selectedTabIndex,
                    modifier = Modifier.fillMaxWidth().padding(horizontal=16.dp)
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { Text(title) }
                        )
                    }
                }

                when (selectedTabIndex) {
                    0 -> ExercisesTabContent(uiState.warmups, uiState.isEditing, Modifier.fillMaxSize())
                    1 -> ExercisesTabContent(uiState.workouts, uiState.isEditing, Modifier.fillMaxSize())
                    else -> ExercisesTabContent(uiState.warmups, uiState.isEditing, Modifier.fillMaxSize())
                }
            }

//            TextButton(
//                onClick = { /* TODO: Implement start workout action, perhaps call viewModel.startWorkout() */ },
//                modifier = Modifier
//                    .align(Alignment.BottomCenter)
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp, vertical = 16.dp)
//            ) {
//                Text("Start Workout") // TODO: Make string resource
//            }
        }
    }
}

@Composable
private fun ExercisesTabContent(
    exercises: List<WorkoutTemplateExerciseWithDetails>, isEditing: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        exercises.forEach { exerciseDetail ->
            // Box for horizontal padding removed, add padding to ExerciseItemCard if needed
            ExerciseItemCard(exercise = exerciseDetail, isEditing = isEditing)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExerciseItemCard(exercise: WorkoutTemplateExerciseWithDetails, isEditing: Boolean) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ImageWithDefault(
                imageUri = exercise.exercise.imageUri,
                imageResource = getExerciseImage(exercise.exercise.imageUri, exercise.exercise.default),
                default = exercise.exercise.default,
                contentDescription = "${getExerciseName(exercise.exercise.name, exercise.exercise.default)} image",
                defaultContentDescription = "Default exercise image",
                modifier = Modifier.size(56.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = getExerciseName(exercise.exercise.name, exercise.exercise.default),
                    style = MaterialTheme.typography.titleMedium
                )
                if (exercise.notes != null && exercise.notes.isNotBlank()){
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = exercise.notes,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(exercise.sets) { setInfo ->
                        AssistChip(
                            onClick = { /* Non-interactive for now */ },
                            label = { Text("${setInfo.setOrder}. Reps: ${setInfo.targetRepetitions ?: "-"}, Weight: ${setInfo.targetWeightKg ?: "-"} kg, Time: ${setInfo.targetDurationSeconds ?: "-"}s") }
                        )
                    }
                }
            }

            if (isEditing) {
                Spacer(modifier = Modifier.width(12.dp))
                IconButton(
                    onClick = { /* TODO: Implement drag-to-reorder action or other edit actions */ }
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_arrow_forward),
                        contentDescription = stringResource(Res.string.exercise_detail_content_description_edit, getExerciseName(exercise.exercise.name, exercise.exercise.default)), // TODO: Make specific string resource
                    )
                }
            }
        }
    }
}
