package org.darthacheron.fitbe.health.steps

import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.StringResource

data class StepsUiState(
    val isLoading: Boolean = true,
    val steps: List<Steps> = emptyList(),
    val dates: List<LocalDate> = emptyList(),
    val errorMessage: StringResource? = null
)
