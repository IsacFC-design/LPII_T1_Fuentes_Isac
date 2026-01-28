
-- 1. CREAR LA BASE DE DATOS 
CREATE DATABASE IF NOT EXISTS BD1_Fuentes;
USE BD1_Fuentes;

-- =============================================
-- 2. ELIMINAR TABLAS SI EXISTEN 
-- =============================================
DROP TABLE IF EXISTS inventario;
DROP TABLE IF EXISTS productos;
DROP TABLE IF EXISTS categoria;

-- =============================================
-- 3. CREAR TABLA CATEGORIA
-- =============================================
CREATE TABLE categoria (
    id_cate INT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(100) NOT NULL,
    frecuencia_compra ENUM('Diario', 'Interdiario', 'Semanal', 'Por_Stock') NOT NULL
);

-- =============================================
-- 4. CREAR TABLA PRODUCTOS
-- =============================================
CREATE TABLE productos (
    id_prod INT AUTO_INCREMENT PRIMARY KEY,
    nom_prod VARCHAR(100) NOT NULL,
    id_cate INT NOT NULL,
    stock_actual INT DEFAULT 0,
    FOREIGN KEY (id_cate) REFERENCES categoria(id_cate)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

-- =============================================
-- 5. CREAR TABLA INVENTARIO
-- =============================================
CREATE TABLE inventario (
    nro_inventario INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    id_prod INT NOT NULL,
    costo_ingreso DECIMAL(10,2) NOT NULL,
    motivo_ingreso VARCHAR(200) NOT NULL,
    FOREIGN KEY (id_prod) REFERENCES productos(id_prod)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

-- =============================================
-- 6. INSERTAR DATOS DE PRUEBA (3 registros cada tabla)
-- =============================================

-- 6.1 INSERTAR CATEGORÍAS
INSERT INTO categoria (descripcion, frecuencia_compra) VALUES
('Electrónicos', 'Semanal'),
('Ropa', 'Diario'),
('Alimentos', 'Interdiario');

-- 6.2 INSERTAR PRODUCTOS
INSERT INTO productos (nom_prod, id_cate, stock_actual) VALUES
('Laptop HP Pavilion', 1, 10),
('Camiseta de Algodón M', 2, 50),
('Arroz Extra 5kg', 3, 100);

-- 6.3 INSERTAR REGISTROS DE INVENTARIO
INSERT INTO inventario (id_prod, costo_ingreso, motivo_ingreso) VALUES
(1, 2500.00, 'Compra nueva para stock Q1'),
(2, 25.50, 'Reposición temporada verano'),
(3, 18.75, 'Compra mensual de abastecimiento');

-- =============================================
-- 7. CONSULTAS DE VERIFICACIÓN
-- =============================================
SELECT '=== VERIFICACIÓN DE TABLAS ===' AS Mensaje;

-- 7.1 Mostrar todas las categorías
SELECT 'Categorías:' AS Tabla;
SELECT * FROM categoria;

-- 7.2 Mostrar todos los productos
SELECT 'Productos:' AS Tabla;
SELECT 
    p.id_prod,
    p.nom_prod,
    c.descripcion AS categoria,
    p.stock_actual
FROM productos p
JOIN categoria c ON p.id_cate = c.id_cate;

-- 7.3 Mostrar todos los inventarios
SELECT 'Inventarios:' AS Tabla;
SELECT 
    i.nro_inventario,
    DATE_FORMAT(i.fecha, '%d/%m/%Y %H:%i') AS fecha,
    p.nom_prod AS producto,
    c.descripcion AS categoria,
    i.costo_ingreso,
    i.motivo_ingreso
FROM inventario i
JOIN productos p ON i.id_prod = p.id_prod
JOIN categoria c ON p.id_cate = c.id_cate;

-- =============================================
-- 8. INFORMACIÓN DE LA BASE DE DATOS
-- =============================================
SELECT '=== RESUMEN ===' AS Mensaje;
SELECT 
    (SELECT COUNT(*) FROM categoria) AS total_categorias,
    (SELECT COUNT(*) FROM productos) AS total_productos,
    (SELECT COUNT(*) FROM inventario) AS total_inventarios,
    (SELECT DATABASE()) AS base_datos,
    NOW() AS fecha_ejecucion;