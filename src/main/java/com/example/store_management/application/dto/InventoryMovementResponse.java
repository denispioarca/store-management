package com.example.store_management.application.dto;

import java.time.OffsetDateTime;

/**
 * DTO representing a single inventory movement event.
 */
public record InventoryMovementResponse(
        Long id,
        Long quantityChange,
        Long resultingQuantity,
        String type,
        Long performedByUserId,
        String reason,
        OffsetDateTime createdAt
) {
}