package org.darthacheron.fitbe.health.sleep

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi

enum class SleepViewType { WEEK, MONTH, YEAR }

@OptIn(ExperimentalTime::class)
class SleepViewModel(private val repository: SleepRepository) : ViewModel() {
    private val _viewType = MutableStateFlow(SleepViewType.WEEK)
    private val _startDate = MutableStateFlow(Clock.System.now()
        .toLocalDateTime(TimeZone.UTC).date.minus(1, DateTimeUnit.WEEK))
    private val _endDate = MutableStateFlow(Clock.System.now()
        .toLocalDateTime(TimeZone.UTC).date)

    val viewType: StateFlow<SleepViewType> = _viewType
    val startDate: StateFlow<LocalDate> = _startDate
    val endDate: StateFlow<LocalDate> = _endDate

    val sleeps: StateFlow<List<Sleep>> = combine(_startDate, _endDate) { start, end ->
        repository.getSleepsBetween(
            start.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds(),
            end.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
        )
    }.flatMapLatest { it }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    // TODO: return List<Point<X, Y>> -> X = date, y = sleep

    fun setViewType(type: SleepViewType) { _viewType.value = type }
    fun setStartDate(date: LocalDate) { _startDate.value = date }
    fun setEndDate(date: LocalDate) { _endDate.value = date }

    @OptIn(ExperimentalUuidApi::class)
    fun addSleep(hours: Int, minutes: Int, date: LocalDate) {
        viewModelScope.launch {
            repository.addSleep(
                SleepEntity(
                    hours = hours,
                    minutes = minutes,
                    dateUtc = date.toString()
                )
            )
        }
    }
}