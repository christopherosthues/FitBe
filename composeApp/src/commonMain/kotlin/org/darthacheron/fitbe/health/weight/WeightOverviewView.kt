package org.darthacheron.fitbe.health.weight


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.body_weight_add_body_weight_body_fat
import fitbe.composeapp.generated.resources.body_weight_add_body_weight_body_water
import fitbe.composeapp.generated.resources.body_weight_add_body_weight_bone_mass
import fitbe.composeapp.generated.resources.body_weight_add_body_weight_date
import fitbe.composeapp.generated.resources.body_weight_add_body_weight_muscle_mass
import fitbe.composeapp.generated.resources.body_weight_add_body_weight_title
import fitbe.composeapp.generated.resources.body_weight_add_body_weight_total_weight
import fitbe.composeapp.generated.resources.body_weight_body_fat_error
import fitbe.composeapp.generated.resources.body_weight_body_water_error
import fitbe.composeapp.generated.resources.body_weight_bone_mass_error_kg
import fitbe.composeapp.generated.resources.body_weight_bone_mass_error_lb
import fitbe.composeapp.generated.resources.body_weight_cancel
import fitbe.composeapp.generated.resources.body_weight_muscle_mass_error_kg
import fitbe.composeapp.generated.resources.body_weight_muscle_mass_error_lb
import fitbe.composeapp.generated.resources.body_weight_save
import fitbe.composeapp.generated.resources.body_weight_total_weight_error_kg
import fitbe.composeapp.generated.resources.body_weight_total_weight_error_lb
import fitbe.composeapp.generated.resources.chart_grouping
import fitbe.composeapp.generated.resources.ic_add
import fitbe.composeapp.generated.resources.ic_arrow_back
import fitbe.composeapp.generated.resources.ic_arrow_forward
import fitbe.composeapp.generated.resources.ic_date_range
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.gestures.GestureConfig
import io.github.koalaplot.core.line.StairstepPlot
import io.github.koalaplot.core.style.LineStyle
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.xygraph.CategoryAxisModel
import io.github.koalaplot.core.xygraph.DoubleLinearAxisModel
import io.github.koalaplot.core.xygraph.Point
import io.github.koalaplot.core.xygraph.XYGraph
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.components.DatePickerModal
import org.darthacheron.fitbe.components.DateRangePickerModal
import org.darthacheron.fitbe.components.DropdownSelection
import org.darthacheron.fitbe.health.sleep.SleepViewType
import org.darthacheron.fitbe.settings.Settings
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.settings.WeightUnit
import org.darthacheron.fitbe.utils.toDoubleString
import org.darthacheron.fitbe.utils.toPositiveDouble
import org.darthacheron.fitbe.utils.toPositiveDoubleOrNull
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.ceil
import kotlin.uuid.ExperimentalUuidApi

// StackedAreaChart

@OptIn(ExperimentalUuidApi::class)
@Preview
@Composable
fun WeightOverviewView(
    bodyWeightOverviewViewModel: WeightOverviewViewModel,
    settingsRepository: SettingsRepository
) {
    val bodyWeights by bodyWeightOverviewViewModel.bodyWeights.collectAsState()
    val maxBodyWeight by bodyWeightOverviewViewModel.maxWeight.collectAsState()
    val settings by settingsRepository.getSettingsFlow().collectAsState(Settings())
    var selectedViewTypeIndex by remember { mutableStateOf(0) }
    val startDate by bodyWeightOverviewViewModel.startDate.collectAsState()
    val endDate by bodyWeightOverviewViewModel.endDate.collectAsState()
    var showDateRangeDialog by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }
    val viewTypes = SleepViewType.entries

    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            TextButton(
                onClick = { showDateRangeDialog = true },
            ) {
                Row {
                    Column {
                        Text(
                            text =
                                startDate.toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()
                        )
                        Text(
                            text =
                                endDate.toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()
                        )
                    }
                    Icon(
                        painterResource(Res.drawable.ic_date_range),
                        contentDescription = null,
                        modifier = Modifier.padding(horizontal = 8.dp)
                            .align(Alignment.CenterVertically)
                    )
                }
            }
            DropdownSelection(
                initialState = false,
                selectedIndex = selectedViewTypeIndex,
                items = SleepViewType.entries,
                title = stringResource(Res.string.chart_grouping),
                itemContent = { item, onClick ->
                    DropdownMenuItem(
                        text = { Text(item.localizedString()) },
                        onClick = onClick
                    )
                },
                itemToString = {
                    it.localizedString()
                },
                onItemSelected = {
                    selectedViewTypeIndex = it
                    bodyWeightOverviewViewModel.setViewType(viewTypes[it])
                }
            )
        }
        Box(modifier = Modifier.fillMaxSize()) {
            if (!bodyWeights.first.isEmpty()) {
                PlotBodyWeights(bodyWeights, maxBodyWeight, false)
            }
            IconButton(
                onClick = {},
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_back),
                    contentDescription = null
                )
            }
            IconButton(
                onClick = {},
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_forward),
                    contentDescription = null
                )
            }
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = Color(0xFF2196F3),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
            ) {
                Icon(painter = painterResource(Res.drawable.ic_add), contentDescription = null)
            }
        }
    }

    if (showDateRangeDialog) {
        DateRangePickerModal(
            onDateRangeSelected = {
                if (it.first != null) {
                    bodyWeightOverviewViewModel.setStartDate(
                        Instant.fromEpochMilliseconds(it.first!!)
                    )
                }
                if (it.second != null) {
                    bodyWeightOverviewViewModel.setEndDate(
                        Instant.fromEpochMilliseconds(it.second!!)
                    )
                }
                showDateRangeDialog = false
            },
            onDismiss = { showDateRangeDialog = false }
        )
    }

    if (showAddDialog) {
        AddBodyWeightDialog(
            settings = settings,
            onSave = { date,
                       weightInKg,
                       bodyFatPercentage,
                       muscleMassInKg,
                       boneMassInKg,
                       bodyWaterInPercentage ->
                bodyWeightOverviewViewModel.addBodyWeight(
                    date,
                    weightInKg,
                    bodyFatPercentage,
                    muscleMassInKg,
                    boneMassInKg,
                    bodyWaterInPercentage
                )
                showAddDialog = false
            },
            onDismiss = { showAddDialog = false }
        )
    }
}
