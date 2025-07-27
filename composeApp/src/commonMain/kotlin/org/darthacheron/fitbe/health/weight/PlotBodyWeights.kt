package org.darthacheron.fitbe.health.weight

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.line.AreaBaseline
import io.github.koalaplot.core.line.StackedAreaPlot
import io.github.koalaplot.core.line.StackedAreaPlotEntry
import io.github.koalaplot.core.line.StackedAreaStyle
import io.github.koalaplot.core.style.AreaStyle
import io.github.koalaplot.core.style.LineStyle
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.util.toString
import io.github.koalaplot.core.xygraph.CategoryAxisModel
import io.github.koalaplot.core.xygraph.DoubleLinearAxisModel
import io.github.koalaplot.core.xygraph.XYGraph
import kotlinx.datetime.LocalDate

class StackedAreaPlotDoubleDataAdapter<X>(private val xData: List<X>, private val yData: List<List<Double>>) :
    AbstractList<StackedAreaPlotEntry<X, Double>>() {

    init {
        if (xData.isNotEmpty()) {
            require(yData.isNotEmpty()) { "yData must not be empty if xData is not empty" }
            yData.forEachIndexed { index, data ->
                require(xData.size == data.size) {
                    "Size of yData with index $index must be the same size as xData."
                }
            }
        }
    }

    override val size: Int = xData.size

    override fun get(index: Int): StackedAreaPlotEntry<X, Double> {
        return object : StackedAreaPlotEntry<X, Double> {
            override val x: X = xData[index]
            override val y: Array<Double>
                get() {
                    var last = 0.0
                    return Array(yData.size) {
                        last += yData[it][index]
                        last
                    }
                }
        }
    }
}

@Suppress("MagicNumber")
private val colorPalette = listOf(
    Color(0xFF00498F),
    Color(0xFF37A78F),
    Color(0xFFC05050),
    Color(0xFFED7D31),
    Color(0xFF8068A0)
)

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
fun PlotBodyWeights(bodyWeights: Pair<List<LocalDate>, List<List<Double>>>, maxWeight: Double, thumbnail: Boolean) {
    ChartLayout(
//        modifier = paddingMod.padding(end = 16.dp),
//        title = { ChartTitle(title) },
//        legendLocation = LegendLocation.BOTTOM
    ) {
        val dates = bodyWeights.first
        val weights: List<List<Double>> = bodyWeights.second
        val bodyWeightsStackedData = StackedAreaPlotDoubleDataAdapter(dates, weights)
        XYGraph(
            xAxisModel = CategoryAxisModel(dates),
            yAxisModel = DoubleLinearAxisModel(0.0..maxWeight),
            horizontalMajorGridLineStyle = null,
            horizontalMinorGridLineStyle = null,
            verticalMajorGridLineStyle = null,
            verticalMinorGridLineStyle = null,
            xAxisLabels = {
                if (!thumbnail) {
                    Text(
                        it.toString(),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 2.dp),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            },
            xAxisTitle = {
//                if (!thumbnail) {
//                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
//                        AxisTitle("Year")
//                    }
//                }
            },
            yAxisLabels = {
                if (!thumbnail) {
                    Text(
                        it.toString(0),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 2.dp),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            },
//            yAxisTitle = {
//                if (!thumbnail) {
//                    Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
//                        AxisTitle(
//                            "Population (Millions)",
//                            modifier = Modifier.rotateVertically(VerticalRotation.COUNTER_CLOCKWISE)
//                        )
//                    }
//                }
//            }
        ) {
            StackedAreaPlot(
                bodyWeightsStackedData,
                colorPalette.map {
                    StackedAreaStyle(
                        LineStyle(brush = SolidColor(Color.White), strokeWidth = 8.dp),
                        AreaStyle(brush = SolidColor(it))
                    )
                },
                AreaBaseline.ConstantLine(0.0)
            )

//            annotations(thumbnail)
        }
    }
}