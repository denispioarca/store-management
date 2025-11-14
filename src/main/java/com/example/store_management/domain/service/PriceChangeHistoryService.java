package com.example.store_management.domain.service;

import com.example.store_management.domain.model.PriceChangeHistory;
import com.example.store_management.infrastructure.persistence.entity.PriceChangeHistoryEntity;
import com.example.store_management.infrastructure.persistence.mapper.PriceChangeHistoryMapper;
import com.example.store_management.infrastructure.persistence.repo.PriceChangeHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Domain service responsible for managing price change history.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PriceChangeHistoryService {

    private final PriceChangeHistoryRepository priceChangeHistoryRepository;

    /**
     * Records a new price change entry in the history.
     */
    public PriceChangeHistory recordPriceChange(UUID productId,
                                                BigDecimal oldPrice,
                                                BigDecimal newPrice,
                                                String currency,
                                                Long changedByUserId,
                                                String reason) {

        if (oldPrice != null && oldPrice.compareTo(newPrice) == 0) {
            // No effective change, do not record history
            throw new IllegalArgumentException("The new price shouldn't be equal with the old price for the product with id " + productId);
        }

        PriceChangeHistoryEntity entity = PriceChangeHistoryEntity.builder()
                .productId(productId)
                .oldPrice(oldPrice)
                .newPrice(newPrice)
                .currency(currency)
                .changedByUserId(changedByUserId)
                .reason(reason)
                .changedAt(OffsetDateTime.now())
                .build();

        PriceChangeHistoryEntity saved = priceChangeHistoryRepository.save(entity);
        return PriceChangeHistoryMapper.toDomain(saved);
    }

    /**
     * Returns the price change history for a given product, ordered by newest first.
     */
    @Transactional(readOnly = true)
    public List<PriceChangeHistory> getHistoryForProduct(UUID productId) {
        return priceChangeHistoryRepository
                .findByProductIdOrderByChangedAtDesc(productId)
                .stream()
                .map(PriceChangeHistoryMapper::toDomain)
                .collect(Collectors.toList());
    }
}