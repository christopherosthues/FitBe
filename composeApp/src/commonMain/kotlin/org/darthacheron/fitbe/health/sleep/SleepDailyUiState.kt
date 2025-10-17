package org.darthacheron.fitbe.health.sleep

import kotlinx.datetime.LocalTime
import org.darthacheron.fitbe.ui.UiState
import kotlin.time.Duration

class SleepDailyUiState(
    isLoading: Boolean = true,
    error: SleepDailyError = SleepDailyError(),
    val sleeps: List<Sleep> = emptyList(),
    val times: List<LocalTime> = emptyList(),
    val target: Int? = null,
    val sleepHours: Int = 0,
    val sleepMinutes: Int = 0,
) : UiState<SleepDailyError>(isLoading, error)