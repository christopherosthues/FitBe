package org.darthacheron.fitbe.health.sleep

import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.sleep_add_dialog_error_end_time
import fitbe.composeapp.generated.resources.sleep_add_dialog_error_start_time
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import org.darthacheron.fitbe.health.componenets.AddDialogViewModel
import org.jetbrains.compose.resources.StringResource

class AddSleepDialogViewModel : AddDialogViewModel<AddSleepDialogUiState>() {
    override val uiState = MutableStateFlow(AddSleepDialogUiState())

    override fun dismissDialog() {
        uiState.update { AddSleepDialogUiState() }
    }

    fun onStartDateChange(startDate: LocalDate) {
        uiState.update { it.copy(startDateTime = LocalDateTime(startDate, it.startDateTime.time)) }
        validateDateTimes()
    }

    fun onStartTimeChange(startTime: LocalTime) {
        uiState.update { it.copy(startDateTime = LocalDateTime(it.startDateTime.date, startTime)) }
        validateDateTimes()
    }

    fun onEndDateChange(endDate: LocalDate) {
        uiState.update { it.copy(endDateTime = LocalDateTime(endDate, it.endDateTime.time)) }
        validateDateTimes()
    }

    fun onEndTimeChange(endTime: LocalTime) {
        uiState.update { it.copy(endDateTime = LocalDateTime(it.endDateTime.date, endTime)) }
        validateDateTimes()
    }

    private fun validateDateTimes() {
        var startTimeError: StringResource? = null
        var endTimeError: StringResource? = null
        if (uiState.value.startDateTime >= uiState.value.endDateTime) {
            startTimeError =  Res.string.sleep_add_dialog_error_start_time
            endTimeError = Res.string.sleep_add_dialog_error_end_time
        }
        // TODO: check if there is already a sleep on the selected date and time
        uiState.update { it.copy(startDateTimeError = startTimeError, endDateTimeError = endTimeError) }
    }
}