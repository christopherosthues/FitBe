package org.darthacheron.fitbe.health.beverages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.beverages_daily_view_content_description_delete
import fitbe.composeapp.generated.resources.beverages_daily_view_content_description_edit
import fitbe.composeapp.generated.resources.beverages_daily_view_content_description_progress_percent_target
import fitbe.composeapp.generated.resources.beverages_daily_view_progress_percent
import fitbe.composeapp.generated.resources.beverages_daily_view_progress_total
import fitbe.composeapp.generated.resources.beverages_daily_view_progress_total_target
import fitbe.composeapp.generated.resources.ic_delete
import fitbe.composeapp.generated.resources.ic_edit
import fitbe.composeapp.generated.resources.local_time_format
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.components.CircularWaveAnimationProgressIndicator
import org.darthacheron.fitbe.health.beverages.manage.AddBeverageDialog
import org.darthacheron.fitbe.health.beverages.manage.EditBeverageDialog
import org.darthacheron.fitbe.health.components.DailyView
import org.darthacheron.fitbe.health.components.format
import org.darthacheron.fitbe.utils.roundToDecimals
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalTime::class)
@Composable
fun BeverageDailyView(
    beverageDailyViewModel: BeverageDailyViewModel = koinViewModel(),
) {
    DailyView(
        dailyViewModel = beverageDailyViewModel,
        detailView = { state, date ->
            BeverageDailyView(
                state = state,
                beverageDailyViewModel = beverageDailyViewModel
            )
        },
        addDialog = { date, onDismiss ->
            AddBeverageDialog(
                initialDate = date,
                onSave = { amount, name, unit, date ->
                    beverageDailyViewModel.addBeverage(amount, name, unit, date)
                    onDismiss()
                },
                onDismiss = onDismiss
            )
        }
    )
}

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
@Composable
private fun BeverageDailyView(
    state: BeverageDailyUiState,
    beverageDailyViewModel: BeverageDailyViewModel
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedBeverageId by remember { mutableStateOf<Uuid?>(null) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(top = 8.dp, bottom = 80.dp)
        ) {
            item {
                Column(
                    modifier =
                        Modifier
                            .padding(
                                top = 16.dp,
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 16.dp
                            ).fillMaxSize().background(MaterialTheme.colorScheme.background),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    val progressInPercent = (state.progress.toFloat() * 100).toInt()
                    val progressText: String
                    var totalAmountText: String? = null
                    var contentDescription: String? = null
                    if (state.target != null) {
                        progressText =
                            stringResource(Res.string.beverages_daily_view_progress_percent, progressInPercent)
                        totalAmountText =
                            stringResource(
                                Res.string.beverages_daily_view_progress_total_target,
                                state.total.roundToDecimals(2),
                                state.target
                            )
                        contentDescription =
                            stringResource(
                                Res.string.beverages_daily_view_content_description_progress_percent_target,
                                progressInPercent
                            )
                    } else {
                        progressText =
                            stringResource(
                                Res.string.beverages_daily_view_progress_total,
                                state.total.toFloat()
                            )
                    }
                    CircularWaveAnimationProgressIndicator(
                        progress = state.progress.toFloat(),
                        text = progressText,
                        label = totalAmountText,
                        contentDescription = contentDescription
                    )
                }
            }
            items(
                items = state.beverages,
                key = { it.id }
            ) { beverage ->
                BeveragesListItem(
                    beverage = beverage,
                    editDialog = { id ->
                        showEditDialog = true
                        selectedBeverageId = id
                    },
                    delete = beverageDailyViewModel::deleteBeverage
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }

    if (showEditDialog && selectedBeverageId != null) {
        EditBeverageDialog(
            id = selectedBeverageId!!,
            onSave = { id, amount, name, unit, date ->
                beverageDailyViewModel.editBeverage(id, amount, name, unit, date)
                showEditDialog = false
                selectedBeverageId = null
            },
            onDismiss = {
                showEditDialog = false
                selectedBeverageId = null
            }
        )
    }
}

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
@Composable
private fun BeveragesListItem(
    beverage: Beverage,
    editDialog: (id: Uuid) -> Unit,
    delete: (id: Uuid) -> Unit
) {
    ListItem(
        leadingContent = {
            Icon(
                painterResource(beverage.unit.iconResource()),
                contentDescription = null,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        },
        headlineContent = {
            Text(
                text = beverage.localizedString(),
                fontWeight = FontWeight.Bold
            )
        },
        overlineContent = {
            Text(
                text = beverage.date.toLocalDateTime(TimeZone.currentSystemDefault()).time.format(
                    stringResource(Res.string.local_time_format)
                )
            )
        },
        trailingContent = {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { editDialog(beverage.id) }
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_edit),
                        contentDescription =
                            stringResource(Res.string.beverages_daily_view_content_description_edit, beverage.beverage)
                    )
                }
                IconButton(
                    onClick = { delete(beverage.id) }
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_delete),
                        contentDescription =
                            stringResource(
                                Res.string.beverages_daily_view_content_description_delete,
                                beverage.beverage
                            )
                    )
                }
            }
        },
        supportingContent = { null }
    )
}