package org.darthacheron.fitbe.health.sleep

import kotlinx.datetime.LocalTime
import org.darthacheron.fitbe.ui.UiState

class SleepDailyUiState(
    isLoading: Boolean = true,
    error: SleepDailyError = SleepDailyError(),
    val sleeps: List<Sleep> = emptyList(),
    val times: List<LocalTime> = emptyList(),
    val target: Int? = null,
) : UiState<SleepDailyError>(isLoading, error)