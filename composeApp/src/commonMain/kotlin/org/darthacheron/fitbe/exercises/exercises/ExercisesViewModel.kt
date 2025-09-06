package org.darthacheron.fitbe.exercises.exercises

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class ExercisesViewModel(private val exerciseRepository: ExerciseRepository) : ViewModel() {

    val allExercises: StateFlow<List<Exercise>> =
        exerciseRepository.getAllExercises()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000), // Keep active for 5s after last subscriber
                initialValue = emptyList()
            )
}
