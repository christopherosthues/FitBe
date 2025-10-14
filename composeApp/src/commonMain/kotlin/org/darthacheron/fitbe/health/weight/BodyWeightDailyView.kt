package org.darthacheron.fitbe.health.weight

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import org.jetbrains.compose.resources.stringResource

@Composable
fun BodyWeightDailyView(
    bodyWeightDailyViewModel: BodyWeightDailyViewModel,
    addBodyWeightDialogViewModel: AddBodyWeightDialogViewModel
) {
    DailyView(
        dailyViewModel = bodyWeightDailyViewModel,
        detailView = { state, date ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(bottom = 64.dp)
                ) {
                    stickyHeader {
                        Text(text = "Header plot")
                    }
                    state.bodyWeights.forEach { bodyWeight ->
                        item {
                            Column(
                                modifier = Modifier.fillMaxSize().padding(vertical = 8.dp),
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    Text(
                                        text =
                                            bodyWeight
                                                .date
                                                .toLocalDateTime(TimeZone.currentSystemDefault())
                                                .time
                                                .format(stringResource(Res.string.local_time_format)),
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.padding(end = 8.dp)
                                    )

                                    Text(
                                        text =
                                            stringResource(
                                                Res.string.body_weight_daily_view_body_weight,
                                                bodyWeight.weightInKg,
                                                stringResource(state.weightUnit.toStringResource())
                                            ),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Row(
                                    horizontalArrangement = Arrangement.Start,
                                ) {
                                    if (bodyWeight.muscleMassInKg != null) {
                                        Text(
                                            text =
                                                stringResource(
                                                    Res.string.body_weight_daily_view_muscle_mass,
                                                    bodyWeight.muscleMassInKg,
                                                    stringResource(state.weightUnit.toStringResource())
                                                ),
                                            style = MaterialTheme.typography.titleSmall,
                                            modifier = Modifier.padding(end = 8.dp)
                                        )
                                    }
                                    if (bodyWeight.bodyFatPercentage != null) {
                                        Text(
                                            text =
                                                stringResource(
                                                    Res.string.body_weight_daily_view_body_fat,
                                                    bodyWeight.bodyFatPercentage
                                                ),
                                            style = MaterialTheme.typography.titleSmall,
                                            modifier = Modifier.padding(end = 8.dp)
                                        )
                                    }
                                    if (bodyWeight.bodyWaterInPercentage != null) {
                                        Text(
                                            text =
                                                stringResource(
                                                    Res.string.body_weight_daily_view_body_water,
                                                    bodyWeight.bodyWaterInPercentage
                                                ),
                                            style = MaterialTheme.typography.titleSmall,
                                            modifier = Modifier.padding(end = 8.dp)
                                        )
                                    }
                                    if (bodyWeight.boneMassInKg != null) {
                                        Text(
                                            text =
                                                stringResource(
                                                    Res.string.body_weight_daily_view_bone_mass,
                                                    bodyWeight.boneMassInKg,
                                                    stringResource(state.weightUnit.toStringResource())
                                                ),
                                            style = MaterialTheme.typography.titleSmall,
                                            modifier = Modifier.padding(end = 8.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
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