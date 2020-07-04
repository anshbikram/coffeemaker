package com.github.anshbikram.coffeemaker.machine;

import com.github.anshbikram.coffeemaker.common.enums.IngredientType;
import com.github.anshbikram.coffeemaker.common.enums.BeverageType;
import java.util.Map;

public interface CoffeMachine {

    /**
     * Make the beverage
     *
     * @param beverageType
     * @return
     */
    boolean makeBeverage(BeverageType beverageType);

    /**
     * Add ingredients.
     *
     * @param ingredients
     */
    void addIngredients(Map<IngredientType, Integer> ingredients);
}
