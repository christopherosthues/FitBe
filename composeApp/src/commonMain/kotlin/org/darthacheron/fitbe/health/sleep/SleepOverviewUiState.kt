package org.darthacheron.fitbe.health.sleep

import kotlinx.datetime.LocalDate
import org.darthacheron.fitbe.profile.ProfileDefaults
import org.darthacheron.fitbe.ui.UiState

class SleepOverviewUiState(
    isLoading: Boolean = true,
    val sleeps: List<SleepOverview> = emptyList(),
    val dates: List<LocalDate> = emptyList(),
    val target: Int? = null,
    val maxSleeps: Int = ProfileDefaults.SLEEP_DURATION.toInt(),
    error: SleepOverviewError = SleepOverviewError()
) : UiState<SleepOverviewError>(isLoading, error)