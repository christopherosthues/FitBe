package org.darthacheron.fitbe.exercises.exercises

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.ic_cancel
import fitbe.composeapp.generated.resources.ic_delete
import fitbe.composeapp.generated.resources.ic_edit
import fitbe.composeapp.generated.resources.ic_reset_default
import fitbe.composeapp.generated.resources.ic_save
import org.darthacheron.fitbe.exercises.equipment.getEquipmentName // Assuming this can be used or adapted
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

// TODO: Add actual string resources

@OptIn(ExperimentalMaterial3Api::class, ExperimentalUuidApi::class)
@Composable
fun ExerciseDetailView(
    exerciseId: Uuid?,
    viewModel: ExerciseDetailViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    // Default to edit mode if it's a new exercise (exerciseId is null)
    // or if the exercise is being loaded (uiState.isLoading and uiState.exerciseId != null)
    // or if uiState.isEditing is true (e.g. after failing to load, or explicitly set by VM)
    var isInEditMode by remember(exerciseId, uiState.isLoading, uiState.isEditing, uiState.exerciseId) {
        mutableStateOf(exerciseId == null || uiState.isEditing)
    }

    LaunchedEffect(exerciseId) {
        viewModel.loadExercise(exerciseId?.toString())
        // After loading, ViewModel's uiState.isEditing will reflect the correct edit mode for existing exercises.
        // For new exercises, exerciseId is null, loadExercise sets isEditing = true.
    }

    LaunchedEffect(Unit) {
        viewModel.updateTopBarConfig()
        viewModel.saveCompletedEvent.collect {
            isInEditMode = false // Exit edit mode after successful save
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxSize()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 72.dp) // padding for FABs
        ) {
            if (uiState.isLoading && uiState.exerciseId != null && !isInEditMode) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = uiState.name,
                        onValueChange = { if (isInEditMode) viewModel.onNameChange(it) },
                        label = { Text("Exercise Name") }, // TODO: String resource
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        readOnly = !isInEditMode,
                        isError = uiState.error != null && uiState.name.isBlank(),
                        supportingText = {
                            if (uiState.error != null && uiState.name.isBlank()) Text(uiState.error!!)
                        }
                    )

                    OutlinedTextField(
                        value = uiState.guide,
                        onValueChange = { if (isInEditMode) viewModel.onGuideChange(it) },
                        label = { Text("Guide") }, // TODO: String resource
                        modifier = Modifier.fillMaxWidth().height(120.dp), // Allow multiple lines
                        readOnly = !isInEditMode
                    )

                    // Target Muscle Groups Display & Edit Button
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text("Target Muscle Groups", style = MaterialTheme.typography.titleMedium) // TODO: SR
                        Text(uiState.targetMuscleGroups.joinToString { it.name }) // TODO: SR for each muscle group
                        if (isInEditMode) {
                            Button(onClick = { /* TODO: Implement muscle group selection */ }) {
                                Text("Edit Muscle Groups") // TODO: SR
                            }
                        }
                    }

                    // Equipment List Display & Edit Button
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text("Required Equipment", style = MaterialTheme.typography.titleMedium) // TODO: SR
                        uiState.equipmentList.forEach {
                            Text(text = getEquipmentName(it.name, it.default))
                        }
                        if (isInEditMode) {
                            Button(onClick = { /* TODO: Implement equipment selection */ }) {
                                Text("Edit Equipment") // TODO: SR
                            }
                        }
                    }

                    if (uiState.error != null && !(uiState.name.isBlank() && isInEditMode)) {
                        Text(
                            text = uiState.error!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }

        // FAB for Reset to Default (Top End)
        AnimatedVisibility(
            visible = isInEditMode && uiState.exerciseId != null && uiState.default && uiState.isModifiedFromPersistedDefault,
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            FloatingActionButton(
                onClick = { viewModel.resetExerciseToDefault() },
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_reset_default),
                    contentDescription = "Reset to Default" // TODO: String Resource
                )
            }
        }

        // FABs for Delete, Cancel, Save/Edit (Bottom)
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Delete FAB (Bottom Start)
            AnimatedVisibility(visible = !isInEditMode && uiState.exerciseId != null && !uiState.default) {
                FloatingActionButton(
                    onClick = {
                        if (!uiState.isLoading) {
                            viewModel.deleteExercise()
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_delete),
                        contentDescription = "Delete Exercise" // TODO: String Resource
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Cancel FAB
            AnimatedVisibility(visible = isInEditMode && uiState.exerciseId != null) {
                FloatingActionButton(
                    onClick = {
                        viewModel.loadExercise(uiState.exerciseId.toString()) // Reload original data
                        isInEditMode = false
                    },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_cancel),
                        contentDescription = "Cancel" // TODO: String Resource
                    )
                }
            }

            // Save FAB (if in edit mode)
            AnimatedVisibility(visible = isInEditMode) {
                FloatingActionButton(
                    onClick = {
                        if (!uiState.isLoading) {
                            viewModel.saveExercise()
                        }
                    },
                    containerColor = if (!uiState.isLoading && uiState.name.isNotBlank()) MaterialTheme.colorScheme.primary else Color.Gray,
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_save),
                        contentDescription = "Save Exercise" // TODO: String Resource
                    )
                }
            }

            // Edit FAB (if not in edit mode and exercise exists)
            AnimatedVisibility(visible = !isInEditMode && uiState.exerciseId != null) {
                FloatingActionButton(
                    onClick = {
                        isInEditMode = true
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_edit),
                        contentDescription = "Edit Exercise" // TODO: String Resource
                    )
                }
            }
        }
    }
}
