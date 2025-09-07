package org.darthacheron.fitbe.exercises.exercises

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.ic_add
import fitbe.composeapp.generated.resources.ic_cancel
import fitbe.composeapp.generated.resources.ic_delete
import fitbe.composeapp.generated.resources.ic_edit
import fitbe.composeapp.generated.resources.ic_remove
import fitbe.composeapp.generated.resources.ic_reset_default
import fitbe.composeapp.generated.resources.ic_save
import org.darthacheron.fitbe.exercises.equipment.getEquipmentName 
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalMaterial3Api::class, ExperimentalUuidApi::class, ExperimentalLayoutApi::class)
@Composable
fun ExerciseDetailView(
    exerciseId: Uuid?,
    viewModel: ExerciseDetailViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    val availableMuscleGroups by viewModel.availableMuscleGroups.collectAsState()
    val scrollState = rememberScrollState()

    var isInEditMode by remember {
        mutableStateOf(exerciseId == null)
    }

    LaunchedEffect(exerciseId) {
        viewModel.loadExercise(exerciseId?.toString())
    }

    LaunchedEffect(Unit) {
        viewModel.updateTopBarConfig()
        viewModel.saveCompletedEvent.collect {
            isInEditMode = false 
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxSize()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 72.dp) 
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
                        label = { Text("Exercise Name") }, // TODO: SR
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
                        label = { Text("Guide") }, // TODO: SR
                        modifier = Modifier.fillMaxWidth().height(120.dp), 
                        readOnly = !isInEditMode
                    )

                    // Target Muscle Groups
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text("Target Muscle Groups", style = MaterialTheme.typography.titleMedium) // TODO: SR
                        Spacer(modifier = Modifier.height(8.dp))
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            uiState.targetMuscleGroups.forEach { muscleGroup ->
                                InputChip(
                                    selected = false, // Not a selectable chip, just for display
                                    onClick = { /* Could be used for something else if needed */ },
                                    label = { Text(muscleGroup.name) }, // TODO: Consider localized names
                                    trailingIcon = {
                                        if (isInEditMode) {
                                            IconButton(onClick = { viewModel.removeMuscleGroup(muscleGroup) }, modifier = Modifier.size(18.dp)) {
                                                Icon(painterResource(Res.drawable.ic_remove), "Remove muscle group") // TODO: SR
                                            }
                                        }
                                    },
                                    enabled = isInEditMode
                                )
                            }

                            if (isInEditMode && availableMuscleGroups.isNotEmpty()) {
                                var muscleGroupDropdownExpanded by remember { mutableStateOf(false) }
                                ExposedDropdownMenuBox(
                                    expanded = muscleGroupDropdownExpanded,
                                    onExpandedChange = { muscleGroupDropdownExpanded = !muscleGroupDropdownExpanded }
                                ) {
                                    TextButton(onClick = { muscleGroupDropdownExpanded = true }, modifier = Modifier.menuAnchor()) {
                                        Icon(painterResource(Res.drawable.ic_add), contentDescription = "Add Muscle Group", modifier = Modifier.size(18.dp)) // TODO: SR
                                        Spacer(Modifier.size(8.dp))
                                        Text("Add Group") // TODO: SR
                                    }
                                    ExposedDropdownMenu(
                                        expanded = muscleGroupDropdownExpanded,
                                        onDismissRequest = { muscleGroupDropdownExpanded = false }
                                    ) {
                                        availableMuscleGroups.forEach { muscleGroup ->
                                            DropdownMenuItem(
                                                text = { Text(muscleGroup.name) }, // TODO: SR for muscle group names
                                                onClick = {
                                                    viewModel.addMuscleGroup(muscleGroup)
                                                    muscleGroupDropdownExpanded = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Equipment List Display & Edit Button
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text("Required Equipment", style = MaterialTheme.typography.titleMedium) // TODO: SR
                        // TODO: Implement similar Chip + Add Dropdown for Equipment
                        uiState.equipmentList.forEach {
                            Text(text = getEquipmentName(it.name, it.default))
                        }
                        if (isInEditMode) {
                           // Button(onClick = { /* TODO: Implement equipment selection */ }) {
                           //     Text("Edit Equipment") // TODO: SR
                           // }
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

        // FABs ... (rest of the code remains the same)
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
