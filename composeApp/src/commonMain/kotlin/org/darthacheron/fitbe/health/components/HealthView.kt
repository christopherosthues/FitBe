package org.darthacheron.fitbe.health.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import org.darthacheron.fitbe.health.ViewState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HealthView(
    healthViewModel: HealthViewModel = koinViewModel(),
    overviewView: @Composable () -> Unit,
    detailView: @Composable () -> Unit
) {
    val uiState by healthViewModel.viewStateFlow.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        if (uiState == ViewState.Overview) {
            overviewView()
        } else if (uiState == ViewState.DetailView) {
            detailView()
        }

        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            SegmentedButton(
                onClick = { healthViewModel.toggleViewState() },
                selected = true,
                shape = RectangleShape,
                icon = { }
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(uiState.drawableResource()),
                    contentDescription = stringResource(uiState.contentDescriptionStringResource())
                )
            }
        }
    }
}