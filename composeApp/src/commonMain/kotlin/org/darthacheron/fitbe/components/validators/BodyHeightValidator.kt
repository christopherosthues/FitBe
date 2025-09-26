package org.darthacheron.fitbe.components.validators

class BodyHeightValidator {
    fun validate(value: Double?): Boolean {
        return value == null || value >= 0.0 && value <= 300.0
    }
}