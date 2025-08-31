package org.darthacheron.fitbe.navigation

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
sealed class Screen {
    @Serializable
    data object Root : Screen()

    @Serializable
    data object BottomBarGraph : Screen()

    @Serializable
    data object Home : Screen()

    @Serializable
    data object ExercisesDashboard : Screen()

    @Serializable
    data object Exercises : Screen()

    @Serializable
    data object TrainingEquipment : Screen()

    @OptIn(ExperimentalUuidApi::class)
    @Serializable
    data class AddEditTrainingEquipment(val id: String?) : Screen()

    @Serializable
    data object Health : Screen()

    @Serializable
    data object Profile : Screen()

    @Serializable
    data object Sleeps : Screen()

    @Serializable
    data object Steps : Screen()

    @Serializable
    data object Beverages : Screen()

    @Serializable
    data object BeveragesOverview : Screen()

    @Serializable
    data object BodyWeights : Screen()

    @Serializable
    data object Settings : Screen()
}