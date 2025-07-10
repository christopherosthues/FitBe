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
import fitbe.composeapp.generated.resources.fluid_unit_centiliter
import fitbe.composeapp.generated.resources.fluid_unit_cup
import fitbe.composeapp.generated.resources.fluid_unit_deciliter
import fitbe.composeapp.generated.resources.fluid_unit_large_glass
import fitbe.composeapp.generated.resources.fluid_unit_liter
import fitbe.composeapp.generated.resources.fluid_unit_milliliter
import fitbe.composeapp.generated.resources.fluid_unit_normal_glass
import fitbe.composeapp.generated.resources.fluid_unit_small_glass
import fitbe.composeapp.generated.resources.ic_centiliter
import fitbe.composeapp.generated.resources.ic_cup
import fitbe.composeapp.generated.resources.ic_deciliter
import fitbe.composeapp.generated.resources.ic_glass_cup
import fitbe.composeapp.generated.resources.ic_liter
import fitbe.composeapp.generated.resources.ic_milliliter
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

enum class FluidUnit(private val conversionFactorToMilliliter: Int) {
    Cup(236),
    SmallGlass(150),
    NormalGlass(250),
    LargeGlass(300),
    Milliliter(1),
    Centiliter(10),
    Deciliter(100),
    Liter(1000);

    fun toMilliliter(amount: Int): Int {
        return conversionFactorToMilliliter * amount
    }

    @Composable
    fun localizedString(amount: Int): String {
        return when (this) {
            Cup -> pluralStringResource(Res.plurals.fluid_unit_cup, quantity = amount, amount)
            SmallGlass -> pluralStringResource(Res.plurals.fluid_unit_small_glass, quantity = amount, amount)
            NormalGlass -> pluralStringResource(Res.plurals.fluid_unit_normal_glass, quantity = amount, amount)
            LargeGlass -> pluralStringResource(Res.plurals.fluid_unit_large_glass, quantity = amount, amount)
            Milliliter -> stringResource(Res.string.fluid_unit_milliliter, amount)
            Centiliter -> stringResource(Res.string.fluid_unit_centiliter, amount)
            Deciliter -> stringResource(Res.string.fluid_unit_deciliter, amount)
            Liter -> stringResource(Res.string.fluid_unit_liter, amount)
        }
    }

    @Composable
    fun iconResource(): DrawableResource {
        return when (this) {
            Cup -> Res.drawable.ic_cup
            SmallGlass -> Res.drawable.ic_glass_cup
            NormalGlass -> Res.drawable.ic_glass_cup
            LargeGlass -> Res.drawable.ic_glass_cup
            Milliliter -> Res.drawable.ic_milliliter
            Centiliter -> Res.drawable.ic_centiliter
            Deciliter -> Res.drawable.ic_deciliter
            Liter -> Res.drawable.ic_liter
        }
    }
}