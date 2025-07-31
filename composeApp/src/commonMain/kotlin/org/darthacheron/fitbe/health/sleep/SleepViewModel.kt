package org.darthacheron.fitbe.health.sleep

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
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.components.DateUnit
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.utils.roundToDecimals
import kotlin.time.Duration.Companion.days
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalTime::class)
class SleepViewModel(
    private val repository: SleepRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _viewType = MutableStateFlow(DateUnit.WEEK)
    private val _startDate = MutableStateFlow(Clock.System.now().minus(6.days))
    private val _endDate = MutableStateFlow(Clock.System.now())

    val viewType: StateFlow<DateUnit> = _viewType
    val startDate: StateFlow<Instant> = _startDate
    val endDate: StateFlow<Instant> = _endDate

    @OptIn(ExperimentalCoroutinesApi::class, ExperimentalUuidApi::class)
    val sleeps: StateFlow<List<Point<LocalDate, Double>>> =
        combine(_startDate, _endDate, settingsRepository.getSettingsFlow()) { start, end, settings ->
            repository.getSleepsBetween(start, end, settings.selectedProfileId!!)
        }.flatMapLatest { it }
            .map { s ->
                s.map { value ->
                    Point(
                        value.dateUtc.toLocalDateTime(TimeZone.currentSystemDefault()).date,
                        (value.hours.toDouble() + value.minutes.toDouble() / 60).roundToDecimals(2)
                    )
                }
            }
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    // TODO: return List<Point<X, Y>> -> X = date, y = sleep

    fun setViewType(type: DateUnit) {
        _viewType.value = type
    }
    fun setStartDate(date: Instant) { _startDate.value = date }
    fun setEndDate(date: Instant) { _endDate.value = date }

    @OptIn(ExperimentalUuidApi::class)
    fun addSleep(hours: UInt, minutes: UInt, date: Instant) {
        viewModelScope.launch {
            val settings = settingsRepository.getSettings()
            repository.addSleep(
                SleepEntity(
                    profileId = settings.selectedProfileId!!,
                    hours = hours.toInt(),
                    minutes = minutes.toInt(),
                    dateUtc = date
                )
            )
        }
    }
}