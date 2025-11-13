package com.example.store_management.domain.model;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Domain model representing an application user.
 */
public record User(
        Long id,
        String username,
        String email,
        String firstName,
        String lastName,
        String phone,
        boolean enabled,
        boolean locked,
        Set<Role> roles,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {

    public User {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username must not be blank");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must not be blank");
        }
        if (roles == null) {
            roles = Collections.emptySet();
        } else {
            roles = Collections.unmodifiableSet(new HashSet<>(roles));
        }
    }

    public User enable() {
        return new User(
                this.id,
                this.username,
                this.email,
                this.firstName,
                this.lastName,
                this.phone,
                true,
                this.locked,
                this.roles,
                this.createdAt,
                OffsetDateTime.now()
        );
    }

    public User lock() {
        return new User(
                this.id,
                this.username,
                this.email,
                this.firstName,
                this.lastName,
                this.phone,
                this.enabled,
                true,
                this.roles,
                this.createdAt,
                OffsetDateTime.now()
        );
    }

    public User unlock() {
        return new User(
                this.id,
                this.username,
                this.email,
                this.firstName,
                this.lastName,
                this.phone,
                this.enabled,
                false,
                this.roles,
                this.createdAt,
                OffsetDateTime.now()
        );
    }
}