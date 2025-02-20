package com.example.exception;

public class IllegalUserAccessException extends RuntimeException {
    public IllegalUserAccessException(String message) {
        super(message);
    }
}
