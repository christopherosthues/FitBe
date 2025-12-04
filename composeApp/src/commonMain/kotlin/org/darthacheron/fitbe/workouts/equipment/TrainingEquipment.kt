package org.darthacheron.fitbe.workouts.equipment

import androidx.compose.runtime.Composable
import kotlinx.datetime.LocalDate
import org.darthacheron.fitbe.database.equipmentList
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
open class TrainingEquipment(
    open val id: Uuid,
    open val name: String,
    open val imageUri: String? = null,
    open val default: Boolean = false,
    open val dateUtc: LocalDate,
    open val lastModified: Instant
)

@Composable
internal fun getEquipmentName(
    name: String,
    default: Boolean
): String =
    if (default && equipmentList.any { it.key == name }) {
        DefaultEquipmentResProvider.equipmentNameMap[name]?.let { stringResource(it) } ?: name
    } else {
        name
    }

@Composable
internal fun getEquipmentImage(
    imageUri: String?,
    default: Boolean
): DrawableResource? =
    if (default && imageUri != null && imageUri.startsWith("default_training_equipment_")) {
        DefaultEquipmentResProvider.equipmentImageMap[imageUri]
    } else {
        null
    }