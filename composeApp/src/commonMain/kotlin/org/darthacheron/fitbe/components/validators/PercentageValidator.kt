package org.darthacheron.fitbe.components.validators

class PercentageValidator {
    fun validate(percentage: Double?): Boolean {
        return percentage == null || (percentage >= 0.0 && percentage <= 100.0)
    }
}