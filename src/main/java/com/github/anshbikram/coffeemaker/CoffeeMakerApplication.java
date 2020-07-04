package com.github.anshbikram.coffeemaker;

import com.github.anshbikram.coffeemaker.common.enums.BeverageType;
import com.github.anshbikram.coffeemaker.common.enums.IngredientType;
import com.github.anshbikram.coffeemaker.common.enums.configurations.MachineConfiguration;
import com.github.anshbikram.coffeemaker.common.enums.configurations.OutletConfig;
import com.github.anshbikram.coffeemaker.machine.CoffeMachine;
import com.github.anshbikram.coffeemaker.machine.CoffeeMachineImpl;
import com.github.anshbikram.coffeemaker.resource.ResourceManagerImpl;
import com.github.anshbikram.coffeemaker.resource.ResourceManager;
import java.util.EnumMap;
import java.util.Map;

public final class CoffeeMakerApplication {

    public static final MachineConfiguration machineCconfig;
    public static final OutletConfig outletConfig;
    public static final Map<IngredientType, Integer> totalItemsQuantity;
    public static final Map<BeverageType, Map<IngredientType, Integer>> beverages;
    public static final Map<IngredientType, Integer> hotCoffeeConfig;

    static {
        outletConfig = new OutletConfig(3);

        totalItemsQuantity = new EnumMap<>(IngredientType.class);
        totalItemsQuantity.put(IngredientType.hot_water, 500);
        totalItemsQuantity.put(IngredientType.hot_milk, 500);
        totalItemsQuantity.put(IngredientType.sugar_syrup, 100);
        totalItemsQuantity.put(IngredientType.ginger_syrup, 50);
        totalItemsQuantity.put(IngredientType.tea_leaves_syrup, 100);
        totalItemsQuantity.put(IngredientType.water, 55);

        beverages = new EnumMap<>(BeverageType.class);
        Map<IngredientType, Integer> hotTeaConfig = new EnumMap<>(IngredientType.class);
        hotTeaConfig.put(IngredientType.hot_water, 200);
        hotTeaConfig.put(IngredientType.hot_milk, 100);
        hotTeaConfig.put(IngredientType.ginger_syrup, 10);
        hotTeaConfig.put(IngredientType.sugar_syrup, 10);
        hotTeaConfig.put(IngredientType.tea_leaves_syrup, 30);
        beverages.put(BeverageType.hot_tea, hotTeaConfig);

        hotCoffeeConfig = new EnumMap<>(IngredientType.class);
        hotCoffeeConfig.put(IngredientType.hot_water, 100);
        hotCoffeeConfig.put(IngredientType.ginger_syrup, 30);
        hotCoffeeConfig.put(IngredientType.hot_milk, 400);
        hotCoffeeConfig.put(IngredientType.sugar_syrup, 50);
        hotCoffeeConfig.put(IngredientType.tea_leaves_syrup, 30);
        beverages.put(BeverageType.hot_coffee, hotCoffeeConfig);

        Map<IngredientType, Integer> greenTeaConfig = new EnumMap<>(IngredientType.class);
        greenTeaConfig.put(IngredientType.hot_water, 100);
        greenTeaConfig.put(IngredientType.ginger_syrup, 30);
        greenTeaConfig.put(IngredientType.sugar_syrup, 50);
        greenTeaConfig.put(IngredientType.green_mixture, 30);
        beverages.put(BeverageType.green_tea, greenTeaConfig);

        Map<IngredientType, Integer> blackTeaConfig = new EnumMap<>(IngredientType.class);
        blackTeaConfig.put(IngredientType.hot_water, 300);
        blackTeaConfig.put(IngredientType.ginger_syrup, 30);
        blackTeaConfig.put(IngredientType.sugar_syrup, 50);
        blackTeaConfig.put(IngredientType.tea_leaves_syrup, 30);
        beverages.put(BeverageType.black_tea, blackTeaConfig);

        Map<IngredientType, Integer> elaichiTeaConfig = new EnumMap<>(IngredientType.class);
        elaichiTeaConfig.put(IngredientType.hot_water, 50);
        elaichiTeaConfig.put(IngredientType.hot_milk, 10);
        elaichiTeaConfig.put(IngredientType.tea_leaves_syrup, 10);
        elaichiTeaConfig.put(IngredientType.elaichi_syrup, 5);
        elaichiTeaConfig.put(IngredientType.sugar_syrup, 10);
        beverages.put(BeverageType.elaichi_tea, elaichiTeaConfig);

        Map<IngredientType, Integer> hotMilkConfig = new EnumMap<>(IngredientType.class);
        hotMilkConfig.put(IngredientType.milk, 50);
        beverages.put(BeverageType.hot_milk, hotMilkConfig);

        Map<IngredientType, Integer> hotWaterConfig = new EnumMap<>(IngredientType.class);
        hotWaterConfig.put(IngredientType.water, 50);
        beverages.put(BeverageType.hot_water, hotWaterConfig);

        machineCconfig = new MachineConfiguration(outletConfig, totalItemsQuantity, beverages);
    }

    private CoffeeMakerApplication() {

    }

    public static void main(String[] args) {
        ResourceManager resourceManager = new ResourceManagerImpl(machineCconfig.getTotalItemsQuantity());
        CoffeMachine coffeMachine = new CoffeeMachineImpl(machineCconfig.getOutlets(), beverages, resourceManager);

        coffeMachine.makeBeverage(BeverageType.hot_coffee);
        coffeMachine.makeBeverage(BeverageType.hot_tea);
        coffeMachine.makeBeverage(BeverageType.black_tea);
        coffeMachine.makeBeverage(BeverageType.hot_milk);

//        resourceManager.getPresentIngredients().forEach((k, v) -> System.out.println(k + ": " + v));
        coffeMachine.makeBeverage(BeverageType.hot_coffee);
        coffeMachine.addIngredients(hotCoffeeConfig);
//        resourceManager.getPresentIngredients().forEach((k, v) -> System.out.println(k + ": " + v));
        coffeMachine.makeBeverage(BeverageType.hot_coffee);

        coffeMachine.makeBeverage(BeverageType.hot_water);
        coffeMachine.makeBeverage(BeverageType.hot_water);
        resourceManager.getPresentIngredients().forEach((k, v) -> System.out.println(k + ": " + v));

        /* For checking concurrent cases */
//        ExecutorService executorService = Executors.newFixedThreadPool(10);
//        List<BeverageType> beverageTypeList = Arrays.asList(
//                BeverageType.hot_coffee,
//                BeverageType.hot_tea,
//                BeverageType.black_tea,
//                BeverageType.hot_water,
//                BeverageType.hot_milk);
//        for (BeverageType beverageType : beverageTypeList) {
//            executorService.submit(() -> {
//                coffeMachine.makeBeverage(beverageType);
//            });
//        }
    }
}
