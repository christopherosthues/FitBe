package org.darthacheron.fitbe.health.weight


import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.darthacheron.fitbe.health.OverviewView
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Composable
fun WeightOverviewView(
    bodyWeightOverviewViewModel: WeightOverviewViewModel,
) {
    OverviewView(
        overviewViewModel = bodyWeightOverviewViewModel,
        plot = { state, dateRange ->
            val maxBodyWeight by bodyWeightOverviewViewModel.maxWeight.collectAsState()
            val targetWeight by bodyWeightOverviewViewModel.targetWeight.collectAsState()
            PlotBodyWeights(
                Modifier.padding(bottom = 64.dp),
                state.bodyWeights,
                dateRange,
                state.dates,
                state.settings,
                maxBodyWeight,
                false,
                targetWeight,
            )
        },
        addDialog = { dismissDialog ->
            val uiState by bodyWeightOverviewViewModel.uiState.collectAsState()
            AddBodyWeightDialog(
                settings = uiState.settings,
                onSave = { date,
                           weightInKg,
                           bodyFatPercentage,
                           muscleMassInKg,
                           boneMassInKg,
                           bodyWaterInPercentage ->
                    bodyWeightOverviewViewModel.addBodyWeight(
                        date,
                        weightInKg,
                        bodyFatPercentage,
                        muscleMassInKg,
                        boneMassInKg,
                        bodyWaterInPercentage
                    )
                    dismissDialog()
                },
                onDismiss = { dismissDialog() }
            )
        }
    )
}
