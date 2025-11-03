package org.darthacheron.fitbe.health.weight

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import fitbe.composeapp.generated.resources.body_weight_daily_view_body_fat
import fitbe.composeapp.generated.resources.body_weight_daily_view_body_water
import fitbe.composeapp.generated.resources.body_weight_daily_view_body_weight
import fitbe.composeapp.generated.resources.body_weight_daily_view_bone_mass
import fitbe.composeapp.generated.resources.body_weight_daily_view_content_description_delete
import fitbe.composeapp.generated.resources.body_weight_daily_view_content_description_edit
import fitbe.composeapp.generated.resources.body_weight_daily_view_muscle_mass
import fitbe.composeapp.generated.resources.ic_delete
import fitbe.composeapp.generated.resources.ic_edit
import fitbe.composeapp.generated.resources.local_time_format
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.health.components.DailyView
import org.darthacheron.fitbe.health.components.format
import org.darthacheron.fitbe.health.weight.manage.AddBodyWeightDialog
import org.darthacheron.fitbe.health.weight.manage.EditBodyWeightDialog
import org.darthacheron.fitbe.settings.WeightUnit
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalTime::class)
@Composable
fun BodyWeightDailyView(
    bodyWeightDailyViewModel: BodyWeightDailyViewModel = koinViewModel()
) {
    DailyView(
        dailyViewModel = bodyWeightDailyViewModel,
        detailView = { state, date ->
            BodyWeightDetailView(
                state = state,
                bodyWeightDailyViewModel = bodyWeightDailyViewModel
            )
        },
        addDialog = { date, onDismiss ->
            AddBodyWeightDialog(
                initialDate = date,
                onSave = { date,
                           weightInKg,
                           bodyFatPercentage,
                           muscleMassInKg,
                           boneMassInKg,
                           bodyWaterInPercentage
                    ->
                    bodyWeightDailyViewModel.addBodyWeight(
                        date,
                        weightInKg,
                        bodyFatPercentage,
                        muscleMassInKg,
                        boneMassInKg,
                        bodyWaterInPercentage
                    )
                    onDismiss()
                },
                onDismiss = onDismiss
            )
        }
    )
}

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
@Composable
private fun BodyWeightDetailView(
    state: BodyWeightDailyUiState,
    bodyWeightDailyViewModel: BodyWeightDailyViewModel
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedBodyWeightId by remember { mutableStateOf<Uuid?>(null) }

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
                PlotDailyBodyWeights(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    bodyWeights = state.bodyWeights,
                    times = state.times,
                    weightUnit = state.weightUnit,
                    maxBodyWeight = state.maxBodyWeight,
                    targetBodyWeight = state.targetBodyWeight
                )
            }
            items(
                items = state.bodyWeights,
                key = { it.id }
            ) { bodyWeight ->
                BodyWeightListItem(
                    bodyWeight = bodyWeight,
                    weightUnit = state.weightUnit,
                    editDialog = { id ->
                        showEditDialog = true
                        selectedBodyWeightId = id
                    },
                    delete = bodyWeightDailyViewModel::deleteBodyWeight
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }

    if (showEditDialog && selectedBodyWeightId != null) {
        EditBodyWeightDialog(
            id = selectedBodyWeightId!!,
            onSave = { id,
                       date,
                       weightInKg,
                       bodyFatPercentage,
                       muscleMassInKg,
                       boneMassInKg,
                       bodyWaterInPercentage ->
                bodyWeightDailyViewModel.editBodyWeight(
                    id = id,
                    date = date,
                    weightInKg = weightInKg,
                    bodyFatPercentage = bodyFatPercentage,
                    muscleMassInKg = muscleMassInKg,
                    boneMassInKg = boneMassInKg,
                    bodyWaterInPercentage = bodyWaterInPercentage
                )
                showEditDialog = false
                selectedBodyWeightId = null
            },
            onDismiss = {
                showEditDialog = false
                selectedBodyWeightId = null
            }
        )
    }
}

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
@Composable
private fun BodyWeightListItem(
    bodyWeight: BodyWeight,
    weightUnit: WeightUnit,
    editDialog: (id: Uuid) -> Unit,
    delete: (id: Uuid) -> Unit
) {
    val hasSupportingContent = bodyWeight.muscleMassInKg != null ||
        bodyWeight.bodyFatPercentage != null ||
        bodyWeight.bodyWaterInPercentage != null ||
        bodyWeight.boneMassInKg != null

    val timeText = bodyWeight.date.toLocalDateTime(TimeZone.currentSystemDefault()).time.format(
        stringResource(Res.string.local_time_format)
    )
    ListItem(
        headlineContent = {
            Text(
                text = stringResource(
                    Res.string.body_weight_daily_view_body_weight,
                    bodyWeight.weightInKg,
                    stringResource(weightUnit.toStringResource())
                ),
                fontWeight = FontWeight.Bold
            )
        },
        overlineContent = {
            Text(text = timeText)
        },
        supportingContent = if (hasSupportingContent) {
            {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    bodyWeight.muscleMassInKg?.let {
                        Text(
                            text = stringResource(
                                Res.string.body_weight_daily_view_muscle_mass,
                                it,
                                stringResource(weightUnit.toStringResource())
                            ),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    bodyWeight.bodyFatPercentage?.let {
                        Text(
                            text = stringResource(
                                Res.string.body_weight_daily_view_body_fat,
                                it
                            ),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    bodyWeight.bodyWaterInPercentage?.let {
                        Text(
                            text = stringResource(
                                Res.string.body_weight_daily_view_body_water,
                                it
                            ),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    bodyWeight.boneMassInKg?.let {
                        Text(
                            text = stringResource(
                                Res.string.body_weight_daily_view_bone_mass,
                                it,
                                stringResource(weightUnit.toStringResource())
                            ),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        } else {
            null
        },
        trailingContent = {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { editDialog(bodyWeight.id) }
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_edit),
                        contentDescription =
                            stringResource(
                                Res.string.body_weight_daily_view_content_description_edit,
                                bodyWeight.weightInKg,
                                timeText
                            )
                    )
                }
                IconButton(
                    onClick = { delete(bodyWeight.id) }
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_delete),
                        contentDescription =
                            stringResource(
                                Res.string.body_weight_daily_view_content_description_delete,
                                bodyWeight.weightInKg,
                                timeText
                            )
                    )
                }
            }
        }
    )
}