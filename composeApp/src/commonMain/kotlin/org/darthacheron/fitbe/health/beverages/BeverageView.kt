package org.darthacheron.fitbe.health.beverages

import androidx.compose.runtime.Composable
import org.darthacheron.fitbe.health.components.HealthView
import org.darthacheron.fitbe.health.components.HealthViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BeverageView() {
    HealthView(
        overviewView = { BeverageOverviewView() },
        detailView = { BeverageDailyView() }
    )
}