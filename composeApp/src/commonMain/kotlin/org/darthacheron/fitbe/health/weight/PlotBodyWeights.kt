package org.darthacheron.fitbe.health.weight

import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.gestures.GestureConfig
import io.github.koalaplot.core.line.StairstepPlot
import io.github.koalaplot.core.style.LineStyle
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.xygraph.CategoryAxisModel
import io.github.koalaplot.core.xygraph.DoubleLinearAxisModel
import io.github.koalaplot.core.xygraph.Point
import io.github.koalaplot.core.xygraph.XYGraph
import kotlinx.datetime.LocalDate
import kotlin.math.ceil

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
fun PlotBodyWeights(sleeps: List<Point<LocalDate, Double>>) {
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