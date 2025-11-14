package com.example.store_management.common.exception;

/**
 * Thrown when trying to reduce inventory below zero.
 */
public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String message) {
        super(message);
    }
}