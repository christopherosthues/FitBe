package org.darthacheron.fitbe.health.weight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.koalaplot.core.bar.DefaultVerticalBarPlotStackedPointEntry
import io.github.koalaplot.core.bar.VerticalBarPlotStackedPointEntry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import org.darthacheron.fitbe.health.sleep.SleepViewType
import org.darthacheron.fitbe.profile.ProfileRepository
import org.darthacheron.fitbe.settings.Settings
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.settings.WeightUnit
import org.darthacheron.fitbe.settings.converters.WeightUnitConverter
import org.darthacheron.fitbe.utils.firstDayOfIsoWeek
import org.darthacheron.fitbe.utils.firstDayOfMonth
import org.darthacheron.fitbe.utils.firstDayOfYear
import org.darthacheron.fitbe.utils.isoWeekAndYear
import org.darthacheron.fitbe.utils.roundToDecimals
import org.darthacheron.fitbe.utils.roundUpToNextTen
import kotlin.math.max
import kotlin.time.Duration.Companion.days
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class WeightOverviewViewModel(
    private val bodyWeightRepository: BodyWeightRepository,
    private val settingsRepository: SettingsRepository,
    private val profileRepository: ProfileRepository,
    private val weightUnitConverter: WeightUnitConverter,
) : ViewModel() {
    private val _viewType = MutableStateFlow(SleepViewType.DAY)
    private val _startDate = MutableStateFlow(Clock.System.now().minus(6.days))
    private val _endDate = MutableStateFlow(Clock.System.now())

    private val _maxWeight = MutableStateFlow(600.0) // TODO extract default max weight

    val viewType: StateFlow<SleepViewType> = _viewType
    val startDate: StateFlow<Instant> = _startDate
    val endDate: StateFlow<Instant> = _endDate

    val maxWeight: StateFlow<Double> = _maxWeight

    val targetWeight: StateFlow<Double?> = settingsRepository.getSettingsFlow()
        .flatMapLatest { settings ->
            val profileId = settings.selectedProfileId
            if (profileId != null) {
                profileRepository.getProfileFlowById(profileId)
                    .map { profile -> profile?.targetWeight }
            } else {
                flowOf(null)
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val bodyWeights: StateFlow<List<BodyWeight>> = combine(
        _startDate,
        _endDate,
        viewType,
        settingsRepository.getSettingsFlow()
    ) { start, end, viewType, settings ->
        Pair(settings, viewType) to bodyWeightRepository.getEntries(
            start,
            end,
            settings.selectedProfileId!!
        )
    }.flatMapLatest { (settingsViewType, bodyWeightFlow) ->
        bodyWeightFlow.map { bodyWeights ->
            when (settingsViewType.second) {
                SleepViewType.DAY -> mapDay(bodyWeights, settingsViewType.first)
                SleepViewType.WEEK -> mapWeek(bodyWeights, settingsViewType.first)
                SleepViewType.MONTH -> mapMonth(bodyWeights, settingsViewType.first)
                SleepViewType.YEAR -> mapYear(bodyWeights, settingsViewType.first)
            }
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

    private fun mapWeek(
        bodyWeights: List<BodyWeight>,
        settings: Settings
    ): List<BodyWeight> = bodyWeights.groupBy { bodyWeight -> bodyWeight.date.isoWeekAndYear() }
        .map { bodyWeightDateMap ->
            var totalWeight = 0.0
            var boneMass = 0.0
            var muscleMass = 0.0
            var bodyFat = 0.0
            var bodyWater = 0.0
            bodyWeightDateMap.value.forEach {
                totalWeight += it.weightInKg
                boneMass += it.boneMassInKg ?: 0.0
                muscleMass += it.muscleMassInKg ?: 0.0
                bodyFat += it.bodyFatPercentage ?: 0.0
                bodyWater += it.bodyWaterInPercentage ?: 0.0
            }

            totalWeight = weightUnitConverter.convert(
                totalWeight, WeightUnit.KG, settings.weightUnit
            )!!.roundToDecimals(2)
            muscleMass = weightUnitConverter.convert(
                muscleMass, WeightUnit.KG, settings.weightUnit
            )!!.roundToDecimals(2)
            boneMass = weightUnitConverter.convert(
                boneMass, WeightUnit.KG, settings.weightUnit
            )!!.roundToDecimals(2)

            BodyWeight(
                id = Uuid.random(),
                date = bodyWeightDateMap.value.first().date.firstDayOfIsoWeek(),
                profileId = bodyWeightDateMap.value.first().profileId,
                weightInKg = totalWeight / bodyWeightDateMap.value.size,
                muscleMassInKg = muscleMass / bodyWeightDateMap.value.size,
                boneMassInKg = boneMass / bodyWeightDateMap.value.size,
                bodyFatPercentage = bodyFat / bodyWeightDateMap.value.size,
                bodyWaterInPercentage = bodyWater / bodyWeightDateMap.value.size,
            )
        }

    private fun mapMonth(bodyWeights: List<BodyWeight>, settings: Settings): List<BodyWeight> {
        return bodyWeights.groupBy { bodyWeight -> bodyWeight.date.year to bodyWeight.date.month }
            .map { bodyWeightDateMap ->
                var totalWeight = 0.0
                var boneMass = 0.0
                var muscleMass = 0.0
                var bodyFat = 0.0
                var bodyWater = 0.0
                bodyWeightDateMap.value.forEach {
                    totalWeight += it.weightInKg
                    boneMass += it.boneMassInKg ?: 0.0
                    muscleMass += it.muscleMassInKg ?: 0.0
                    bodyFat += it.bodyFatPercentage ?: 0.0
                    bodyWater += it.bodyWaterInPercentage ?: 0.0
                }

                totalWeight = weightUnitConverter.convert(
                    totalWeight, WeightUnit.KG, settings.weightUnit
                )!!.roundToDecimals(2)
                muscleMass = weightUnitConverter.convert(
                    muscleMass, WeightUnit.KG, settings.weightUnit
                )!!.roundToDecimals(2)
                boneMass = weightUnitConverter.convert(
                    boneMass, WeightUnit.KG, settings.weightUnit
                )!!.roundToDecimals(2)

                BodyWeight(
                    id = Uuid.random(),
                    date = bodyWeightDateMap.value.first().date.firstDayOfMonth(),
                    profileId = bodyWeightDateMap.value.first().profileId,
                    weightInKg = totalWeight / bodyWeightDateMap.value.size,
                    muscleMassInKg = muscleMass / bodyWeightDateMap.value.size,
                    boneMassInKg = boneMass / bodyWeightDateMap.value.size,
                    bodyFatPercentage = bodyFat / bodyWeightDateMap.value.size,
                    bodyWaterInPercentage = bodyWater / bodyWeightDateMap.value.size,
                )
            }
    }

    private fun mapYear(bodyWeights: List<BodyWeight>, settings: Settings): List<BodyWeight> {
        return bodyWeights.groupBy { bodyWeight -> bodyWeight.date.year }
            .map { bodyWeightDateMap ->
                var totalWeight = 0.0
                var boneMass = 0.0
                var muscleMass = 0.0
                var bodyFat = 0.0
                var bodyWater = 0.0
                bodyWeightDateMap.value.forEach {
                    totalWeight += it.weightInKg
                    boneMass += it.boneMassInKg ?: 0.0
                    muscleMass += it.muscleMassInKg ?: 0.0
                    bodyFat += it.bodyFatPercentage ?: 0.0
                    bodyWater += it.bodyWaterInPercentage ?: 0.0
                }

                totalWeight = weightUnitConverter.convert(
                    totalWeight, WeightUnit.KG, settings.weightUnit
                )!!.roundToDecimals(2)
                muscleMass = weightUnitConverter.convert(
                    muscleMass, WeightUnit.KG, settings.weightUnit
                )!!.roundToDecimals(2)
                boneMass = weightUnitConverter.convert(
                    boneMass, WeightUnit.KG, settings.weightUnit
                )!!.roundToDecimals(2)

                BodyWeight(
                    id = Uuid.random(),
                    date = bodyWeightDateMap.value.first().date.firstDayOfYear(),
                    profileId = bodyWeightDateMap.value.first().profileId,
                    weightInKg = totalWeight / bodyWeightDateMap.value.size,
                    muscleMassInKg = muscleMass / bodyWeightDateMap.value.size,
                    boneMassInKg = boneMass / bodyWeightDateMap.value.size,
                    bodyFatPercentage = bodyFat / bodyWeightDateMap.value.size,
                    bodyWaterInPercentage = bodyWater / bodyWeightDateMap.value.size,
                )
            }
    }

    fun toVerticalStackedBodyWeightData(
        bodyWeights: List<BodyWeight>
    ): List<VerticalBarPlotStackedPointEntry<LocalDate, Double>> {
        var maxWeight = 0.0

        return bodyWeights.map { bodyWeight ->
            val totalWeight = bodyWeight.weightInKg
            maxWeight = max(maxWeight, totalWeight)

            val boneMass = bodyWeight.boneMassInKg ?: 0.0
            val muscleMass = bodyWeight.muscleMassInKg ?: 0.0
            val bodyFat =
                (totalWeight * (bodyWeight.bodyFatPercentage ?: 0.0) / 100).roundToDecimals(
                    2
                )
            val bodyWater =
                (totalWeight * (bodyWeight.bodyWaterInPercentage ?: 0.0) / 100).roundToDecimals(
                    2
                )
            DefaultVerticalBarPlotStackedPointEntry(
                bodyWeight.date, 0.0, listOf(
                    boneMass,
                    boneMass + muscleMass,
                    boneMass + muscleMass + bodyFat,
                    boneMass + muscleMass + bodyFat + bodyWater,
                    totalWeight
                )
            )
        }
    }

    fun toVerticalStackedAreaBodyWeightData(
        bodyWeights: List<BodyWeight>
    ): List<List<Double>> {
        val totalWeights = mutableListOf<Double>()
        val boneMasses = mutableListOf<Double>()
        val muscleMasses = mutableListOf<Double>()
        val bodyFats = mutableListOf<Double>()
        val bodyWaters = mutableListOf<Double>()
        var maxWeight = 0.0
        for (bodyWeight in bodyWeights) {
            val totalWeight = bodyWeight.weightInKg
            maxWeight = max(maxWeight, totalWeight)

            val boneMass = bodyWeight.boneMassInKg ?: 0.0
            val muscleMass = bodyWeight.muscleMassInKg ?: 0.0
            val bodyFat =
                (totalWeight * (bodyWeight.bodyFatPercentage ?: 0.0) / 100).roundToDecimals(
                    2
                )
            val bodyWater =
                (totalWeight * (bodyWeight.bodyWaterInPercentage ?: 0.0) / 100).roundToDecimals(
                    2
                )
            val restWeight = max(
                totalWeight - boneMass - muscleMass - bodyFat - bodyWater, 0.0
            ).roundToDecimals(2)

            boneMasses.add(boneMass)
            muscleMasses.add(muscleMass)
            bodyFats.add(bodyFat)
            bodyWaters.add(bodyWater)
            totalWeights.add(restWeight)

            _maxWeight.value = maxWeight.roundUpToNextTen().roundToDecimals(2)
        }
        return listOf(
            boneMasses, muscleMasses, bodyFats, bodyWaters, totalWeights
        )
    }

    fun dates(bodyWeights: List<BodyWeight>): List<LocalDate> {
        return bodyWeights.map { it.date }
    }

    fun setViewType(type: SleepViewType) {
        _viewType.value = type
    }

    fun setStartDate(date: Instant) {
        _startDate.value = date
    }

    fun setEndDate(date: Instant) {
        _endDate.value = date
    }

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