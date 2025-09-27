package org.darthacheron.fitbe.workouts.programs

import org.jetbrains.compose.resources.StringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class ProgramsUiState(
    val isLoading: Boolean = true,
    val rawProgramList: List<Program> = emptyList(),
    val favoriteProgramIds: Set<Uuid> = emptySet(),
    val programListError: StringResource? = null,
    val favoriteStateError: StringResource? = null
)