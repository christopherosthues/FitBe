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

enum class FluidUnit(private val conversionFactorToMilliliter: Double) {
    Cup(236.0),
    SmallGlass(150.0),
    NormalGlass(250.0),
    LargeGlass(300.0),
    Milliliter(1.0),
    Centiliter(10.0),
    Deciliter(100.0),
    Liter(1000.0);

    fun toMilliliter(amount: Double): Double {
        return conversionFactorToMilliliter * amount
    }

    @Composable
    fun localizedString(amount: Double): String {
        return when (this) {
            Cup -> pluralStringResource(Res.plurals.fluid_unit_cup, quantity = amount.toInt())
            SmallGlass -> pluralStringResource(Res.plurals.fluid_unit_small_glass, quantity = amount.toInt())
            NormalGlass -> pluralStringResource(Res.plurals.fluid_unit_normal_glass, quantity = amount.toInt())
            LargeGlass -> pluralStringResource(Res.plurals.fluid_unit_large_glass, quantity = amount.toInt())
            Milliliter -> stringResource(Res.string.fluid_unit_milliliter)
            Centiliter -> stringResource(Res.string.fluid_unit_centiliter)
            Deciliter -> stringResource(Res.string.fluid_unit_deciliter)
            Liter -> stringResource(Res.string.fluid_unit_liter)
        }
    }

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