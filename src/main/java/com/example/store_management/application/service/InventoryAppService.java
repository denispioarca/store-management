package com.example.store_management.application.service;

import com.example.store_management.application.dto.InventoryAdjustmentRequest;
import com.example.store_management.application.dto.InventoryResponse;
import com.example.store_management.domain.model.Inventory;
import com.example.store_management.domain.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Application service responsible for orchestrating inventory-related use cases.
 */
@Service
@RequiredArgsConstructor
public class InventoryAppService {

    private final InventoryService inventoryService;

    /**
     * Returns inventory information for a given product as a response DTO.
     */
    @Transactional(readOnly = true)
    public InventoryResponse getInventoryForProduct(UUID productId) {
        Inventory inventory = inventoryService.getInventoryForProduct(productId);
        return mapToResponse(inventory);
    }

    /**
     * Adjusts inventory quantity based on the request and returns the updated inventory as DTO.
     */
    @Transactional
    public InventoryResponse adjustInventory(InventoryAdjustmentRequest request) {
        Inventory inventory = inventoryService.adjustQuantity(
                request.productId(),
                request.quantityChange(),
                request.performedByUserId(),
                request.reason()
        );
        return mapToResponse(inventory);
    }

    // ---------- Mapping helpers ----------

    private InventoryResponse mapToResponse(Inventory inventory) {
        return new InventoryResponse(
                inventory.productId(),
                inventory.quantity(),
                inventory.version(),
                inventory.updatedAt()
        );
    }
}