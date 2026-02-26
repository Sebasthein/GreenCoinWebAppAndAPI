-- SQL Script for GreenCoinWebAppAndAPI - Mock Data
-- This script contains demo data including diverse users and realistic recycling history

-- ==========================
-- 1. ROLES (If not already present)
-- ==========================
INSERT INTO roles (nombre, descripcion) VALUES ('ROLE_USER', 'Rol de usuario estándar') ON CONFLICT (nombre) DO NOTHING;
INSERT INTO roles (nombre, descripcion) VALUES ('ROLE_ADMIN', 'Rol de administrador del sistema') ON CONFLICT (nombre) DO NOTHING;

-- ==========================
-- 2. USUARIOS
-- ==========================
-- Asumiendo que el Nivel 1 tiene ID 1
INSERT INTO usuarios (nombre, email, password, puntos, direccion, telefono, avatar_id, nivel_id)
VALUES 
    ('Maria Gomez', 'maria.gomez@example.com', '$2a$10$wT55Qk1U/zV/67OqI98l/uV73Fq7uHl0V8wLWeF.A8t1E8/G4.3G2', 120, 'Calle Primavera 123', '0991234567', 'avatar1.png', 1),
    ('Carlos Ruiz', 'carlos.ruiz@example.com', '$2a$10$wT55Qk1U/zV/67OqI98l/uV73Fq7uHl0V8wLWeF.A8t1E8/G4.3G2', 450, 'Av. Siempre Viva 742', '0987654321', 'avatar2.png', 2),
    ('Ana Mendez', 'ana.mendez@example.com', '$2a$10$wT55Qk1U/zV/67OqI98l/uV73Fq7uHl0V8wLWeF.A8t1E8/G4.3G2', 50, 'Conjunto Las Palmas', '0971122334', 'avatar3.png', 1),
    ('Luis Torres', 'luis.torres@example.com', '$2a$10$wT55Qk1U/zV/67OqI98l/uV73Fq7uHl0V8wLWeF.A8t1E8/G4.3G2', 1100, 'Condominio El Sol', '0969988776', 'avatar4.png', 4);

-- Asignar el rol de USER a los nuevos usuarios
INSERT INTO usuario_roles (usuario_id, rol_id)
SELECT u.id, r.id FROM usuarios u CROSS JOIN roles r 
WHERE u.email LIKE '%@example.com' AND r.nombre = 'ROLE_USER';

-- ==========================
-- 3. MATERIALES
-- ==========================
INSERT INTO materiales (nombre, descripcion, categoria, puntos_por_unidad, reciclable, codigo_barra) 
VALUES 
    ('Botella de Plástico PET', 'Botella de plástico transparente de 500ml', 'Plástico', 10, true, '7891234567890'),
    ('Lata de Aluminio', 'Lata de bebida de aluminio', 'Metal', 15, true, '7891234567891'),
    ('Caja de Cartón Pequeña', 'Caja de cartón sin plastificar', 'Papel', 5, true, '7891234567892'),
    ('Envase de Vidrio', 'Frasco de vidrio transparente lavado', 'Vidrio', 12, true, '7891234567894')
ON CONFLICT (codigo_barra) DO NOTHING;

-- ==========================
-- 4. RECICLAJES
-- ==========================
-- Insertar algunos reciclajes de prueba ya validados (status = true o equivalente)
-- Asumimos que la tabla de reciclajes toma el 'usuario_id' y el 'material_id'
WITH user_maria AS (SELECT id FROM usuarios WHERE email = 'maria.gomez@example.com' LIMIT 1),
     user_carlos AS (SELECT id FROM usuarios WHERE email = 'carlos.ruiz@example.com' LIMIT 1),
     mat_botella AS (SELECT id FROM materiales WHERE codigo_barra = '7891234567890' LIMIT 1),
     mat_lata AS (SELECT id FROM materiales WHERE codigo_barra = '7891234567891' LIMIT 1)
     
INSERT INTO reciclajes (cantidad, puntos_ganados, fecha_reciclaje, validado, estado, usuario_validador_id, usuario_id, material_id)
VALUES 
    (5, 50, CURRENT_TIMESTAMP - INTERVAL '5 days', true, 'VALIDADO', null, (SELECT id FROM user_maria), (SELECT id FROM mat_botella)),
    (2, 30, CURRENT_TIMESTAMP - INTERVAL '3 days', true, 'VALIDADO', null, (SELECT id FROM user_maria), (SELECT id FROM mat_lata)),
    (10, 100, CURRENT_TIMESTAMP - INTERVAL '10 days', true, 'VALIDADO', null, (SELECT id FROM user_carlos), (SELECT id FROM mat_botella)),
    (15, 225, CURRENT_TIMESTAMP - INTERVAL '2 days', true, 'VALIDADO', null, (SELECT id FROM user_carlos), (SELECT id FROM mat_lata)),
    -- Un par de reciclajes pendientes de validación
    (3, 30, CURRENT_TIMESTAMP, false, 'PENDIENTE', null, (SELECT id FROM user_maria), (SELECT id FROM mat_botella)),
    (5, 75, CURRENT_TIMESTAMP - INTERVAL '1 hour', false, 'PENDIENTE', null, (SELECT id FROM user_carlos), (SELECT id FROM mat_lata));
