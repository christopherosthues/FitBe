package org.darthacheron.fitbe.health.componenets


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.darthacheron.fitbe.health.ViewState
import org.jetbrains.compose.resources.painterResource

@Composable
fun HealthView(
    healthViewModel: HealthViewModel,
    overviewView: @Composable () -> Unit,
    detailView: @Composable () -> Unit,
) {
    val uiState by healthViewModel.viewStateFlow.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        if (uiState == ViewState.Overview) {
            overviewView()
        } else if (uiState == ViewState.DetailView) {
            detailView()
        }

        SingleChoiceSegmentedButtonRow(modifier = Modifier.align(Alignment.TopEnd)) {
            val viewStates = ViewState.entries.toTypedArray()
            viewStates.forEachIndexed { index, viewState ->
                val isSelected = uiState == viewState
                SegmentedButton(
                    onClick = { healthViewModel.setViewState(viewState) },
                    selected = isSelected,
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = viewStates.size
                    ),
                ) {
                    Icon(painter = painterResource(viewState.drawableResource()), contentDescription = null)
                }
            }
        }
    }
}

