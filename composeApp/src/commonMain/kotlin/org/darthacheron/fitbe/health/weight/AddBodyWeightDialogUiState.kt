package org.darthacheron.fitbe.health.weight

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.health.componenets.DialogUiState
import org.jetbrains.compose.resources.StringResource

data class AddBodyWeightDialogUiState(
    val weight: String = "",
    val bodyFatInPercentage: String = "",
    val muscleMass: String = "",
    val boneMass: String = "",
    val bodyWaterInPercentage: String = "",
    val date: LocalDate =
        Clock.System
            .now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date,
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