package com.example.store_management.infrastructure.persistence.repo;

import com.example.store_management.infrastructure.persistence.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InventoryRepository extends JpaRepository<InventoryEntity, UUID> {

    Optional<InventoryEntity> findByProductId(UUID productId);
}