package org.darthacheron.fitbe.exercises.equipment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class TrainingEquipmentViewModel(private val equipmentRepository: EquipmentRepository) : ViewModel() {

    val allEquipment: StateFlow<List<TrainingEquipment>> =
        equipmentRepository.getAllEquipments()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000), // Keep active for 5s after last subscriber
                initialValue = emptyList()
            )
}
