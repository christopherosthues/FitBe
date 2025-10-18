package org.darthacheron.fitbe.health.beverages

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
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
            PlotBeverages(
                modifier = Modifier.padding(top = 8.dp, bottom = 64.dp),
                beverages = state.beverages,
                dateRange = dateRange,
                dates = state.dates,
                maxBeverages = state.maxBeverages,
                thumbnail = false,
                targetBeverages = state.target
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