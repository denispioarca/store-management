-- V3 - Seed initial admin user and its roles

------------------------------------------------------------
-- Create admin user if it does not exist
------------------------------------------------------------
INSERT INTO users (
    username,
    email,
    password,
    first_name,
    last_name,
    phone,
    enabled,
    locked
)
SELECT
    'admin'          AS username,
    'admin@example.com' AS email,
    'admin'          AS password,      -- plain text for now; will switch to BCrypt when Security is implemented
    'System'         AS first_name,
    'Administrator'  AS last_name,
    '0000000000'     AS phone,
    TRUE             AS enabled,
    FALSE            AS locked
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE username = 'admin'
);

------------------------------------------------------------
-- Link admin user with ROLE_ADMIN and ROLE_MANAGER
------------------------------------------------------------

-- Admin + ROLE_ADMIN
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
JOIN roles r ON r.name = 'ROLE_ADMIN'
WHERE u.username = 'admin'
  AND NOT EXISTS (
      SELECT 1 FROM user_roles ur
      WHERE ur.user_id = u.id AND ur.role_id = r.id
  );

-- Admin + ROLE_MANAGER
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
JOIN roles r ON r.name = 'ROLE_MANAGER'
WHERE u.username = 'admin'
  AND NOT EXISTS (
      SELECT 1 FROM user_roles ur
      WHERE ur.user_id = u.id AND ur.role_id = r.id
  );