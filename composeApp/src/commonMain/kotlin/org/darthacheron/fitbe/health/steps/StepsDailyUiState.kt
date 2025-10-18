package org.darthacheron.fitbe.health.steps

import kotlinx.datetime.LocalTime
import org.darthacheron.fitbe.health.weight.BodyWeight
import org.darthacheron.fitbe.health.weight.BodyWeightDailyError
import org.darthacheron.fitbe.profile.ProfileDefaults
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.settings.WeightUnit
import org.darthacheron.fitbe.ui.TopBarManager
import org.darthacheron.fitbe.ui.UiState

class StepsDailyUiState(
    isLoading: Boolean = true,
    error: StepsDailyError = StepsDailyError(),
    val steps: List<Steps> = emptyList(),
    val times: List<LocalTime> = emptyList(),
    val target: Int? = null,
    val maxSteps: Int = ProfileDefaults.STEPS.toInt(),
) : UiState<StepsDailyError>(isLoading, error)
