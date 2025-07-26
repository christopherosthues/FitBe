package org.darthacheron.fitbe.health.weight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.koalaplot.core.xygraph.Point
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
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.utils.roundToDecimals
import kotlin.time.Duration.Companion.days
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class WeightOverviewViewModel(
    private val bodyWeightRepository: BodyWeightRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _viewType = MutableStateFlow(SleepViewType.WEEK)
    private val _startDate = MutableStateFlow(Clock.System.now().minus(6.days))
    private val _endDate = MutableStateFlow(Clock.System.now())

    val viewType: StateFlow<SleepViewType> = _viewType
    val startDate: StateFlow<Instant> = _startDate
    val endDate: StateFlow<Instant> = _endDate

    @OptIn(ExperimentalCoroutinesApi::class)
    val bodyWeights: StateFlow<List<Point<LocalDate, Double>>> =
        combine(_startDate, _endDate, settingsRepository.getSettingsFlow()) { start, end, settings ->
            bodyWeightRepository.getEntries(start, end, settings.selectedProfileId!!)
        }.flatMapLatest { it }
            .map { s ->
                s.map { value ->
                    Point(
                        value.date,
                        value.weightInKg.roundToDecimals(2)
                    )
                }
            }
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    // TODO: return List<Point<X, Y>> -> X = date, y = sleep

    fun setViewType(type: SleepViewType) {
        _viewType.value = type
    }
    fun setStartDate(date: Instant) { _startDate.value = date }
    fun setEndDate(date: Instant) { _endDate.value = date }

    fun addBodyWeight(
        date: LocalDate,
        weightInKg: Double,
        bodyFatPercentage: Double,
        muscleMassInKg: Double,
        boneMassInKg: Double,
        bodyWaterInPercentage: Double
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