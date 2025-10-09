package org.darthacheron.fitbe.health.steps

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.health.componenets.DialogUiState
import org.jetbrains.compose.resources.StringResource

data class AddStepsDialogUiState(
    val date: LocalDate =
        Clock.System
            .now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date,
    val steps: String = "",
    val stepsError: StringResource? = null
) : DialogUiState {
    override val canSave: Boolean
        get() = stepsError == null && steps.isNotBlank()
}