package com.example.store_management.infrastructure.persistence.mapper;

import com.example.store_management.domain.model.Product;
import com.example.store_management.domain.model.ProductStatus;
import com.example.store_management.infrastructure.persistence.entity.ProductEntity;

public final class ProductMapper {

    private ProductMapper() {
        // utility class
    }

    public static Product toDomain(ProductEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Product(
                entity.getId(),
                entity.getSku(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getCurrency(),
                ProductStatus.valueOf(entity.getStatus()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static ProductEntity toEntity(Product product) {
        if (product == null) {
            return null;
        }
        return ProductEntity.builder()
                .id(product.id())
                .sku(product.sku())
                .name(product.name())
                .description(product.description())
                .price(product.price())
                .currency(product.currency())
                .status(product.status().name())
                .createdAt(product.createdAt())
                .updatedAt(product.updatedAt())
                .build();
    }
}