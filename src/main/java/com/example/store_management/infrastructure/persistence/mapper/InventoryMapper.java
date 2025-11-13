package com.example.store_management.infrastructure.persistence.mapper;

import com.example.store_management.domain.model.Inventory;
import com.example.store_management.infrastructure.persistence.entity.InventoryEntity;

public final class InventoryMapper {

    private InventoryMapper() {
        // utility class
    }

    public static Inventory toDomain(InventoryEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Inventory(
                entity.getProductId(),
                entity.getQuantity() != null ? entity.getQuantity() : 0L,
                entity.getVersion() != null ? entity.getVersion() : 0L,
                entity.getUpdatedAt()
        );
    }

    public static InventoryEntity toEntity(Inventory inventory) {
        if (inventory == null) {
            return null;
        }
        return InventoryEntity.builder()
                .productId(inventory.productId())
                .quantity(inventory.quantity())
                .version(inventory.version())
                .updatedAt(inventory.updatedAt())
                .build();
    }
}