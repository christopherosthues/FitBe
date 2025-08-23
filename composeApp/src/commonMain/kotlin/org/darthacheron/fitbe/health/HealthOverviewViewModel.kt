package org.darthacheron.fitbe.health


import androidx.lifecycle.ViewModel
import org.darthacheron.fitbe.health.steps.StepsViewModel
import org.darthacheron.fitbe.health.weight.WeightOverviewViewModel
import org.darthacheron.fitbe.profile.ProfileRepository
import org.darthacheron.fitbe.settings.SettingsRepository
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class HealthOverviewViewModel(
    private val settingsRepository: SettingsRepository,
    private val profileRepository: ProfileRepository,
    val bodyWeightOverviewViewModel: WeightOverviewViewModel,
    val stepsViewModel: StepsViewModel,
) : ViewModel() {
}