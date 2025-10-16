package org.darthacheron.fitbe.health.beverages

import androidx.lifecycle.viewModelScope
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.beverages_overview_content_description_add_beverage
import fitbe.composeapp.generated.resources.beverages_overview_error_loading
import fitbe.composeapp.generated.resources.beverages_overview_error_saving
import fitbe.composeapp.generated.resources.top_bar_title_overview_beverages
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.components.date.DateUnit
import org.darthacheron.fitbe.health.components.OverviewViewModel
import org.darthacheron.fitbe.navigation.Screen
import org.darthacheron.fitbe.profile.ProfileDefaults
import org.darthacheron.fitbe.profile.ProfileRepository
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.ui.TopBarManager
import org.darthacheron.fitbe.utils.firstDayOfIsoWeek
import org.darthacheron.fitbe.utils.firstDayOfMonth
import org.darthacheron.fitbe.utils.firstDayOfYear
import org.darthacheron.fitbe.utils.isoWeekAndYear
import org.darthacheron.fitbe.utils.roundToDecimals
import org.jetbrains.compose.resources.StringResource
import kotlin.math.roundToInt
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalUuidApi::class)
class BeverageOverviewViewModel(
    private val beverageRepository: BeverageRepository,
    settingsRepository: SettingsRepository,
    profileRepository: ProfileRepository,
    topBarManager: TopBarManager
) : OverviewViewModel<BeverageOverviewError, BeverageOverviewUiState>(settingsRepository, topBarManager) {
    override val title: StringResource
        get() = Res.string.top_bar_title_overview_beverages

    override val backNavigationIconVisible: Boolean?
        get() = true

    override val bottomBarSelected: Screen?
        get() = Screen.Health

    override val addButtonContentDescription: StringResource
        get() = Res.string.beverages_overview_content_description_add_beverage

    val targetBeverages: StateFlow<UInt> =
        settingsRepository
            .getSettingsFlow()
            .flatMapLatest { settings ->
                val profileId = settings.selectedProfileId
                if (profileId != null) {
                    profileRepository
                        .getProfileFlowById(profileId)
                        .map { profile -> profile?.targetBeverageInMilliliter ?: ProfileDefaults.BEVERAGE }
                } else {
                    flowOf(ProfileDefaults.BEVERAGE)
                }
            }.stateIn(viewModelScope, SharingStarted.Lazily, ProfileDefaults.BEVERAGE)

    private val beveragesFlow: StateFlow<List<BeverageOverview>> =
        combine(
            dateRange,
            settingsRepository.getSettingsFlow()
        ) { range, settings ->
            settings.selectedProfileId?.let { profileId ->
                Pair(settings, range.dateUnit) to
                    beverageRepository.getBeveragesOverview(
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
        }.onStart {
            isLoading.value = true
            errorMessage.value = null
        }.catch {
            isLoading.value = false
            errorMessage.value = Res.string.beverages_overview_error_loading
            emit(emptyList())
        }.map { beverages ->
            isLoading.value = false
            beverages
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    override val uiState: StateFlow<BeverageOverviewUiState> =
        combine(
            beveragesFlow,
            isLoading,
            errorMessage
        ) { beverages, isLoading, errorMessage ->
            BeverageOverviewUiState(
                isLoading = isLoading,
                beverages = beverages,
                dates = beverages.map { it.date },
                error = BeverageOverviewError(errorMessage)
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = BeverageOverviewUiState(isLoading = true)
        )

    val maxBeverages: StateFlow<UInt> =
        uiState
            .map { it.beverages }
            .map { beverages ->
                if (beverages.isEmpty()) {
                    ProfileDefaults.BEVERAGE
                } else {
                    beverages.maxOfOrNull { it.amountMl }?.roundToInt()?.toUInt() ?: ProfileDefaults.BEVERAGE
                }
            }.stateIn(viewModelScope, SharingStarted.Lazily, ProfileDefaults.BEVERAGE)

    private fun mapDay(beverages: List<Beverage>): List<BeverageOverview> = aggregateDailyBeverages(beverages)

    private fun aggregateDailyBeverages(beverages: List<Beverage>): List<BeverageOverview> {
        if (beverages.isEmpty()) {
            return emptyList()
        }

        return beverages
            .groupBy { it.date.toLocalDateTime(TimeZone.currentSystemDefault()).date }
            .map { (date, beverageEntries) ->
                BeverageOverview(
                    date = date,
                    amountMl = beverageEntries.sumOf { it.unit.toMilliliter(it.amount) }
                )
            }
    }

    private fun <K> aggregateBeveragesByPeriod(
        beverages: List<BeverageOverview>,
        groupKeySelector: (BeverageOverview) -> K,
        representativeDateSelector: (List<BeverageOverview>) -> LocalDate,
        daysInPeriodSelector: (List<BeverageOverview>) -> Int
    ): List<BeverageOverview> {
        if (beverages.isEmpty()) return emptyList()

        return beverages
            .groupBy(groupKeySelector)
            .mapNotNull { (_, group) ->
                if (group.isEmpty()) return@mapNotNull null

                val sumBeverageAmount = group.sumOf { it.amountMl }
                val daysInPeriod = daysInPeriodSelector(group)
                if (daysInPeriod == 0) return@mapNotNull null

                val avgAmount = (sumBeverageAmount / daysInPeriod).roundToDecimals(2)

                BeverageOverview(
                    date = representativeDateSelector(group),
                    amountMl = avgAmount
                )
            }
    }

    private fun mapWeek(beverages: List<Beverage>): List<BeverageOverview> {
        val dailyBeverages = aggregateDailyBeverages(beverages)
        return aggregateBeveragesByPeriod(
            beverages = dailyBeverages,
            groupKeySelector = { it.date.isoWeekAndYear() },
            representativeDateSelector = { group -> group.first().date.firstDayOfIsoWeek() },
            daysInPeriodSelector = { _ -> 7 }
        )
    }

    private fun mapMonth(beverages: List<Beverage>): List<BeverageOverview> {
        val dailyBeverages = aggregateDailyBeverages(beverages)
        return aggregateBeveragesByPeriod(
            beverages = dailyBeverages,
            groupKeySelector = { it.date.year to it.date.month },
            representativeDateSelector = { group -> group.first().date.firstDayOfMonth() },
            daysInPeriodSelector = { group ->
                val localDate = group.first().date.firstDayOfMonth()
                val firstDay = localDate.firstDayOfMonth()
                val nextMonth =
                    if (firstDay.monthNumber == 12) {
                        LocalDate(firstDay.year + 1, 1, 1)
                    } else {
                        LocalDate(firstDay.year, firstDay.monthNumber + 1, 1)
                    }
                firstDay.daysUntil(nextMonth)
            }
        )
    }

    private fun mapYear(beverages: List<Beverage>): List<BeverageOverview> {
        val dailyBeverages = aggregateDailyBeverages(beverages)
        return aggregateBeveragesByPeriod(
            beverages = dailyBeverages,
            groupKeySelector = { it.date.year },
            representativeDateSelector = { group -> group.first().date.firstDayOfYear() },
            daysInPeriodSelector = { group ->
                val date = group.first().date.firstDayOfYear()
                val year = date.year
                val isLeap = year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)
                if (isLeap) 366 else 365
            }
        )
    }

    fun saveBeverage(
        amount: Double,
        name: String,
        unit: FluidUnit,
        date: Instant
    ) {
        viewModelScope.launch {
            try {
                val profileId = settingsRepository.getSettings().selectedProfileId

                if (profileId == null) {
                    errorMessage.value = Res.string.beverages_overview_error_saving
                    return@launch
                }

                beverageRepository.addBeverage(
                    Beverage(
                        amount = amount,
                        beverage = name,
                        unit = unit,
                        date = date,
                        profileId = profileId
                    )
                )
            } catch (e: Exception) {
                errorMessage.value = Res.string.beverages_overview_error_saving
            }
        }
    }
}