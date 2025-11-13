package com.example.store_management.domain.model;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Domain model representing a single inventory movement event.
 */
public record InventoryMovement(
        Long id,
        UUID productId,
        Long userId,
        long quantityChange,
        long resultingQuantity,
        InventoryMovementType type,
        String reason,
        OffsetDateTime createdAt
) {

    public InventoryMovement {
        if (productId == null) {
            throw new IllegalArgumentException("Product id must not be null");
        }
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User id must be positive");
        }
        if (type == null) {
            throw new IllegalArgumentException("Movement type must not be null");
        }
        if (resultingQuantity < 0) {
            throw new IllegalArgumentException("Resulting quantity must not be negative");
        }
    }
}