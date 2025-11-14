package com.example.store_management.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * Request payload for adjusting the inventory quantity of a product.
 */
public record InventoryAdjustmentRequest(

        @NotNull
        UUID productId,

        /**
         * Positive for INCREASE, negative for DECREASE,
         * zero for a neutral adjustment.
         */
        @NotNull
        Long quantityChange,

        /**
         * Optional id of the user who performed the action.
         * Typically resolved from security context.
         */
        Long performedByUserId,

        @Size(max = 2000)
        String reason
) {
}