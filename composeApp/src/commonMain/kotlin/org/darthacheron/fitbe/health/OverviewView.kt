package org.darthacheron.fitbe.health

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.ic_add
import fitbe.composeapp.generated.resources.ic_arrow_back
import fitbe.composeapp.generated.resources.ic_arrow_forward
import kotlinx.coroutines.launch
import org.darthacheron.fitbe.components.date.DateRange
import org.darthacheron.fitbe.health.componenets.DateRangeControl
import org.darthacheron.fitbe.ui.UiState
import org.darthacheron.fitbe.ui.UiStateError
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun <Error : UiStateError, State : UiState<Error>> OverviewView(
    overviewViewModel: OverviewViewModel<Error, State>,
    plot: @Composable (state: State, dateRange: DateRange) -> Unit,
    addDialog: @Composable (() -> Unit) -> Unit
) {
    LaunchedEffect(Unit) {
        overviewViewModel.updateTopBarConfig()
    }
    val uiState by overviewViewModel.uiState.collectAsState()
    val dateRange by overviewViewModel.dateRangeFlow.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    uiState.error.generalError?.let {
        val message = stringResource(it)
        LaunchedEffect(it, message) {
            scope.launch {
                snackbarHostState.showSnackbar(message)
                overviewViewModel.clearErrorMessage()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            plot(uiState, dateRange)

            IconButton(
                onClick = { overviewViewModel.movePast() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_back),
                    contentDescription = null
                )
            }

            IconButton(
                onClick = { overviewViewModel.moveFuture() },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_forward),
                    contentDescription = null
                )
            }

            Row(
                modifier = Modifier.align(Alignment.BottomEnd).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                DateRangeControl(
                    dateRange,
                    overviewViewModel
                )

                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(16.dp),
                ) {
                    Icon(painter = painterResource(Res.drawable.ic_add), contentDescription = null)
                }
            }
        }
    }

    if (showAddDialog) {
        addDialog { showAddDialog = false }
    }
}