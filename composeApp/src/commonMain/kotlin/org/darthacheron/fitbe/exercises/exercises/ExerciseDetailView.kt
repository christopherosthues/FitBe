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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
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
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.exercise_detail_content_description_add_muscle_groups
import fitbe.composeapp.generated.resources.exercise_detail_content_description_cancel
import fitbe.composeapp.generated.resources.exercise_detail_content_description_delete
import fitbe.composeapp.generated.resources.exercise_detail_content_description_edit
import fitbe.composeapp.generated.resources.exercise_detail_content_description_remove_muscle_groups
import fitbe.composeapp.generated.resources.exercise_detail_content_description_reset_to_default
import fitbe.composeapp.generated.resources.exercise_detail_content_description_save
import fitbe.composeapp.generated.resources.exercise_detail_guide
import fitbe.composeapp.generated.resources.exercise_detail_name
import fitbe.composeapp.generated.resources.exercise_detail_target_muscle_groups
import fitbe.composeapp.generated.resources.ic_add
import fitbe.composeapp.generated.resources.ic_cancel
import fitbe.composeapp.generated.resources.ic_delete
import fitbe.composeapp.generated.resources.ic_edit
import fitbe.composeapp.generated.resources.ic_photo_library
import fitbe.composeapp.generated.resources.ic_remove
import fitbe.composeapp.generated.resources.ic_reset_default
import fitbe.composeapp.generated.resources.ic_save
import fitbe.composeapp.generated.resources.exercise_detail_content_description_select_image
import fitbe.composeapp.generated.resources.exercise_detail_content_description_default_equipment
import fitbe.composeapp.generated.resources.exercise_detail_content_description_image
import fitbe.composeapp.generated.resources.exercise_detail_content_description_remove_image
import io.github.vinceglb.filekit.absolutePath
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import org.darthacheron.fitbe.components.ImagePlaceholder
import org.darthacheron.fitbe.components.ImageWithDefault
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

    val galleryLauncher = rememberFilePickerLauncher(
        type = FileKitType.Image,
        mode = FileKitMode.Single,
        onResult = {
            if (it != null && uiState.isEditing) {
                viewModel.onImageUriChange(it.absolutePath())
            }
        }
    )

    LaunchedEffect(exerciseId) {
        viewModel.loadExercise(exerciseId?.toString())
    }

    LaunchedEffect(Unit) {
        viewModel.updateTopBarConfig()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxSize()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 72.dp) 
        ) {
            if (uiState.isLoading && uiState.exerciseId != null && !uiState.isEditing) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box {
                        if (uiState.imageUri != null) {
                            ImageWithDefault(
                                imageUri = uiState.imageUri,
                                imageResource = getExerciseImage(uiState.imageUri, uiState.default),
                                default = uiState.default,
                                contentDescription = stringResource(Res.string.exercise_detail_content_description_image),
                                defaultContentDescription = stringResource(Res.string.exercise_detail_content_description_default_equipment),
                                modifier = Modifier.size(256.dp).align(Alignment.Center)
                            )
                            if (uiState.isEditing) {
                                IconButton(
                                    onClick = { viewModel.onImageUriChange(null) },
                                    modifier = Modifier.align(Alignment.TopEnd),
                                    enabled = uiState.isEditing
                                ) {
                                    Icon(
                                        painter = painterResource(Res.drawable.ic_remove),
                                        contentDescription = stringResource(Res.string.exercise_detail_content_description_remove_image)
                                    )
                                }
                            }
                        } else {
                            ImagePlaceholder(
                                isEditing = uiState.isEditing,
                                default = uiState.default,
                                contentDescription = stringResource(Res.string.exercise_detail_content_description_default_equipment),
                            )
                        }
                        if (uiState.isEditing) {
                            IconButton(
                                onClick = { galleryLauncher.launch() },
                                modifier = Modifier.align(Alignment.BottomStart),
                                enabled = uiState.isEditing
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_photo_library),
                                    contentDescription = stringResource(Res.string.exercise_detail_content_description_select_image)
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = getExerciseName(uiState.name, uiState.default),
                        onValueChange = { if (uiState.isEditing) viewModel.onNameChange(it) },
                        label = { Text(text = stringResource(Res.string.exercise_detail_name)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        readOnly = !uiState.isEditing,
                        isError = uiState.error.hasNameError,
                        supportingText = {
                            if (uiState.error.hasNameError) Text(text = stringResource(uiState.error.nameError!!))
                        }
                    )

                    OutlinedTextField(
                        value = getExerciseGuide(uiState.guide, uiState.default),
                        onValueChange = { if (uiState.isEditing) viewModel.onGuideChange(it) },
                        label = { Text(text = stringResource(Res.string.exercise_detail_guide)) },
                        modifier = Modifier.fillMaxWidth().height(120.dp), 
                        readOnly = !uiState.isEditing,
                        isError = uiState.error.hasGuideError,
                        supportingText = {
                            if (uiState.error.hasGuideError) Text(text = stringResource(uiState.error.guideError!!))
                        }
                    )

                    // Target Muscle Groups
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(Res.string.exercise_detail_target_muscle_groups),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            uiState.targetMuscleGroups.forEach { muscleGroup ->
                                InputChip(
                                    selected = false,
                                    onClick = {  },
                                    label = { Text(text = stringResource(muscleGroup.localizedString()))  },
                                    trailingIcon = {
                                        if (uiState.isEditing) {
                                            IconButton(onClick = { viewModel.removeMuscleGroup(muscleGroup) }, modifier = Modifier.size(18.dp)) {
                                                Icon(
                                                    painterResource(Res.drawable.ic_remove),
                                                    stringResource(Res.string.exercise_detail_content_description_remove_muscle_groups)
                                                )
                                            }
                                        }
                                    },
                                    enabled = uiState.isEditing,
                                    colors = InputChipDefaults.inputChipColors()
                                )
                            }

                            if (uiState.isEditing && availableMuscleGroups.isNotEmpty()) {
                                var muscleGroupDropdownExpanded by remember { mutableStateOf(false) }
                                ExposedDropdownMenuBox(
                                    expanded = muscleGroupDropdownExpanded,
                                    onExpandedChange = { muscleGroupDropdownExpanded = !muscleGroupDropdownExpanded },
                                ) {
                                    TextButton(onClick = { muscleGroupDropdownExpanded = true }, modifier = Modifier.menuAnchor(
                                        MenuAnchorType.SecondaryEditable)) {
                                        Icon(
                                            painterResource(Res.drawable.ic_add),
                                            contentDescription = stringResource(Res.string.exercise_detail_content_description_add_muscle_groups),
                                            modifier = Modifier.size(18.dp)
                                        )
//                                        Spacer(Modifier.size(8.dp))
//                                        Text("Add Group") // TODO: SR
                                    }
                                    ExposedDropdownMenu(
                                        expanded = muscleGroupDropdownExpanded,
                                        onDismissRequest = { muscleGroupDropdownExpanded = false }
                                    ) {
                                        availableMuscleGroups.forEach { muscleGroup ->
                                            DropdownMenuItem(
                                                text = { Text(text = stringResource(muscleGroup.localizedString())) },
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
                        if (uiState.error.hasMuscleGroupError) {
                            Text(
                                text = stringResource(uiState.error.muscleGroupError!!),
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.titleSmall,
                            )
                        }
                    }

                    // Equipment List Display & Edit Button
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text("Required Equipment", style = MaterialTheme.typography.titleMedium) // TODO: SR
                        // TODO: Implement similar Chip + Add Dropdown for Equipment
                        uiState.equipmentList.forEach {
                            Text(text = getEquipmentName(it.name, it.default))
                        }
                        if (uiState.isEditing) {
                           // Button(onClick = { /* TODO: Implement equipment selection */ }) {
                           //     Text("Edit Equipment") // TODO: SR
                           // }
                        }
                    }

                    if (uiState.error.hasGeneralError) {
                        Text(
                            text = stringResource(uiState.error.generalError!!),
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
            visible = uiState.isEditing && uiState.exerciseId != null && uiState.default && uiState.isModifiedFromPersistedDefault,
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            FloatingActionButton(
                onClick = { viewModel.resetExerciseToDefault() },
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_reset_default),
                    contentDescription = stringResource(Res.string.exercise_detail_content_description_reset_to_default)
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
            AnimatedVisibility(visible = !uiState.isEditing && uiState.exerciseId != null && !uiState.default) {
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
                        contentDescription = stringResource(Res.string.exercise_detail_content_description_delete)
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
            AnimatedVisibility(visible = uiState.isEditing && uiState.exerciseId != null) {
                FloatingActionButton(
                    onClick = {
                        viewModel.loadExercise(uiState.exerciseId.toString()) // Reload original data
                        viewModel.setIsEditing(false)
                    },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_cancel),
                        contentDescription = stringResource(Res.string.exercise_detail_content_description_cancel)
                    )
                }
            }

            // Save FAB (if in edit mode)
            AnimatedVisibility(visible = uiState.isEditing) {
                FloatingActionButton(
                    onClick = {
                        if (!uiState.isLoading && !uiState.error.hasError) {
                            viewModel.saveExercise()
                        }
                    },
                    containerColor = if (!uiState.isLoading && !uiState.error.hasError) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_save),
                        contentDescription = stringResource(Res.string.exercise_detail_content_description_save)
                    )
                }
            }

            // Edit FAB (if not in edit mode and exercise exists)
            AnimatedVisibility(visible = !uiState.isEditing && uiState.exerciseId != null) {
                FloatingActionButton(
                    onClick = {
                        viewModel.setIsEditing(true)
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_edit),
                        contentDescription = stringResource(Res.string.exercise_detail_content_description_edit)
                    )
                }
            }
        }
    }
}
