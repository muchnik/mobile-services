package ru.muchnik.yota.mobileservices.model.exception;

/**
 * Common validation exception, when any validation exception occurs
 * Details passed via #message
 */
public class ValidationException extends RuntimeException {
    public ValidationException(final String message) {
        super(message);
    }
}
