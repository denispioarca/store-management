package com.example.store_management.application.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Response payload representing inventory information for a product.
 */
public record InventoryResponse(
        UUID productId,
        long quantity,
        long version,
        OffsetDateTime updatedAt
) {
}