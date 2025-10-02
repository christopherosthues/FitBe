package org.darthacheron.fitbe.health.sleep

import kotlinx.datetime.LocalDateTime
import org.darthacheron.fitbe.health.componenets.DialogUiState
import org.jetbrains.compose.resources.StringResource

data class AddSleepDialogUiState(
    val startDateTime: LocalDateTime? = null,
    val endDateTime: LocalDateTime? = null,
    val startDateTimeError: StringResource? = null,
    val endDateTimeError: StringResource? = null,
) : DialogUiState {
    override val canSave: Boolean
        get() = TODO("Not yet implemented")
}