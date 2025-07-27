package org.darthacheron.fitbe.utils

fun String.toPositiveDoubleOrNull(): Double? {
    return if (this.startsWith("-")) this.substring(1).toDoubleOrNull() else this.toDoubleOrNull()
}

fun String.toPositiveDouble(): Double {
    return if (this.startsWith("-")) this.substring(1).toDoubleOrNull()
        ?: 0.0 else this.toDoubleOrNull() ?: 0.0
}