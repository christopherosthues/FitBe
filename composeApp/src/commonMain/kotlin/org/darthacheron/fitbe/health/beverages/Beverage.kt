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
import kotlinx.datetime.Instant
import org.darthacheron.fitbe.health.beverages.FluidUnit.Centiliter
import org.darthacheron.fitbe.health.beverages.FluidUnit.Cup
import org.darthacheron.fitbe.health.beverages.FluidUnit.Deciliter
import org.darthacheron.fitbe.health.beverages.FluidUnit.LargeGlass
import org.darthacheron.fitbe.health.beverages.FluidUnit.Liter
import org.darthacheron.fitbe.health.beverages.FluidUnit.Milliliter
import org.darthacheron.fitbe.health.beverages.FluidUnit.NormalGlass
import org.darthacheron.fitbe.health.beverages.FluidUnit.SmallGlass
import org.darthacheron.fitbe.utils.roundToDecimals
import org.jetbrains.compose.resources.PluralStringResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
data class Beverage(
    val id: Uuid = Uuid.random(),
    val profileId: Uuid,
    val date: Instant,
    val amount: Double,
    val beverage: String,
    val unit: FluidUnit
) {
    @Composable
    fun localizedString(): String {
        val resource = when (unit) {
            Cup -> Res.plurals.beverage_in_cup
            SmallGlass -> Res.plurals.beverage_in_small_glass
            NormalGlass -> Res.plurals.beverage_in_normal_glass
            LargeGlass -> Res.plurals.beverage_in_large_glass
            Milliliter -> Res.string.beverage_in_milliliter
            Centiliter -> Res.string.beverage_in_centiliter
            Deciliter -> Res.string.beverage_in_deciliter
            Liter -> Res.string.beverage_in_liter
        }

        if (resource is PluralStringResource) {
            return pluralStringResource(resource, quantity = amount.toInt(), amount.roundToDecimals(2), beverage)
        } else if (resource is StringResource) {
            return stringResource(resource, amount.roundToDecimals(2), beverage)
        } else {
            return ""
        }
    }
}