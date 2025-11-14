-- V2 - Seed initial roles

-- ROLE_ADMIN
INSERT INTO roles (name, description)
SELECT 'ROLE_ADMIN', 'Administrator with full access'
WHERE NOT EXISTS (
    SELECT 1 FROM roles WHERE name = 'ROLE_ADMIN'
);

-- ROLE_MANAGER
INSERT INTO roles (name, description)
SELECT 'ROLE_MANAGER', 'Store manager with product and inventory permissions'
WHERE NOT EXISTS (
    SELECT 1 FROM roles WHERE name = 'ROLE_MANAGER'
);

-- ROLE_VIEWER
INSERT INTO roles (name, description)
SELECT 'ROLE_VIEWER', 'Read-only user'
WHERE NOT EXISTS (
    SELECT 1 FROM roles WHERE name = 'ROLE_VIEWER'
);