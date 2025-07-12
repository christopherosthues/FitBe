package org.darthacheron.fitbe.health.sleep

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.koalaplot.core.xygraph.Point
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.atTime
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.serializers.DayBasedDateTimeUnitSerializer
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.math.round
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi

enum class SleepViewType {
    DAY, WEEK, MONTH, YEAR;

    fun toDateTimeUnit(): DateTimeUnit {
        return when(this) {
            DAY -> DateTimeUnit.DAY
            WEEK -> DateTimeUnit.WEEK
            MONTH -> DateTimeUnit.MONTH
            YEAR -> DateTimeUnit.YEAR
        }
    }
}

@OptIn(ExperimentalTime::class)
class SleepViewModel(private val repository: SleepRepository) : ViewModel() {
    private val _viewType = MutableStateFlow(SleepViewType.WEEK)
    private val _startDate = MutableStateFlow(
        Clock.System.now().plus(-1, SleepViewType.WEEK.toDateTimeUnit(), TimeZone.UTC).plus(
            1, SleepViewType.DAY.toDateTimeUnit(),
            TimeZone.UTC
        )
    )
    private val _endDate = MutableStateFlow(Clock.System.now())

    val viewType: StateFlow<SleepViewType> = _viewType
    val startDate: StateFlow<Instant> = _startDate
    val endDate: StateFlow<Instant> = _endDate

    @OptIn(ExperimentalCoroutinesApi::class)
    val sleeps: StateFlow<List<Point<LocalDate, Double>>> =
        combine(_startDate, _endDate) { start, end ->
            repository.getSleepsBetween(start, end)
        }.flatMapLatest { it }
            .map { s ->
                s.map { value ->
                    Point(
                        value.dateUtc.toLocalDateTime(TimeZone.currentSystemDefault()).date,
                        round(((value.hours.toDouble() + value.minutes.toDouble() / 60)) * 100) / 100
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

    @OptIn(ExperimentalUuidApi::class)
    fun addSleep(hours: Int, minutes: Int, date: Instant) {
        viewModelScope.launch {
            repository.addSleep(
                SleepEntity(
                    hours = hours,
                    minutes = minutes,
                    dateUtc = date
                )
            )
        }
    }
}