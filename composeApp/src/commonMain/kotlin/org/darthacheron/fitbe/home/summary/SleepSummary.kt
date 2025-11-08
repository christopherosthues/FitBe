package org.darthacheron.fitbe.home.summary

import AnimatedSemiCircularProgressIndicator
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.darthacheron.fitbe.components.CircularWaveAnimationProgressIndicator
import org.koin.compose.koinInject

@Composable
fun SleepSummary(sleepSummaryViewModel: SleepSummaryViewModel = koinInject()) {
    val uiState by sleepSummaryViewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        AnimatedSemiCircularProgressIndicator(
            progress = uiState.progress,
            centerText = uiState.progressText(),
            bottomText = uiState.totalAmountText(),
            contentDescription = uiState.contentDescription()
        )
    }
}
