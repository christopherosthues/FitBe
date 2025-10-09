package org.darthacheron.fitbe.components.validators

class PercentageValidator {
    fun validate(percentage: Double?): Boolean = percentage == null || (percentage >= 0.0 && percentage <= 100.0)
}