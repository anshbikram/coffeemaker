package com.github.anshbikram.coffeemaker.common.enums.exceptions;

public class GenericException extends RuntimeException {

    public GenericException(String msg) {
        super(msg);
    }

    public GenericException(String msg, Exception e) {
        super(msg, e);
    }
}
