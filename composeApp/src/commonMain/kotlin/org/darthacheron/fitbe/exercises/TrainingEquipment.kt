package org.darthacheron.fitbe.exercises

import androidx.compose.runtime.Composable
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
open class TrainingEquipment(
    open val id: Uuid,
    open val name: String,
    open val imageUri: String? = null,
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

    @Composable
    fun getLocalizedImage(): DrawableResource? {
        return if (this.default) {
            DefaultEquipmentResProvider.equipmentImageMap[this.name]
        } else {
            // For non-default equipment, imageUri should point to a user-defined image.
            // This part needs to be handled by image loading logic outside this class.
            // For now, returning null for non-default, or if you have a way to resolve imageUri to DrawableResource.
            null
        }
    }
}