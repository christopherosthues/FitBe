package org.darthacheron.fitbe.workouts.exercises

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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.exercise_detail_add_equipment
import fitbe.composeapp.generated.resources.exercise_detail_add_muscle_groups
import fitbe.composeapp.generated.resources.exercise_detail_add_recommended_for
import fitbe.composeapp.generated.resources.exercise_detail_content_description_add_equipment
import fitbe.composeapp.generated.resources.exercise_detail_content_description_add_muscle_groups
import fitbe.composeapp.generated.resources.exercise_detail_content_description_add_recommended_for
import fitbe.composeapp.generated.resources.exercise_detail_content_description_cancel
import fitbe.composeapp.generated.resources.exercise_detail_content_description_default_exercise
import fitbe.composeapp.generated.resources.exercise_detail_content_description_delete
import fitbe.composeapp.generated.resources.exercise_detail_content_description_edit
import fitbe.composeapp.generated.resources.exercise_detail_content_description_image
import fitbe.composeapp.generated.resources.exercise_detail_content_description_remove_equipment
import fitbe.composeapp.generated.resources.exercise_detail_content_description_remove_image
import fitbe.composeapp.generated.resources.exercise_detail_content_description_remove_muscle_groups
import fitbe.composeapp.generated.resources.exercise_detail_content_description_remove_recommended_for
import fitbe.composeapp.generated.resources.exercise_detail_content_description_reset_to_default
import fitbe.composeapp.generated.resources.exercise_detail_content_description_save
import fitbe.composeapp.generated.resources.exercise_detail_content_description_select_image
import fitbe.composeapp.generated.resources.exercise_detail_content_description_start_workout
import fitbe.composeapp.generated.resources.exercise_detail_guide
import fitbe.composeapp.generated.resources.exercise_detail_name
import fitbe.composeapp.generated.resources.exercise_detail_recommended_for
import fitbe.composeapp.generated.resources.exercise_detail_required_equipment
import fitbe.composeapp.generated.resources.exercise_detail_start_workout
import fitbe.composeapp.generated.resources.exercise_detail_target_muscle_groups
import fitbe.composeapp.generated.resources.exercise_type_label
import fitbe.composeapp.generated.resources.ic_add
import fitbe.composeapp.generated.resources.ic_cancel
import fitbe.composeapp.generated.resources.ic_delete
import fitbe.composeapp.generated.resources.ic_edit
import fitbe.composeapp.generated.resources.ic_photo_library
import fitbe.composeapp.generated.resources.ic_remove
import fitbe.composeapp.generated.resources.ic_reset_default
import fitbe.composeapp.generated.resources.ic_save
import io.github.vinceglb.filekit.absolutePath
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import kotlinx.coroutines.launch
import org.darthacheron.fitbe.components.ImagePlaceholder
import org.darthacheron.fitbe.components.ImageWithDefault
import org.darthacheron.fitbe.workouts.equipment.TrainingEquipment
import org.darthacheron.fitbe.workouts.equipment.getEquipmentName
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

// TODO: Reset button and cancel button not correctly displayed (All views)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalUuidApi::class, ExperimentalLayoutApi::class)
@Composable
fun ExerciseDetailView(
    exerciseId: Uuid?,
    viewModel: ExerciseDetailViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val availableMuscleGroups by viewModel.availableMuscleGroups.collectAsState()
    val availableEquipments by viewModel.availableEquipments.collectAsState()
    val availableRecommendedForItems by viewModel.availableRecommendedFor.collectAsState()
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val galleryLauncher =
        rememberFilePickerLauncher(
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

    val generalErrorResId = uiState.error.generalError
    val generalErrorMessage =
        if (generalErrorResId != null && uiState.error.hasGeneralError) {
            stringResource(generalErrorResId)
        } else {
            null
        }

    LaunchedEffect(generalErrorMessage) {
        if (generalErrorMessage != null) {
            scope.launch {
                snackbarHostState.showSnackbar(generalErrorMessage)
                viewModel.clearGeneralError()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier =
                Modifier
                    .verticalScroll(scrollState)
                    .fillMaxSize()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 128.dp)
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
                                contentDescription =
                                    stringResource(Res.string.exercise_detail_content_description_image),
                                defaultContentDescription =
                                    stringResource(Res.string.exercise_detail_content_description_default_exercise),
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
                                        contentDescription =
                                            stringResource(Res.string.exercise_detail_content_description_remove_image)
                                    )
                                }
                            }
                        } else {
                            ImagePlaceholder(
                                isEditing = uiState.isEditing,
                                default = uiState.default,
                                contentDescription =
                                    stringResource(Res.string.exercise_detail_content_description_default_exercise)
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
                                    contentDescription =
                                        stringResource(Res.string.exercise_detail_content_description_select_image)
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

                    ExerciseTypeSelectionView(uiState, viewModel)
                    TargetMuscleGroupListView(uiState, viewModel, availableMuscleGroups)
                    RecommendedForListView(uiState, viewModel, availableRecommendedForItems)
                    EquipmentListView(uiState, viewModel, availableEquipments)
                    Spacer(modifier = Modifier.height(56.dp))
                }
            }
        }

        AnimatedVisibility(
            visible = uiState.canResetToDefault,
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

        Row(
            modifier =
                Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedVisibility(visible = uiState.canDelete) {
                FloatingActionButton(
                    onClick = {
                        if (!uiState.isLoading) {
                            viewModel.deleteExercise()
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_delete),
                        contentDescription = stringResource(Res.string.exercise_detail_content_description_delete)
                    )
                }
            }
        }

        Row(
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedVisibility(visible = uiState.canCancelEditing) {
                FloatingActionButton(
                    onClick = {
                        viewModel.loadExercise(uiState.exerciseId.toString())
                        viewModel.setIsEditing(false)
                    },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_cancel),
                        contentDescription = stringResource(Res.string.exercise_detail_content_description_cancel)
                    )
                }
            }

            AnimatedVisibility(visible = uiState.isEditing) {
                FloatingActionButton(
                    onClick = {
                        if (!uiState.isLoading && !uiState.error.hasError) {
                            viewModel.saveExercise()
                        }
                    },
                    containerColor =
                        if (!uiState.isLoading && !uiState.error.hasError) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                        }
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_save),
                        contentDescription = stringResource(Res.string.exercise_detail_content_description_save)
                    )
                }
            }

            AnimatedVisibility(visible = uiState.canEdit) {
                FloatingActionButton(
                    onClick = {
                        viewModel.setIsEditing(true)
                    },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_edit),
                        contentDescription = stringResource(Res.string.exercise_detail_content_description_edit)
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = uiState.canStartWorkout,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp)
        ) {
            val exerciseDisplayName = getExerciseName(uiState.name, uiState.default)
            val startWorkoutContentDescription =
                stringResource(Res.string.exercise_detail_content_description_start_workout, exerciseDisplayName)
            Button(
                onClick = { viewModel.navigateToExerciseExecution() },
                modifier =
                    Modifier
                        .fillMaxWidth(0.6f)
                        .semantics {
                            this.contentDescription = startWorkoutContentDescription
                        }
            ) {
                Text(stringResource(Res.string.exercise_detail_start_workout))
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 80.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExerciseTypeSelectionView(
    uiState: ExerciseDetailUiState,
    viewModel: ExerciseDetailViewModel
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(Res.string.exercise_type_label),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        var exerciseTypeDropdownExpanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = exerciseTypeDropdownExpanded,
            onExpandedChange = {
                exerciseTypeDropdownExpanded = !exerciseTypeDropdownExpanded && uiState.isEditing
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = stringResource(uiState.exerciseType.toStringResource()),
                onValueChange = {},
                label = { Text(stringResource(Res.string.exercise_type_label)) },
                readOnly = true,
                trailingIcon = {
                    if (uiState.isEditing) {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = exerciseTypeDropdownExpanded
                        )
                    }
                },
                modifier = Modifier.menuAnchor(MenuAnchorType.SecondaryEditable).fillMaxWidth(),
                enabled = uiState.isEditing,
                isError = uiState.error.hasExerciseTypeError
            )
            ExposedDropdownMenu(
                expanded = exerciseTypeDropdownExpanded && uiState.isEditing,
                onDismissRequest = { exerciseTypeDropdownExpanded = false }
            ) {
                ExerciseType.entries.filter { it != ExerciseType.UNKNOWN }.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(stringResource(type.toStringResource())) },
                        onClick = {
                            viewModel.onExerciseTypeChange(type)
                            exerciseTypeDropdownExpanded = false
                        }
                    )
                }
            }
        }
        if (uiState.error.hasExerciseTypeError) {
            Text(
                text = stringResource(uiState.error.exerciseTypeError!!),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TargetMuscleGroupListView(
    uiState: ExerciseDetailUiState,
    viewModel: ExerciseDetailViewModel,
    availableMuscleGroups: List<MuscleGroup>
) {
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
                    onClick = { },
                    label = { Text(text = stringResource(muscleGroup.toStringResource())) },
                    trailingIcon = {
                        if (uiState.isEditing) {
                            IconButton(
                                onClick = { viewModel.removeMuscleGroup(muscleGroup) },
                                modifier = Modifier.size(18.dp)
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_remove),
                                    contentDescription =
                                        stringResource(
                                            Res.string.exercise_detail_content_description_remove_muscle_groups
                                        )
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
                    onExpandedChange = { muscleGroupDropdownExpanded = !muscleGroupDropdownExpanded }
                ) {
                    TextButton(
                        onClick = { muscleGroupDropdownExpanded = true },
                        modifier = Modifier.menuAnchor(MenuAnchorType.SecondaryEditable)
                    ) {
                        Icon(
                            painterResource(Res.drawable.ic_add),
                            contentDescription =
                                stringResource(
                                    Res.string.exercise_detail_content_description_add_muscle_groups
                                ),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.size(8.dp))
                        Text(stringResource(Res.string.exercise_detail_add_muscle_groups))
                    }
                    ExposedDropdownMenu(
                        expanded = muscleGroupDropdownExpanded,
                        onDismissRequest = { muscleGroupDropdownExpanded = false }
                    ) {
                        availableMuscleGroups.forEach { muscleGroup ->
                            DropdownMenuItem(
                                text = { Text(text = stringResource(muscleGroup.toStringResource())) },
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
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecommendedForListView(
    uiState: ExerciseDetailUiState,
    viewModel: ExerciseDetailViewModel,
    availableRecommendedForItems: List<RecommendedFor>
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(Res.string.exercise_detail_recommended_for),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            uiState.recommendedFor.forEach { recommendedFor ->
                InputChip(
                    selected = false,
                    onClick = { },
                    label = { Text(text = stringResource(recommendedFor.toStringResource())) },
                    trailingIcon = {
                        if (uiState.isEditing) {
                            IconButton(
                                onClick = { viewModel.removeRecommendedFor(recommendedFor) },
                                modifier = Modifier.size(18.dp)
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_remove),
                                    contentDescription =
                                        stringResource(
                                            Res.string.exercise_detail_content_description_remove_recommended_for
                                        )
                                )
                            }
                        }
                    },
                    enabled = uiState.isEditing,
                    colors = InputChipDefaults.inputChipColors()
                )
            }

            if (uiState.isEditing && availableRecommendedForItems.isNotEmpty()) {
                var recommendedForDropdownExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = recommendedForDropdownExpanded,
                    onExpandedChange = {
                        recommendedForDropdownExpanded = !recommendedForDropdownExpanded
                    },
                ) {
                    TextButton(
                        onClick = { recommendedForDropdownExpanded = true },
                        modifier = Modifier.menuAnchor(
                            MenuAnchorType.SecondaryEditable
                        )
                    ) {
                        Icon(
                            painterResource(Res.drawable.ic_add),
                            contentDescription =
                                stringResource(
                                    Res.string.exercise_detail_content_description_add_recommended_for
                                ),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.size(8.dp))
                        Text(text = stringResource(Res.string.exercise_detail_add_recommended_for))
                    }
                    ExposedDropdownMenu(
                        expanded = recommendedForDropdownExpanded,
                        onDismissRequest = { recommendedForDropdownExpanded = false }
                    ) {
                        availableRecommendedForItems.forEach { recommendedFor ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = stringResource(recommendedFor.toStringResource())
                                    )
                                },
                                onClick = {
                                    viewModel.addRecommendedFor(recommendedFor)
                                    recommendedForDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }
        if (uiState.error.hasRecommendedForError) {
            Text(
                text = stringResource(uiState.error.recommendedForError!!),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EquipmentListView(
    uiState: ExerciseDetailUiState,
    viewModel: ExerciseDetailViewModel,
    availableEquipments: List<TrainingEquipment>
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(Res.string.exercise_detail_required_equipment),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            uiState.equipmentList.forEach { equipment ->
                InputChip(
                    selected = false,
                    onClick = { },
                    label = { Text(text = getEquipmentName(equipment.name, equipment.default)) },
                    trailingIcon = {
                        if (uiState.isEditing) {
                            IconButton(
                                onClick = { viewModel.removeEquipment(equipment) },
                                modifier = Modifier.size(18.dp)
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_remove),
                                    contentDescription =
                                        stringResource(
                                            Res.string.exercise_detail_content_description_remove_equipment
                                        )
                                )
                            }
                        }
                    },
                    enabled = uiState.isEditing,
                    colors = InputChipDefaults.inputChipColors()
                )
            }

            if (uiState.isEditing && availableEquipments.isNotEmpty()) {
                var equipmentDropdownExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = equipmentDropdownExpanded,
                    onExpandedChange = { equipmentDropdownExpanded = !equipmentDropdownExpanded }
                ) {
                    TextButton(
                        onClick = { equipmentDropdownExpanded = true },
                        modifier = Modifier.menuAnchor(MenuAnchorType.SecondaryEditable)
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_add),
                            contentDescription =
                                stringResource(
                                    Res.string.exercise_detail_content_description_add_equipment
                                ),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.size(8.dp))
                        Text(text = stringResource(Res.string.exercise_detail_add_equipment))
                    }
                    ExposedDropdownMenu(
                        expanded = equipmentDropdownExpanded,
                        onDismissRequest = { equipmentDropdownExpanded = false }
                    ) {
                        availableEquipments.forEach { equipment ->
                            DropdownMenuItem(
                                text = {
                                    Text(text = getEquipmentName(equipment.name, equipment.default))
                                },
                                onClick = {
                                    viewModel.addEquipment(equipment)
                                    equipmentDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }
        if (uiState.error.hasEquipmentError) {
            Text(
                text = stringResource(uiState.error.equipmentError!!),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}