package org.darthacheron.fitbe.health.weight

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.darthacheron.fitbe.components.date.DateUnit
import org.darthacheron.fitbe.health.OverviewViewModel
import org.darthacheron.fitbe.profile.ProfileDefaults
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
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class)
class WeightOverviewViewModel(
    private val bodyWeightRepository: BodyWeightRepository,
    settingsRepository: SettingsRepository,
    profileRepository: ProfileRepository,
    private val weightUnitConverter: WeightUnitConverter,
) : OverviewViewModel<BodyWeight>(settingsRepository, profileRepository) {
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
        dateRange,
        settingsRepository.getSettingsFlow()
    ) { range, settings ->
        Pair(settings, range.dateUnit) to bodyWeightRepository.getEntries(
            range.startDate,
            range.endDate,
            settings.selectedProfileId!!
        )
    }.flatMapLatest { (settingsDateUnit, bodyWeightFlow) ->
        bodyWeightFlow.map { bodyWeights ->
            when (settingsDateUnit.second) {
                DateUnit.DAY -> mapDay(bodyWeights, settingsDateUnit.first)
                DateUnit.WEEK -> mapWeek(bodyWeights, settingsDateUnit.first)
                DateUnit.MONTH -> mapMonth(bodyWeights, settingsDateUnit.first)
                DateUnit.YEAR -> mapYear(bodyWeights, settingsDateUnit.first)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, listOf())

    val maxWeight: StateFlow<Double> = bodyWeights.map { bodyWeights ->
                bodyWeights.maxOfOrNull { it.weightInKg }
                    ?.roundUpToNextTen()
                    ?.roundToDecimals(2) ?: ProfileDefaults.MAX_BODY_WEIGHT
            }.stateIn(viewModelScope, SharingStarted.Lazily, ProfileDefaults.MAX_BODY_WEIGHT)

    private fun mapDay(bodyWeights: List<BodyWeight>, settings: Settings): List<BodyWeight> {
        return bodyWeights.map {
            it.copy(
                weightInKg = weightUnitConverter.convert(
                    it.weightInKg, WeightUnit.KG, settings.weightUnit
                )?.roundToDecimals(2) ?: it.weightInKg, // Fallback if conversion is null
                muscleMassInKg = it.muscleMassInKg?.let { mm ->
                    weightUnitConverter.convert(mm, WeightUnit.KG, settings.weightUnit)?.roundToDecimals(2) ?: mm
                },
                boneMassInKg = it.boneMassInKg?.let { bm ->
                    weightUnitConverter.convert(bm, WeightUnit.KG, settings.weightUnit)?.roundToDecimals(2) ?: bm
                }
            )
        }
    }

    private fun <K> aggregateBodyWeightsByPeriod(
        bodyWeights: List<BodyWeight>,
        settings: Settings,
        groupKeySelector: (BodyWeight) -> K,
        representativeDateSelector: (List<BodyWeight>) -> LocalDate
    ): List<BodyWeight> {
        if (bodyWeights.isEmpty()) return emptyList()

        return bodyWeights.groupBy(groupKeySelector)
            .mapNotNull { (_, group) ->
                if (group.isEmpty()) return@mapNotNull null

                val groupSize = group.size
                val avgWeightInKg = group.sumOf { it.weightInKg } / groupSize

                val (totalMuscleMass, muscleMassCount) = group.fold(0.0 to 0) { acc, bw ->
                    bw.muscleMassInKg?.let { (acc.first + it) to (acc.second + 1) } ?: acc
                }
                val avgMuscleMassInKg = if (muscleMassCount > 0) totalMuscleMass / muscleMassCount else null

                val (totalBoneMass, boneMassCount) = group.fold(0.0 to 0) { acc, bw ->
                    bw.boneMassInKg?.let { (acc.first + it) to (acc.second + 1) } ?: acc
                }
                val avgBoneMassInKg = if (boneMassCount > 0) totalBoneMass / boneMassCount else null

                val (totalBodyFat, bodyFatCount) = group.fold(0.0 to 0) { acc, bw ->
                    bw.bodyFatPercentage?.let { (acc.first + it) to (acc.second + 1) } ?: acc
                }
                val avgBodyFatPercentage = if (bodyFatCount > 0) totalBodyFat / bodyFatCount else null

                val (totalBodyWater, bodyWaterCount) = group.fold(0.0 to 0) { acc, bw ->
                    bw.bodyWaterInPercentage?.let { (acc.first + it) to (acc.second + 1) } ?: acc
                }
                val avgBodyWaterInPercentage = if (bodyWaterCount > 0) totalBodyWater / bodyWaterCount else null

                BodyWeight(
                    id = Uuid.random(),
                    profileId = group.first().profileId,
                    dateUtc = representativeDateSelector(group),
                    weightInKg = weightUnitConverter.convert(
                        avgWeightInKg, WeightUnit.KG, settings.weightUnit
                    )?.roundToDecimals(2) ?: avgWeightInKg,
                    muscleMassInKg = avgMuscleMassInKg?.let {
                        weightUnitConverter.convert(it, WeightUnit.KG, settings.weightUnit)?.roundToDecimals(2) ?: it
                    },
                    boneMassInKg = avgBoneMassInKg?.let {
                        weightUnitConverter.convert(it, WeightUnit.KG, settings.weightUnit)?.roundToDecimals(2) ?: it
                    },
                    bodyFatPercentage = avgBodyFatPercentage?.roundToDecimals(2),
                    bodyWaterInPercentage = avgBodyWaterInPercentage?.roundToDecimals(2)
                )
            }
    }

    private fun mapWeek(bodyWeights: List<BodyWeight>, settings: Settings): List<BodyWeight> {
        return aggregateBodyWeightsByPeriod(
            bodyWeights = bodyWeights,
            settings = settings,
            groupKeySelector = { it.dateUtc.isoWeekAndYear() },
            representativeDateSelector = { group -> group.first().dateUtc.firstDayOfIsoWeek() }
        )
    }

    private fun mapMonth(bodyWeights: List<BodyWeight>, settings: Settings): List<BodyWeight> {
        return aggregateBodyWeightsByPeriod(
            bodyWeights = bodyWeights,
            settings = settings,
            groupKeySelector = { it.dateUtc.year to it.dateUtc.month },
            representativeDateSelector = { group -> group.first().dateUtc.firstDayOfMonth() }
        )
    }

    private fun mapYear(bodyWeights: List<BodyWeight>, settings: Settings): List<BodyWeight> {
        return aggregateBodyWeightsByPeriod(
            bodyWeights = bodyWeights,
            settings = settings,
            groupKeySelector = { it.dateUtc.year },
            representativeDateSelector = { group -> group.first().dateUtc.firstDayOfYear() }
        )
    }

   override fun dates(list: List<BodyWeight>): List<LocalDate> {
        return list.map { it.dateUtc }
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
