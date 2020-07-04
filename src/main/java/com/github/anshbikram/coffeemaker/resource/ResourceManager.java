package com.github.anshbikram.coffeemaker.resource;

import com.github.anshbikram.coffeemaker.common.enums.IngredientType;
import java.util.Map;

public interface ResourceManager {

    /**
     *
     * @param IngredientType
     * @return
     * @throws ResourceAcqusitionFailedException
     */
    Resource acquireResource(Map<IngredientType, Integer> IngredientType) throws ResourceAcqusitionFailedException;

    /**
     *
     * @param ingredients
     */
    void addIngredients(Map<IngredientType, Integer> ingredients);

    Map<IngredientType, Integer> getPresentIngredients();
}
