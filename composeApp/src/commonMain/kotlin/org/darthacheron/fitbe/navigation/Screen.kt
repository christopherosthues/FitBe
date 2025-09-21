package org.darthacheron.fitbe.navigation

import kotlinx.serialization.Serializable

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
    data object ExercisesOverview : Screen()

    @Serializable
    data class ExerciseDetail(val id: String?) : Screen()

    @Serializable
    data object TrainingEquipmentOverview : Screen()

    @Serializable
    data class TrainingEquipmentDetail(val id: String?) : Screen()

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

    @Serializable
    data object ProgramOverview : Screen()

    @Serializable
    data object WorkoutsOverview : Screen()

    @Serializable
    data class WorkoutTemplateDetail(val id: String?) : Screen()

    @Serializable
    data class ExerciseExecution(val id: String) : Screen()
}
