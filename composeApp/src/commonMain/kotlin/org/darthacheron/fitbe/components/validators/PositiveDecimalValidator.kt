package org.darthacheron.fitbe.components.validators

class PositiveDecimalValidator {
    fun validate(value: String): Boolean {
        val regex = Regex("^$|^(0|[1-9]\\d*)([.,]\\d+)?$")
        return regex.matches(value)
    }
}