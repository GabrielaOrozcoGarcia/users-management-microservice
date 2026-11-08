-- =============================================
-- Migracion: agregar username a una tabla users que YA existe
-- (use este script solo si ya habia ejecutado el schema.sql anterior en TiDB/Aiven)
-- =============================================
USE crud_usuarios;

ALTER TABLE users ADD COLUMN username VARCHAR(50) NULL AFTER id;

-- Username por defecto: la parte del email antes de la arroba
UPDATE users SET username = LOWER(SUBSTRING_INDEX(email, '@', 1)) WHERE username IS NULL;

ALTER TABLE users MODIFY username VARCHAR(50) NOT NULL;
ALTER TABLE users ADD UNIQUE INDEX uk_users_username (username);
