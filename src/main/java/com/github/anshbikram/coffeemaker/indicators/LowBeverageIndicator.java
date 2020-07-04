package com.github.anshbikram.coffeemaker.indicators;

import com.github.anshbikram.coffeemaker.common.enums.IngredientType;
import com.github.anshbikram.coffeemaker.common.enums.exceptions.GenericException;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class LowBeverageIndicator {

    private static final LowBeverageIndicator INSTANCE = new LowBeverageIndicator();

    private final Map<IngredientType, Boolean> lowIndicator = new ConcurrentHashMap<>();

    public static LowBeverageIndicator getInstance() {
        return INSTANCE;
    }

    private LowBeverageIndicator() {

    }

    public void markLow(IngredientType ingredientType) {
        System.out.println(ingredientType + " is marked as low");
        lowIndicator.put(ingredientType, true);
    }

    public void unmarkLow(IngredientType ingredientType) {
        System.out.println(ingredientType + " is unmarked as low");
        lowIndicator.put(ingredientType, false);
    }

    public boolean isLow(IngredientType ingredientType) {
        Boolean lowStatus = lowIndicator.get(ingredientType);
        if (Objects.isNull(lowStatus)) {
            throw new GenericException("No low indicator present for " + ingredientType);
        }

        return lowStatus;
    }

    public Map<IngredientType, Boolean> getAllIndicators(IngredientType ingredientType) {
        return new EnumMap<>(lowIndicator);
    }
}
