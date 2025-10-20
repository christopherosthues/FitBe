package org.darthacheron.fitbe.health.sleep.manage

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.health.components.DialogUiState
import org.jetbrains.compose.resources.StringResource
import kotlin.time.Duration.Companion.hours
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class EditSleepDialogUiState(
    val id: Uuid = Uuid.NIL,
    val startDateTime: LocalDateTime =
        Clock.System
            .now()
            .minus(8.hours)
            .toLocalDateTime(TimeZone.currentSystemDefault()),
    val endDateTime: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val startDateTimeError: StringResource? = null,
    val endDateTimeError: StringResource? = null
) : DialogUiState {
    override val canSave: Boolean
        get() {
            val startInstant = startDateTime.toInstant(TimeZone.currentSystemDefault())
            val endInstant = endDateTime.toInstant(TimeZone.currentSystemDefault())
            return startInstant < endInstant && startDateTimeError == null && endDateTimeError == null
        }
}