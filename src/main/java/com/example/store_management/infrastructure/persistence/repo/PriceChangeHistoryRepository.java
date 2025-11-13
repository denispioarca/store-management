package com.example.store_management.infrastructure.persistence.repo;

import com.example.store_management.infrastructure.persistence.entity.PriceChangeHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PriceChangeHistoryRepository extends JpaRepository<PriceChangeHistoryEntity, Long> {

    List<PriceChangeHistoryEntity> findByProductIdOrderByChangedAtDesc(UUID productId);
}