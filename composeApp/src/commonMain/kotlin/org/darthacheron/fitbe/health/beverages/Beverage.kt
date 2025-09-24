package org.darthacheron.fitbe.health.beverages

import androidx.compose.runtime.Composable
import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.beverage_in_centiliter
import fitbe.composeapp.generated.resources.beverage_in_cup
import fitbe.composeapp.generated.resources.beverage_in_deciliter
import fitbe.composeapp.generated.resources.beverage_in_large_glass
import fitbe.composeapp.generated.resources.beverage_in_liter
import fitbe.composeapp.generated.resources.beverage_in_milliliter
import fitbe.composeapp.generated.resources.beverage_in_normal_glass
import fitbe.composeapp.generated.resources.beverage_in_small_glass
import kotlinx.datetime.Clock
import org.darthacheron.fitbe.health.beverages.FluidUnit.Centiliter
import org.darthacheron.fitbe.health.beverages.FluidUnit.Cup
import org.darthacheron.fitbe.health.beverages.FluidUnit.Deciliter
import org.darthacheron.fitbe.health.beverages.FluidUnit.LargeGlass
import org.darthacheron.fitbe.health.beverages.FluidUnit.Liter
import org.darthacheron.fitbe.health.beverages.FluidUnit.Milliliter
import org.darthacheron.fitbe.health.beverages.FluidUnit.NormalGlass
import org.darthacheron.fitbe.health.beverages.FluidUnit.SmallGlass
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import kotlin.time.ExperimentalTime
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
data class Beverage(
    val id: Uuid = Uuid.random(),
    val profileId: Uuid,
    val dateUtc: Instant =Clock.System.now().toLocalDateTime(TimeZone.UTC).date.atStartOfDayIn(TimeZone.UTC),
    val amount: UInt,
    val beverage: String,
    val unit: FluidUnit
) {
    @Composable
    fun localizedString(): String {
        return when (unit) {
            Cup -> pluralStringResource(Res.plurals.beverage_in_cup, quantity = amount.toInt(), amount, beverage)
            SmallGlass -> pluralStringResource(Res.plurals.beverage_in_small_glass, quantity = amount.toInt(), amount, beverage)
            NormalGlass -> pluralStringResource(Res.plurals.beverage_in_normal_glass, quantity = amount.toInt(), amount, beverage)
            LargeGlass -> pluralStringResource(Res.plurals.beverage_in_large_glass, quantity = amount.toInt(), amount, beverage)
            Milliliter -> stringResource(Res.string.beverage_in_milliliter, amount, beverage)
            Centiliter -> stringResource(Res.string.beverage_in_centiliter, amount, beverage)
            Deciliter -> stringResource(Res.string.beverage_in_deciliter, amount, beverage)
            Liter -> stringResource(Res.string.beverage_in_liter, amount, beverage)
        }
    }
}
