package org.darthacheron.fitbe.nutrition.beverages

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
import org.darthacheron.fitbe.nutrition.beverages.FluidUnit.Centiliter
import org.darthacheron.fitbe.nutrition.beverages.FluidUnit.Cup
import org.darthacheron.fitbe.nutrition.beverages.FluidUnit.Deciliter
import org.darthacheron.fitbe.nutrition.beverages.FluidUnit.LargeGlass
import org.darthacheron.fitbe.nutrition.beverages.FluidUnit.Liter
import org.darthacheron.fitbe.nutrition.beverages.FluidUnit.Milliliter
import org.darthacheron.fitbe.nutrition.beverages.FluidUnit.NormalGlass
import org.darthacheron.fitbe.nutrition.beverages.FluidUnit.SmallGlass
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Beverage(
    val id: Uuid,
    val dateUtc: String,
    val dateLocal: String,
    val amount: Int,
    val beverage: String,
    val unit: FluidUnit
) {
    @Composable
    fun localizedString(): String {
        return when (unit) {
            Cup -> pluralStringResource(Res.plurals.beverage_in_cup, quantity = amount, amount, beverage)
            SmallGlass -> pluralStringResource(Res.plurals.beverage_in_small_glass, quantity = amount, amount, beverage)
            NormalGlass -> pluralStringResource(Res.plurals.beverage_in_normal_glass, quantity = amount, amount, beverage)
            LargeGlass -> pluralStringResource(Res.plurals.beverage_in_large_glass, quantity = amount, amount, beverage)
            Milliliter -> stringResource(Res.string.beverage_in_milliliter, amount, beverage)
            Centiliter -> stringResource(Res.string.beverage_in_centiliter, amount, beverage)
            Deciliter -> stringResource(Res.string.beverage_in_deciliter, amount, beverage)
            Liter -> stringResource(Res.string.beverage_in_liter, amount, beverage)
        }
    }
}
