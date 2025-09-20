package org.darthacheron.fitbe.workouts.workouts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.exercise_execution_cancel_button
import fitbe.composeapp.generated.resources.exercise_execution_title_completed
import fitbe.composeapp.generated.resources.exercise_execution_title_log_set
import fitbe.composeapp.generated.resources.exercise_execution_title_resting
import fitbe.composeapp.generated.resources.exercise_execution_title_set_progress
import fitbe.composeapp.generated.resources.exercise_execution_title_set_sets
import fitbe.composeapp.generated.resources.exercise_execution_title_set_targets
import fitbe.composeapp.generated.resources.ic_close
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.ui.TopBarManager
import org.darthacheron.fitbe.ui.state.TopBarAction
import org.darthacheron.fitbe.ui.state.TopBarConfig
import org.darthacheron.fitbe.workouts.exercises.Exercise
import org.darthacheron.fitbe.workouts.exercises.ExerciseRepository
import org.darthacheron.fitbe.workouts.exercises.ExerciseType
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

enum class ExecutionPhase {
    SET_COUNT_INPUT, TARGET_INPUT, EXECUTING, RESTING, SET_COMPLETED_DIALOG, WORKOUT_COMPLETED
}

private const val DEFAULT_REST_DURATION_SECONDS = 60

@OptIn(ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class)
class ExerciseExecutionViewModel(
    private val navHostController: NavHostController,
    private val exerciseRepository: ExerciseRepository,
    private val workoutExecutionRepository: WorkoutExecutionRepository,
    private val settingsRepository: SettingsRepository,
    private val topBarManager: TopBarManager
) : ViewModel() {

    private val _exercise = MutableStateFlow<Exercise?>(null)
    val exercise: StateFlow<Exercise?> = _exercise.asStateFlow()

    private val _currentProfileId = MutableStateFlow<Uuid?>(null)

    private val _currentWorkoutExecutionId = MutableStateFlow<Uuid?>(null)

    private val _currentPhase = MutableStateFlow(ExecutionPhase.SET_COUNT_INPUT)
    val currentPhase: StateFlow<ExecutionPhase> = _currentPhase.asStateFlow()

    private val _phaseBeforeDialog = MutableStateFlow<ExecutionPhase?>(null)
    val phaseBeforeDialog: StateFlow<ExecutionPhase?> = _phaseBeforeDialog.asStateFlow()

    private val _totalSetsInput = MutableStateFlow("")
    val totalSetsInput: StateFlow<String> = _totalSetsInput.asStateFlow()

    private val _totalSets = MutableStateFlow<Int?>(null)
    val totalSets: StateFlow<Int?> = _totalSets.asStateFlow()

    private val _currentSet = MutableStateFlow(1)
    val currentSet: StateFlow<Int> = _currentSet.asStateFlow()

    private val _elapsedTimeSeconds = MutableStateFlow(0)
    val elapsedTimeSeconds: StateFlow<Int> = _elapsedTimeSeconds.asStateFlow()

    private val _elapsedTimeSecondsForSetCompleted = MutableStateFlow(0)
    val elapsedTimeSecondsForSetCompleted: StateFlow<Int> = _elapsedTimeSecondsForSetCompleted.asStateFlow()

    private var timerJob: Job? = null
    private val _timerIsRunning = MutableStateFlow(false)
    val timerIsRunning: StateFlow<Boolean> = _timerIsRunning.asStateFlow()

    private val _targetReps = MutableStateFlow<Int?>(null)
    val targetReps: StateFlow<Int?> = _targetReps.asStateFlow()
    private val _targetWeight = MutableStateFlow<Double?>(null)
    val targetWeight: StateFlow<Double?> = _targetWeight.asStateFlow()
    private val _targetDurationSeconds = MutableStateFlow<Int?>(null)
    val targetDurationSeconds: StateFlow<Int?> = _targetDurationSeconds.asStateFlow()
    private val _targetDistanceKm = MutableStateFlow<Double?>(null)
    val targetDistanceKm: StateFlow<Double?> = _targetDistanceKm.asStateFlow()

    private val _actualReps = MutableStateFlow<Int?>(null)
    val actualReps: StateFlow<Int?> = _actualReps.asStateFlow()
    private val _actualWeight = MutableStateFlow<Double?>(null)
    val actualWeight: StateFlow<Double?> = _actualWeight.asStateFlow()
    private val _actualDurationSeconds = MutableStateFlow<Int?>(null)
    val actualDurationSeconds: StateFlow<Int?> = _actualDurationSeconds.asStateFlow()
    private val _actualDistanceKm = MutableStateFlow<Double?>(null)
    val actualDistanceKm: StateFlow<Double?> = _actualDistanceKm.asStateFlow()

    val showRepsField: StateFlow<Boolean> = exercise.flatMapLatest { ex -> flowOf(ex?.exerciseType in listOf(ExerciseType.WEIGHT_REPS, ExerciseType.REPS_ONLY, ExerciseType.WEIGHT_REPS_TIMED)) }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    val showWeightField: StateFlow<Boolean> = exercise.flatMapLatest { ex -> flowOf(ex?.exerciseType in listOf(ExerciseType.WEIGHT_REPS, ExerciseType.WEIGHT_TIMED, ExerciseType.WEIGHT_REPS_TIMED)) }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    val showDurationField: StateFlow<Boolean> = exercise.flatMapLatest { ex -> flowOf(ex?.exerciseType in listOf(ExerciseType.TIMED, ExerciseType.WEIGHT_TIMED, ExerciseType.WEIGHT_REPS_TIMED, ExerciseType.DISTANCE_TIMED)) }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    val showDistanceField: StateFlow<Boolean> = exercise.flatMapLatest { ex -> flowOf(ex?.exerciseType in listOf(ExerciseType.DISTANCE, ExerciseType.DISTANCE_TIMED)) }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    init {
        viewModelScope.launch {
            settingsRepository.getSettingsFlow().collect {
                _currentProfileId.value = it.selectedProfileId
            }
        }

        viewModelScope.launch {
            combine(_currentPhase, _currentSet, _totalSets) { phase, set, totalS ->
                updateTopBarConfiguration(phase, set, totalS)
            }.collect {}
        }
    }

    fun loadExercise(id: Uuid) {
        viewModelScope.launch {
            _exercise.value = exerciseRepository.getExerciseById(id).firstOrNull()
        }
    }

    private fun updateTopBarConfiguration(phase: ExecutionPhase, currentSetVal: Int, totalSetsVal: Int?) {
        val titleRes = when (phase) {
            ExecutionPhase.SET_COUNT_INPUT -> Res.string.exercise_execution_title_set_sets
            ExecutionPhase.TARGET_INPUT -> Res.string.exercise_execution_title_set_targets
            ExecutionPhase.EXECUTING -> Res.string.exercise_execution_title_set_progress
            ExecutionPhase.RESTING -> Res.string.exercise_execution_title_resting
            ExecutionPhase.SET_COMPLETED_DIALOG -> Res.string.exercise_execution_title_log_set
            ExecutionPhase.WORKOUT_COMPLETED -> Res.string.exercise_execution_title_completed
        }

        val actions = mutableListOf<TopBarAction>()
        val backNavVisible = phase != ExecutionPhase.WORKOUT_COMPLETED

        when (phase) {
            ExecutionPhase.SET_COUNT_INPUT,
            ExecutionPhase.TARGET_INPUT,
            ExecutionPhase.EXECUTING,
            ExecutionPhase.RESTING -> {
                actions.add(
                    TopBarAction(
                        icon = Res.drawable.ic_close,
                        contentDescription = Res.string.exercise_execution_cancel_button,
                        onClick = {
                            cancelWorkout()
                            navigateBack()
                        }
                    )
                )
            }
            ExecutionPhase.SET_COMPLETED_DIALOG -> { /* Dialog has its own cancel */ }
            ExecutionPhase.WORKOUT_COMPLETED -> { /* No actions */ }
        }
        topBarManager.setConfig(TopBarConfig(title = titleRes, backNavigationIconVisible = backNavVisible, actions = actions))
    }

    fun onTotalSetsChanged(sets: String) { _totalSetsInput.value = sets.filter { it.isDigit() } }

    fun confirmTotalSets() {
        val sets = _totalSetsInput.value.toIntOrNull()
        if (sets != null && sets > 0) {
            _totalSets.value = sets
            _currentSet.value = 1
            _currentPhase.value = ExecutionPhase.TARGET_INPUT
        } else { /* TODO: Show error, e.g., via a StateFlow<String?> for errors */ }
    }

    fun startExecutionPhase() {
        viewModelScope.launch {
            val currentExercise = _exercise.value
            val currentPlannedSets = _totalSets.value
            val profileId = _currentProfileId.value

            if (currentExercise == null || currentPlannedSets == null || profileId == null) {
                // TODO: Handle error - cannot start execution without exercise, planned sets or profileId
                return@launch
            }
            if (_currentWorkoutExecutionId.value == null) { // Start a new one only if not already started
                 _currentWorkoutExecutionId.value = workoutExecutionRepository.startNewWorkoutExecution(
                    profileId = profileId,
                    exerciseId = currentExercise.id,
                    plannedSets = currentPlannedSets
                )
            }
            _currentPhase.value = ExecutionPhase.EXECUTING
            _elapsedTimeSeconds.value = 0
            startTimer()
        }
    }

    private fun startTimer() {
        if (_timerIsRunning.value && _currentPhase.value == ExecutionPhase.EXECUTING) return
        _timerIsRunning.value = true
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_timerIsRunning.value && _currentPhase.value == ExecutionPhase.EXECUTING) {
                delay(1000)
                _elapsedTimeSeconds.value += 1
            }
        }
    }

    private fun startRestTimer() {
        _timerIsRunning.value = true
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_elapsedTimeSeconds.value > 0 && _timerIsRunning.value && _currentPhase.value == ExecutionPhase.RESTING) {
                delay(1000)
                _elapsedTimeSeconds.value -= 1
            }
            if (_elapsedTimeSeconds.value == 0 && _currentPhase.value == ExecutionPhase.RESTING) {
                proceedToNextSetExecution()
            }
        }
    }

    fun skipRestAndStartNextSet() {
        if (_currentPhase.value == ExecutionPhase.RESTING) {
            pauseTimer()
            proceedToNextSetExecution()
        }
    }

    private fun proceedToNextSetExecution() {
        _currentSet.value += 1
        _currentPhase.value = ExecutionPhase.EXECUTING
        _elapsedTimeSeconds.value = 0
        startTimer()
    }

    private fun pauseTimer() {
        _timerIsRunning.value = false
        timerJob?.cancel()
    }

    fun completeSet() {
        pauseTimer()
        _elapsedTimeSecondsForSetCompleted.value = _elapsedTimeSeconds.value
        // Pre-fill actual duration for the dialog with the elapsed time for the set.
        _actualDurationSeconds.value = _elapsedTimeSecondsForSetCompleted.value
        _phaseBeforeDialog.value = _currentPhase.value // Should be EXECUTING
        _currentPhase.value = ExecutionPhase.SET_COMPLETED_DIALOG
    }

    fun cancelLogActuals() {
        _currentPhase.value = _phaseBeforeDialog.value ?: ExecutionPhase.EXECUTING
        _phaseBeforeDialog.value = null
        // Restore actual duration to what it was when set was completed, as dialog was cancelled
        _actualDurationSeconds.value = _elapsedTimeSecondsForSetCompleted.value
        // Optionally clear other actuals if they were partially filled then cancelled
        _actualReps.value = null
        _actualWeight.value = null
        _actualDistanceKm.value = null
    }

    fun saveSetActualsAndProceed() {
        viewModelScope.launch {
            val execId = _currentWorkoutExecutionId.value
            if (execId == null) {
                // TODO: Handle error - no workout execution to save to
                return@launch
            }
            workoutExecutionRepository.saveWorkoutSet(
                workoutExecutionId = execId,
                setNumber = _currentSet.value,
                status = WorkoutSetStatus.COMPLETED,
                targetReps = _targetReps.value,
                targetWeightKg = _targetWeight.value,
                targetDurationSeconds = _targetDurationSeconds.value,
                targetDistanceKm = _targetDistanceKm.value,
                actualReps = _actualReps.value,
                actualWeightKg = _actualWeight.value,
                actualDurationSeconds = _actualDurationSeconds.value, // User might have edited this in dialog
                actualDistanceKm = _actualDistanceKm.value,
                restTimeSecondsAfterSet = if (_currentSet.value < (_totalSets.value ?: 0)) DEFAULT_REST_DURATION_SECONDS else null
            )

            _actualReps.value = null; _actualWeight.value = null; _actualDurationSeconds.value = null; _actualDistanceKm.value = null
            _phaseBeforeDialog.value = null

            if (_currentSet.value < (_totalSets.value ?: 0)) {
                _currentPhase.value = ExecutionPhase.RESTING
                _elapsedTimeSeconds.value = DEFAULT_REST_DURATION_SECONDS
                startRestTimer()
            } else {
                _currentPhase.value = ExecutionPhase.WORKOUT_COMPLETED
            }
        }
    }

    fun previousSet() {
        // This might need more thought regarding saved sets if we allow going back to re-do a saved set.
        // For now, it only works if current set > 1 and in EXECUTING phase (before saving current set)
        if (_currentPhase.value == ExecutionPhase.EXECUTING && _currentSet.value > 1) {
            pauseTimer() // Pause current set timer
            // TODO: Potentially mark current timed set data as invalid or unsaved if any tracking started.
            _currentSet.value -= 1
            _elapsedTimeSeconds.value = 0 // Reset timer for the previous set (now current)
            startTimer() // Restart timer for the (now current) previous set
        }
    }

    fun nextSet() { // Called when "Next Set" button in ExecutingPhase is clicked
        viewModelScope.launch {
            val execId = _currentWorkoutExecutionId.value
            if (execId != null && _currentPhase.value == ExecutionPhase.EXECUTING) {
                pauseTimer()
                // Save current set as SKIPPED
                workoutExecutionRepository.saveWorkoutSet(
                    workoutExecutionId = execId,
                    setNumber = _currentSet.value,
                    status = WorkoutSetStatus.SKIPPED,
                    targetReps = _targetReps.value,
                    targetWeightKg = _targetWeight.value,
                    targetDurationSeconds = _targetDurationSeconds.value,
                    targetDistanceKm = _targetDistanceKm.value,
                    actualReps = null, // No actuals for skipped set
                    actualWeightKg = null,
                    actualDurationSeconds = _elapsedTimeSeconds.value, // Could save how long it was active before skip
                    actualDistanceKm = null,
                    restTimeSecondsAfterSet = if (_currentSet.value < (_totalSets.value ?: 0)) DEFAULT_REST_DURATION_SECONDS else null
                )

                if (_currentSet.value < (_totalSets.value ?: 0)) {
                    _currentPhase.value = ExecutionPhase.RESTING
                    _elapsedTimeSeconds.value = DEFAULT_REST_DURATION_SECONDS
                    startRestTimer()
                } else {
                    _currentPhase.value = ExecutionPhase.WORKOUT_COMPLETED
                }
            } else if (_currentPhase.value == ExecutionPhase.EXECUTING) {
                 // Case where execId is null but trying to skip, indicates an issue
                 // TODO: Handle error - cannot skip set if workout session not started
            }
        }
    }

    fun onTargetRepsChanged(reps: String) { _targetReps.value = reps.toIntOrNull() }
    fun onTargetWeightChanged(weight: String) { _targetWeight.value = weight.toDoubleOrNull() }
    fun onTargetDurationChanged(duration: String) { _targetDurationSeconds.value = duration.toIntOrNull() }
    fun onTargetDistanceChanged(distance: String) { _targetDistanceKm.value = distance.toDoubleOrNull() }

    fun onActualRepsChanged(reps: String) { _actualReps.value = reps.toIntOrNull() }
    fun onActualWeightChanged(weight: String) { _actualWeight.value = weight.toDoubleOrNull() }
    fun onActualDurationChanged(duration: String) { _actualDurationSeconds.value = duration.toIntOrNull() }
    fun onActualDistanceChanged(distance: String) { _actualDistanceKm.value = distance.toDoubleOrNull() }

    fun overallWorkoutSave() {
        viewModelScope.launch {
            val execId = _currentWorkoutExecutionId.value
            if (execId != null) {
                // Ensure last set is saved if workout is completed prematurely (e.g. from resting phase)
                // This check might be redundant if all paths to WORKOUT_COMPLETED save the last set.
                workoutExecutionRepository.updateWorkoutExecutionStatus(execId, WorkoutExecutionStatus.COMPLETED)
                _currentWorkoutExecutionId.value = null // Clear after successful save
            } else {
                 // TODO: Handle case where workout ID is null but trying to save overall workout.
                 // This could happen if user navigates away before starting any sets, or an error occurred.
            }
        }
    }

    fun cancelWorkout() {
        viewModelScope.launch {
            pauseTimer()
            val execId = _currentWorkoutExecutionId.value
            if (execId != null) {
                workoutExecutionRepository.updateWorkoutExecutionStatus(execId, WorkoutExecutionStatus.CANCELLED)
                _currentWorkoutExecutionId.value = null
            }
            // Reset all states
            _currentPhase.value = ExecutionPhase.SET_COUNT_INPUT
            _totalSetsInput.value = ""; _totalSets.value = null; _currentSet.value = 1; _elapsedTimeSeconds.value = 0
            _phaseBeforeDialog.value = null; _elapsedTimeSecondsForSetCompleted.value = 0
            _targetReps.value = null; _targetWeight.value = null; _targetDurationSeconds.value = null; _targetDistanceKm.value = null
            _actualReps.value = null; _actualWeight.value = null; _actualDurationSeconds.value = null; _actualDistanceKm.value = null
            // onNavigateBack() is called by the TopBarAction's onClick
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        // If workout is in progress and VM is cleared (e.g. screen destroyed unexpectedly),
        // consider if it should be marked as CANCELLED or if state restoration is handled elsewhere.
        // For now, just reset TopBar.
        val execId = _currentWorkoutExecutionId.value
        if (execId != null && _currentPhase.value != ExecutionPhase.WORKOUT_COMPLETED) {
            viewModelScope.launch {
                // workoutExecutionRepository.updateWorkoutExecutionStatus(execId, WorkoutExecutionStatus.CANCELLED) // Or IN_PROGRESS for later resume?
            }
        }
        topBarManager.setConfig(TopBarConfig()) // Reset to default when leaving screen
    }

    fun navigateBack() {
        navHostController.navigateUp()
    }
}
