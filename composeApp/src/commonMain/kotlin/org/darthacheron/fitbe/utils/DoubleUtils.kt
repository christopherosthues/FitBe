package org.darthacheron.fitbe.utils

fun Double?.toDoubleString(): String {
    return this?.toString() ?: ""
}

fun UInt?.toUintString(): String {
    return this?.toString() ?: ""
}