package org.darthacheron.fitbe.health.steps

import androidx.compose.runtime.Composable
import org.darthacheron.fitbe.health.components.HealthView
import org.darthacheron.fitbe.health.components.HealthViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StepsView() {
    HealthView(
        overviewView = { StepsOverviewView() },
        detailView = { StepsDailyView() }
    )
}