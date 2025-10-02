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
        uiState.update { it.copy(
            startDateTime = null,
            endDateTime = null,
            startDateTimeError = null,
            endDateTimeError = null
        ) }
    }

    fun onStartDateChange(startDate: LocalDate) {
//        uiState.update { it.copy(startDateTime = LocalDateTime(startDate, it.startDateTime?.time)) }
    }

    fun onStartTimeChange(startTime: LocalTime) {

    }

    fun onEndDateChange(endDate: LocalDate) {

    }

    fun onEndTimeChange(endTime: LocalTime) {

    }
}