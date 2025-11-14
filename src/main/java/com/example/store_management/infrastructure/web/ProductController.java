package com.example.store_management.infrastructure.web;

import com.example.store_management.application.dto.ProductCreateRequest;
import com.example.store_management.application.dto.ProductPriceChangeRequest;
import com.example.store_management.application.dto.ProductResponse;
import com.example.store_management.application.service.ProductAppService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

/**
 * REST controller exposing product-related endpoints.
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductAppService productAppService;

    /**
     * Creates a new product.
     * <p>
     * POST /api/products
     */
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductCreateRequest request) {

        ProductResponse created = productAppService.createProduct(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    /**
     * Returns a product by id.
     * <p>
     * GET /api/products/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable UUID id) {
        ProductResponse response = productAppService.getProductById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Changes the price of a product.
     * <p>
     * PATCH /api/products/{id}/price
     * <p>
     * The path variable is the source of truth for productId.
     */
    @PatchMapping("/{id}/price")
    public ResponseEntity<ProductResponse> changePrice(
            @PathVariable UUID id,
            @Valid @RequestBody ProductPriceChangeRequest request) {

        ProductResponse response = productAppService.changePrice(id, request);
        return ResponseEntity.ok(response);
    }

}