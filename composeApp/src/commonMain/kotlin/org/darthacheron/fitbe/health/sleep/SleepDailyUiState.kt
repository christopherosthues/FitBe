package org.darthacheron.fitbe.health.sleep

import org.darthacheron.fitbe.ui.UiState

class SleepDailyUiState(
    isLoading: Boolean = true,
    error: SleepDailyError = SleepDailyError(),
) : UiState<SleepDailyError>(isLoading, error)