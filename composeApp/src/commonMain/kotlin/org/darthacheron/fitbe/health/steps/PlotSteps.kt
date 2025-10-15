package org.darthacheron.fitbe.health.steps

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.steps_chart_annotation_steps_value
import fitbe.composeapp.generated.resources.steps_chart_thumbnail_title
import fitbe.composeapp.generated.resources.steps_chart_y_axis_title
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.bar.DefaultVerticalBar
import io.github.koalaplot.core.bar.DefaultVerticalBarPlotEntry
import io.github.koalaplot.core.bar.DefaultVerticalBarPosition
import io.github.koalaplot.core.bar.VerticalBarPlot
import io.github.koalaplot.core.bar.VerticalBarPlotEntry
import io.github.koalaplot.core.line.AreaBaseline
import io.github.koalaplot.core.line.AreaPlot
import io.github.koalaplot.core.line.LinePlot
import io.github.koalaplot.core.style.AreaStyle
import io.github.koalaplot.core.style.LineStyle
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.util.VerticalRotation
import io.github.koalaplot.core.util.rotateVertically
import io.github.koalaplot.core.xygraph.CategoryAxisModel
import io.github.koalaplot.core.xygraph.IntLinearAxisModel
import io.github.koalaplot.core.xygraph.Point
import io.github.koalaplot.core.xygraph.XYGraph
import io.github.koalaplot.core.xygraph.rememberAxisStyle
import kotlinx.datetime.LocalDate
import org.darthacheron.fitbe.components.date.DateRange
import org.darthacheron.fitbe.health.components.dateLabel
import org.darthacheron.fitbe.health.components.representatives
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
fun PlotSteps(
    modifier: Modifier = Modifier,
    stepsData: List<StepsOverview>,
    dateRange: DateRange,
    dates: List<LocalDate>,
    maxSteps: UInt,
    thumbnail: Boolean = false,
    targetSteps: UInt? = null
) {
    ChartLayout(
        modifier = modifier.padding(horizontal = 8.dp),
        title = {
            if (thumbnail) {
                Text(text = stringResource(Res.string.steps_chart_thumbnail_title))
            }
        }
    ) {
        val representatives = dates.representatives()

        XYGraph(
            xAxisModel = CategoryAxisModel(dates),
            yAxisModel = IntLinearAxisModel(0..maxSteps.toInt()),
            horizontalMajorGridLineStyle = null,
            horizontalMinorGridLineStyle = null,
            verticalMajorGridLineStyle = null,
            verticalMinorGridLineStyle = null,
            xAxisLabels = { representative ->
                if (!thumbnail && representative in representatives) {
                    Text(
                        text = representative.dateLabel(dateRange.dateUnit),
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
                        text = it.toString(),
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
                            text = stringResource(Res.string.steps_chart_y_axis_title),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.rotateVertically(VerticalRotation.COUNTER_CLOCKWISE)
                        )
                    }
                }
            }
        ) {
            // TODO: accessible plot data
            if (dates.size > 1) {
                AreaPlot(
                    data = stepsData.map { Point(it.date, it.steps.toInt()) },
                    areaBaseline = AreaBaseline.ConstantLine(0),
                    areaStyle = AreaStyle(brush = SolidColor(Color(0xFFCC6666))),
                    lineStyle =
                        LineStyle(
                            brush = SolidColor(MaterialTheme.colorScheme.primary),
                            strokeWidth = 2.dp
                        )
                )
            } else if (dates.size == 1) {
                val stepsChartData = toVerticalBarData(stepsData)
                VerticalBarPlot(
                    data = stepsChartData,
                    barWidth = 0.8f,
                    bar = { index ->
                        DefaultVerticalBar(
                            brush = SolidColor(Color(0xFFCC6666)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (!thumbnail) {
                                Surface(
                                    shadowElevation = 2.dp,
                                    shape = MaterialTheme.shapes.medium,
                                    color = Color.LightGray,
                                    modifier = modifier.padding(8.dp)
                                ) {
                                    Box(modifier = Modifier.padding(8.dp)) {
                                        Text(
                                            text =
                                                stringResource(
                                                    Res.string.steps_chart_annotation_steps_value,
                                                    stepsChartData[index].y.yMax
                                                )
                                        )
                                    }
                                }
                            }
                        }
                    }
                )
            }
            if (stepsData.isNotEmpty()) {
                LinePlot(
                    data = stepsData.map { Point(it.date, it.steps.toInt()) },
                    lineStyle =
                        LineStyle(
                            brush = SolidColor(MaterialTheme.colorScheme.primary),
                            strokeWidth = 2.dp
                        )
                )
            }

            if (targetSteps != null && targetSteps > 0u) {
                LinePlot(
                    data = dates.map { Point(it, targetSteps.toInt()) },
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

private fun toVerticalBarData(steps: List<StepsOverview>): List<VerticalBarPlotEntry<LocalDate, Int>> =
    steps.map {
        DefaultVerticalBarPlotEntry(it.date, DefaultVerticalBarPosition(0, it.steps.toInt()))
    }