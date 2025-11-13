-- V1 - Initial database schema for Store Management API

------------------------------------------------------------
-- PRODUCTS
------------------------------------------------------------
CREATE TABLE products (
    id UUID PRIMARY KEY,
    sku VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price NUMERIC(19,2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    status VARCHAR(30) NOT NULL, -- e.g. ACTIVE, INACTIVE, DISCONTINUED
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

------------------------------------------------------------
-- INVENTORY (1-1 with products)
------------------------------------------------------------
CREATE TABLE inventory (
    product_id UUID PRIMARY KEY,
    quantity BIGINT NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_inventory_product
        FOREIGN KEY (product_id) REFERENCES products (id)
);

------------------------------------------------------------
-- USERS
------------------------------------------------------------
CREATE TABLE users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    phone VARCHAR(30),
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    locked BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

------------------------------------------------------------
-- ROLES
------------------------------------------------------------
CREATE TABLE roles (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE, -- e.g. ROLE_ADMIN, ROLE_MANAGER, ROLE_VIEWER
    description VARCHAR(255)
);

------------------------------------------------------------
-- USER_ROLES (many-to-many between users and roles)
------------------------------------------------------------
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user
        FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_user_roles_role
        FOREIGN KEY (role_id) REFERENCES roles (id)
);

------------------------------------------------------------
-- INVENTORY MOVEMENTS (stock change audit)
------------------------------------------------------------
CREATE TABLE inventory_movements (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    product_id UUID NOT NULL,
    user_id BIGINT NOT NULL,
    quantity_change BIGINT NOT NULL,
    resulting_quantity BIGINT NOT NULL,
    type VARCHAR(30) NOT NULL, -- INCREASE, DECREASE, ADJUSTMENT
    reason TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_inventory_movements_product
        FOREIGN KEY (product_id) REFERENCES products (id),
    CONSTRAINT fk_inventory_movements_user
        FOREIGN KEY (user_id) REFERENCES users (id)
);

------------------------------------------------------------
-- PRICE CHANGE HISTORY (price change audit)
------------------------------------------------------------
CREATE TABLE price_change_history (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    product_id UUID NOT NULL,
    old_price NUMERIC(19,2) NOT NULL,
    new_price NUMERIC(19,2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    changed_by_user_id BIGINT NOT NULL,
    reason TEXT,
    changed_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_price_change_history_product
        FOREIGN KEY (product_id) REFERENCES products (id),
    CONSTRAINT fk_price_change_history_user
        FOREIGN KEY (changed_by_user_id) REFERENCES users (id)
);

------------------------------------------------------------
-- OPTIONAL INDEXES (for faster lookups on foreign keys)
------------------------------------------------------------
CREATE INDEX idx_inventory_movements_product_id
    ON inventory_movements (product_id);

CREATE INDEX idx_inventory_movements_user_id
    ON inventory_movements (user_id);

CREATE INDEX idx_price_change_history_product_id
    ON price_change_history (product_id);

CREATE INDEX idx_price_change_history_user_id
    ON price_change_history (changed_by_user_id);