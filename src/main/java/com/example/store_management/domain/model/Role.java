package com.example.store_management.domain.model;

/**
 * Domain model representing a user role.
 * Example names: ROLE_ADMIN, ROLE_MANAGER, ROLE_VIEWER
 */
public record Role(
        Long id,
        String name,
        String description
) {

    public Role {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Role name must not be blank");
        }
    }
}