package com.springboot.demo.exception;

/**
 * Exception thrown when a requested welcome message template is not found.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
