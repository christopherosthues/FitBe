package org.darthacheron.fitbe.workouts.workouts

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.exercise_execution_back_button_description
import fitbe.composeapp.generated.resources.exercise_execution_cancel_button
import fitbe.composeapp.generated.resources.exercise_execution_complete_set_button
import fitbe.composeapp.generated.resources.exercise_execution_confirm_sets_button
import fitbe.composeapp.generated.resources.exercise_execution_distance_km
import fitbe.composeapp.generated.resources.exercise_execution_duration_seconds
import fitbe.composeapp.generated.resources.exercise_execution_enter_sets_prompt
import fitbe.composeapp.generated.resources.exercise_execution_exercise_count
import fitbe.composeapp.generated.resources.exercise_execution_finish_button
import fitbe.composeapp.generated.resources.exercise_execution_log_actuals_prompt_dialog_title
import fitbe.composeapp.generated.resources.exercise_execution_next_set_button
import fitbe.composeapp.generated.resources.exercise_execution_next_set_info
import fitbe.composeapp.generated.resources.exercise_execution_number_of_sets
import fitbe.composeapp.generated.resources.exercise_execution_previous_set_button
import fitbe.composeapp.generated.resources.exercise_execution_reps
import fitbe.composeapp.generated.resources.exercise_execution_rest_timer_label
import fitbe.composeapp.generated.resources.exercise_execution_save_set_button
import fitbe.composeapp.generated.resources.exercise_execution_set_count
import fitbe.composeapp.generated.resources.exercise_execution_set_targets_prompt
import fitbe.composeapp.generated.resources.exercise_execution_skip_rest_button
import fitbe.composeapp.generated.resources.exercise_execution_start_workout_button
import fitbe.composeapp.generated.resources.exercise_execution_target_display
import fitbe.composeapp.generated.resources.exercise_execution_weight_kg
import fitbe.composeapp.generated.resources.exercise_execution_workout_completed_message
import fitbe.composeapp.generated.resources.ic_back
import org.darthacheron.fitbe.ui.TopBarManager
import org.darthacheron.fitbe.ui.state.TopBarConfig
import org.darthacheron.fitbe.workouts.exercises.ExerciseType
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalMaterial3Api::class, ExperimentalUuidApi::class)
@Composable
fun ExerciseExecutionView(
    exerciseId: Uuid,
    viewModel: ExerciseExecutionViewModel,
    topBarManager: TopBarManager,
    onNavigateBack: () -> Unit
) {
    val currentPhase by viewModel.currentPhase.collectAsState()
    val topBarConfig by topBarManager.topBarConfigFlow.collectAsState(initial = TopBarConfig())

    LaunchedEffect(exerciseId) {
        viewModel.loadExercise(exerciseId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val title = topBarConfig.title?.let { stringResource(it) } ?: ""
                    Text(text = title)
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                navigationIcon = {
                    AnimatedVisibility(visible = topBarConfig.backNavigationIconVisible ?: false) {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_back),
                                contentDescription = stringResource(Res.string.exercise_execution_back_button_description)
                            )
                        }
                    }
                },
                actions = {
                    topBarConfig.actions.forEach { action ->
                        AnimatedVisibility(visible = action.isVisible) {
                            IconButton(onClick = action.onClick) {
                                Icon(
                                    painter = painterResource(action.icon),
                                    contentDescription = action.contentDescription?.let { stringResource(it) }
                                )
                            }
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        val backgroundPhase = if (currentPhase == ExecutionPhase.SET_COMPLETED_DIALOG) {
            viewModel.phaseBeforeDialog.collectAsState().value ?: ExecutionPhase.EXECUTING
        } else {
            currentPhase
        }

        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            when (backgroundPhase) {
                ExecutionPhase.SET_COUNT_INPUT -> SetCountInputPhase(viewModel)
                ExecutionPhase.TARGET_INPUT -> TargetInputPhase(viewModel)
                ExecutionPhase.EXECUTING -> ExecutingPhase(viewModel)
                ExecutionPhase.RESTING -> RestingPhase(viewModel)
                ExecutionPhase.WORKOUT_COMPLETED -> WorkoutCompletedPhase(viewModel, onNavigateBack)
                else -> Unit
            }

            if (currentPhase == ExecutionPhase.SET_COMPLETED_DIALOG) {
                ActualsInputDialog(viewModel)
            }
        }
    }
}

@Composable
fun SetCountInputPhase(viewModel: ExerciseExecutionViewModel) {
    val totalSetsInput by viewModel.totalSetsInput.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(Res.string.exercise_execution_enter_sets_prompt), style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = totalSetsInput,
            onValueChange = viewModel::onTotalSetsChanged,
            label = { Text(text = stringResource(Res.string.exercise_execution_number_of_sets)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.6f)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = viewModel::confirmTotalSets) {
            Text(text = stringResource(Res.string.exercise_execution_confirm_sets_button))
        }
    }
}

@Composable
fun TargetInputPhase(viewModel: ExerciseExecutionViewModel) {
    val exercise by viewModel.exercise.collectAsState()
    val targetReps by viewModel.targetReps.collectAsState()
    val targetWeight by viewModel.targetWeight.collectAsState()
    val targetDuration by viewModel.targetDurationSeconds.collectAsState()
    val targetDistance by viewModel.targetDistanceKm.collectAsState()
    val showRepsField by viewModel.showRepsField.collectAsState()
    val showWeightField by viewModel.showWeightField.collectAsState()
    val showDurationField by viewModel.showDurationField.collectAsState()
    val showDistanceField by viewModel.showDistanceField.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = exercise?.name ?: "", style = MaterialTheme.typography.headlineSmall)
        Text(text = stringResource(Res.string.exercise_execution_set_targets_prompt), style = MaterialTheme.typography.titleMedium)
        if (showRepsField) {
            OutlinedTextField(
                value = targetReps?.toString() ?: "", 
                onValueChange = viewModel::onTargetRepsChanged, 
                label = { Text(text = stringResource(Res.string.exercise_execution_reps)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (showWeightField) {
            OutlinedTextField(
                value = targetWeight?.toString() ?: "", 
                onValueChange = viewModel::onTargetWeightChanged, 
                label = { Text(text = stringResource(Res.string.exercise_execution_weight_kg)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (showDurationField) {
            OutlinedTextField(
                value = targetDuration?.toString() ?: "", 
                onValueChange = viewModel::onTargetDurationChanged, 
                label = { Text(text = stringResource(Res.string.exercise_execution_duration_seconds)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (showDistanceField) {
            OutlinedTextField(
                value = targetDistance?.toString() ?: "", 
                onValueChange = viewModel::onTargetDistanceChanged, 
                label = { Text(text = stringResource(Res.string.exercise_execution_distance_km)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp)) 
        Button(onClick = viewModel::startExecutionPhase, modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(Res.string.exercise_execution_start_workout_button))
        }
    }
}

@Composable
fun ExecutingPhase(viewModel: ExerciseExecutionViewModel) {
    val exercise by viewModel.exercise.collectAsState()
    val currentSet by viewModel.currentSet.collectAsState()
    val totalSets by viewModel.totalSets.collectAsState()
    val elapsedTimeSeconds by viewModel.elapsedTimeSeconds.collectAsState()
    val targetReps by viewModel.targetReps.collectAsState()
    val targetWeight by viewModel.targetWeight.collectAsState()
    val targetDuration by viewModel.targetDurationSeconds.collectAsState()
    val targetDistanceKm by viewModel.targetDistanceKm.collectAsState()
    val exerciseType = exercise?.exerciseType

    val formattedTime = remember(elapsedTimeSeconds) {
        val minutes = elapsedTimeSeconds / 60
        val seconds = elapsedTimeSeconds % 60
        "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
    }
    val exerciseTargetDisplay = remember(exercise, targetReps, targetWeight, targetDuration, targetDistanceKm) {
        when (exerciseType) {
            ExerciseType.WEIGHT_REPS -> "${targetWeight ?: "-"} kg x ${targetReps ?: "-"} reps"
            ExerciseType.REPS_ONLY -> "${targetReps ?: "-"} reps"
            ExerciseType.TIMED -> "${targetDuration ?: "-"} sec"
            ExerciseType.DISTANCE -> "${targetDistanceKm ?: "-"} km"
            ExerciseType.WEIGHT_TIMED -> "${targetWeight ?: "-"} kg / ${targetDuration ?: "-"} sec"
            ExerciseType.WEIGHT_REPS_TIMED -> "${targetWeight ?: "-"} kg x ${targetReps ?: "-"} reps / ${targetDuration ?: "-"} sec"
            ExerciseType.DISTANCE_TIMED -> "${targetDistanceKm ?: "-"} km / ${targetDuration ?: "-"} sec"
            else -> exercise?.name ?: "Exercise"
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
            Text(text = formattedTime, style = MaterialTheme.typography.headlineSmall)
            Column(horizontalAlignment = Alignment.End) {
                Text(text = stringResource(Res.string.exercise_execution_exercise_count, 1, 1), style = MaterialTheme.typography.bodyLarge)
                Text(text = stringResource(Res.string.exercise_execution_set_count, currentSet, totalSets ?: 0), style = MaterialTheme.typography.titleMedium)
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(text = exercise?.name ?: "Loading...", style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        Text(text = stringResource(Res.string.exercise_execution_target_display, exerciseTargetDisplay), style = MaterialTheme.typography.titleSmall, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = viewModel::previousSet, enabled = currentSet > 1) { Text(stringResource(Res.string.exercise_execution_previous_set_button)) }
            Spacer(Modifier.width(8.dp))
            Button(onClick = viewModel::nextSet) { Text(stringResource(Res.string.exercise_execution_next_set_button)) }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = viewModel::completeSet, modifier = Modifier.fillMaxWidth().height(60.dp)) {
            Text(text = stringResource(Res.string.exercise_execution_complete_set_button), style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun RestingPhase(viewModel: ExerciseExecutionViewModel) {
    val exercise by viewModel.exercise.collectAsState()
    val nextSetNumber by remember { derivedStateOf { (viewModel.currentSet.value + 1).coerceAtMost(viewModel.totalSets.value ?: (viewModel.currentSet.value + 1)) } }
    val remainingRestTimeSeconds by viewModel.elapsedTimeSeconds.collectAsState()
    val formattedRestTime = remember(remainingRestTimeSeconds) {
        val minutes = remainingRestTimeSeconds / 60
        val seconds = remainingRestTimeSeconds % 60
        "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(Res.string.exercise_execution_rest_timer_label), style = MaterialTheme.typography.titleMedium)
        Text(text = formattedRestTime, style = MaterialTheme.typography.displayLarge.copy(textAlign = TextAlign.Center))
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = stringResource(Res.string.exercise_execution_next_set_info, nextSetNumber, exercise?.name ?: "Exercise"), style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = viewModel::skipRestAndStartNextSet, modifier = Modifier.fillMaxWidth(0.8f).height(50.dp)) {
            Text(text = stringResource(Res.string.exercise_execution_skip_rest_button))
        }
    }
}

@Composable
fun ActualsInputDialog(viewModel: ExerciseExecutionViewModel) {
    val currentSetNumber by viewModel.currentSet.collectAsState()
    val actualReps by viewModel.actualReps.collectAsState()
    val actualWeight by viewModel.actualWeight.collectAsState()
    val actualDistance by viewModel.actualDistanceKm.collectAsState()
    val showRepsField by viewModel.showRepsField.collectAsState()
    val showWeightField by viewModel.showWeightField.collectAsState()
    val showDistanceField by viewModel.showDistanceField.collectAsState()
    val setElapsedTime by viewModel.elapsedTimeSecondsForSetCompleted.collectAsState()

    AlertDialog(
        onDismissRequest = { viewModel.cancelLogActuals() },
        title = { Text(text = stringResource(Res.string.exercise_execution_log_actuals_prompt_dialog_title, currentSetNumber)) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (showRepsField) {
                    OutlinedTextField(
                        value = actualReps?.toString() ?: "", 
                        onValueChange = viewModel::onActualRepsChanged, 
                        label = { Text(text = stringResource(Res.string.exercise_execution_reps)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                if (showWeightField) {
                    OutlinedTextField(
                        value = actualWeight?.toString() ?: "", 
                        onValueChange = viewModel::onActualWeightChanged, 
                        label = { Text(text = stringResource(Res.string.exercise_execution_weight_kg)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                OutlinedTextField(
                    value = setElapsedTime.toString(), 
                    onValueChange = viewModel::onActualDurationChanged, 
                    label = { Text(text = stringResource(Res.string.exercise_execution_duration_seconds)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = if(showDistanceField) ImeAction.Next else ImeAction.Done),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                if (showDistanceField) {
                    OutlinedTextField(
                        value = actualDistance?.toString() ?: "", 
                        onValueChange = viewModel::onActualDistanceChanged, 
                        label = { Text(text = stringResource(Res.string.exercise_execution_distance_km)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = { viewModel.saveSetActualsAndProceed() }) {
                Text(text = stringResource(Res.string.exercise_execution_save_set_button))
            }
        },
        dismissButton = {
            TextButton(onClick = { viewModel.cancelLogActuals() }) {
                Text(text = stringResource(Res.string.exercise_execution_cancel_button))
            }
        }
    )
}

@Composable
fun WorkoutCompletedPhase(viewModel: ExerciseExecutionViewModel, onNavigateBack: () -> Unit) {
    LaunchedEffect(Unit) {
        viewModel.overallWorkoutSave() 
    }
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(Res.string.exercise_execution_workout_completed_message), style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onNavigateBack) {
            Text(text = stringResource(Res.string.exercise_execution_finish_button))
        }
    }
}