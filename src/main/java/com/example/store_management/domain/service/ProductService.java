package com.example.store_management.domain.service;

import com.example.store_management.common.exception.ProductNotFoundException;
import com.example.store_management.domain.model.Product;
import com.example.store_management.infrastructure.persistence.entity.ProductEntity;
import com.example.store_management.infrastructure.persistence.mapper.ProductMapper;
import com.example.store_management.infrastructure.persistence.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Domain service responsible for product-related business logic.
 * It does NOT handle price history (that is delegated to PriceChangeHistoryService).
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * Creates a new product.
     * - validates SKU uniqueness
     * - persists the product
     * - returns the saved domain model (with generated id and timestamps)
     */
    public Product createProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product must not be null");
        }

        if (productRepository.existsBySku(product.sku())) {
            throw new IllegalStateException("Product with SKU " + product.sku() + " already exists");
        }

        ProductEntity toSave = ProductMapper.toEntity(product);
        ProductEntity saved = productRepository.save(toSave);
        return ProductMapper.toDomain(saved);
    }

    /**
     * Returns a product by id or throws if not found.
     */
    @Transactional(readOnly = true)
    public Product getProductById(UUID id) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id " + id));
        return ProductMapper.toDomain(entity);
    }

    /**
     * Returns a list with all the products.
     */
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(ProductMapper::toDomain)
                .toList();
    }

    /**
     * Changes the price of a product.
     * Actual recording of the price change in history is done by PriceChangeHistoryService.
     * * If the new price is equal to the current price, this method is a no-op.
     *
     * @param productId product identifier
     * @param newPrice  new price (> 0)
     * @return updated Product domain model
     */
    public Product changePrice(UUID productId, BigDecimal newPrice) {
        if (newPrice == null || newPrice.signum() <= 0) {
            throw new IllegalArgumentException("New price must be greater than 0");
        }

        ProductEntity entity = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id " + productId));

        BigDecimal currentPrice = entity.getPrice();
        if (currentPrice != null && currentPrice.compareTo(newPrice) == 0) {
            // No effective price change, return current state
            return ProductMapper.toDomain(entity);
        }

        entity.setPrice(newPrice);
        entity.setUpdatedAt(OffsetDateTime.now());

        ProductEntity saved = productRepository.save(entity);
        return ProductMapper.toDomain(saved);
    }
}