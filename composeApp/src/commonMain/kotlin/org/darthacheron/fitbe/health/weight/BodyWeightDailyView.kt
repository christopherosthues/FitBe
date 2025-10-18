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
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.body_weight_daily_view_body_fat
import fitbe.composeapp.generated.resources.body_weight_daily_view_body_water
import fitbe.composeapp.generated.resources.body_weight_daily_view_body_weight
import fitbe.composeapp.generated.resources.body_weight_daily_view_bone_mass
import fitbe.composeapp.generated.resources.body_weight_daily_view_muscle_mass
import fitbe.composeapp.generated.resources.local_time_format
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.health.components.DailyView
import org.darthacheron.fitbe.health.components.format
import org.darthacheron.fitbe.settings.WeightUnit
import org.jetbrains.compose.resources.stringResource
import kotlin.uuid.ExperimentalUuidApi

@Composable
fun BodyWeightDailyView(
    bodyWeightDailyViewModel: BodyWeightDailyViewModel,
    addBodyWeightDialogViewModel: AddBodyWeightDialogViewModel
) {
    DailyView(
        dailyViewModel = bodyWeightDailyViewModel,
        detailView = { state, date ->
            BodyWeightDetailView(
                state = state
            )
        },
        addDialog = { onDismiss ->
            AddBodyWeightDialog(
                viewModel = addBodyWeightDialogViewModel,
                onSave = {
                        date,
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

@OptIn(ExperimentalUuidApi::class)
@Composable
private fun BodyWeightDetailView(
    state: BodyWeightDailyUiState
) {
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
                    weightUnit = state.weightUnit
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}

@Composable
private fun BodyWeightListItem(bodyWeight: BodyWeight, weightUnit: WeightUnit) {
    val hasSupportingContent = bodyWeight.muscleMassInKg != null ||
        bodyWeight.bodyFatPercentage != null ||
        bodyWeight.bodyWaterInPercentage != null ||
        bodyWeight.boneMassInKg != null

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
            Text(
                text = bodyWeight.date.toLocalDateTime(TimeZone.currentSystemDefault()).time.format(
                    stringResource(Res.string.local_time_format)
                )
            )
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
        }
    )
}