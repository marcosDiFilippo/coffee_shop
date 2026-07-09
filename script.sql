CREATE DATABASE coffee_shop;
USE coffee_shop;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,

    email VARCHAR(150) UNIQUE,
    phone VARCHAR(30),

    active BOOLEAN NOT NULL DEFAULT TRUE,

    rol ENUM('CUSTOMER', 'EMPLOYEE', 'MANAGER', 'ADMIN') NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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
    price_multiplier DECIMAL(4,2) NOT NULL
);

-- Categorias
CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255)
);

-- Productos
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    category_id BIGINT NOT NULL,

    name VARCHAR(120) NOT NULL,
    description VARCHAR(255),

    base_price DECIMAL(10,2) NOT NULL,

    available BOOLEAN NOT NULL DEFAULT TRUE,

    FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- Ingredientes
CREATE TABLE ingredients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    name VARCHAR(120) NOT NULL UNIQUE,

    stock_quantity DECIMAL(10,2) NOT NULL,

    unit VARCHAR(20) NOT NULL,

    minimum_stock DECIMAL(10,2) NOT NULL,

    active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Product recipes
CREATE TABLE product_ingredients (
    product_id BIGINT NOT NULL,
    ingredient_id BIGINT NOT NULL,

    quantity DECIMAL(10,2) NOT NULL,

    PRIMARY KEY(product_id, ingredient_id),

    FOREIGN KEY(product_id) REFERENCES products(id),
    FOREIGN KEY(ingredient_id) REFERENCES ingredients(id)
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

    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

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

    FOREIGN KEY(size_id) REFERENCES sizes(id)
);

-- Metodos de pago
CREATE TABLE payment_methods (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    payment_method_name VARCHAR(50) NOT NULL UNIQUE
);

-- Pagos
CREATE TABLE payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    order_id BIGINT NOT NULL UNIQUE,

    payment_method_id BIGINT NOT NULL,

    amount DECIMAL(10,2) NOT NULL,

    payment_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY(order_id) REFERENCES orders(id),

    FOREIGN KEY(payment_method_id) REFERENCES payment_methods(id)
);

-- Stock movements
CREATE TABLE stock_movements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    ingredient_id BIGINT NOT NULL,

    movement_type ENUM(
        'PURCHASE',
        'SALE',
        'ADJUSTMENT'
    ) NOT NULL,

    quantity DECIMAL(10,2) NOT NULL,

    movement_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY(ingredient_id) REFERENCES ingredients(id)
);