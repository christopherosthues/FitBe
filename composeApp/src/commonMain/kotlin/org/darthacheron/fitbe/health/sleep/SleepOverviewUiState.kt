package org.darthacheron.fitbe.health.sleep

import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.StringResource

data class SleepOverviewUiState(
    val isLoading: Boolean = true,
    val sleeps: List<Sleep> = emptyList(),
    val dates: List<LocalDate> = emptyList(),
    val errorMessage: StringResource? = null
)
