package com.example.store_management.common.exception;

/**
 * Thrown when a product cannot be found in the database.
 */
public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String message) {
        super(message);
    }
}