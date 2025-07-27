package org.darthacheron.fitbe.utils

import kotlinx.datetime.LocalTime

fun Double?.toDoubleString(): String {
    return this?.toString() ?: ""
}

fun UInt?.toUintString(): String {
    return this?.toString() ?: ""
}