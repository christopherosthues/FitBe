package org.darthacheron.fitbe.health.sleep

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import org.darthacheron.fitbe.health.componenets.AddDialogViewModel

class AddSleepDialogViewModel : AddDialogViewModel<AddSleepDialogUiState>() {
    override val uiState = MutableStateFlow(AddSleepDialogUiState())

    override fun dismissDialog() {
        uiState.update { AddSleepDialogUiState() }
    }

    fun onStartDateChange(startDate: LocalDate) {
        uiState.update { it.copy(startDateTime = LocalDateTime(startDate, it.startDateTime.time)) }
    }

    fun onStartTimeChange(startTime: LocalTime) {
        uiState.update { it.copy(startDateTime = LocalDateTime(it.startDateTime.date, startTime)) }
    }

    fun onEndDateChange(endDate: LocalDate) {
        uiState.update { it.copy(startDateTime = LocalDateTime(endDate, it.endDateTime.time)) }
    }

    fun onEndTimeChange(endTime: LocalTime) {
        uiState.update { it.copy(startDateTime = LocalDateTime(it.endDateTime.date, endTime)) }
    }
}