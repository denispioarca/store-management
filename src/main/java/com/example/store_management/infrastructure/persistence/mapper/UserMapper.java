package com.example.store_management.infrastructure.persistence.mapper;

import com.example.store_management.domain.model.Role;
import com.example.store_management.domain.model.User;
import com.example.store_management.infrastructure.persistence.entity.RoleEntity;
import com.example.store_management.infrastructure.persistence.entity.UserEntity;

import java.util.Set;
import java.util.stream.Collectors;

public final class UserMapper {

    private UserMapper() {
        // utility class
    }

    public static User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        Set<Role> roles = entity.getRoles() == null
                ? Set.of()
                : entity.getRoles().stream()
                .map(RoleMapper::toDomain)
                .collect(Collectors.toUnmodifiableSet());

        return new User(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getPhone(),
                entity.isEnabled(),
                entity.isLocked(),
                roles,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    /**
     * Creates a new UserEntity from a domain User and an encoded password.
     * Used when registering or creating a new user.
     */
    public static UserEntity toEntity(User user, String encodedPassword) {
        if (user == null) {
            return null;
        }

        Set<RoleEntity> roleEntities = user.roles() == null
                ? Set.of()
                : user.roles().stream()
                .map(RoleMapper::toEntity)
                .collect(Collectors.toSet());

        UserEntity entity = UserEntity.builder()
                .id(user.id())
                .username(user.username())
                .email(user.email())
                .password(encodedPassword)
                .firstName(user.firstName())
                .lastName(user.lastName())
                .phone(user.phone())
                .enabled(user.enabled())
                .locked(user.locked())
                .createdAt(user.createdAt())
                .updatedAt(user.updatedAt())
                .build();

        entity.setRoles(roleEntities);
        roleEntities.forEach(roleEntity -> roleEntity.getUsers().add(entity));

        return entity;
    }

    /**
     * Updates an existing UserEntity from a domain User without touching the password.
     */
    public static void updateEntity(User user, UserEntity entity) {
        if (user == null || entity == null) {
            return;
        }

        entity.setUsername(user.username());
        entity.setEmail(user.email());
        entity.setFirstName(user.firstName());
        entity.setLastName(user.lastName());
        entity.setPhone(user.phone());
        entity.setEnabled(user.enabled());
        entity.setLocked(user.locked());
        entity.setCreatedAt(user.createdAt());
        entity.setUpdatedAt(user.updatedAt());

        Set<RoleEntity> roleEntities = user.roles() == null
                ? Set.of()
                : user.roles().stream()
                .map(RoleMapper::toEntity)
                .collect(Collectors.toSet());

        entity.getRoles().clear();
        entity.getRoles().addAll(roleEntities);
        roleEntities.forEach(roleEntity -> roleEntity.getUsers().add(entity));
    }
}