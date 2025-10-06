package org.darthacheron.fitbe.health.beverages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
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
import org.darthacheron.fitbe.health.OverviewView
import org.darthacheron.fitbe.health.componenets.DateRangeControl
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun BeverageOverviewView(
    beverageOverviewViewModel: BeverageOverviewViewModel,
    addBeverageDialogViewModel: AddBeverageDialogViewModel
) {
    OverviewView(
        overviewViewModel = beverageOverviewViewModel,
        plot = { state, dateRange ->
            val targetBeverages by beverageOverviewViewModel.targetBeverages.collectAsState()
            val maxBeverages by beverageOverviewViewModel.maxBeverages.collectAsState()
            PlotBeverages(
                Modifier.padding(top = 8.dp, bottom = 64.dp),
                state.beverages,
                dateRange,
                state.dates,
                maxBeverages,
                false,
                targetBeverages,
            )
        },
        addDialog = { dismissDialog ->
            AddBeverageDialog(
                viewModel = addBeverageDialogViewModel,
                onSave = { amount, name, unit, date ->
                    beverageOverviewViewModel.saveBeverage(amount, name, unit, date)
                    dismissDialog()
                },
                onDismiss = { dismissDialog() }
            )
        }
    )
}
