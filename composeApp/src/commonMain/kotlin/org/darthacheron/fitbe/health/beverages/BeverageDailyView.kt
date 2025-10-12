package org.darthacheron.fitbe.health.beverages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.beverages_daily_view_content_description_progress_percent_target
import fitbe.composeapp.generated.resources.beverages_daily_view_progress_percent
import fitbe.composeapp.generated.resources.beverages_daily_view_progress_total
import fitbe.composeapp.generated.resources.beverages_daily_view_progress_total_target
import org.darthacheron.fitbe.components.CircularWaveAnimationProgressIndicator
import org.darthacheron.fitbe.health.componenets.DailyView
import org.darthacheron.fitbe.utils.roundToDecimals
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun BeverageDailyView(
    beverageDailyViewModel: BeverageDailyViewModel,
    addBeverageDialogViewModel: AddBeverageDialogViewModel
) {
    DailyView(
        dailyViewModel = beverageDailyViewModel,
        detailView = { state, date ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    stickyHeader {
                        Column(
                            modifier =
                                Modifier
                                    .padding(
                                        top = 16.dp,
                                        start = 16.dp,
                                        end = 16.dp,
                                        bottom = 16.dp
                                    ).fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val progress = (state.progress.toFloat() * 100).toInt()
                            val progressText: String
                            var totalAmountText: String? = null
                            var contentDescription: String? = null
                            if (state.target != null) {
                                progressText =
                                    stringResource(Res.string.beverages_daily_view_progress_percent, progress)
                                totalAmountText =
                                    stringResource(
                                        Res.string.beverages_daily_view_progress_total_target,
                                        state.total.roundToDecimals(2),
                                        state.target.toInt()
                                    )
                                contentDescription =
                                    stringResource(
                                        Res.string.beverages_daily_view_content_description_progress_percent_target,
                                        progress
                                    )
                            } else {
                                progressText =
                                    stringResource(
                                        Res.string.beverages_daily_view_progress_total,
                                        state.total.toFloat()
                                    )
                            }
                            CircularWaveAnimationProgressIndicator(
                                progress = { state.progress.toFloat() },
                                text = progressText,
                                label = totalAmountText,
                                contentDescription = contentDescription
                            )
                        }
                    }
                    state.beverages.forEach { beverage ->
                        item {
                            Row(
                                horizontalArrangement = Arrangement.Start,
                                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                            ) {
                                Icon(
                                    painterResource(beverage.unit.iconResource()),
                                    contentDescription = null,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                                Text(text = beverage.localizedString())
                            }
                        }
                    }
                }
            }
        },
        addDialog = { onDismiss ->
            AddBeverageDialog(
                viewModel = addBeverageDialogViewModel,
                onSave = { amount, name, unit, date ->
                    beverageDailyViewModel.addBeverage(amount, name, unit, date)
                    onDismiss()
                },
                onDismiss = onDismiss
            )
        }
    )
}