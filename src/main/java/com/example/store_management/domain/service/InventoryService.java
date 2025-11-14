package com.example.store_management.domain.service;

import com.example.store_management.common.exception.InsufficientStockException;
import com.example.store_management.common.exception.InventoryNotFoundException;
import com.example.store_management.common.exception.ProductNotFoundException;
import com.example.store_management.domain.model.Inventory;
import com.example.store_management.domain.model.InventoryMovement;
import com.example.store_management.infrastructure.persistence.entity.InventoryEntity;
import com.example.store_management.infrastructure.persistence.entity.InventoryMovementEntity;
import com.example.store_management.infrastructure.persistence.mapper.InventoryMapper;
import com.example.store_management.infrastructure.persistence.mapper.InventoryMovementMapper;
import com.example.store_management.infrastructure.persistence.repo.InventoryMovementRepository;
import com.example.store_management.infrastructure.persistence.repo.InventoryRepository;
import com.example.store_management.infrastructure.persistence.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
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
    private final ProductRepository productRepository;

    /**
     * Creates an initial inventory entry for a newly created product.
     * If the inventory already exists, it is simply returned.
     * <p>
     * Note: we deliberately do NOT set the version field here.
     * Leaving version as null allows Hibernate to treat this as a new entity,
     * perform an INSERT, and initialize the version (optimistic locking) correctly.
     */
    @Transactional
    public Inventory createInitialInventoryForProduct(UUID productId) {
        return inventoryRepository.findById(productId)
                // If inventory already exists, return domain object
                .map(InventoryMapper::toDomain)
                .orElseGet(() -> {
                    InventoryEntity entity = InventoryEntity.builder()
                            .productId(productId)
                            .quantity(0L)
                            // version is intentionally left null
                            .updatedAt(OffsetDateTime.now())
                            .build();

                    InventoryEntity saved = inventoryRepository.save(entity);
                    return InventoryMapper.toDomain(saved);
                });
    }

    /**
     * Returns the inventory for a product.
     */
    @Transactional(readOnly = true)
    public Inventory getInventoryForProduct(UUID productId) {
        checkIfTheProductExists(productId);

        InventoryEntity entity = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new InventoryNotFoundException("Inventory not found for product " + productId));
        return InventoryMapper.toDomain(entity);
    }

    /**
     * Returns all inventory movements for a given product, ordered by newest first.
     * Throws InventoryNotFoundException if no inventory exists for the product.
     */
    @Transactional(readOnly = true)
    public List<InventoryMovement> getMovementsForProduct(UUID productId) {
        checkIfTheProductExists(productId);

        boolean inventoryExists = inventoryRepository.existsById(productId);
        if (!inventoryExists) {
            throw new InventoryNotFoundException(
                    "Inventory for product id %s was not found".formatted(productId)
            );
        }

        return inventoryMovementRepository
                .findByProductIdOrderByCreatedAtDesc(productId)
                .stream()
                .map(InventoryMovementMapper::toDomain)
                .toList();
    }

    /**
     * Adjusts quantity for an existing inventory row and records an inventory movement.
     *
     * @param productId         product identifier
     * @param quantityChange    positive for INCREASE, negative for DECREASE
     * @param performedByUserId user id that triggered the change (can be null for system)
     * @param reason            optional reason/comment
     */
    @Transactional
    public Inventory adjustQuantity(UUID productId,
                                    long quantityChange,
                                    Long performedByUserId,
                                    String reason) {

        checkIfTheProductExists(productId);

        InventoryEntity entity = inventoryRepository.findById(productId)
                .orElseThrow(() -> new InventoryNotFoundException(
                        "Inventory for product id %s was not found".formatted(productId)
                ));

        if (quantityChange == 0) {
            // No change in quantity, do not touch DB or log movement
            return InventoryMapper.toDomain(entity);
        }

        long currentQuantity = entity.getQuantity() != null ? entity.getQuantity() : 0L;
        long newQuantity = currentQuantity + quantityChange;

        if (newQuantity < 0) {
            throw new InsufficientStockException(
                    "Not enough stock for product id %s. Current quantity: %d, requested change: %d"
                            .formatted(productId, currentQuantity, quantityChange)
            );
        }

        entity.setQuantity(newQuantity);
        entity.setUpdatedAt(OffsetDateTime.now());
        InventoryEntity savedInventory = inventoryRepository.save(entity);

        String movementType = resolveMovementType(quantityChange);

        InventoryMovementEntity movement = InventoryMovementEntity.builder()
                .productId(productId)
                .userId(performedByUserId)
                .quantityChange(quantityChange)
                .resultingQuantity(newQuantity)
                .type(movementType)
                .reason(reason)
                .createdAt(OffsetDateTime.now())
                .build();

        inventoryMovementRepository.save(movement);

        return InventoryMapper.toDomain(savedInventory);
    }

    private void checkIfTheProductExists(UUID productId) {
        boolean productExists = productRepository.existsById(productId);
        if (!productExists) {
            throw new ProductNotFoundException("Product not found for productId " + productId);
        }
    }

    private String resolveMovementType(long quantityChange) {
        if (quantityChange > 0) {
            return "INCREASE";
        } else if (quantityChange < 0) {
            return "DECREASE";
        }
        return "ADJUSTMENT";
    }
}