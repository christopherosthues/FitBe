package org.darthacheron.fitbe.health.weight.manage

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.health.components.DialogUiState
import org.jetbrains.compose.resources.StringResource
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
data class AddBodyWeightDialogUiState(
    val weight: String = "",
    val bodyFatInPercentage: String = "",
    val muscleMass: String = "",
    val boneMass: String = "",
    val bodyWaterInPercentage: String = "",
    val dateTime: LocalDateTime =
        Clock.System
            .now()
            .toLocalDateTime(TimeZone.currentSystemDefault()),
    val weightError: StringResource? = null,
    val bodyFatError: StringResource? = null,
    val muscleMassError: StringResource? = null,
    val boneMassError: StringResource? = null,
    val bodyWaterError: StringResource? = null
) : DialogUiState {
    override val canSave: Boolean
        get() =
            weightError == null &&
                bodyFatError == null &&
                muscleMassError == null &&
                boneMassError == null &&
                bodyWaterError == null &&
                weight.isNotBlank()
}