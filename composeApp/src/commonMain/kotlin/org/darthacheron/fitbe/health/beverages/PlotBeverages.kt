package org.darthacheron.fitbe.health.beverages

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.beverages_chart_annotation_beverage_value
import fitbe.composeapp.generated.resources.beverages_chart_thumbnail_title
import fitbe.composeapp.generated.resources.beverages_chart_y_axis_title
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
import kotlinx.datetime.Month
import org.darthacheron.fitbe.components.date.DateRange
import org.darthacheron.fitbe.components.date.DateUnit
import org.darthacheron.fitbe.health.componenets.monthResourceString
import org.darthacheron.fitbe.utils.isoWeekAndYear
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.roundToInt

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
fun PlotBeverages(
    modifier: Modifier = Modifier,
    beverages: List<BeverageOverview>,
    dateRange: DateRange,
    dates: List<LocalDate>,
    maxBeverages: UInt,
    thumbnail: Boolean = false,
    targetBeverages: UInt? = null,
) {
    ChartLayout(modifier = modifier.padding(horizontal = 8.dp),
        title = {
            if (thumbnail) {
                Text(text = stringResource(Res.string.beverages_chart_thumbnail_title))
            }
        }) {
        val maxConfigurableLabels = 7
        val actualDatesForLabels: Set<LocalDate> = if (dates.isEmpty()) {
            emptySet()
        } else if (dates.size <= maxConfigurableLabels) {
            dates.toSet()
        } else {
            val selectedDates = mutableSetOf<LocalDate>()
            for (i in 0 until maxConfigurableLabels) {
                val idealPositionRatio = i.toDouble() / (maxConfigurableLabels - 1)
                val indexInDates = (idealPositionRatio * (dates.size - 1)).roundToInt()
                selectedDates.add(dates[indexInDates.coerceIn(0, dates.size - 1)])
            }
            selectedDates
        }

        XYGraph(
            xAxisModel = CategoryAxisModel(dates),
            yAxisModel = IntLinearAxisModel(0..maxBeverages.toInt()),
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
                                val monthResource = labelDate.monthResourceString()
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
                        text = it.toString(),
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
                            text = stringResource(Res.string.beverages_chart_y_axis_title),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.rotateVertically(VerticalRotation.COUNTER_CLOCKWISE)
                        )
                    }
                }
            },
        ) {
            if (dates.size > 1) {
                AreaPlot(
                    data = beverages.map { Point(it.date, it.amountMl.toInt()) },
                    areaBaseline = AreaBaseline.ConstantLine(0),
                    areaStyle = AreaStyle(brush = SolidColor(Color(0xFFCC6666))),
                    lineStyle = LineStyle(
                        brush = SolidColor(MaterialTheme.colorScheme.primary),
                        strokeWidth = 2.dp
                    ),
                )
            } else if (dates.size == 1) {
                val beveragesChartData = toVerticalBarData(beverages)
                VerticalBarPlot(
                    data = beveragesChartData,
                    barWidth = 0.8f,
                    bar = { index ->
                        DefaultVerticalBar(
                            brush = SolidColor(Color(0xFFCC6666)),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            if (!thumbnail) {
                                Surface(
                                    shadowElevation = 2.dp,
                                    shape = MaterialTheme.shapes.medium,
                                    color = Color.LightGray,
                                    modifier = modifier.padding(8.dp)
                                ) {
                                    Box(modifier = Modifier.padding(8.dp)) {
                                        Text(text = stringResource(Res.string.beverages_chart_annotation_beverage_value, beveragesChartData[index].y.yMax))
                                    }
                                }
                            }
                        }
                    },
                )
            }
            if (beverages.isNotEmpty()) {
                LinePlot(
                    data = beverages.map { Point(it.date, it.amountMl.toInt()) },
                    lineStyle = LineStyle(
                        brush = SolidColor(MaterialTheme.colorScheme.primary),
                        strokeWidth = 2.dp
                    ),
                )
            }

            if (targetBeverages != null && targetBeverages > 0u) {
                LinePlot(
                    data = dates.map { Point(it, targetBeverages.toInt()) },
                    lineStyle = LineStyle(
                        brush = SolidColor(Color(0xFFED7D31)),
                        strokeWidth = 2.dp,
                    ),
                )
            }
        }
    }
}

private fun toVerticalBarData(steps: List<BeverageOverview>): List<VerticalBarPlotEntry<LocalDate, Int>> {
    return steps.map {
        DefaultVerticalBarPlotEntry(it.date, DefaultVerticalBarPosition(0, it.amountMl.toInt()))
    }
}