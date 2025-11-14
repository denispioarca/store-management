package com.example.store_management.domain.service;

import com.example.store_management.domain.model.Inventory;
import com.example.store_management.domain.model.InventoryMovementType;
import com.example.store_management.infrastructure.persistence.entity.InventoryEntity;
import com.example.store_management.infrastructure.persistence.entity.InventoryMovementEntity;
import com.example.store_management.infrastructure.persistence.mapper.InventoryMapper;
import com.example.store_management.infrastructure.persistence.repo.InventoryMovementRepository;
import com.example.store_management.infrastructure.persistence.repo.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * Domain service responsible for inventory-related business logic.
 * It also records inventory movements for auditing purposes.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMovementRepository inventoryMovementRepository;

    /**
     * Returns the inventory for a product.
     */
    @Transactional(readOnly = true)
    public Inventory getInventoryForProduct(UUID productId) {
        InventoryEntity entity = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new NoSuchElementException("Inventory not found for product " + productId));
        return InventoryMapper.toDomain(entity);
    }

    /**
     * Adjusts quantity and records an inventory movement.
     *
     * @param productId         product identifier
     * @param quantityChange    positive for INCREASE, negative for DECREASE
     * @param performedByUserId user id that triggered the change (can be null for system)
     * @param reason            optional reason/comment
     */
    public Inventory adjustQuantity(UUID productId,
                                    long quantityChange,
                                    Long performedByUserId,
                                    String reason) {

        // load or create inventory row
        InventoryEntity entity = inventoryRepository.findByProductId(productId)
                .orElseGet(() -> InventoryEntity.builder()
                        .productId(productId)
                        .quantity(0L)
                        .version(0L)
                        .updatedAt(OffsetDateTime.now())
                        .build());

        long currentQuantity = entity.getQuantity() != null ? entity.getQuantity() : 0L;
        long newQuantity = currentQuantity + quantityChange;

        if (newQuantity < 0) {
            throw new IllegalStateException("Insufficient stock for product " + productId);
        }

        entity.setQuantity(newQuantity);
        entity.setUpdatedAt(OffsetDateTime.now());
        InventoryEntity savedInventory = inventoryRepository.save(entity);

        InventoryMovementType type;
        if (quantityChange > 0) {
            type = InventoryMovementType.INCREASE;
        } else if (quantityChange < 0) {
            type = InventoryMovementType.DECREASE;
        } else {
            type = InventoryMovementType.ADJUSTMENT;
        }

        InventoryMovementEntity movement = InventoryMovementEntity.builder()
                .productId(productId)
                .userId(performedByUserId)
                .quantityChange(quantityChange)
                .resultingQuantity(newQuantity)
                .type(type.name())
                .reason(reason)
                .createdAt(OffsetDateTime.now())
                .build();

        inventoryMovementRepository.save(movement);

        return InventoryMapper.toDomain(savedInventory);
    }
}