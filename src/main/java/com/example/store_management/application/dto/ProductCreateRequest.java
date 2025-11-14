package com.example.store_management.application.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * Request payload for creating a new product.
 */
public record ProductCreateRequest(

        @NotBlank
        @Size(max = 100)
        String sku,

        @NotBlank
        @Size(max = 255)
        String name,

        @Size(max = 2000)
        String description,

        @NotNull
        @Positive
        @Digits(integer = 17, fraction = 2)
        BigDecimal price,

        @NotBlank
        @Size(min = 3, max = 3)
        String currency
) {
}