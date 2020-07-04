package com.github.anshbikram.coffeemaker.common.enums.configurations;

import com.github.anshbikram.coffeemaker.common.enums.IngredientType;
import com.github.anshbikram.coffeemaker.common.enums.BeverageType;
import java.util.Collections;
import java.util.Map;

public class MachineConfiguration {

    private final OutletConfig outlets;
    private final Map<IngredientType, Integer> totalItemsQuantity;
    private final Map<BeverageType, Map<IngredientType, Integer>> beverages;

    public MachineConfiguration(OutletConfig outlets, Map<IngredientType, Integer> totalItemsQuantity,
            Map<BeverageType, Map<IngredientType, Integer>> beverages) {
        this.outlets = outlets;
        this.totalItemsQuantity = Collections.unmodifiableMap(totalItemsQuantity);
        this.beverages = Collections.unmodifiableMap(beverages);
    }

    public OutletConfig getOutlets() {
        return outlets;
    }

    public Map<IngredientType, Integer> getTotalItemsQuantity() {
        return totalItemsQuantity;
    }

    public Map<BeverageType, Map<IngredientType, Integer>> getBeverages() {
        return beverages;
    }

    @Override
    public String toString() {
        return "MachineConfiguration{" + "outlets=" + outlets + ", totalItemsQuantity=" + totalItemsQuantity
                + ", beverages=" + beverages + '}';
    }
}
