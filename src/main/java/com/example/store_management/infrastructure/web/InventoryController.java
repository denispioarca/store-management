package com.example.store_management.infrastructure.web;

import com.example.store_management.application.dto.InventoryAdjustmentRequest;
import com.example.store_management.application.dto.InventoryResponse;
import com.example.store_management.application.service.InventoryAppService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST controller exposing inventory-related endpoints.
 */
@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryAppService inventoryAppService;

    /**
     * Returns inventory information for a given product.
     * <p>
     * GET /api/inventory/{productId}
     */
    @GetMapping("/{productId}")
    public ResponseEntity<InventoryResponse> getInventoryForProduct(@PathVariable UUID productId) {
        InventoryResponse response = inventoryAppService.getInventoryForProduct(productId);
        return ResponseEntity.ok(response);
    }

    /**
     * Adjusts inventory quantity for a product.
     * <p>
     * PATCH /api/inventory/{productId}/adjust
     * <p>
     * The path variable is the source of truth for productId.
     */
    @PatchMapping("/{productId}/adjust")
    public ResponseEntity<InventoryResponse> adjustInventory(
            @PathVariable UUID productId,
            @Valid @RequestBody InventoryAdjustmentRequest request) {

        InventoryResponse response = inventoryAppService.adjustInventory(productId, request);
        return ResponseEntity.ok(response);
    }
}