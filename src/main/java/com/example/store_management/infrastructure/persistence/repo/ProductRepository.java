package com.example.store_management.infrastructure.persistence.repo;

import com.example.store_management.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {

    Optional<ProductEntity> findBySku(String sku);

    boolean existsBySku(String sku);
}