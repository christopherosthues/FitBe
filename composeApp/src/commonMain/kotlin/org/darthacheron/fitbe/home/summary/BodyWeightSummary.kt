package org.darthacheron.fitbe.home.summary

import AnimatedSemiCircularProgressIndicator
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            AnimatedSemiCircularProgressIndicator(
                progress = if (uiState.totalWeight == null) 0f else 1f,
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
