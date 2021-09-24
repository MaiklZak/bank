package com.app.bank.error;

public class NoSuchEntityException extends Exception {
    public NoSuchEntityException(String message) {
        super(message);
    }
}
