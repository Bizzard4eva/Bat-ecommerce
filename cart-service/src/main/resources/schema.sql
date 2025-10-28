CREATE DATABASE carritocompras;

\c carritocompras;

-- Tabla para almacenar los carritos de compra
CREATE TABLE IF NOT EXISTS cart (
    id_cart SERIAL PRIMARY KEY,
    id_usuario INT NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla para almacenar los ítems dentro de cada carrito
CREATE TABLE IF NOT EXISTS cart_item (
    id_item SERIAL PRIMARY KEY,
    id_cart INT NOT NULL REFERENCES cart(id_cart) ON DELETE CASCADE,
    id_producto INT NOT NULL,
    cantidad INT NOT NULL CHECK (cantidad > 0),
    precio_unitario DECIMAL(10,2),
    subtotal DECIMAL(10,2)
);

-- Índices para mejorar el rendimiento
CREATE INDEX idx_cart_usuario ON cart(id_usuario);
CREATE INDEX idx_cart_item_cart ON cart_item(id_cart);
CREATE INDEX idx_cart_item_producto ON cart_item(id_producto);