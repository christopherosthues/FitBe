package org.darthacheron.fitbe.health.weight

import io.github.koalaplot.core.line.StackedAreaPlotEntry

class StackedAreaPlotDoubleDataAdapter<X>(
    private val xData: List<X>,
    private val yData: List<List<Double>>
) :
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