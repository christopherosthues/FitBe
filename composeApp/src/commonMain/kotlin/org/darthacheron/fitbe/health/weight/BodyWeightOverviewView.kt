package org.darthacheron.fitbe.health.weight

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.darthacheron.fitbe.health.components.OverviewView
import org.koin.compose.viewmodel.koinViewModel
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Composable
fun BodyWeightOverviewView(
    bodyWeightOverviewViewModel: BodyWeightOverviewViewModel = koinViewModel()
) {
    OverviewView(
        overviewViewModel = bodyWeightOverviewViewModel,
        plot = { state, dateRange ->
            PlotBodyWeights(
                modifier = Modifier.padding(top = 8.dp, bottom = 64.dp),
                bodyWeights = state.bodyWeights,
                dateRange = dateRange,
                dates = state.dates,
                weightUnit = state.weightUnit,
                maxWeight = state.maxBodyWeight,
                thumbnail = false,
                targetWeight = state.targetBodyWeight
            )
        },
        addDialog = { dismissDialog ->
            AddBodyWeightDialog(
                onSave = {
                    date,
                    weightInKg,
                    bodyFatPercentage,
                    muscleMassInKg,
                    boneMassInKg,
                    bodyWaterInPercentage
                    ->
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