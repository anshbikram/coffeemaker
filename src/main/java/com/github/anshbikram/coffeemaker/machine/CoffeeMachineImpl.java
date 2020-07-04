package com.github.anshbikram.coffeemaker.machine;

import com.github.anshbikram.coffeemaker.common.enums.IngredientType;
import com.github.anshbikram.coffeemaker.common.enums.configurations.OutletConfig;
import com.github.anshbikram.coffeemaker.common.enums.exceptions.GenericException;
import com.github.anshbikram.coffeemaker.common.enums.BeverageType;
import com.github.anshbikram.coffeemaker.jobs.BeverageMakerJob;
import com.github.anshbikram.coffeemaker.resource.Resource;
import com.github.anshbikram.coffeemaker.resource.ResourceAcqusitionFailedException;
import com.github.anshbikram.coffeemaker.resource.ResourceManager;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CoffeeMachineImpl implements CoffeMachine {

    private final ExecutorService executorService;
    private final ResourceManager resourceManager;
    private final Map<BeverageType, Map<IngredientType, Integer>> beverages;

    public CoffeeMachineImpl(OutletConfig outlets, Map<BeverageType, Map<IngredientType, Integer>> beverages,
            ResourceManager resourceManager) {
        this.beverages = Collections.unmodifiableMap(beverages);
        this.resourceManager = resourceManager;

        this.executorService = Executors.newFixedThreadPool(outlets.getCountN());
    }

    public boolean makeBeverage(BeverageType beverageType) {
        Map<IngredientType, Integer> requiredIngredients = beverages.get(beverageType);
        if (Objects.isNull(requiredIngredients)) {
            System.out.println("No ingredient config found for " + beverageType);
            return false;
        }

        Resource resource;
        try {
            resource = resourceManager.acquireResource(requiredIngredients);
        } catch (ResourceAcqusitionFailedException e) {
            System.out.println(beverageType + " cannot be prepared because `" + e.getResourceItemName() + "` is " + e.getReason());
            return false;
        }

        /* For running with multi-threading */
        try {
            return executorService.submit(new BeverageMakerJob(beverageType, resource)).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new GenericException("Error while making " + beverageType, e);
        }

        /* For running without multi-threading */
//        return new BeverageMakerJob(beverageType, resource).call();
    }

    @Override
    public void addIngredients(Map<IngredientType, Integer> ingredients) {
        resourceManager.addIngredients(ingredients);
    }
}
