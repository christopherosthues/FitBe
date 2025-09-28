package org.darthacheron.fitbe.health.beverages

import androidx.lifecycle.viewModelScope
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.beverages_overview_error_loading
import fitbe.composeapp.generated.resources.beverages_overview_error_saving
import fitbe.composeapp.generated.resources.top_bar_title_beverages_overview
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
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
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.components.date.DateUnit
import org.darthacheron.fitbe.health.OverviewViewModel
import org.darthacheron.fitbe.navigation.Screen
import org.darthacheron.fitbe.profile.ProfileDefaults
import org.darthacheron.fitbe.profile.ProfileRepository
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.ui.TopBarManager
import org.darthacheron.fitbe.utils.firstDayOfIsoWeek
import org.darthacheron.fitbe.utils.firstDayOfMonth
import org.darthacheron.fitbe.utils.firstDayOfYear
import org.darthacheron.fitbe.utils.isoWeekAndYear
import org.jetbrains.compose.resources.StringResource
import kotlin.math.roundToInt
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalUuidApi::class)
class BeverageOverviewViewModel(
    private val beverageRepository: BeverageRepository,
    settingsRepository: SettingsRepository,

    profileRepository: ProfileRepository,
    topBarManager: TopBarManager
) : OverviewViewModel<BeverageOverviewError, BeverageOverviewUiState>(settingsRepository, topBarManager) {
    override val title: StringResource
        get() = Res.string.top_bar_title_beverages_overview

    override val backNavigationIconVisible: Boolean?
        get() = true

    override val bottomBarSelected: Screen?
        get() = Screen.Health

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

    private val beveragesFlow: StateFlow<List<BeverageOverview>> = combine(
        dateRange,
        settingsRepository.getSettingsFlow()
    ) { range, settings ->
        settings.selectedProfileId?.let { profileId ->
            Pair(settings, range.dateUnit) to beverageRepository.getBeveragesOverview(
                range.startDate,
                range.endDate,
                profileId
            )
        } ?: (Pair(settings, range.dateUnit) to flowOf(emptyList()))
    }.flatMapLatest { (settingsDateUnit, beveragesSource) ->
        beveragesSource.map { beverages ->
            val beveragesInMl = beverages.map { it.copy(amount = it.unit.toMilliliter(it.amount)) }
            when (settingsDateUnit.second) {
                DateUnit.DAY -> mapDay(beveragesInMl)
                DateUnit.WEEK -> mapWeek(beveragesInMl)
                DateUnit.MONTH -> mapMonth(beveragesInMl)
                DateUnit.YEAR -> mapYear(beveragesInMl)
            }
        }
    }
        .onStart {
            _isLoading.value = true
            _errorMessage.value = null
        }
        .catch {
            _isLoading.value = false
            _errorMessage.value = Res.string.beverages_overview_error_loading
            emit(emptyList())
        }
        .map { beverages ->
            _isLoading.value = false
            beverages
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    override val uiState: StateFlow<BeverageOverviewUiState> = combine(
        beveragesFlow,
        _isLoading,
        _errorMessage,
    ) { beverages, isLoading, errorMessage ->
        BeverageOverviewUiState(
            isLoading = isLoading,
            beverages = beverages,
            dates = beverages.map { it.dateUtc },
            error = BeverageOverviewError(errorMessage),
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BeverageOverviewUiState(isLoading = true)
    )

    val maxBeverages: StateFlow<UInt> = uiState
        .map { it.beverages }
        .map { beverages ->
            if (beverages.isEmpty()) ProfileDefaults.BEVERAGE else beverages.maxOfOrNull { it.amountMl }
                ?: ProfileDefaults.BEVERAGE
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, ProfileDefaults.BEVERAGE)

    private fun mapDay(beverages: List<Beverage>): List<BeverageOverview> {
        return beverages.groupBy { it.dateUtc.toLocalDateTime(TimeZone.UTC).date }
            .map { (date, beverageEntries) ->
                BeverageOverview(
                    date,
                    beverageEntries.sumOf { it.amount.toInt() }.toUInt()
                )
            }
    }

    private fun <K> aggregateStepsByPeriod(
        beverages: List<Beverage>,
        groupKeySelector: (Beverage) -> K,
        representativeDateSelector: (List<Beverage>) -> LocalDate
    ): List<BeverageOverview> {
        if (beverages.isEmpty()) return emptyList()

        return beverages.groupBy(groupKeySelector)
            .mapNotNull { (_, group) ->
                if (group.isEmpty()) return@mapNotNull null

                val avgAmount = group.sumOf { it.amount.toDouble() } / group.size

                BeverageOverview(
                    dateUtc = representativeDateSelector(group),
                    amountMl = avgAmount.roundToInt().toUInt(),
                )
            }
    }

    private fun mapWeek(
        beverages: List<Beverage>,
    ): List<BeverageOverview> {
        return aggregateStepsByPeriod(
            beverages = beverages,
            groupKeySelector = { it.dateUtc.toLocalDateTime(TimeZone.UTC).date.isoWeekAndYear() },
            representativeDateSelector = { group -> group.first().dateUtc.toLocalDateTime(TimeZone.UTC).date.firstDayOfIsoWeek() }
        )
    }

    private fun mapMonth(beverages: List<Beverage>): List<BeverageOverview> {
        return aggregateStepsByPeriod(
            beverages = beverages,
            groupKeySelector = { val date = it.dateUtc.toLocalDateTime(TimeZone.UTC).date; date.year to date.month },
            representativeDateSelector = { group -> group.first().dateUtc.toLocalDateTime(TimeZone.UTC).date.firstDayOfMonth() }
        )
    }

    private fun mapYear(beverages: List<Beverage>): List<BeverageOverview> {
        return aggregateStepsByPeriod(
            beverages = beverages,
            groupKeySelector = { it.dateUtc.toLocalDateTime(TimeZone.UTC).date.year },
            representativeDateSelector = { group -> group.first().dateUtc.toLocalDateTime(TimeZone.UTC).date.firstDayOfYear() }
        )
    }

    fun saveBeverage(amount: Double, name: String, unit: FluidUnit, date: Instant) {
        viewModelScope.launch {
            try {
                val profileId = settingsRepository.getSettings().selectedProfileId

                if (profileId == null) {
                    _errorMessage.value = Res.string.beverages_overview_error_saving
                    return@launch
                }

                beverageRepository.addBeverage(
                    Beverage(
                        id = Uuid.random(),
                        amount = amount,
                        beverage = name,
                        unit = unit,
                        dateUtc = date,
                        profileId = profileId
                    )
                )
            } catch (e: Exception) {
                _errorMessage.value = Res.string.beverages_overview_error_saving
            }
        }
    }
}
