package org.darthacheron.fitbe.home.summary

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.darthacheron.fitbe.components.AnimatedSemiCircularProgressIndicator
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun BeveragesSummary(beveragesSummaryViewModel: BeveragesSummaryViewModel = koinInject()) {
    val uiState by beveragesSummaryViewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else if (uiState.error.hasGeneralError) {
            uiState.error.generalError?.let { Text(stringResource(it)) }
        } else {
            AnimatedSemiCircularProgressIndicator(
                progress = uiState.progress,
                modifier = Modifier.fillMaxSize(),
                centerText = uiState.progressText(),
                bottomText = uiState.totalAmountText(),
                contentDescription = uiState.contentDescription()
            )
        }
    }
}
