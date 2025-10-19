package org.darthacheron.fitbe.health.steps

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.health.components.DialogUiState
import org.jetbrains.compose.resources.StringResource

data class AddStepsDialogUiState(
    val dateTime: LocalDateTime =
        Clock.System
            .now()
            .toLocalDateTime(TimeZone.currentSystemDefault()),
    val steps: String = "",
    val stepsError: StringResource? = null
) : DialogUiState {
    override val canSave: Boolean
        get() = stepsError == null && steps.isNotBlank()
}