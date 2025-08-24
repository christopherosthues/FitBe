package org.darthacheron.fitbe.health.weight

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.body_weight_chart_annotation_body_fat
import fitbe.composeapp.generated.resources.body_weight_chart_annotation_body_fat_value
import fitbe.composeapp.generated.resources.body_weight_chart_annotation_body_water
import fitbe.composeapp.generated.resources.body_weight_chart_annotation_body_water_value
import fitbe.composeapp.generated.resources.body_weight_chart_annotation_bone_mass
import fitbe.composeapp.generated.resources.body_weight_chart_annotation_bone_mass_value
import fitbe.composeapp.generated.resources.body_weight_chart_annotation_muscle_mass
import fitbe.composeapp.generated.resources.body_weight_chart_annotation_muscle_mass_value
import fitbe.composeapp.generated.resources.body_weight_chart_annotation_total_weight
import fitbe.composeapp.generated.resources.body_weight_chart_annotation_total_weight_value
import fitbe.composeapp.generated.resources.body_weight_chart_thumbnail_title
import fitbe.composeapp.generated.resources.month_april
import fitbe.composeapp.generated.resources.month_august
import fitbe.composeapp.generated.resources.month_december
import fitbe.composeapp.generated.resources.month_february
import fitbe.composeapp.generated.resources.month_january
import fitbe.composeapp.generated.resources.month_july
import fitbe.composeapp.generated.resources.month_june
import fitbe.composeapp.generated.resources.month_march
import fitbe.composeapp.generated.resources.month_may
import fitbe.composeapp.generated.resources.month_november
import fitbe.composeapp.generated.resources.month_october
import fitbe.composeapp.generated.resources.month_september
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.bar.DefaultVerticalBar
import io.github.koalaplot.core.bar.DefaultVerticalBarPlotStackedPointEntry
import io.github.koalaplot.core.bar.StackedVerticalBarPlot
import io.github.koalaplot.core.bar.VerticalBarPlotStackedPointEntry
import io.github.koalaplot.core.line.AreaBaseline
import io.github.koalaplot.core.line.LinePlot
import io.github.koalaplot.core.line.StackedAreaPlot
import io.github.koalaplot.core.line.StackedAreaStyle
import io.github.koalaplot.core.style.AreaStyle
import io.github.koalaplot.core.style.KoalaPlotTheme
import io.github.koalaplot.core.style.LineStyle
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.util.VerticalRotation
import io.github.koalaplot.core.util.rotateVertically
import io.github.koalaplot.core.util.toString
import io.github.koalaplot.core.xygraph.AnchorPoint
import io.github.koalaplot.core.xygraph.CategoryAxisModel
import io.github.koalaplot.core.xygraph.DoubleLinearAxisModel
import io.github.koalaplot.core.xygraph.Point
import io.github.koalaplot.core.xygraph.XYAnnotation
import io.github.koalaplot.core.xygraph.XYGraph
import io.github.koalaplot.core.xygraph.XYGraphScope
import io.github.koalaplot.core.xygraph.rememberAxisStyle
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import org.darthacheron.fitbe.components.date.DateRange
import org.darthacheron.fitbe.components.date.DateUnit
import org.darthacheron.fitbe.settings.Settings
import org.darthacheron.fitbe.utils.StackedAreaPlotDoubleDataAdapter
import org.darthacheron.fitbe.utils.isoWeekAndYear
import org.darthacheron.fitbe.utils.roundToDecimals
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.max
import kotlin.math.roundToInt

@Suppress("MagicNumber")
private val colorPalette = listOf(
    Color(0xFFE3DAC9),
    Color(0xFFCC6666),
    Color(0xFFE6BC00),
    Color(0xFF0F5E9C),
    Color(0xFF8068A0)
)

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
fun PlotBodyWeights(
    modifier: Modifier = Modifier,
    bodyWeights: List<BodyWeight>,
    dateRange: DateRange,
    dates: List<LocalDate>,
    settings: Settings,
    maxWeight: Double,
    thumbnail: Boolean = false,
    targetWeight: Double? = null,
) {
    ChartLayout(
        modifier = modifier,
        title = {
            if (thumbnail) {
                Text(text = stringResource(Res.string.body_weight_chart_thumbnail_title))
            }
        }) {
        val maxConfigurableLabels = 7
        val actualDatesForLabels: Set<LocalDate> = if (dates.isEmpty()) {
            emptySet()
        } else if (dates.size <= maxConfigurableLabels) {
            dates.toSet()
        } else { // dates.size > maxConfigurableLabels (which is 7)
            val selectedDates = mutableSetOf<LocalDate>()
            // maxConfigurableLabels is 7, so maxConfigurableLabels - 1 is 6 (not zero).
            // dates.size > 7, so dates.size - 1 is at least 7.
            for (i in 0 until maxConfigurableLabels) { // Loop i from 0 to 6
                // Calculate the ideal proportional position from 0.0 to 1.0
                val idealPositionRatio = i.toDouble() / (maxConfigurableLabels - 1)
                // Map this ratio to an index in the 'dates' list
                val indexInDates = (idealPositionRatio * (dates.size - 1)).roundToInt()
                // Add the date at the calculated index, ensuring it's within bounds
                selectedDates.add(dates[indexInDates.coerceIn(0, dates.size - 1)])
            }
            selectedDates
        }

        XYGraph(
            xAxisModel = CategoryAxisModel(dates),
            yAxisModel = DoubleLinearAxisModel(0.0..maxWeight),
            horizontalMajorGridLineStyle = null,
            horizontalMinorGridLineStyle = null,
            verticalMajorGridLineStyle = null,
            verticalMinorGridLineStyle = null,
            xAxisLabels = { labelDate ->
                if (!thumbnail && labelDate in actualDatesForLabels) {
                    Text(
                        text = when (dateRange.dateUnit) {
                            DateUnit.DAY -> labelDate.toString()
                            DateUnit.WEEK -> "W${labelDate.isoWeekAndYear().second}/${labelDate.year}"
                            DateUnit.MONTH -> {
                                val monthResource = monthResourceString(labelDate)
                                "${stringResource(monthResource)}/${labelDate.year}"
                            }
                            DateUnit.YEAR -> labelDate.year.toString()
                        },
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 2.dp),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            },
            xAxisStyle = rememberAxisStyle(labelRotation = 45),
            xAxisTitle = { },
            yAxisLabels = {
                if (!thumbnail) {
                    Text(
                        text = it.toString(0),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 2.dp),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )
                }
            },
            yAxisTitle = {
                if (!thumbnail) {
                    Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                        Text(
                            text = stringResource(settings.weightUnit.localizedString()),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.rotateVertically(VerticalRotation.COUNTER_CLOCKWISE)
                        )
                    }
                }
            },
        ) {
            if (dates.size > 1) {
                val yData =
                    toVerticalStackedAreaBodyWeightData(bodyWeights)
                StackedAreaPlot(
                    data = StackedAreaPlotDoubleDataAdapter(dates, yData),
                    styles = colorPalette.map {
                        StackedAreaStyle(
                            LineStyle(brush = SolidColor(Color.White), strokeWidth = 8.dp),
                            AreaStyle(brush = SolidColor(it))
                        )
                    },
                    firstBaseline = AreaBaseline.ConstantLine(0.0)
                )
                Annotations(dates, yData, thumbnail)
            } else if (dates.size == 1) {
                StackedVerticalBarPlot(
                    data = toVerticalStackedBodyWeightData(bodyWeights),
                    barWidth = 0.8f,
                    bar = { xIndex, barIndex ->
                        DefaultVerticalBar(
                            brush = SolidColor(colorPalette[barIndex]),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (!thumbnail) {
                                Surface(
                                    shadowElevation = 2.dp,
                                    shape = MaterialTheme.shapes.medium,
                                    color = Color.LightGray,
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier.padding(8.dp)
                                    ) {
                                        val bodyWeight = bodyWeights[xIndex]
                                        val bodyWeightAnnotation =
                                            when (barIndex) {
                                                0 -> stringResource(
                                                    Res.string.body_weight_chart_annotation_bone_mass_value,
                                                    bodyWeight.boneMassInKg!!,
                                                    stringResource(settings.weightUnit.localizedString())
                                                )

                                                1 -> stringResource(
                                                    Res.string.body_weight_chart_annotation_muscle_mass_value,
                                                    bodyWeight.muscleMassInKg!!,
                                                    stringResource(settings.weightUnit.localizedString())
                                                )

                                                2 -> stringResource(
                                                    Res.string.body_weight_chart_annotation_body_fat_value,
                                                    bodyWeight.bodyFatPercentage!!
                                                )

                                                3 -> stringResource(
                                                    Res.string.body_weight_chart_annotation_body_water_value,
                                                    bodyWeight.bodyWaterInPercentage!!
                                                )

                                                else -> stringResource(
                                                    Res.string.body_weight_chart_annotation_total_weight_value,
                                                    bodyWeight.weightInKg,
                                                    stringResource(settings.weightUnit.localizedString())
                                                )
                                            }
                                        Text(text = bodyWeightAnnotation)
                                    }
                                }
                            }
                        }
                    }
                )
            }

            if (targetWeight != null && targetWeight > 0.0) {
                LinePlot(
                    data = dates.map { Point(it, targetWeight) },
                    lineStyle = LineStyle(
                        brush = SolidColor(Color(0xFFED7D31)),
                        strokeWidth = 2.dp
                    ),
                )
            }
        }
    }
}

private fun monthResourceString(labelDate: LocalDate): StringResource {
    val monthResource = when (labelDate.month) {
        Month.JANUARY -> Res.string.month_january
        Month.FEBRUARY -> Res.string.month_february
        Month.MARCH -> Res.string.month_march
        Month.APRIL -> Res.string.month_april
        Month.MAY -> Res.string.month_may
        Month.JUNE -> Res.string.month_june
        Month.JULY -> Res.string.month_july
        Month.AUGUST -> Res.string.month_august
        Month.SEPTEMBER -> Res.string.month_september
        Month.OCTOBER -> Res.string.month_october
        Month.NOVEMBER -> Res.string.month_november
        Month.DECEMBER -> Res.string.month_december
        else -> Res.string.month_january
    }
    return monthResource
}

@Suppress("MagicNumber")
@Composable
private fun XYGraphScope<LocalDate, Double>.Annotations(
    dates: List<LocalDate>,
    bodyWeightData: List<List<Double>>,
    thumbnail: Boolean
) {
    if (!thumbnail) {
        val max = bodyWeightData.map { it.max() }
        val maxIndices = bodyWeightData.mapIndexed { index, entry ->
            entry.indexOfFirst { it == max[index] }
        }

        bodyWeightData.forEachIndexed { index, data ->
            val dateIndex = maxIndices[index]

            var sum = 0.0
            for (i in 0..<index) {
                sum += bodyWeightData[i][dateIndex]
            }

            val anchorPoint = when (dateIndex) {
                0 -> AnchorPoint.LeftMiddle
                dates.lastIndex -> AnchorPoint.RightMiddle
                else -> AnchorPoint.Center
            }

            XYAnnotation(
                Point(dates[dateIndex], (sum + data[dateIndex] / 2.0)),
                anchorPoint
            ) {
                Text(
                    text = when (index) {
                        0 -> stringResource(
                            Res.string.body_weight_chart_annotation_bone_mass,
                        )

                        1 -> stringResource(
                            Res.string.body_weight_chart_annotation_muscle_mass,
                        )

                        2 -> stringResource(
                            Res.string.body_weight_chart_annotation_body_fat,
                        )

                        3 -> stringResource(
                            Res.string.body_weight_chart_annotation_body_water,
                        )

                        else -> stringResource(
                            Res.string.body_weight_chart_annotation_total_weight,
                        )
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(horizontal = KoalaPlotTheme.sizes.gap)
                        .background(Color.LightGray, RoundedCornerShape(4.dp))
                        .padding(horizontal = KoalaPlotTheme.sizes.gap)
                )
            }
        }
    }
}

private fun toVerticalStackedBodyWeightData(
    bodyWeights: List<BodyWeight>
): List<VerticalBarPlotStackedPointEntry<LocalDate, Double>> {
    val bodyWeightEntries = bodyWeights.map { bodyWeight ->
        val totalWeight = bodyWeight.weightInKg

        val boneMass = bodyWeight.boneMassInKg ?: 0.0
        val muscleMass = bodyWeight.muscleMassInKg ?: 0.0
        val bodyFat =
            (totalWeight * (bodyWeight.bodyFatPercentage ?: 0.0) / 100).roundToDecimals(
                2
            )
        val bodyWater =
            (totalWeight * (bodyWeight.bodyWaterInPercentage ?: 0.0) / 100).roundToDecimals(
                2
            )
        DefaultVerticalBarPlotStackedPointEntry(
            bodyWeight.dateUtc, 0.0, listOf(
                boneMass,
                boneMass + muscleMass,
                boneMass + muscleMass + bodyFat,
                boneMass + muscleMass + bodyFat + bodyWater,
                totalWeight
            )
        )
    }

    return bodyWeightEntries
}

private fun toVerticalStackedAreaBodyWeightData(
    bodyWeights: List<BodyWeight>
): List<List<Double>> {
    val totalWeights = mutableListOf<Double>()
    val boneMasses = mutableListOf<Double>()
    val muscleMasses = mutableListOf<Double>()
    val bodyFats = mutableListOf<Double>()
    val bodyWaters = mutableListOf<Double>()
    for (bodyWeight in bodyWeights) {
        val totalWeight = bodyWeight.weightInKg

        val boneMass = bodyWeight.boneMassInKg ?: 0.0
        val muscleMass = bodyWeight.muscleMassInKg ?: 0.0
        val bodyFat =
            (totalWeight * (bodyWeight.bodyFatPercentage ?: 0.0) / 100).roundToDecimals(
                2
            )
        val bodyWater =
            (totalWeight * (bodyWeight.bodyWaterInPercentage ?: 0.0) / 100).roundToDecimals(
                2
            )
        val restWeight = max(
            totalWeight - boneMass - muscleMass - bodyFat - bodyWater, 0.0
        ).roundToDecimals(2)

        boneMasses.add(boneMass)
        muscleMasses.add(muscleMass)
        bodyFats.add(bodyFat)
        bodyWaters.add(bodyWater)
        totalWeights.add(restWeight)
    }
    return listOf(
        boneMasses, muscleMasses, bodyFats, bodyWaters, totalWeights
    )
}