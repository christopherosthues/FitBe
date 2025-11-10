package org.darthacheron.fitbe.home.summary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.darthacheron.fitbe.components.AnimatedSegmentedCircularProgressIndicator
import org.darthacheron.fitbe.ui.BodyWeightColors
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun BodyWeightSummary(bodyWeightSummaryViewModel: BodyWeightSummaryViewModel = koinInject()) {
    val uiState by bodyWeightSummaryViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else if (uiState.error.hasGeneralError) {
            uiState.error.generalError?.let { Text(stringResource(it)) }
        } else {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                val values = uiState.cumulatedWeights()

                AnimatedSegmentedCircularProgressIndicator(
                    values = values,
                    colors = BodyWeightColors,
                    modifier = Modifier.fillMaxSize(),
                    centerText = uiState.totalWeightText(),
                    bottomText = uiState.targetWeightText()
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                uiState.muscleMassText()?.let {
                    Text(text = it, style = MaterialTheme.typography.bodySmall)
                }
                uiState.boneMassText()?.let {
                    Text(text = it, style = MaterialTheme.typography.bodySmall)
                }
                uiState.bodyFatPercentageText()?.let {
                    Text(text = it, style = MaterialTheme.typography.bodySmall)
                }
                uiState.bodyWaterPercentageText()?.let {
                    Text(text = it, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
