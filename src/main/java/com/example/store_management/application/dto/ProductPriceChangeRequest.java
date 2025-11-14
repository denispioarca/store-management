package com.example.store_management.application.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Request payload for changing the price of a product.
 * Product id is provided via path variable, not in the body.
 */
public record ProductPriceChangeRequest(

        @NotNull
        @Positive
        @Digits(integer = 17, fraction = 2)
        BigDecimal newPrice,

        /**
         * Optional id of the user who performed the change.
         * Typically resolved from security context.
         */
        Long changedByUserId,

        @Size(max = 2000)
        String reason
) {
}