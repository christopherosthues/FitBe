package org.darthacheron.fitbe.health.beverages


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import org.darthacheron.fitbe.components.date.DateRange
import org.darthacheron.fitbe.components.date.DateUnit
import org.darthacheron.fitbe.profile.ProfileDefaults
import org.darthacheron.fitbe.profile.ProfileRepository
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.utils.firstDayOfIsoWeek
import org.darthacheron.fitbe.utils.firstDayOfMonth
import org.darthacheron.fitbe.utils.firstDayOfYear
import org.darthacheron.fitbe.utils.isoWeekAndYear
import org.darthacheron.fitbe.utils.minusOne
import org.darthacheron.fitbe.utils.plusOne
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.days
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalUuidApi::class)
class BeverageOverviewViewModel(
    private val beverageRepository: BeverageRepository,
    private val settingsRepository: SettingsRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {
    private val _dateRange = MutableStateFlow(
        DateRange(
            Clock.System.now().minus(6.days),
            Clock.System.now(),
            DateUnit.DAY
        )
    )

    val dateRange: StateFlow<DateRange> = _dateRange

    val targetBeverages: StateFlow<UInt> = settingsRepository.getSettingsFlow()
        .flatMapLatest { settings ->
            val profileId = settings.selectedProfileId
            if (profileId != null) {
                profileRepository.getProfileFlowById(profileId)
                    .map { profile -> profile?.targetBeverageInMilliliter ?: ProfileDefaults.BEVERAGE }
            } else {
                flowOf(ProfileDefaults.BEVERAGE)
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, ProfileDefaults.BEVERAGE)

    @OptIn(ExperimentalCoroutinesApi::class)
    val beverages: StateFlow<List<BeverageOverview>> = combine(
        _dateRange,
        settingsRepository.getSettingsFlow()
    ) { range, settings ->
        Pair(settings, range.dateUnit) to beverageRepository.getBeveragesOverview(
            range.startDate,
            range.endDate,
            settings.selectedProfileId!!
        )
    }.flatMapLatest { (settingsDateUnit, stepsFlow) ->
        stepsFlow.map { beverages ->
            when (settingsDateUnit.second) {
                DateUnit.DAY -> mapDay(beverages)
                DateUnit.WEEK -> mapWeek(beverages)
                DateUnit.MONTH -> mapMonth(beverages)
                DateUnit.YEAR -> mapYear(beverages)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, listOf())

    val maxBeverages: StateFlow<UInt> = beverages
        .map { beverages ->
            if (beverages.isEmpty()) ProfileDefaults.BEVERAGE else beverages.maxOf { it.amount }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, ProfileDefaults.BEVERAGE)

    private fun mapDay(beverages: List<BeverageOverview>): List<BeverageOverview> {
        return beverages
    }

    private fun <K> aggregateStepsByPeriod(
        beverages: List<BeverageOverview>,
        groupKeySelector: (BeverageOverview) -> K,
        representativeDateSelector: (List<BeverageOverview>) -> LocalDate
    ): List<BeverageOverview> {
        if (beverages.isEmpty()) return emptyList()

        return beverages.groupBy(groupKeySelector)
            .mapNotNull { (_, group) ->
                if (group.isEmpty()) return@mapNotNull null

                val groupSize = group.size
                val avgSteps = group.sumOf { it.amount }.toDouble() / groupSize

                BeverageOverview(
                    dateUtc = representativeDateSelector(group),
                    amount = avgSteps.roundToInt().toUInt(),
                )
            }
    }

    private fun mapWeek(
        beverages: List<BeverageOverview>,
    ): List<BeverageOverview> {
        return aggregateStepsByPeriod(
            beverages = beverages,
            groupKeySelector = { it.dateUtc.isoWeekAndYear() },
            representativeDateSelector = { group -> group.first().dateUtc.firstDayOfIsoWeek() }
        )
    }

    private fun mapMonth(beverages: List<BeverageOverview>): List<BeverageOverview> {
        return aggregateStepsByPeriod(
            beverages = beverages,
            groupKeySelector = { it.dateUtc.year to it.dateUtc.month },
            representativeDateSelector = { group -> group.first().dateUtc.firstDayOfMonth() }
        )
    }

    private fun mapYear(beverages: List<BeverageOverview>): List<BeverageOverview> {
        return aggregateStepsByPeriod(
            beverages = beverages,
            groupKeySelector = { it.dateUtc.year },
            representativeDateSelector = { group -> group.first().dateUtc.firstDayOfYear() }
        )
    }

    fun movePast() {
        val range = dateRange.value.minusOne()
        setRange(range)
    }

    fun moveFuture() {
        val range = dateRange.value.plusOne()
        setRange(range)
    }

    fun dates(beverages: List<BeverageOverview>): List<LocalDate> {
        return beverages.map { it.dateUtc }
    }

    fun setRange(startDate: Instant, endDate: Instant, dateUnit: DateUnit) {
        _dateRange.value = DateRange(startDate, endDate, dateUnit)
    }

    fun setRange(range: DateRange) {
        _dateRange.value = range
    }
}