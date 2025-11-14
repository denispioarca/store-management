package com.example.store_management.application.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Response payload representing a product.
 */
public record ProductResponse(
        UUID id,
        String sku,
        String name,
        String description,
        BigDecimal price,
        String currency,
        String status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}