package org.darthacheron.fitbe.components.validators

class BeverageValidator {
    fun validate(value: Double?): Boolean = value == null || value <= 5000
}