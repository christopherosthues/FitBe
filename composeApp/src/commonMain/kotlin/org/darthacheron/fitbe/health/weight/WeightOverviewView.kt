package org.darthacheron.fitbe.health.weight


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledIconButton
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
import fitbe.composeapp.generated.resources.body_weight_body_fat_error
import fitbe.composeapp.generated.resources.body_weight_body_water_error
import fitbe.composeapp.generated.resources.body_weight_bone_mass_error_kg
import fitbe.composeapp.generated.resources.body_weight_bone_mass_error_lb
import fitbe.composeapp.generated.resources.body_weight_muscle_mass_error_kg
import fitbe.composeapp.generated.resources.body_weight_muscle_mass_error_lb
import fitbe.composeapp.generated.resources.body_weight_total_weight_error_kg
import fitbe.composeapp.generated.resources.body_weight_total_weight_error_lb
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
import org.darthacheron.fitbe.settings.BodyMeasurementUnit
import org.darthacheron.fitbe.settings.Settings
import org.darthacheron.fitbe.settings.SettingsRepository
import org.darthacheron.fitbe.settings.WeightUnit
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
    val weights by bodyWeightOverviewViewModel.bodyWeights.collectAsState()
    val settings by settingsRepository.getSettingsFlow().collectAsState(Settings())
    var selectedViewTypeIndex by remember { mutableStateOf(0) }
    val startDate by bodyWeightOverviewViewModel.startDate.collectAsState()
    val endDate by bodyWeightOverviewViewModel.endDate.collectAsState()
    var showDateRangeDialog by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }
    val viewTypes = SleepViewType.entries

    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly
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
                title = "Choose an option",
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
            if (!weights.isEmpty()) {
                Plot(weights)
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
            FilledIconButton(
                onClick = { showAddDialog = true },
                modifier = Modifier.align(Alignment.BottomEnd)
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
//                 viewModel.addSleep(start.toUInt(), end.toUInt(), )
                showAddDialog = false
            },
            onDismiss = { showAddDialog = false }
        )
    }
}

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
fun Plot(sleeps: List<Point<LocalDate, Double>>) {
    ChartLayout(
    ) {
        val dates = sleeps.map { it.x }
        XYGraph(
            xAxisModel = CategoryAxisModel(dates),
            yAxisModel = DoubleLinearAxisModel(
                range = 0.0..sleeps.maxOf { ceil(it.y) },
                minorTickCount = 1
            ),
            xAxisLabels = {
                Text(
                    it.toString(),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 2.dp).graphicsLayer {
                        rotationZ = -75f
                    },
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
            },
            xAxisTitle = { null },
            gestureConfig = GestureConfig(zoomXEnabled = true, zoomYEnabled = true),
            yAxisLabels = {
                Text(
                    "${it.toInt()}h",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.absolutePadding(right = 2.dp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
            },
        ) {
            StairstepPlot(
                data = sleeps,
                lineStyle = LineStyle(
                    brush = SolidColor(Color.Black),
                    strokeWidth = 2.dp
                )
            )
        }
    }
}

@Composable
fun AddBodyWeightDialog(
    settings: Settings,
    onDismiss: () -> Unit,
    onSave: (
        date: LocalDate,
        weightInKg: Double,
        bodyFatPercentage: Double,
        muscleMassInKg: Double,
        boneMassInKg: Double,
        bodyWaterInPercentage: Double
    ) -> Unit
) {
    var weight by remember { mutableStateOf(0.0) }
    var bodyFat by remember { mutableStateOf(0.0) }
    var muscleMass by remember { mutableStateOf(0.0) }
    var boneMass by remember { mutableStateOf(0.0) }
    var bodyWater by remember { mutableStateOf(0.0) }
    var date by remember { mutableStateOf(Clock.System.now().toLocalDateTime(TimeZone.UTC).date) }
    var showDateDialog by remember { mutableStateOf(false) }
    var weightError by remember { mutableStateOf(false) }
    var bodyFatError by remember { mutableStateOf(false) }
    var muscleMassError by remember { mutableStateOf(false) }
    var boneMassError by remember { mutableStateOf(false) }
    var bodyWaterError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Weight Entry") },
        confirmButton = {
            Button(onClick = {
                val partsSum = listOf(
                    bodyFat,
                    muscleMass,
                    boneMass,
                    bodyWater,
                ).sum()

                if (partsSum <= weight) {
                    onSave(date, weight, bodyFat, muscleMass, boneMass, bodyWater)
                }
            }) { Text("Save") }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
        },
        text = {
            Column {
                OutlinedTextField(
                    value = date.toString(),
                    onValueChange = {},
                    label = { Text("Date") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showDateDialog = true }) {
                            Icon(
                                painterResource(Res.drawable.ic_date_range),
                                contentDescription = null
                            )
                        }
                    },
                    modifier = Modifier.weight(1f)
                )

                Spacer(Modifier.width(8.dp))

                OutlinedTextField(
                    value = weight.toDoubleStringOrDash(),
                    onValueChange = {
                        weightError = it.startsWith("-") && it.length > 1
                        weight = it.toDoubleOrMinusOne()
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    label = { Text("Total Weight (kg)") },
                    isError = weightError,
                    supportingText = {
                        if (weightError) Text(
                            stringResource(
                                when (settings.weightUnit) {
                                    WeightUnit.KG -> Res.string.body_weight_total_weight_error_kg
                                    WeightUnit.POUND -> Res.string.body_weight_total_weight_error_lb
                                }
                            )
                        )
                    },
                )
                OutlinedTextField(
                    value = bodyFat.toDoubleStringOrDash(),
                    onValueChange = { bodyFat = it.toDoubleOrMinusOne() },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    label = { Text("Body Fat (%)") },
                    isError = bodyFatError,
                    supportingText = {
                        if (bodyFatError) Text(
                            stringResource(Res.string.body_weight_body_fat_error)
                        )
                    },
                )
                OutlinedTextField(
                    value = muscleMass.toDoubleStringOrDash(),
                    onValueChange = { muscleMass = it.toDoubleOrMinusOne() },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    label = { Text("Muscle Mass (kg)") },
                    isError = muscleMassError,
                    supportingText = {
                        if (muscleMassError) Text(
                            stringResource(
                                when (settings.weightUnit) {
                                    WeightUnit.KG -> Res.string.body_weight_muscle_mass_error_kg
                                    WeightUnit.POUND -> Res.string.body_weight_muscle_mass_error_lb
                                }
                            )
                        )
                    },
                )
                OutlinedTextField(
                    value = boneMass.toDoubleStringOrDash(),
                    onValueChange = { boneMass = it.toDoubleOrMinusOne() },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    label = { Text("Bone Mass (kg)") },
                    isError = boneMassError,
                    supportingText = {
                        if (boneMassError) Text(
                            stringResource(
                                when (settings.weightUnit) {
                                    WeightUnit.KG -> Res.string.body_weight_bone_mass_error_kg
                                    WeightUnit.POUND -> Res.string.body_weight_bone_mass_error_lb
                                }
                            )
                        )
                    },
                )
                OutlinedTextField(
                    value = bodyWater.toDoubleStringOrDash(),
                    onValueChange = { bodyWater = it.toDoubleOrMinusOne() },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    label = { Text("Body Water (%)") },
                    isError = bodyWaterError,
                    supportingText = {
                        if (bodyWaterError) Text(
                            stringResource(Res.string.body_weight_body_water_error)
                        )
                    },
                )
            }
        }
    )

    if (showDateDialog) {
        DatePickerModal(
            onDateSelected = { millis ->
                millis?.let {
                    date = Instant.fromEpochMilliseconds(it).toLocalDateTime(TimeZone.UTC).date
                }
                showDateDialog = false
            },
            onDismiss = { showDateDialog = false }
        )
    }
}

fun String.toDoubleOrMinusOne(): Double {
    return this.toDoubleOrNull() ?: -1.0
}

fun Double.toDoubleStringOrDash(): String {
    return if (this < 0) "-" else this.toString()
}