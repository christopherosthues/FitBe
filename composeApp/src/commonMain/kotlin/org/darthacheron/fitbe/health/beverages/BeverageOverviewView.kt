package org.darthacheron.fitbe.health.beverages

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.darthacheron.fitbe.health.components.OverviewView

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
                targetBeverages
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