package org.darthacheron.fitbe.nutrition.water

enum class FluidUnit(val conversionFactorToMilliliter: Double) {
    Cup(236.6),
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
}