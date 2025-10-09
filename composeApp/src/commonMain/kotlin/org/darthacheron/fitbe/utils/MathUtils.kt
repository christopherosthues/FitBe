package org.darthacheron.fitbe.utils

import kotlin.math.ceil
import kotlin.math.pow
import kotlin.math.round

fun Double.roundToDecimals(decimals: Int): Double {
    val factor = 10.0.pow(decimals)
    return round(this * factor) / factor
}

fun Double.roundUpToNextTen(): Double = (ceil(this / 10).toInt() * 10).toDouble()