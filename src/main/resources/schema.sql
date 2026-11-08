-- =============================================
-- Script de creación de la base de datos
-- Gestión de Usuarios - Arquitectura Hexagonal
-- =============================================

CREATE DATABASE IF NOT EXISTS crud_usuarios
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE crud_usuarios;

CREATE TABLE IF NOT EXISTS users (
    id          VARCHAR(36)  NOT NULL PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    role        ENUM('ADMIN', 'MEMBER', 'REVIEWER') NOT NULL,
    status      ENUM('ACTIVE', 'INACTIVE', 'PENDING', 'BLOCKED') NOT NULL DEFAULT 'PENDING',
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Usuario administrador inicial (username: admin / password: Admin1234!)
INSERT INTO users (id, username, name, email, password, role, status)
VALUES (
    '00000000-0000-0000-0000-000000000001',
    'admin',
    'Administrador',
    'admin@example.com',
    '$2a$12$sfwkajsls.fktQb0Ngw5cOiSiV3z4LIwbucSAy4sw2wyNlxbUBt4q',
    'ADMIN',
    'ACTIVE'
);


-- =============================================
-- Ejercicio 21: Calificaciones
-- =============================================
CREATE TABLE IF NOT EXISTS calificaciones (
    id                  BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    fecha               DATE         NOT NULL,
    estudiante          VARCHAR(150) NOT NULL,
    docente             VARCHAR(150) NOT NULL,
    asignatura          VARCHAR(150) NOT NULL,
    carrera             VARCHAR(150) NOT NULL,
    universidad         VARCHAR(150) NOT NULL,
    periodo             VARCHAR(20)  NOT NULL,
    actividad_evaluada  VARCHAR(150) NOT NULL,
    porcentaje          DECIMAL(5,2) NOT NULL,
    nota                DECIMAL(3,1) NOT NULL,
    created_at          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT chk_calificaciones_porcentaje CHECK (porcentaje BETWEEN 0 AND 100),
    CONSTRAINT chk_calificaciones_nota CHECK (nota BETWEEN 0 AND 5)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Datos de ejemplo
INSERT INTO calificaciones
    (fecha, estudiante, docente, asignatura, carrera, universidad, periodo, actividad_evaluada, porcentaje, nota)
VALUES
    ('2026-09-15', 'Ana Perez',   'John Arrieta', 'Electiva II', 'Ingenieria de Sistemas', 'Unicolombo', '2026-2', 'Parcial 1', 30, 4.2),
    ('2026-10-20', 'Ana Perez',   'John Arrieta', 'Electiva II', 'Ingenieria de Sistemas', 'Unicolombo', '2026-2', 'Taller microservicios', 20, 4.8),
    ('2026-09-15', 'Luis Gomez',  'John Arrieta', 'Electiva II', 'Ingenieria de Sistemas', 'Unicolombo', '2026-2', 'Parcial 1', 30, 3.5);
