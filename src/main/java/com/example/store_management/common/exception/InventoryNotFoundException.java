package com.example.store_management.common.exception;

/**
 * Thrown when inventory is missing for a given product.
 */
public class InventoryNotFoundException extends RuntimeException {

    public InventoryNotFoundException(String message) {
        super(message);
    }
}