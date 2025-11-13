package com.example.store_management.infrastructure.persistence.mapper;

import com.example.store_management.domain.model.InventoryMovement;
import com.example.store_management.domain.model.InventoryMovementType;
import com.example.store_management.infrastructure.persistence.entity.InventoryMovementEntity;

public final class InventoryMovementMapper {

    private InventoryMovementMapper() {
        // utility class
    }

    public static InventoryMovement toDomain(InventoryMovementEntity entity) {
        if (entity == null) {
            return null;
        }
        return new InventoryMovement(
                entity.getId(),
                entity.getProductId(),
                entity.getUserId(),
                entity.getQuantityChange(),
                entity.getResultingQuantity(),
                InventoryMovementType.valueOf(entity.getType()),
                entity.getReason(),
                entity.getCreatedAt()
        );
    }

    public static InventoryMovementEntity toEntity(InventoryMovement movement) {
        if (movement == null) {
            return null;
        }
        return InventoryMovementEntity.builder()
                .id(movement.id())
                .productId(movement.productId())
                .userId(movement.userId())
                .quantityChange(movement.quantityChange())
                .resultingQuantity(movement.resultingQuantity())
                .type(movement.type().name())
                .reason(movement.reason())
                .createdAt(movement.createdAt())
                .build();
    }
}