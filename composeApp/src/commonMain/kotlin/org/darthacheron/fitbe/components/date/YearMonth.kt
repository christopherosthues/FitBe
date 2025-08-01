package org.darthacheron.fitbe.components.date

data class YearMonth(val year: Int, val month: Int) : Comparable<YearMonth> {
    init {
        require(year in 1..9999) { "Year must be between 1 and 9999" }
        require(month in 1..12) { "Month must be between 1 and 12" }
    }

    fun monthsUntil(other: YearMonth): Int {
        return (other.year - this.year) * 12 + (other.month - this.month)
    }

    override fun compareTo(other: YearMonth): Int {
        return when {
            this.year != other.year -> this.year.compareTo(other.year)
            else -> this.month.compareTo(other.month)
        }
    }

    override fun toString(): String = "$year-$month"
}