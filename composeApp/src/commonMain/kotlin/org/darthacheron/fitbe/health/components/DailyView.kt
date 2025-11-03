package org.darthacheron.fitbe.health.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import kotlin.time.Instant
import org.darthacheron.fitbe.ui.UiState
import org.darthacheron.fitbe.ui.UiStateError
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun <Error : UiStateError, State : UiState<Error>> DailyView(
    dailyViewModel: DailyViewModel<Error, State>,
    detailView: @Composable (state: State, date: Instant) -> Unit,
    addDialog: @Composable (date: Instant, dismiss: () -> Unit) -> Unit
) {
    LaunchedEffect(Unit) {
        dailyViewModel.updateTopBarConfig()
    }
    val uiState by dailyViewModel.uiState.collectAsState()
    val date by dailyViewModel.dateFlow.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    uiState.error.generalError?.let {
        val message = stringResource(it)
        LaunchedEffect(it, message) {
            scope.launch {
                snackbarHostState.showSnackbar(message)
                dailyViewModel.clearErrorMessage()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    IconButton(
                        onClick = { dailyViewModel.movePast() },
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_arrow_back),
                            contentDescription = dailyViewModel.movePastContentDescription()
                        )
                    }

                    DateControl(
                        date,
                        dailyViewModel
                    )

                    IconButton(
                        onClick = { dailyViewModel.moveFuture() },
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_arrow_forward),
                            contentDescription = dailyViewModel.moveFutureContentDescription()
                        )
                    }
                }

                detailView(uiState, date)
            }

            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_add),
                    contentDescription = stringResource(dailyViewModel.addButtonContentDescription)
                )
            }
        }
    }

    if (showAddDialog) {
        addDialog(
            date,
            { showAddDialog = false }
        )
    }
}