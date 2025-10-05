package org.darthacheron.fitbe.health

import androidx.navigation.NavHostController
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.top_bar_title_health
import org.darthacheron.fitbe.health.beverages.BeverageOverviewViewModel
import org.darthacheron.fitbe.health.sleep.SleepOverviewViewModel
import org.darthacheron.fitbe.health.steps.StepsOverviewViewModel
import org.darthacheron.fitbe.health.weight.WeightOverviewViewModel
import org.darthacheron.fitbe.navigation.Screen
import org.darthacheron.fitbe.ui.BottomNavigationBarViewModel
import org.darthacheron.fitbe.ui.TopBarManager
import org.jetbrains.compose.resources.StringResource
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class HealthOverviewViewModel(
    val bodyWeightOverviewViewModel: WeightOverviewViewModel,
    val stepsOverviewViewModel: StepsOverviewViewModel,
    val beverageOverviewViewModel: BeverageOverviewViewModel,
    val sleepOverviewViewModel: SleepOverviewViewModel,
    topNavHostController: NavHostController,
    navHostController: NavHostController,
    topBarManager: TopBarManager
) : BottomNavigationBarViewModel(topNavHostController, navHostController, topBarManager) {
    override val title: StringResource
        get() = Res.string.top_bar_title_health

    override val bottomBarSelected: Screen?
        get() = Screen.Health

    fun navigateToBeverageOverview() {
        navHostController.navigate(Screen.BeveragesOverview)
    }

    fun navigateToSleepOverview() {
        navHostController.navigate(Screen.Sleeps)
    }

    fun navigateToStepsOverview() {
        navHostController.navigate(Screen.Steps)
    }

    fun navigateToBodyWeightOverview() {
        navHostController.navigate(Screen.BodyWeights)
    }
}