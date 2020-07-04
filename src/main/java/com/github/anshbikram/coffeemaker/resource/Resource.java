package com.github.anshbikram.coffeemaker.resource;

public interface Resource {

    void claim();

    void releaseIfNotClaimed();
}
