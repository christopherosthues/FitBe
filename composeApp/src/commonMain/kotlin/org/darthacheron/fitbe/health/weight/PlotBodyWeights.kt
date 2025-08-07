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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.bar.DefaultVerticalBar
import io.github.koalaplot.core.bar.StackedVerticalBarPlot
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
import org.darthacheron.fitbe.components.DateUnit
import org.darthacheron.fitbe.settings.Settings
import org.darthacheron.fitbe.utils.isoWeekAndYear
import org.jetbrains.compose.resources.stringResource
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
    bodyWeightOverviewViewModel: WeightOverviewViewModel,
    bodyWeights: List<BodyWeight>,
    settings: Settings,
    maxWeight: Double,
    thumbnail: Boolean
) {
    ChartLayout(modifier = Modifier.padding(bottom = 64.dp)) {
        val dates = bodyWeightOverviewViewModel.dates(bodyWeights)
        val targetWeight by bodyWeightOverviewViewModel.targetWeight.collectAsState()
        val dateRange by bodyWeightOverviewViewModel.dateRange.collectAsState()

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
                        when (dateRange.dateUnit) {
                            DateUnit.DAY -> labelDate.toString()
                            DateUnit.WEEK -> "W${labelDate.isoWeekAndYear().second}/${labelDate.year}"
                            DateUnit.MONTH -> "${labelDate.month}/${labelDate.year}"
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
                        it.toString(0),
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
                            stringResource(settings.weightUnit.localizedString()),
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
                    bodyWeightOverviewViewModel.toVerticalStackedAreaBodyWeightData(bodyWeights)
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
                    data = bodyWeightOverviewViewModel.toVerticalStackedBodyWeightData(bodyWeights),
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
                                        Text(bodyWeightAnnotation)
                                    }
                                }
                            }
                        }
                    }
                )
            }

            if (targetWeight != null) {
                LinePlot(
                    data = dates.map { Point(it, targetWeight!!) },
                    lineStyle = LineStyle(
                        brush = SolidColor(Color(0xFFED7D31)),
                        strokeWidth = 2.dp
                    ),
                )
            }
        }
    }
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
                    when (index) {
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
