package org.darthacheron.fitbe.health.beverages

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
import fitbe.composeapp.generated.resources.beverages_overview_content_description_add_beverage
import fitbe.composeapp.generated.resources.ic_add
import fitbe.composeapp.generated.resources.ic_arrow_back
import fitbe.composeapp.generated.resources.ic_arrow_forward
import kotlinx.coroutines.launch
import org.darthacheron.fitbe.health.componenets.DateRangeControl
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun BeverageOverviewView(
    beverageOverviewViewModel: BeverageOverviewViewModel,
    addBeverageDialogViewModel: AddBeverageDialogViewModel
) {
    LaunchedEffect(Unit) {
        beverageOverviewViewModel.updateTopBarConfig()
    }

    val uiState by beverageOverviewViewModel.uiState.collectAsState()
    val dateRange by beverageOverviewViewModel.dateRangeFlow.collectAsState()
    val targetBeverages by beverageOverviewViewModel.targetBeverages.collectAsState()
    val maxBeverages by beverageOverviewViewModel.maxBeverages.collectAsState()
    var showAddBeverageDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    uiState.errorMessage?.let {
        val message = stringResource(it)
        LaunchedEffect(it, message) {
            scope.launch {
                snackbarHostState.showSnackbar(message)
                beverageOverviewViewModel.clearErrorMessage()
            }
        }
    }

    if (showAddBeverageDialog) {
        AddBeverageDialog(
            viewModel = addBeverageDialogViewModel,
            onDismiss = { showAddBeverageDialog = false },
            onSave = { amount, name, unit, date -> {
                beverageOverviewViewModel.saveBeverage(amount, name, unit, date) }
                showAddBeverageDialog = false
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            PlotBeverages(
                Modifier.padding(bottom = 64.dp),
                uiState.beverages,
                dateRange,
                uiState.dates, // Changed here
                maxBeverages,
                false,
                targetBeverages,
            )

            IconButton(
                onClick = { beverageOverviewViewModel.movePast() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_back),
                    contentDescription = null
                )
            }

            IconButton(
                onClick = { beverageOverviewViewModel.moveFuture() },
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
                    beverageOverviewViewModel
                )
            }

            FloatingActionButton(
                onClick = { showAddBeverageDialog = true },
                modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_add),
                    contentDescription = stringResource(Res.string.beverages_overview_content_description_add_beverage)
                )
            }
        }
    }
}
