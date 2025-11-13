package com.example.store_management.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Domain model representing a product in the store.
 */
public record Product(
        UUID id,
        String sku,
        String name,
        String description,
        BigDecimal price,
        String currency,
        ProductStatus status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {

    public Product {
        // Basic invariants (can be refined later)
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("SKU must not be blank");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be blank");
        }
        if (price == null || price.signum() <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
        if (currency == null || currency.length() != 3) {
            throw new IllegalArgumentException("Currency must be a 3-letter ISO code");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status must not be null");
        }
    }

    public Product changePrice(BigDecimal newPrice) {
        if (newPrice == null || newPrice.signum() <= 0) {
            throw new IllegalArgumentException("New price must be greater than 0");
        }
        return new Product(
                this.id,
                this.sku,
                this.name,
                this.description,
                newPrice,
                this.currency,
                this.status,
                this.createdAt,
                OffsetDateTime.now()
        );
    }

    public Product changeStatus(ProductStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("New status must not be null");
        }
        return new Product(
                this.id,
                this.sku,
                this.name,
                this.description,
                this.price,
                this.currency,
                newStatus,
                this.createdAt,
                OffsetDateTime.now()
        );
    }
}