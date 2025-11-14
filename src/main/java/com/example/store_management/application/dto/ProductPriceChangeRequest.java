package com.example.store_management.application.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Request payload for changing the price of a product.
 */
public record ProductPriceChangeRequest(

        @NotNull
        UUID productId,

        @NotNull
        @Positive
        @Digits(integer = 17, fraction = 2)
        BigDecimal newPrice,

        /**
         * Optional id of the user who performed the change.
         * This will be filled from the authenticated user in a real scenario.
         */
        Long changedByUserId,

        @Size(max = 2000)
        String reason
) {
}