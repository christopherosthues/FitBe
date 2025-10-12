package org.darthacheron.fitbe.health.sleep

import androidx.lifecycle.viewModelScope
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.sleep_add_dialog_error_end_time
import fitbe.composeapp.generated.resources.sleep_add_dialog_error_start_time
import fitbe.composeapp.generated.resources.sleep_add_dialog_error_time_interval
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.darthacheron.fitbe.health.components.AddDialogViewModel
import org.darthacheron.fitbe.settings.SettingsRepository
import org.jetbrains.compose.resources.StringResource
import kotlin.uuid.ExperimentalUuidApi

class AddSleepDialogViewModel(
    private val settingsRepository: SettingsRepository,
    private val sleepRepository: SleepRepository
) : AddDialogViewModel<AddSleepDialogUiState>() {
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

    @OptIn(ExperimentalUuidApi::class)
    private fun validateDateTimes() {
        viewModelScope.launch {
            var startTimeError: StringResource? = null
            var endTimeError: StringResource? = null

            val startDateTime = uiState.value.startDateTime
            val endDateTime = uiState.value.endDateTime

            if (startDateTime >= endDateTime) {
                startTimeError = Res.string.sleep_add_dialog_error_start_time
                endTimeError = Res.string.sleep_add_dialog_error_end_time
            } else {
                val startInstant = startDateTime.toInstant(TimeZone.currentSystemDefault())
                val endInstant = endDateTime.toInstant(TimeZone.currentSystemDefault())
                val profileId = settingsRepository.getSettings().selectedProfileId ?: return@launch

                val potentialOverlaps = sleepRepository.getSleepsBetween(startInstant, endInstant, profileId).first()
                val hasActualOverlap =
                    potentialOverlaps.any { existingSleep ->
                        // Check for actual time collision: (StartA < EndB) and (EndA > StartB)
                        startInstant < existingSleep.end && endInstant > existingSleep.start
                    }

                if (hasActualOverlap) {
                    startTimeError = Res.string.sleep_add_dialog_error_time_interval
                    endTimeError = Res.string.sleep_add_dialog_error_time_interval
                }
            }

            uiState.update { it.copy(startDateTimeError = startTimeError, endDateTimeError = endTimeError) }
        }
    }
}