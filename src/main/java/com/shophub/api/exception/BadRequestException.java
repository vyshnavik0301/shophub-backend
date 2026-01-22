package com.shophub.api.exception;

/**
 * Thrown for invalid business logic or bad request data.
 * Mapped to HTTP 400 by GlobalExceptionHandler.
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
