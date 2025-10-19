package org.darthacheron.fitbe.health.weight

import androidx.compose.runtime.Composable
import org.darthacheron.fitbe.health.components.HealthView
import org.darthacheron.fitbe.health.components.HealthViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BodyWeightView() {
    HealthView(
        overviewView = { BodyWeightOverviewView() },
        detailView = { BodyWeightDailyView() }
    )
}