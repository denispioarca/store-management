package com.example.store_management.domain.model;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Domain model representing the inventory state for a single product.
 */
public record Inventory(
        UUID productId,
        long quantity,
        long version,
        OffsetDateTime updatedAt
) {

    public Inventory {
        if (productId == null) {
            throw new IllegalArgumentException("Product id must not be null");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity must not be negative");
        }
        if (version < 0) {
            throw new IllegalArgumentException("Version must not be negative");
        }
    }

    public Inventory withNewQuantity(long newQuantity) {
        if (newQuantity < 0) {
            throw new IllegalArgumentException("New quantity must not be negative");
        }
        return new Inventory(
                this.productId,
                newQuantity,
                this.version + 1,
                OffsetDateTime.now()
        );
    }
}