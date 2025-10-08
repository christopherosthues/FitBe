package org.darthacheron.fitbe.health.componenets

import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.month_april
import fitbe.composeapp.generated.resources.month_august
import fitbe.composeapp.generated.resources.month_december
import fitbe.composeapp.generated.resources.month_february
import fitbe.composeapp.generated.resources.month_january
import fitbe.composeapp.generated.resources.month_july
import fitbe.composeapp.generated.resources.month_june
import fitbe.composeapp.generated.resources.month_march
import fitbe.composeapp.generated.resources.month_may
import fitbe.composeapp.generated.resources.month_november
import fitbe.composeapp.generated.resources.month_october
import fitbe.composeapp.generated.resources.month_september
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import org.jetbrains.compose.resources.StringResource

fun LocalDate.monthResourceString(): StringResource {
    return when (this.month) {
        Month.JANUARY -> Res.string.month_january
        Month.FEBRUARY -> Res.string.month_february
        Month.MARCH -> Res.string.month_march
        Month.APRIL -> Res.string.month_april
        Month.MAY -> Res.string.month_may
        Month.JUNE -> Res.string.month_june
        Month.JULY -> Res.string.month_july
        Month.AUGUST -> Res.string.month_august
        Month.SEPTEMBER -> Res.string.month_september
        Month.OCTOBER -> Res.string.month_october
        Month.NOVEMBER -> Res.string.month_november
        Month.DECEMBER -> Res.string.month_december
        else -> Res.string.month_january
    }
}