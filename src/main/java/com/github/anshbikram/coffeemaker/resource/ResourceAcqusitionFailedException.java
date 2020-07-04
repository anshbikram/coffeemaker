package com.github.anshbikram.coffeemaker.resource;

public class ResourceAcqusitionFailedException extends Exception {
    private final String resourceItemName;
    private final String reason;

    public ResourceAcqusitionFailedException(String msg, String resourceItemName, String reason) {
        super(msg);

        this.resourceItemName = resourceItemName;
        this.reason = reason;
    }

    public String getResourceItemName() {
        return resourceItemName;
    }

    public String getReason() {
        return reason;
    }
}
