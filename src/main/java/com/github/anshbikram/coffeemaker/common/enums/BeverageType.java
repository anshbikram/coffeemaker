package com.github.anshbikram.coffeemaker.common.enums;

/**
 * The no of beverages one coffee machine can cater are pre-defined
 * and have physical buttons and hence will not change making enum the best choice.
 *
 * Enums can be replaced with strings if any use case of dynamic job configuration is expected.
 */
public enum BeverageType {
    green_tea, black_tea, elaichi_tea, hot_tea, hot_coffee, hot_milk, hot_water
}
