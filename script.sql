CREATE DATABASE coffee_shop;
USE coffee_shop;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,

    email VARCHAR(150) NOT NULL UNIQUE,
    phone VARCHAR(30) NOT NULL UNIQUE,

    active BOOLEAN NOT NULL DEFAULT TRUE,

    rol ENUM('CUSTOMER', 'EMPLOYEE', 'MANAGER') NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE user_credentials (
    user_id BIGINT PRIMARY KEY,
    
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,

    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Drink sizes
CREATE TABLE sizes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE,
    price_multiplier DECIMAL(4,2) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Categorias
CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    requires_size BOOLEAN NOT NULL DEFAULT FALSE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Productos
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    category_id BIGINT NOT NULL,

    name VARCHAR(120) NOT NULL,
    description VARCHAR(255),

    base_price DECIMAL(10,2) NOT NULL,

    available BOOLEAN NOT NULL DEFAULT TRUE,

    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


-- Ordenes
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    customer_id BIGINT NOT NULL,

    employee_id BIGINT,

    status ENUM(
        'PENDING',
        'PREPARING',
        'READY',
        'DELIVERED',
        'CANCELLED'
    ) NOT NULL,

    total DECIMAL(10,2) NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY(customer_id) REFERENCES users(id),
    FOREIGN KEY(employee_id) REFERENCES users(id)
);

-- Ordenes Items
CREATE TABLE order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    order_id BIGINT NOT NULL,

    product_id BIGINT NOT NULL,

    size_id BIGINT,

    quantity INT NOT NULL,

    unit_price DECIMAL(10,2) NOT NULL,

    subtotal DECIMAL(10,2) NOT NULL,

    FOREIGN KEY(order_id) REFERENCES orders(id),

    FOREIGN KEY(product_id) REFERENCES products(id) ON DELETE CASCADE,

    FOREIGN KEY(size_id) REFERENCES sizes(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Metodos de pago
CREATE TABLE payment_methods (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    payment_method_name VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Pagos
CREATE TABLE payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    order_id BIGINT NOT NULL UNIQUE,

    payment_method_id BIGINT NOT NULL,

    amount DECIMAL(10,2) NOT NULL,

    payment_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY(order_id) REFERENCES orders(id),

    FOREIGN KEY(payment_method_id) REFERENCES payment_methods(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


INSERT INTO users (first_name, last_name, email, phone, active, rol) VALUES ('Admin', 'User', 'admin@coffeeshop.com', '123456789', TRUE, 'MANAGER');
INSERT INTO user_credentials (user_id, username, password) VALUES (LAST_INSERT_ID(), 'admin', SHA2('1111', 256));

INSERT INTO sizes (name, price_multiplier) VALUES
('Chico', 1.00),
('Mediano', 1.25),
('Grande', 1.50);

INSERT INTO categories (name, description, requires_size) VALUES
('Bebidas Calientes', 'Café y bebidas calientes clásicas.', TRUE),
('Bebidas Frías', 'Bebidas refrescantes y heladas.', TRUE),
('Pastelería', 'Acompañamientos dulces y horneados.', FALSE),
('Comidas/Sándwiches', 'Opciones saladas y sándwiches.', FALSE);

INSERT INTO products (category_id, name, description, base_price, available) VALUES
(1, 'Espresso', 'Café concentrado puro.', 1500.00, TRUE),
(1, 'Latte', 'Café espresso con leche vaporizada.', 2500.00, TRUE),
(1, 'Cappuccino', 'Partes iguales de espresso, leche y espuma.', 2800.00, TRUE),
(2, 'Iced Americano', 'Espresso con agua fría y hielo.', 1800.00, TRUE),
(2, 'Frappuccino', 'Café batido con hielo y crema.', 3500.00, TRUE),
(3, 'Medialunas', 'Clásicas medialunas de manteca.', 800.00, TRUE),
(3, 'Muffin de Arándanos', 'Muffin casero relleno de arándanos.', 1500.00, TRUE),
(4, 'Tostado de Jamón y Queso', 'Sándwich tostado clásico.', 3000.00, TRUE),
(4, 'Avocado Toast', 'Tostada de masa madre con palta y huevo.', 4500.00, TRUE);

-- ----------------------------------------------------
-- SEED DE DATOS: CLIENTES, ORDENES Y DETALLES (MEDIANO)
-- ----------------------------------------------------

INSERT INTO users (first_name, last_name, email, phone, active, rol) VALUES 
('Juan', 'Perez', 'juan.perez@cliente.com', '11111111', TRUE, 'CUSTOMER'),
('Maria', 'Gomez', 'maria.gomez@cliente.com', '22222222', TRUE, 'CUSTOMER'),
('Carlos', 'Lopez', 'carlos.lopez@cliente.com', '33333333', TRUE, 'CUSTOMER'),
('Ana', 'Martinez', 'ana.martinez@cliente.com', '44444444', TRUE, 'CUSTOMER');

-- O1
INSERT INTO orders (customer_id, employee_id, status, total) VALUES (2, 1, 'DELIVERED', 3925.00);
-- O2
INSERT INTO orders (customer_id, employee_id, status, total) VALUES (3, 1, 'DELIVERED', 4300.00);
-- O3
INSERT INTO orders (customer_id, employee_id, status, total) VALUES (4, 1, 'DELIVERED', 8000.00);
-- O4
INSERT INTO orders (customer_id, employee_id, status, total) VALUES (5, 1, 'PENDING', 2500.00);
-- O5
INSERT INTO orders (customer_id, employee_id, status, total) VALUES (2, 1, 'DELIVERED', 6050.00);
-- O6
INSERT INTO orders (customer_id, employee_id, status, total) VALUES (3, 1, 'READY', 7500.00);
-- O7
INSERT INTO orders (customer_id, employee_id, status, total) VALUES (4, 1, 'DELIVERED', 11400.00);
-- O8
INSERT INTO orders (customer_id, employee_id, status, total) VALUES (5, 1, 'DELIVERED', 4500.00);
-- O9
INSERT INTO orders (customer_id, employee_id, status, total) VALUES (2, 1, 'CANCELLED', 3000.00);
-- O10
INSERT INTO orders (customer_id, employee_id, status, total) VALUES (3, 1, 'DELIVERED', 6000.00);

-- Order Items
INSERT INTO order_items (order_id, product_id, size_id, quantity, unit_price, subtotal) VALUES
(1, 2, 2, 1, 3125.00, 3125.00), -- Latte Mediano
(1, 6, NULL, 1, 800.00, 800.00), -- Medialuna
(2, 1, 1, 1, 1500.00, 1500.00), -- Espresso Chico
(2, 3, 1, 1, 2800.00, 2800.00), -- Cappuccino Chico
(3, 5, 1, 1, 3500.00, 3500.00), -- Frappuccino Chico
(3, 9, NULL, 1, 4500.00, 4500.00), -- Avocado Toast
(4, 2, 1, 1, 2500.00, 2500.00), -- Latte Chico
(5, 4, 2, 1, 2250.00, 2250.00), -- Iced Americano Mediano
(5, 8, NULL, 1, 3000.00, 3000.00), -- Tostado
(5, 6, NULL, 1, 800.00, 800.00), -- Medialuna
(6, 9, NULL, 1, 4500.00, 4500.00), -- Avocado Toast
(6, 8, NULL, 1, 3000.00, 3000.00), -- Tostado
(7, 3, 3, 2, 4200.00, 8400.00), -- Cappuccino Grande x2
(7, 8, NULL, 1, 3000.00, 3000.00), -- Tostado
(8, 1, 1, 3, 1500.00, 4500.00), -- Espresso Chico x3
(9, 8, NULL, 1, 3000.00, 3000.00), -- Tostado
(10, 7, NULL, 4, 1500.00, 6000.00); -- Muffin x4