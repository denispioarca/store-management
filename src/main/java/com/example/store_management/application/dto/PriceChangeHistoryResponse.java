package com.example.store_management.application.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * DTO representing a single price change event for a product.
 */
public record PriceChangeHistoryResponse(
        Long id,
        BigDecimal oldPrice,
        BigDecimal newPrice,
        String currency,
        Long changedByUserId,
        String reason,
        OffsetDateTime changedAt
) {
}