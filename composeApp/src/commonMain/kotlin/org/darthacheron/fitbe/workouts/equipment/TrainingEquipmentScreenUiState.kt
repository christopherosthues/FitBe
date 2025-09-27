package org.darthacheron.fitbe.workouts.equipment

import org.jetbrains.compose.resources.StringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class TrainingEquipmentScreenUiState(
    val isLoading: Boolean = true,
    val rawEquipmentList: List<TrainingEquipment> = emptyList(),
    val favoriteEquipmentIds: Set<Uuid> = emptySet(),
    val equipmentListError: StringResource? = null,
    val favoriteStateError: StringResource? = null
)