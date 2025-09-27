package org.darthacheron.fitbe.components.validators

class BeverageValidator {
    fun validate(value: Double?): Boolean {
        return value == null || value <= 5000
    }
}