package org.darthacheron.fitbe.nutrition.beverages

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
}