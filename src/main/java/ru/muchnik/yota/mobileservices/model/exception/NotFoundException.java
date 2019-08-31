package ru.muchnik.yota.mobileservices.model.exception;

/**
 * Common exception when entity was not found in database
 * Details passed via #message
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(final String message) {
        super(message);
    }
}
