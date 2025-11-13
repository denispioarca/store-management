package com.example.store_management.infrastructure.persistence.repo;

import com.example.store_management.infrastructure.persistence.entity.InventoryMovementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InventoryMovementRepository extends JpaRepository<InventoryMovementEntity, Long> {

    List<InventoryMovementEntity> findByProductIdOrderByCreatedAtDesc(UUID productId);
}