package org.darthacheron.fitbe.workouts.equipment

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import org.darthacheron.fitbe.profile.ProfileEntity
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Entity(
    tableName = "profile_favorite_equipment_cross_ref",
    primaryKeys = ["profileId", "equipmentId"],
    foreignKeys = [
        ForeignKey(
            entity = ProfileEntity::class,
            parentColumns = ["id"],
            childColumns = ["profileId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TrainingEquipmentEntity::class,
            parentColumns = ["id"],
            childColumns = ["equipmentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["equipmentId"])] // Index for faster queries by equipment
)
data class ProfileFavoriteEquipmentCrossRef(
    val profileId: Uuid,
    val equipmentId: Uuid
)
