CREATE DATABASE coffee_shop;
USE coffee_shop;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,

    email VARCHAR(150) NOT NULL UNIQUE,
    phone VARCHAR(30) NOT NULL UNIQUE,

    active BOOLEAN NOT NULL DEFAULT TRUE,

    rol ENUM('CUSTOMER', 'EMPLOYEE', 'MANAGER', 'ADMIN') NOT NULL,

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

    FOREIGN KEY (category_id) REFERENCES categories(id),
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

    FOREIGN KEY(product_id) REFERENCES products(id),

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
INSERT INTO user_credentials (user_id, username, password) VALUES (LAST_INSERT_ID(), 'admin', '1111');

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