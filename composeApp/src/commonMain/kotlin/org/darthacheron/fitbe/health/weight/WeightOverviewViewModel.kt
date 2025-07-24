package org.darthacheron.fitbe.health.weight

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.settings.SettingsRepository
import kotlin.time.Duration.Companion.days
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class WeightOverviewViewModel(
    private val bodyWeightRepository: BodyWeightRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _groupedWeights = MutableStateFlow<List<GroupedWeight>>(emptyList())
    val groupedWeights: StateFlow<List<GroupedWeight>> = _groupedWeights.asStateFlow()

    var selectedGrouping by mutableStateOf(Grouping.DAYS)
    var currentRangeStart by mutableStateOf(Clock.System.now().minus(7.days).toLocalDateTime(TimeZone.UTC).date.atStartOfDayIn(
        TimeZone.UTC))
    var currentRangeEnd by mutableStateOf(Clock.System.now().toLocalDateTime(TimeZone.UTC).date.atStartOfDayIn(
        TimeZone.UTC))

    init {
        viewModelScope.launch {
            settingsRepository.getSettingsFlow().collectLatest { settings ->
                settings.selectedProfileId?.let { profileId ->
                    bodyWeightRepository.getEntries(currentRangeStart, currentRangeEnd, profileId)
                        .map { groupEntries(it, selectedGrouping) }
                        .collect { _groupedWeights.value = it }
                }
            }
        }
    }

    suspend fun loadChartData() {
        val profileId = settingsRepository.getSettings().selectedProfileId ?: return
        val entries = bodyWeightRepository.getEntries(currentRangeStart, currentRangeEnd, profileId)
        chartData = groupEntries(entries, selectedGrouping)
    }

    fun goBack() {
        adjustRange(-1)
    }

    fun goForward() {
        adjustRange(1)
    }

    private fun adjustRange(step: Int) {
        when (selectedGrouping) {
            Grouping.DAYS -> {
                currentRangeStart = currentRangeStart.plus((step * 7).days)
                currentRangeEnd = currentRangeEnd.plus((step * 7).days)
            }
            Grouping.WEEKS -> {
                currentRangeStart = currentRangeStart.plusWeeks(step)
                currentRangeEnd = currentRangeEnd.plusWeeks(step)
            }
            Grouping.MONTHS -> {
                currentRangeStart = currentRangeStart.plusMonths(step)
                currentRangeEnd = currentRangeEnd.plusMonths(step)
            }
            Grouping.YEARS -> {
                currentRangeStart = currentRangeStart.plusYears(step)
                currentRangeEnd = currentRangeEnd.plusYears(step)
            }
        }
        viewModelScope.launch { loadChartData() }
    }

    private fun groupEntries(entries: List<BodyWeight>, grouping: Grouping): List<GroupedWeight> {
        return entries
            .groupBy {
                when (grouping) {
                    Grouping.DAYS -> it.date
                    Grouping.WEEKS -> it.date.with(DayOfWeek.MONDAY)
                    Grouping.MONTHS -> it.date.withDayOfMonth(1)
                    Grouping.YEARS -> it.date.withDayOfYear(1)
                }
            }
            .map { (date, dayEntries) ->
                val avg = { selector: (BodyWeight) -> Double ->
                    dayEntries.map(selector)
                        .filter { it >= 0 }
                        .takeIf { it.isNotEmpty() }?.average() ?: -1.0
                }
                GroupedWeight(
                    date = date,
                    avgWeight = dayEntries.map { it.weightInKg }.average(),
                    avgFat = avg { it.bodyFatPercentage },
                    avgMuscle = avg { it.muscleMassInKg },
                    avgBone = avg { it.boneMassInKg },
                    avgWater = avg { it.bodyWaterInPercentage }
                )
            }.sortedBy { it.date }
    }
}

enum class Grouping { DAYS, WEEKS, MONTHS, YEARS }

data class GroupedWeight(
    val date: LocalDate,
    val avgWeight: Double,
    val avgFat: Double,
    val avgMuscle: Double,
    val avgBone: Double,
    val avgWater: Double
)