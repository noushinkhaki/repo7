package com.codpole.order.exception;

public class ItemAlreadyExistsException extends RuntimeException {

    public ItemAlreadyExistsException(String message) {
        super(message);
    }
}
