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
import androidx.compose.ui.semantics.clearAndSetSemantics
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
import fitbe.composeapp.generated.resources.local_time_format
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
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.darthacheron.fitbe.health.components.format
import org.darthacheron.fitbe.health.components.representatives
import org.darthacheron.fitbe.settings.WeightUnit
import org.darthacheron.fitbe.utils.StackedAreaPlotDoubleDataAdapter
import org.darthacheron.fitbe.utils.roundToDecimals
import org.jetbrains.compose.resources.stringResource
import kotlin.math.max

@Suppress("MagicNumber")
private val colorPalette =
    listOf(
        Color(0xFFE3DAC9),
        Color(0xFFCC6666),
        Color(0xFFE6BC00),
        Color(0xFF0F5E9C),
        Color(0xFF8068A0)
    )

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
fun PlotDailyBodyWeights(
    modifier: Modifier = Modifier,
    bodyWeights: List<BodyWeight>,
    times: List<LocalTime>,
    weightUnit: WeightUnit,
    maxBodyWeight: Double,
    thumbnail: Boolean = false,
    targetBodyWeight: Double? = null
) {
    ChartLayout(
        modifier = modifier.padding(horizontal = 8.dp),
        title = {
            if (thumbnail) {
                Text(text = stringResource(Res.string.body_weight_chart_thumbnail_title))
            }
        }
    ) {
        val representatives = times.representatives()

        XYGraph(
            xAxisModel = CategoryAxisModel(times),
            yAxisModel = DoubleLinearAxisModel(0.0..maxBodyWeight),
            horizontalMajorGridLineStyle = null,
            horizontalMinorGridLineStyle = null,
            verticalMajorGridLineStyle = null,
            verticalMinorGridLineStyle = null,
            xAxisLabels = { representative ->
                if (!thumbnail && representative in representatives) {
                    Text(
                        text = representative.format(stringResource(Res.string.local_time_format)),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodySmall,
                        modifier =
                            Modifier
                                .padding(top = 2.dp)
                                .clearAndSetSemantics { },
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
                        modifier =
                            Modifier
                                .padding(top = 2.dp)
                                .clearAndSetSemantics { },
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            },
            yAxisTitle = {
                if (!thumbnail) {
                    Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                        Text(
                            text = stringResource(weightUnit.toStringResource()),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.rotateVertically(VerticalRotation.COUNTER_CLOCKWISE)
                        )
                    }
                }
            }
        ) {
            // TODO: accessible plot data
            if (times.size > 1) {
                val yData =
                    toVerticalStackedAreaBodyWeightData(bodyWeights)
                StackedAreaPlot(
                    data = StackedAreaPlotDoubleDataAdapter(times, yData),
                    styles =
                        colorPalette.map {
                            StackedAreaStyle(
                                LineStyle(brush = SolidColor(Color.White), strokeWidth = 8.dp),
                                AreaStyle(brush = SolidColor(it))
                            )
                        },
                    firstBaseline = AreaBaseline.ConstantLine(0.0)
                )
                Annotations(times, yData, thumbnail)
            } else if (times.size == 1) {
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
                                                0 ->
                                                    stringResource(
                                                        Res.string.body_weight_chart_annotation_bone_mass_value,
                                                        bodyWeight.boneMassInKg ?: 0.0,
                                                        stringResource(weightUnit.toStringResource())
                                                    )

                                                1 ->
                                                    stringResource(
                                                        Res.string.body_weight_chart_annotation_muscle_mass_value,
                                                        bodyWeight.muscleMassInKg ?: 0.0,
                                                        stringResource(weightUnit.toStringResource())
                                                    )

                                                2 ->
                                                    stringResource(
                                                        Res.string.body_weight_chart_annotation_body_fat_value,
                                                        bodyWeight.bodyFatPercentage ?: 0.0
                                                    )

                                                3 ->
                                                    stringResource(
                                                        Res.string.body_weight_chart_annotation_body_water_value,
                                                        bodyWeight.bodyWaterInPercentage ?: 0.0
                                                    )

                                                else ->
                                                    stringResource(
                                                        Res.string.body_weight_chart_annotation_total_weight_value,
                                                        bodyWeight.weightInKg,
                                                        stringResource(weightUnit.toStringResource())
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

            if (targetBodyWeight != null && targetBodyWeight > 0.0) {
                LinePlot(
                    data = times.map { Point(it, targetBodyWeight) },
                    lineStyle =
                        LineStyle(
                            brush = SolidColor(Color(0xFFED7D31)),
                            strokeWidth = 2.dp
                        )
                )
            }
        }
    }
}

@Suppress("MagicNumber")
@Composable
private fun XYGraphScope<LocalTime, Double>.Annotations(
    times: List<LocalTime>,
    bodyWeightData: List<List<Double>>,
    thumbnail: Boolean
) {
    if (!thumbnail) {
        val max = bodyWeightData.map { it.max() }
        val maxIndices =
            bodyWeightData.mapIndexed { index, entry ->
                entry.indexOfFirst { it == max[index] }
            }

        bodyWeightData.forEachIndexed { index, data ->
            val dataIndex = maxIndices[index]

            var sum = 0.0
            for (i in 0..<index) {
                sum += bodyWeightData[i][dataIndex]
            }

            val anchorPoint =
                when (dataIndex) {
                    0 -> AnchorPoint.LeftMiddle
                    times.lastIndex -> AnchorPoint.RightMiddle
                    else -> AnchorPoint.Center
                }

            XYAnnotation(
                Point(times[dataIndex], (sum + data[dataIndex] / 2.0)),
                anchorPoint
            ) {
                Text(
                    text =
                        when (index) {
                            0 -> stringResource(Res.string.body_weight_chart_annotation_bone_mass)
                            1 -> stringResource(Res.string.body_weight_chart_annotation_muscle_mass)
                            2 -> stringResource(Res.string.body_weight_chart_annotation_body_fat)
                            3 -> stringResource(Res.string.body_weight_chart_annotation_body_water)
                            else -> stringResource(Res.string.body_weight_chart_annotation_total_weight)
                        },
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray,
                    modifier =
                        Modifier
                            .padding(horizontal = KoalaPlotTheme.sizes.gap)
                            .background(Color.LightGray, RoundedCornerShape(4.dp))
                            .padding(horizontal = KoalaPlotTheme.sizes.gap)
                )
            }
        }
    }
}

private fun toVerticalStackedBodyWeightData(
    bodyWeights: List<BodyWeight>
): List<VerticalBarPlotStackedPointEntry<LocalTime, Double>> {
    val bodyWeightEntries =
        bodyWeights.map { bodyWeight ->
            val totalWeight = bodyWeight.weightInKg

            val boneMass = bodyWeight.boneMassInKg ?: 0.0
            val muscleMass = bodyWeight.muscleMassInKg ?: 0.0
            val bodyFat = (totalWeight * (bodyWeight.bodyFatPercentage ?: 0.0) / 100).roundToDecimals(2)
            val bodyWater = (totalWeight * (bodyWeight.bodyWaterInPercentage ?: 0.0) / 100).roundToDecimals(2)
            DefaultVerticalBarPlotStackedPointEntry(
                bodyWeight.date.toLocalDateTime(TimeZone.currentSystemDefault()).time,
                0.0,
                listOf(
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

private fun toVerticalStackedAreaBodyWeightData(bodyWeights: List<BodyWeight>): List<List<Double>> {
    val totalWeights = mutableListOf<Double>()
    val boneMasses = mutableListOf<Double>()
    val muscleMasses = mutableListOf<Double>()
    val bodyFats = mutableListOf<Double>()
    val bodyWaters = mutableListOf<Double>()
    for (bodyWeight in bodyWeights) {
        val totalWeight = bodyWeight.weightInKg

        val boneMass = bodyWeight.boneMassInKg ?: 0.0
        val muscleMass = bodyWeight.muscleMassInKg ?: 0.0
        val bodyFat = (totalWeight * (bodyWeight.bodyFatPercentage ?: 0.0) / 100).roundToDecimals(2)
        val bodyWater = (totalWeight * (bodyWeight.bodyWaterInPercentage ?: 0.0) / 100).roundToDecimals(2)
        val restWeight = max(totalWeight - boneMass - muscleMass - bodyFat - bodyWater, 0.0).roundToDecimals(2)

        boneMasses.add(boneMass)
        muscleMasses.add(muscleMass)
        bodyFats.add(bodyFat)
        bodyWaters.add(bodyWater)
        totalWeights.add(restWeight)
    }
    return listOf(
        boneMasses,
        muscleMasses,
        bodyFats,
        bodyWaters,
        totalWeights
    )
}