package com.github.anshbikram.coffeemaker.jobs;

import com.github.anshbikram.coffeemaker.common.enums.BeverageType;
import com.github.anshbikram.coffeemaker.resource.Resource;
import java.util.concurrent.Callable;

public class BeverageMakerJob implements Callable<Boolean> {
    private final BeverageType beverageType;
    private final Resource resource;

    public BeverageMakerJob(BeverageType beverageType, Resource resource) {
        this.beverageType = beverageType;
        this.resource = resource;
    }

    @Override
    public Boolean call() {
        try {
            resource.claim();
            /* Instruct hardware to prepare the beverage */
            System.out.println(beverageType + " is getting prepared");
            /* Beverage is getting prepared */
            Thread.sleep(1000);

            System.out.println(beverageType + " is prepared");

            return true;
        } catch (Throwable e) {
            System.out.println("Error while making " + beverageType + ' ' + e.getMessage());
            return false;
        } finally {
            resource.releaseIfNotClaimed();
        }
    }
}
