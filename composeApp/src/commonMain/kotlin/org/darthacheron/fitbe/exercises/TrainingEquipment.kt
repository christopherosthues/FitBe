package org.darthacheron.fitbe.exercises

import androidx.compose.runtime.Composable
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.stringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
open class TrainingEquipment(
    open val id: Uuid,
    open val name: String,
    open val default: Boolean = false,
    open val dateUtc: LocalDate,
) {
    @Composable
    fun getLocalizedName(): String {
        return if (this.default) {
            DefaultEquipmentResProvider.equipmentNameMap[this.name]?.let {
                stringResource(it)
            } ?: this.name
        } else {
            this.name
        }
    }
}
