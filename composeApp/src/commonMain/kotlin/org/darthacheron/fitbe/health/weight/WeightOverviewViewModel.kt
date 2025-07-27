package org.darthacheron.fitbe.health.weight

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
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import org.darthacheron.fitbe.health.sleep.SleepViewType
import org.darthacheron.fitbe.settings.Settings
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.settings.WeightUnit
import org.darthacheron.fitbe.settings.converters.WeightUnitConverter
import org.darthacheron.fitbe.utils.roundToDecimals
import org.darthacheron.fitbe.utils.roundUpToNextTen
import kotlin.math.max
import kotlin.time.Duration.Companion.days
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class WeightOverviewViewModel(
    private val bodyWeightRepository: BodyWeightRepository,
    private val settingsRepository: SettingsRepository,
    private val weightUnitConverter: WeightUnitConverter,
) : ViewModel() {
    private val _viewType = MutableStateFlow(SleepViewType.WEEK)
    private val _startDate = MutableStateFlow(Clock.System.now().minus(6.days))
    private val _endDate = MutableStateFlow(Clock.System.now())

    private val _maxWeight = MutableStateFlow(600.0) // TODO extract default max weight

    val viewType: StateFlow<SleepViewType> = _viewType
    val startDate: StateFlow<Instant> = _startDate
    val endDate: StateFlow<Instant> = _endDate

    val maxWeight: StateFlow<Double> = _maxWeight

    @OptIn(ExperimentalCoroutinesApi::class)
    val bodyWeights: StateFlow<Pair<List<LocalDate>, List<List<Double>>>> =
        combine(_startDate, _endDate, settingsRepository.getSettingsFlow()) { start, end, settings ->
            Triple(start, end, settings)
        }.flatMapLatest { (start, end, settings) ->
            bodyWeightRepository.getEntries(start, end, settings.selectedProfileId!!)
                .map { entries -> Pair(entries, settings) }
        }
            .map { (entries, settings) ->
                Pair(
                    entries.map { it.date },
                    toStackedBodyWeightData(entries, settings)
                )
            }
            .stateIn(viewModelScope, SharingStarted.Lazily, Pair(listOf(), listOf()))

    fun toStackedBodyWeightData(bodyWeights: List<BodyWeight>, settings: Settings): List<List<Double>> {
        val totalWeights = mutableListOf<Double>()
        val boneMasses = mutableListOf<Double>()
        val muscleMasses = mutableListOf<Double>()
        val bodyFats = mutableListOf<Double>()
        val bodyWaters = mutableListOf<Double>()
        var maxWeight = 0.0

        // TODO: unit conversion is screwed up

        viewModelScope.launch {
            for (bodyWeight in bodyWeights) {
                val convertedTotalWeight = weightUnitConverter.convert(
                    bodyWeight.weightInKg,
                    WeightUnit.KG,
                    settings.weightUnit
                )!!.roundToDecimals(2)
                maxWeight = max(maxWeight, convertedTotalWeight)

                val boneMass = weightUnitConverter.convert(
                    (bodyWeight.boneMassInKg ?: 0.0),
                    WeightUnit.KG,
                    settings.weightUnit
                )!!.roundToDecimals(2)
                val muscleMass = weightUnitConverter.convert(
                    (bodyWeight.muscleMassInKg ?: 0.0),
                    WeightUnit.KG,
                    settings.weightUnit
                )!!.roundToDecimals(2)
                val bodyFat =
                    weightUnitConverter.convert(
                        (convertedTotalWeight * (bodyWeight.bodyFatPercentage ?: 0.0) / 100),
                        WeightUnit.KG,
                        settings.weightUnit
                    )!!.roundToDecimals(2)
                val bodyWater =
                    weightUnitConverter.convert(
                        (convertedTotalWeight * (bodyWeight.bodyWaterInPercentage ?: 0.0) / 100),
                        WeightUnit.KG,
                        settings.weightUnit
                    )!!.roundToDecimals(
                        2
                    )
                val restWeight = max(
                    convertedTotalWeight - boneMass - muscleMass - bodyFat - bodyWater,
                    0.0
                ).roundToDecimals(2)

                boneMasses.add(boneMass)
                muscleMasses.add(muscleMass)
                bodyFats.add(bodyFat)
                bodyWaters.add(bodyWater)
                totalWeights.add(restWeight)
            }

            _maxWeight.value = maxWeight.roundUpToNextTen().roundToDecimals(2)
        }

        return listOf(
            boneMasses,
            muscleMasses,
            bodyFats,
            bodyWaters,
            totalWeights
        )
    }

    fun setViewType(type: SleepViewType) {
        _viewType.value = type
    }
    fun setStartDate(date: Instant) { _startDate.value = date }
    fun setEndDate(date: Instant) { _endDate.value = date }

    fun addBodyWeight(
        date: LocalDate,
        weightInKg: Double,
        bodyFatPercentage: Double?,
        muscleMassInKg: Double?,
        boneMassInKg: Double?,
        bodyWaterInPercentage: Double?,
    ) {
        viewModelScope.launch {
            val settings = settingsRepository.getSettings()
            bodyWeightRepository.addBodyWeight(
                profileId = settings.selectedProfileId!!,
                date = date,
                weightInKg = weightInKg,
                bodyFatPercentage = bodyFatPercentage,
                muscleMassInKg = muscleMassInKg,
                boneMassInKg = boneMassInKg,
                bodyWaterInPercentage = bodyWaterInPercentage
            )
        }
    }
}