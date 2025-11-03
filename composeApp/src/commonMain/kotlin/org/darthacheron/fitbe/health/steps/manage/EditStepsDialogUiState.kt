package org.darthacheron.fitbe.health.steps.manage

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.health.components.DialogUiState
import org.jetbrains.compose.resources.StringResource
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
data class EditStepsDialogUiState(
    val id: Uuid = Uuid.NIL,
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