package org.darthacheron.fitbe.exercises.exercises

import androidx.compose.runtime.Composable
import kotlinx.datetime.LocalDate
import org.darthacheron.fitbe.database.equipmentList
import org.darthacheron.fitbe.database.exerciseList
import org.darthacheron.fitbe.exercises.equipment.DefaultEquipmentResProvider
import org.jetbrains.compose.resources.stringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
open class Exercise(
    open val id: Uuid,
    open val name: String,
    open val guide: String,
    open val targetMuscleGroups: List<MuscleGroup> = emptyList(),
    open val default: Boolean = false,
    open val dateUtc: LocalDate
)

@Composable
internal fun getExerciseName(name: String, default: Boolean): String {
    return if (default && exerciseList.contains(name)) {
        DefaultExerciseResProvider.exerciseNameMap[name]?.let {
            stringResource(it)
        } ?: name
    } else {
        name
    }
}

@Composable
internal fun getExerciseGuide(guide: String, default: Boolean): String {
    return if (default && exerciseList.contains(guide)) {
        DefaultExerciseResProvider.exerciseGuideMap[guide]?.let {
            stringResource(it)
        } ?: guide
    } else {
        guide
    }
}
