package org.darthacheron.fitbe.health


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.Clock
import org.darthacheron.fitbe.components.date.DateRange
import org.darthacheron.fitbe.components.date.DateUnit
import org.darthacheron.fitbe.health.sleep.SleepRepository
import org.darthacheron.fitbe.health.steps.StepsRepository
import org.darthacheron.fitbe.health.weight.BodyWeight
import org.darthacheron.fitbe.health.weight.BodyWeightRepository
import org.darthacheron.fitbe.profile.ProfileRepository
import org.darthacheron.fitbe.settings.Settings
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.settings.WeightUnit
import org.darthacheron.fitbe.settings.converters.WeightUnitConverter
import org.darthacheron.fitbe.utils.roundToDecimals
import kotlin.time.Duration.Companion.days
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class HealthOverviewViewModel(
    private val settingsRepository: SettingsRepository,
    private val profileRepository: ProfileRepository,
    private val bodyWeightRepository: BodyWeightRepository,
    private val weightUnitConverter: WeightUnitConverter,
    private val stepsRepository: StepsRepository,
    private val sleepRepository: SleepRepository,
) : ViewModel() {
    private val _dateRange = MutableStateFlow(
        DateRange(
            Clock.System.now().minus(6.days),
            Clock.System.now(),
            DateUnit.DAY
        )
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val bodyWeights: StateFlow<List<BodyWeight>> = combine(
        _dateRange,
        settingsRepository.getSettingsFlow()
    ) { range, settings ->
        settings to bodyWeightRepository.getEntries(
            range.startDate,
            range.endDate,
            settings.selectedProfileId!!
        )
    }.flatMapLatest { (settings, bodyWeightFlow) ->
        bodyWeightFlow.map { bodyWeights ->
            mapDay(bodyWeights, settings)
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, listOf())

    private fun mapDay(bodyWeights: List<BodyWeight>, settings: Settings): List<BodyWeight> {
        return bodyWeights.map {
            it.copy(
                weightInKg = weightUnitConverter.convert(
                    it.weightInKg, WeightUnit.KG, settings.weightUnit
                )!!.roundToDecimals(2),
                muscleMassInKg = weightUnitConverter.convert(
                    it.muscleMassInKg, WeightUnit.KG, settings.weightUnit
                )!!.roundToDecimals(2),
                boneMassInKg = weightUnitConverter.convert(
                    it.boneMassInKg, WeightUnit.KG, settings.weightUnit
                )!!.roundToDecimals(2)
            )
        }
    }
}