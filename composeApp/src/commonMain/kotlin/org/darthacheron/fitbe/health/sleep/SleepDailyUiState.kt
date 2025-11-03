package org.darthacheron.fitbe.health.sleep

import org.darthacheron.fitbe.profile.ProfileDefaults
import org.darthacheron.fitbe.ui.UiState

class SleepDailyUiState(
    isLoading: Boolean = true,
    error: SleepDailyError = SleepDailyError(),
    val sleeps: List<Sleep> = emptyList(),
    val target: Int? = null,
    val totalSleep: Int = 0,
    val sleepHours: Int = 0,
    val sleepMinutes: Int = 0,
    val maxSleeps: Int = ProfileDefaults.SLEEP_DURATION.toInt()
) : UiState<SleepDailyError>(isLoading, error)