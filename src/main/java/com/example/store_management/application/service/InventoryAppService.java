package com.example.store_management.application.service;

import com.example.store_management.application.dto.InventoryAdjustmentRequest;
import com.example.store_management.application.dto.InventoryMovementResponse;
import com.example.store_management.application.dto.InventoryResponse;
import com.example.store_management.domain.model.Inventory;
import com.example.store_management.domain.model.InventoryMovement;
import com.example.store_management.domain.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    public InventoryResponse adjustInventory(UUID productId, InventoryAdjustmentRequest request) {
        Inventory inventory = inventoryService.adjustQuantity(
                productId,
                request.quantityChange(),
                request.performedByUserId(),
                request.reason()
        );
        return mapToResponse(inventory);
    }

    /**
     * Returns all inventory movements for the given product.
     */
    @Transactional(readOnly = true)
    public List<InventoryMovementResponse> getInventoryMovements(UUID productId) {
        List<InventoryMovement> movements = inventoryService.getMovementsForProduct(productId);
        return movements.stream()
                .map(this::mapToMovementResponse)
                .toList();
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

    private InventoryMovementResponse mapToMovementResponse(InventoryMovement movement) {
        return new InventoryMovementResponse(
                movement.id(),
                movement.quantityChange(),
                movement.resultingQuantity(),
                movement.type().name(),
                movement.userId(),
                movement.reason(),
                movement.createdAt()
        );
    }
}