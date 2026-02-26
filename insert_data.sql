-- SQL Script for GreenCoinWebAppAndAPI
-- Run these queries in your PostgreSQL database terminal (e.g. pgAdmin, dbeaver, or psql)
-- to populate the database with initial dummy data.
-- ==========================
-- ROLES
-- ==========================
-- Insertar roles base si no existen (Spring Security suele preferir prefijo ROLE_)
INSERT INTO roles (nombre, descripcion)
VALUES ('ROLE_USER', 'Rol de usuario estándar') ON CONFLICT (nombre) DO NOTHING;
INSERT INTO roles (nombre, descripcion)
VALUES ('ROLE_ADMIN', 'Rol de administrador del sistema') ON CONFLICT (nombre) DO NOTHING;
-- ==========================
-- MATERIALES
-- ==========================
INSERT INTO materiales (
        nombre,
        descripcion,
        categoria,
        puntos_por_unidad,
        reciclable,
        codigo_barra
    )
VALUES (
        'Botella de Plástico PET',
        'Botella de plástico transparente de 500ml',
        'Plástico',
        10,
        true,
        '7891234567890'
    ) ON CONFLICT (codigo_barra) DO NOTHING;
INSERT INTO materiales (
        nombre,
        descripcion,
        categoria,
        puntos_por_unidad,
        reciclable,
        codigo_barra
    )
VALUES (
        'Lata de Aluminio',
        'Lata de bebida de aluminio',
        'Metal',
        15,
        true,
        '7891234567891'
    ) ON CONFLICT (codigo_barra) DO NOTHING;
INSERT INTO materiales (
        nombre,
        descripcion,
        categoria,
        puntos_por_unidad,
        reciclable,
        codigo_barra
    )
VALUES (
        'Caja de Cartón Pequeña',
        'Caja de cartón sin plastificar',
        'Papel',
        5,
        true,
        '7891234567892'
    ) ON CONFLICT (codigo_barra) DO NOTHING;
INSERT INTO materiales (
        nombre,
        descripcion,
        categoria,
        puntos_por_unidad,
        reciclable,
        codigo_barra
    )
VALUES (
        'Pilas Alcalinas AA',
        'Baterías desechables AA',
        'Electrónico',
        20,
        false,
        '7891234567893'
    ) ON CONFLICT (codigo_barra) DO NOTHING;
INSERT INTO materiales (
        nombre,
        descripcion,
        categoria,
        puntos_por_unidad,
        reciclable,
        codigo_barra
    )
VALUES (
        'Envase de Vidrio',
        'Frasco de vidrio transparente lavado',
        'Vidrio',
        12,
        true,
        '7891234567894'
    ) ON CONFLICT (codigo_barra) DO NOTHING;
-- ==========================
-- LOGROS
-- ==========================
-- Nota: La tabla no tiene constraint unique en nombre, no se puede usar ON CONFLICT simplemente, 
-- pero puedes ejecutar estos INSERTS una vez.
INSERT INTO logros (
        nombre,
        descripcion,
        imagen_trofeo,
        puntos_requeridos
    )
VALUES (
        'Primer Reciclaje',
        'Realizaste tu primer reciclaje en el sistema',
        'trofeo_bronce.png',
        10
    );
INSERT INTO logros (
        nombre,
        descripcion,
        imagen_trofeo,
        puntos_requeridos
    )
VALUES (
        'Reciclador Constante',
        'Has acumulado 100 puntos de reciclaje',
        'trofeo_plata.png',
        100
    );
INSERT INTO logros (
        nombre,
        descripcion,
        imagen_trofeo,
        puntos_requeridos
    )
VALUES (
        'Protector del Medio Ambiente',
        'Has acumulado 500 puntos de reciclaje',
        'trofeo_oro.png',
        500
    );
INSERT INTO logros (
        nombre,
        descripcion,
        imagen_trofeo,
        puntos_requeridos
    )
VALUES (
        'Héroe Verde',
        'Has alcanzado los 1000 puntos y salvado numerosos árboles',
        'trofeo_diamante.png',
        1000
    );