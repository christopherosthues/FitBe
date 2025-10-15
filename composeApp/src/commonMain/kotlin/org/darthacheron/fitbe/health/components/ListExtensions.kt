package org.darthacheron.fitbe.health.components

import kotlin.math.roundToInt

fun <E> List<E>.representatives(): Set<E> {
    val maxConfigurableLabels = 7
    val representatives: Set<E> =
        if (this.isEmpty()) {
            emptySet()
        } else if (this.size <= maxConfigurableLabels) {
            this.toSet()
        } else {
            val selected = mutableSetOf<E>()
            for (i in 0 until maxConfigurableLabels) {
                val idealPositionRatio = i.toDouble() / (maxConfigurableLabels - 1)
                val indexInDates = (idealPositionRatio * (this.size - 1)).roundToInt()
                selected.add(this[indexInDates.coerceIn(0, this.size - 1)])
            }
            selected
        }
    return representatives
}