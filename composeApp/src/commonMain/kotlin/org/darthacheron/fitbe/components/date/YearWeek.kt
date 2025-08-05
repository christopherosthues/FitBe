package org.darthacheron.fitbe.components.date

//data class YearWeek(val year: Int, val week: Int) : Comparable<YearWeek> {
//    init {
//        require(year in 1..9999) { "Year must be between 1 and 9999" }
//        require(week in 1..53) { "Week must be between 1 and 53" }
//    }
//
//    fun weeksUntil(other: YearWeek): Int {
//        return (other.year - this.year) * 52 + (other.week - this.week)
//    }
//
//    override fun compareTo(other: YearWeek): Int {
//        return when {
//            this.year != other.year -> this.year.compareTo(other.year)
//            else -> this.week.compareTo(other.week)
//        }
//    }
//
//    override fun toString(): String = "$year-W$week"
//}