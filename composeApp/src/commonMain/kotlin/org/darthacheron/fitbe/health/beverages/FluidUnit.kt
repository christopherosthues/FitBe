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

enum class FluidUnit(private val conversionFactorToMilliliter: UInt) {
    Cup(236u),
    SmallGlass(150u),
    NormalGlass(250u),
    LargeGlass(300u),
    Milliliter(1u),
    Centiliter(10u),
    Deciliter(100u),
    Liter(1000u);

    fun toMilliliter(amount: UInt): UInt {
        return conversionFactorToMilliliter * amount
    }

    @Composable
    fun localizedString(amount: UInt): String {
        return when (this) {
            Cup -> pluralStringResource(Res.plurals.fluid_unit_cup, quantity = amount.toInt(), amount)
            SmallGlass -> pluralStringResource(Res.plurals.fluid_unit_small_glass, quantity = amount.toInt(), amount)
            NormalGlass -> pluralStringResource(Res.plurals.fluid_unit_normal_glass, quantity = amount.toInt(), amount)
            LargeGlass -> pluralStringResource(Res.plurals.fluid_unit_large_glass, quantity = amount.toInt(), amount)
            Milliliter -> stringResource(Res.string.fluid_unit_milliliter, amount)
            Centiliter -> stringResource(Res.string.fluid_unit_centiliter, amount)
            Deciliter -> stringResource(Res.string.fluid_unit_deciliter, amount)
            Liter -> stringResource(Res.string.fluid_unit_liter, amount)
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