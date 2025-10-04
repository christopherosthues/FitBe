package org.darthacheron.fitbe.health.sleep

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.health.componenets.DialogUiState
import org.jetbrains.compose.resources.StringResource
import kotlin.time.Duration.Companion.hours

data class AddSleepDialogUiState(
    val startDateTime: LocalDateTime = Clock.System.now().minus(8.hours).toLocalDateTime(TimeZone.currentSystemDefault()),
    val endDateTime: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val startDateTimeError: StringResource? = null,
    val endDateTimeError: StringResource? = null,
) : DialogUiState {
    override val canSave: Boolean
        get() = startDateTimeError == null && endDateTimeError == null
}
