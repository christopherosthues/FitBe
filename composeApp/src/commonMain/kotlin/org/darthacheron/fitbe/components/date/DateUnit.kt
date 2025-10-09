package org.darthacheron.fitbe.components.date

import androidx.compose.runtime.Composable
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.date_unit_day
import fitbe.composeapp.generated.resources.date_unit_month
import fitbe.composeapp.generated.resources.date_unit_week
import fitbe.composeapp.generated.resources.date_unit_year
import org.jetbrains.compose.resources.stringResource

enum class DateUnit {
    DAY,
    WEEK,
    MONTH,
    YEAR;

    @Composable
    fun localizedString(): String =
        when (this) {
            DAY -> stringResource(Res.string.date_unit_day)
            WEEK -> stringResource(Res.string.date_unit_week)
            MONTH -> stringResource(Res.string.date_unit_month)
            YEAR -> stringResource(Res.string.date_unit_year)
        }
}