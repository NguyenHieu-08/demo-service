package com.springboot.demo.exception;

/**
 * Exception thrown when the unique combination of Language - Country - Currency is violated.
 */
public class DuplicateTemplateException extends RuntimeException {
    public DuplicateTemplateException(String message) {
        super(message);
    }
}
