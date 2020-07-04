package com.github.anshbikram.coffeemaker.resource;

import com.github.anshbikram.coffeemaker.common.enums.IngredientType;
import com.github.anshbikram.coffeemaker.common.enums.exceptions.GenericException;
import com.github.anshbikram.coffeemaker.indicators.LowBeverageIndicator;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ResourceManagerImpl implements ResourceManager {
    private static final int LOW_MARK = 5;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final Object resourceLock = new Object();
    private final AtomicInteger resourceIdCounter = new AtomicInteger();
    private final Map<IngredientType, Integer> totalIngredients = new ConcurrentHashMap<>();
    private final Map<Integer, AcquiredResource> acquiredResourceMap = new ConcurrentHashMap<>();

    public ResourceManagerImpl(Map<IngredientType, Integer> totalIngredients) {
        this.totalIngredients.putAll(totalIngredients);

        totalIngredients.forEach((k, v) -> {
            if (v <= LOW_MARK) {
                LowBeverageIndicator.getInstance().markLow(k);
            } else {
                LowBeverageIndicator.getInstance().unmarkLow(k);
            }
        });

        startCleanupJob();
    }

    @Override
    public Resource acquireResource(Map<IngredientType, Integer> ingredients) throws ResourceAcqusitionFailedException {
        synchronized(resourceLock) {

            for (Entry<IngredientType, Integer> ingr : ingredients.entrySet()) {
                if (!totalIngredients.containsKey(ingr.getKey()) || totalIngredients.get(ingr.getKey()) <= 0) {
                    throw new ResourceAcqusitionFailedException("Couldn't acquire resource", ingr.getKey().name(), "not available");
                }

                if (totalIngredients.get(ingr.getKey()) <= ingr.getValue()) {
                    throw new ResourceAcqusitionFailedException("Couldn't acquire resource", ingr.getKey().name(), "not sufficient");
                }

            }

            ingredients.forEach((k, v) -> totalIngredients.put(k, totalIngredients.get(k) - v));
        }

        int resourceId = resourceIdCounter.getAndAdd(1);
        AcquiredResource resource = new AcquiredResource(resourceId, ingredients, this, getExpiry());
        acquiredResourceMap.put(resourceId, resource);

        return resource;
    }

    @Override
    public void addIngredients(Map<IngredientType, Integer> ingredients) {
        synchronized(resourceLock) {
            ingredients.forEach((k, v) -> {
                totalIngredients.put(k, totalIngredients.getOrDefault(k, 0) + v);
                if (totalIngredients.get(k) > LOW_MARK) {
                    LowBeverageIndicator.getInstance().unmarkLow(k);
                }
            });
        }
    }

    @Override
    public Map<IngredientType, Integer> getPresentIngredients() {
        return new HashMap<>(this.totalIngredients);
    }

    private static class AcquiredResource implements Resource {
        private final AtomicBoolean claimState = new AtomicBoolean();
        private final int resourceId;
        private final Map<IngredientType, Integer> ingredients;
        private final ResourceManagerImpl resourceManager;
        private final long expiredAt;

        private AcquiredResource(int resourceId, Map<IngredientType, Integer> ingredients,
                ResourceManagerImpl resourceManager, long expiredAt) {
            this.resourceId = resourceId;
            this.ingredients = Collections.unmodifiableMap(ingredients);
            this.resourceManager = resourceManager;
            this.expiredAt = expiredAt;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expiredAt;
        }

        public Map<IngredientType, Integer> getIngredients() {
            return ingredients;
        }

        @Override
        public void claim() {
            if (!claimState.compareAndSet(false, true)) {
                throw new GenericException("Resource is already claimed.");
            }

            /* If resource is expired, don't let it be claimed. */
//            if (isExpired()) {
//                throw new GenericException("Resource is already expired");
//            }

            AcquiredResource resource = this.resourceManager.acquiredResourceMap.remove(resourceId);
            if (Objects.nonNull(resource)) {
                resource.getIngredients().forEach((k, v) -> {
                    if (this.resourceManager.totalIngredients.get(k) <= LOW_MARK) {
                        LowBeverageIndicator.getInstance().markLow(k);
                    }
                });
            }

            /* Hardware signal to release the ingredients. */
        }

        @Override
        public void releaseIfNotClaimed() {
            boolean canClaim = claimState.compareAndSet(false, true);

            if (canClaim) {
                AcquiredResource resource = this.resourceManager.acquiredResourceMap.remove(resourceId);

                if (Objects.nonNull(resource)) {
                    System.out.println("Releasing resources");
                    resourceManager.addIngredients(resource.getIngredients());
                }
            }
        }
    }

    private long getExpiry() {
        /* n seconds expiry. Keeping higher expiry for ease of debugging and testing. */
        return System.currentTimeMillis() + 30 * 1000;
    }

    private void startCleanupJob() {
        /* Clean up un-claimed resources after they are timed out */
        scheduler.schedule(() -> {
            try {
                acquiredResourceMap.forEach((k, v) -> {
                    if (v.isExpired()) {
                        synchronized(resourceLock) {
                            if (v.isExpired()) {
                                v.releaseIfNotClaimed();
                            }
                        }
                    }
                });
            } catch (Throwable e) {
                System.out.println("Exception in clean up job: " + e.getMessage());
            }
        }, 1, TimeUnit.SECONDS);
    }
}
