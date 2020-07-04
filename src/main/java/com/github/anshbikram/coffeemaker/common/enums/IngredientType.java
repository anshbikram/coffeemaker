package com.github.anshbikram.coffeemaker.common.enums;

/**
 * The ingredients for each beverages one coffee machine can cater are predefined.
 * Ingredients can no be dynamic as it will require additional container to keep them.
 *
 * Enums can be replaced with strings if any use case of dynamic ingrdients type support is expected.
 *
 * Assuming there is no recursive dependency of ingredients on other ingredient as it was
 * confusing from the PDF (showed recursive relation: `hot water` requires `water`) and
 * sample JSON (didn't had water and hot water relation).
 */
public enum IngredientType {
    hot_water, hot_milk, ginger_syrup, sugar_syrup, elaichi_syrup, tea_leaves_syrup, green_mixture, water, milk
}
