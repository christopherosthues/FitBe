package org.darthacheron.fitbe.health.sleep

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
import fitbe.composeapp.generated.resources.local_date_format
import fitbe.composeapp.generated.resources.sleep_chart_annotation_sleep_value
import fitbe.composeapp.generated.resources.sleep_chart_thumbnail_title
import fitbe.composeapp.generated.resources.sleep_chart_y_axis_title
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.bar.DefaultBar
import io.github.koalaplot.core.bar.DefaultBarPosition
import io.github.koalaplot.core.bar.DefaultVerticalBarPlotEntry
import io.github.koalaplot.core.bar.VerticalBarPlot
import io.github.koalaplot.core.bar.VerticalBarPlotEntry
import io.github.koalaplot.core.line.LinePlot2
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
import org.darthacheron.fitbe.health.components.format
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
fun PlotDailySleep(
    modifier: Modifier = Modifier,
    date: LocalDate,
    totalSleep: Int,
    maxSleeps: Int,
    thumbnail: Boolean = false,
    targetSleepDuration: Int? = null
) {
    ChartLayout(
        modifier = modifier.padding(horizontal = 8.dp),
        title = {
            if (thumbnail) {
                Text(text = stringResource(Res.string.sleep_chart_thumbnail_title))
            }
        }
    ) {

        XYGraph(
            xAxisModel = CategoryAxisModel(listOf(date)),
            yAxisModel = IntLinearAxisModel(0..maxSleeps),
            horizontalMajorGridLineStyle = null,
            horizontalMinorGridLineStyle = null,
            verticalMajorGridLineStyle = null,
            verticalMinorGridLineStyle = null,
            xAxisLabels = {
                if (!thumbnail) {
                    Text(
                        text = date.format(stringResource(Res.string.local_date_format)),
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
                            text = stringResource(Res.string.sleep_chart_y_axis_title),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.rotateVertically(VerticalRotation.COUNTER_CLOCKWISE)
                        )
                    }
                }
            }
        ) {
            // TODO: accessible plot data
            val sleepsChartData = toVerticalBarData(date, totalSleep)
            VerticalBarPlot(
                data = sleepsChartData,
                barWidth = 0.8f,
                bar = { index, _, _ ->
                    DefaultBar(
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
                                                Res.string.sleep_chart_annotation_sleep_value,
                                                sleepsChartData[index].y.end.toString()
                                            )
                                    )
                                }
                            }
                        }
                    }
                }
            )

            if (targetSleepDuration != null && targetSleepDuration > 0) {
                LinePlot2(
                    data = listOf(Point(date, targetSleepDuration)),
                    lineStyle =
                        LineStyle(
                            brush = SolidColor(Color(0xFFED7D31)),
                            strokeWidth = 2.dp
                        ),
                    symbol = { Box {} }
                )
            }
        }
    }
}

private fun toVerticalBarData(date: LocalDate, sleep: Int): List<VerticalBarPlotEntry<LocalDate, Int>> =
    listOf(DefaultVerticalBarPlotEntry(date, DefaultBarPosition(0, sleep)))