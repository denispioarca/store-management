package com.example.store_management.infrastructure.persistence.mapper;

import com.example.store_management.domain.model.Role;
import com.example.store_management.infrastructure.persistence.entity.RoleEntity;

public final class RoleMapper {

    private RoleMapper() {
        // utility class
    }

    public static Role toDomain(RoleEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Role(
                entity.getId(),
                entity.getName(),
                entity.getDescription()
        );
    }

    public static RoleEntity toEntity(Role role) {
        if (role == null) {
            return null;
        }
        return RoleEntity.builder()
                .id(role.id())
                .name(role.name())
                .description(role.description())
                .build();
    }
}