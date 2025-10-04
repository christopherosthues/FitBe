package org.darthacheron.fitbe.health.weight

import androidx.lifecycle.viewModelScope
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.top_bar_title_body_weights
import fitbe.composeapp.generated.resources.weight_overview_error_loading
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.components.date.DateUnit
import org.darthacheron.fitbe.health.OverviewViewModel
import org.darthacheron.fitbe.navigation.Screen
import org.darthacheron.fitbe.profile.ProfileDefaults
import org.darthacheron.fitbe.profile.ProfileRepository
import org.darthacheron.fitbe.settings.Settings
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.settings.WeightUnit
import org.darthacheron.fitbe.settings.converters.WeightUnitConverter
import org.darthacheron.fitbe.ui.TopBarManager
import org.darthacheron.fitbe.utils.firstDayOfIsoWeek
import org.darthacheron.fitbe.utils.firstDayOfMonth
import org.darthacheron.fitbe.utils.firstDayOfYear
import org.darthacheron.fitbe.utils.isoWeekAndYear
import org.darthacheron.fitbe.utils.roundToDecimals
import org.darthacheron.fitbe.utils.roundUpToNextTen
import org.jetbrains.compose.resources.StringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class)
class WeightOverviewViewModel(
    private val bodyWeightRepository: BodyWeightRepository,
    settingsRepository: SettingsRepository,
    profileRepository: ProfileRepository,
    private val weightUnitConverter: WeightUnitConverter,
    topBarManager: TopBarManager
) : OverviewViewModel<WeightOverviewError, WeightOverviewUiState>(settingsRepository, topBarManager) {
    override val title: StringResource
        get() = Res.string.top_bar_title_body_weights

    override val backNavigationIconVisible: Boolean?
        get() = true

    override val bottomBarSelected: Screen?
        get() = Screen.Health

    private val settings: Flow<Settings> = settingsRepository.getSettingsFlow()

    val targetWeight: StateFlow<Double?> = settings
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

    private val bodyWeightsDataFlow: StateFlow<List<BodyWeightOverview>> = combine(
        dateRange,
        settingsRepository.getSettingsFlow()
    ) { range, settings ->
        settings.selectedProfileId?.let {
            Pair(settings, range.dateUnit) to bodyWeightRepository.getEntries(
                range.startDate,
                range.endDate,
                it
            )
        } ?: (Pair(settings, range.dateUnit) to flowOf(emptyList()))
    }.flatMapLatest { (settingsDateUnit, bodyWeightFlow) ->
        bodyWeightFlow.map { bodyWeights ->
            when (settingsDateUnit.second) {
                DateUnit.DAY -> mapDay(bodyWeights, settingsDateUnit.first)
                DateUnit.WEEK -> mapWeek(bodyWeights, settingsDateUnit.first)
                DateUnit.MONTH -> mapMonth(bodyWeights, settingsDateUnit.first)
                DateUnit.YEAR -> mapYear(bodyWeights, settingsDateUnit.first)
            }
        }
    }
    .onStart {
        _isLoading.value = true
        _errorMessage.value = null
    }
    .catch {
        _isLoading.value = false
        _errorMessage.value = Res.string.weight_overview_error_loading
        emit(emptyList())
    }
    .map {
        _isLoading.value = false
        it
    }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf())

    override val uiState: StateFlow<WeightOverviewUiState> = combine(
        settings,
        bodyWeightsDataFlow,
        _isLoading,
        _errorMessage
    ) { settings, bodyWeights, isLoading, errorMessage ->
        WeightOverviewUiState(
            isLoading = isLoading,
            bodyWeights = bodyWeights,
            dates = bodyWeights.map { it.date },
            settings = settings,
            error = WeightOverviewError(errorMessage)
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = WeightOverviewUiState(isLoading = true)
    )

    val maxWeight: StateFlow<Double> = uiState.map { it.bodyWeights }
        .map { bodyWeights ->
            bodyWeights.maxOfOrNull { it.weight }
                ?.roundUpToNextTen()
                ?.roundToDecimals(2) ?: ProfileDefaults.MAX_BODY_WEIGHT
        }.stateIn(viewModelScope, SharingStarted.Lazily, ProfileDefaults.MAX_BODY_WEIGHT)

    private fun mapDay(bodyWeights: List<BodyWeight>, settings: Settings): List<BodyWeightOverview> {
        return aggregateBodyWeightsByPeriod(
            bodyWeights = bodyWeights,
            settings = settings,
            groupKeySelector = { it.date.toLocalDateTime(TimeZone.currentSystemDefault()).date },
            representativeDateSelector = { group -> group.first().date.toLocalDateTime(TimeZone.currentSystemDefault()).date }
        )
    }

    private fun <K> aggregateBodyWeightsByPeriod(
        bodyWeights: List<BodyWeight>,
        settings: Settings,
        groupKeySelector: (BodyWeight) -> K,
        representativeDateSelector: (List<BodyWeight>) -> LocalDate
    ): List<BodyWeightOverview> {
        if (bodyWeights.isEmpty()) return emptyList()

        return bodyWeights.groupBy(groupKeySelector)
            .mapNotNull { (_, group) ->
                if (group.isEmpty()) return@mapNotNull null

                val groupSize = group.size
                val avgWeightInKg = group.sumOf { it.weightInKg } / groupSize

                val (totalMuscleMass, muscleMassCount) = group.fold(0.0 to 0) { acc, bw ->
                    bw.muscleMassInKg?.let { (acc.first + it) to (acc.second + 1) } ?: acc
                }
                val avgMuscleMassInKg = if (muscleMassCount > 0) totalMuscleMass / muscleMassCount else 0.0

                val (totalBoneMass, boneMassCount) = group.fold(0.0 to 0) { acc, bw ->
                    bw.boneMassInKg?.let { (acc.first + it) to (acc.second + 1) } ?: acc
                }
                val avgBoneMassInKg = if (boneMassCount > 0) totalBoneMass / boneMassCount else 0.0

                val (totalBodyFat, bodyFatCount) = group.fold(0.0 to 0) { acc, bw ->
                    bw.bodyFatPercentage?.let { (acc.first + it) to (acc.second + 1) } ?: acc
                }
                val avgBodyFatPercentage = if (bodyFatCount > 0) totalBodyFat / bodyFatCount else 0.0

                val (totalBodyWater, bodyWaterCount) = group.fold(0.0 to 0) { acc, bw ->
                    bw.bodyWaterInPercentage?.let { (acc.first + it) to (acc.second + 1) } ?: acc
                }
                val avgBodyWaterInPercentage = if (bodyWaterCount > 0) totalBodyWater / bodyWaterCount else 0.0

                BodyWeightOverview(
                    date = representativeDateSelector(group),
                    weight = weightUnitConverter.convert(
                        avgWeightInKg, WeightUnit.KG, settings.weightUnit
                    )?.roundToDecimals(2) ?: avgWeightInKg,
                    muscleMass =
                        weightUnitConverter.convert(avgMuscleMassInKg, WeightUnit.KG, settings.weightUnit)?.roundToDecimals(2)!!,
                    boneMass = weightUnitConverter.convert(avgBoneMassInKg, WeightUnit.KG, settings.weightUnit)?.roundToDecimals(2)!!,
                    bodyFatPercentage = avgBodyFatPercentage.roundToDecimals(2),
                    bodyWaterPercentage = avgBodyWaterInPercentage.roundToDecimals(2)
                )
            }
    }

    private fun mapWeek(bodyWeights: List<BodyWeight>, settings: Settings): List<BodyWeightOverview> {
        return aggregateBodyWeightsByPeriod(
            bodyWeights = bodyWeights,
            settings = settings,
            groupKeySelector = { it.date.toLocalDateTime(TimeZone.currentSystemDefault()).date.isoWeekAndYear() },
            representativeDateSelector = { group -> group.first().date.toLocalDateTime(TimeZone.currentSystemDefault()).date.firstDayOfIsoWeek() }
        )
    }

    private fun mapMonth(bodyWeights: List<BodyWeight>, settings: Settings): List<BodyWeightOverview> {
        return aggregateBodyWeightsByPeriod(
            bodyWeights = bodyWeights,
            settings = settings,
            groupKeySelector = { it.date.toLocalDateTime(TimeZone.currentSystemDefault()).date.year to it.date.toLocalDateTime(TimeZone.currentSystemDefault()).date.month },
            representativeDateSelector = { group -> group.first().date.toLocalDateTime(TimeZone.currentSystemDefault()).date.firstDayOfMonth() }
        )
    }

    private fun mapYear(bodyWeights: List<BodyWeight>, settings: Settings): List<BodyWeightOverview> {
        return aggregateBodyWeightsByPeriod(
            bodyWeights = bodyWeights,
            settings = settings,
            groupKeySelector = { it.date.toLocalDateTime(TimeZone.currentSystemDefault()).date.year },
            representativeDateSelector = { group -> group.first().date.toLocalDateTime(TimeZone.currentSystemDefault()).date.firstDayOfYear() }
        )
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
            settings.selectedProfileId?.let {
                bodyWeightRepository.addBodyWeight(
                    BodyWeight(
                        profileId = it,
                        date = date.atStartOfDayIn(TimeZone.currentSystemDefault()),
                        weightInKg = weightInKg,
                        bodyFatPercentage = bodyFatPercentage,
                        muscleMassInKg = muscleMassInKg,
                        boneMassInKg = boneMassInKg,
                        bodyWaterInPercentage = bodyWaterInPercentage
                    )
                )
            }
        }
    }
}
