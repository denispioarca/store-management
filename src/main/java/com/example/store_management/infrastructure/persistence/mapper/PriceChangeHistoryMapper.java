package com.example.store_management.infrastructure.persistence.mapper;

import com.example.store_management.domain.model.PriceChangeHistory;
import com.example.store_management.infrastructure.persistence.entity.PriceChangeHistoryEntity;

public final class PriceChangeHistoryMapper {

    private PriceChangeHistoryMapper() {
        // utility class
    }

    public static PriceChangeHistory toDomain(PriceChangeHistoryEntity entity) {
        if (entity == null) {
            return null;
        }
        return new PriceChangeHistory(
                entity.getId(),
                entity.getProductId(),
                entity.getOldPrice(),
                entity.getNewPrice(),
                entity.getCurrency(),
                entity.getChangedByUserId(),
                entity.getReason(),
                entity.getChangedAt()
        );
    }

    public static PriceChangeHistoryEntity toEntity(PriceChangeHistory history) {
        if (history == null) {
            return null;
        }
        return PriceChangeHistoryEntity.builder()
                .id(history.id())
                .productId(history.productId())
                .oldPrice(history.oldPrice())
                .newPrice(history.newPrice())
                .currency(history.currency())
                .changedByUserId(history.changedByUserId())
                .reason(history.reason())
                .changedAt(history.changedAt())
                .build();
    }
}