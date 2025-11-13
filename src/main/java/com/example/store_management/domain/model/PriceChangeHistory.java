package com.example.store_management.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Domain model representing a historical record of a product price change.
 */
public record PriceChangeHistory(
        Long id,
        UUID productId,
        BigDecimal oldPrice,
        BigDecimal newPrice,
        String currency,
        Long changedByUserId,
        String reason,
        OffsetDateTime changedAt
) {

    public PriceChangeHistory {
        if (productId == null) {
            throw new IllegalArgumentException("Product id must not be null");
        }
        if (oldPrice == null || oldPrice.signum() < 0) {
            throw new IllegalArgumentException("Old price must not be negative");
        }
        if (newPrice == null || newPrice.signum() <= 0) {
            throw new IllegalArgumentException("New price must be greater than 0");
        }
        if (currency == null || currency.length() != 3) {
            throw new IllegalArgumentException("Currency must be a 3-letter ISO code");
        }
        if (changedByUserId == null || changedByUserId <= 0) {
            throw new IllegalArgumentException("ChangedBy user id must be positive");
        }
    }
}