# Estructura de la Base de Datos (`coffee_shop`)

Este documento detalla la estructura relacional de la base de datos utilizada por el Sistema de Gestión de Cafetería. 

---

## 1. Tabla: `users`
**Descripción:** Almacena la información personal básica y el rol de todas las personas que interactúan con el negocio, ya sean clientes (`CUSTOMER`) o miembros del personal (`EMPLOYEE`, `MANAGER`).

| Columna | Tipo de Dato | Restricciones | Descripción |
| :--- | :--- | :--- | :--- |
| `id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Identificador único del usuario. |
| `first_name` | VARCHAR(100) | NOT NULL | Nombre del usuario. |
| `last_name` | VARCHAR(100) | NOT NULL | Apellido del usuario. |
| `email` | VARCHAR(150) | NOT NULL, UNIQUE | Correo electrónico de contacto. |
| `phone` | VARCHAR(30) | NOT NULL, UNIQUE | Número de teléfono (usado para búsquedas rápidas). |
| `active` | BOOLEAN | NOT NULL, DEFAULT TRUE | Indica si el usuario está habilitado en el sistema. |
| `rol` | ENUM | NOT NULL | Rol del usuario: 'CUSTOMER', 'EMPLOYEE' o 'MANAGER'. |
| `created_at` | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Fecha de registro. |
| `updated_at` | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP ON UPDATE | Fecha de última modificación. |

---

## 2. Tabla: `user_credentials`
**Descripción:** Tabla complementaria que almacena exclusivamente las credenciales de acceso (usuario y contraseña cifrada) para los usuarios que tienen permisos de inicio de sesión (empleados y gerentes).

| Columna | Tipo de Dato | Restricciones | Descripción |
| :--- | :--- | :--- | :--- |
| `user_id` | BIGINT | PRIMARY KEY, FK (users.id) | Referencia 1 a 1 con el usuario base. |
| `username` | VARCHAR(50) | NOT NULL, UNIQUE | Nombre de usuario para el login. |
| `password` | VARCHAR(255)| NOT NULL | Contraseña cifrada (ej. SHA2-256). |

---

## 3. Tabla: `sizes`
**Descripción:** Define los distintos tamaños disponibles para las bebidas (ej. Chico, Mediano, Grande) y el multiplicador que afectará el precio base del producto.

| Columna | Tipo de Dato | Restricciones | Descripción |
| :--- | :--- | :--- | :--- |
| `id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Identificador único del tamaño. |
| `name` | VARCHAR(20) | NOT NULL, UNIQUE | Nombre del tamaño (ej. 'Grande'). |
| `price_multiplier`| DECIMAL(4,2) | NOT NULL | Multiplicador de precio (ej. 1.50). |
| `active` | BOOLEAN | NOT NULL, DEFAULT TRUE | Indica si el tamaño se sigue ofreciendo. |
| `created_at` | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Fecha de creación. |
| `updated_at` | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP ON UPDATE | Fecha de última modificación. |

---

## 4. Tabla: `categories`
**Descripción:** Clasifica los productos del catálogo (ej. Bebidas Calientes, Pastelería) y determina si los productos dentro de ella requieren que el cliente elija un tamaño.

| Columna | Tipo de Dato | Restricciones | Descripción |
| :--- | :--- | :--- | :--- |
| `id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Identificador único de la categoría. |
| `name` | VARCHAR(100) | NOT NULL, UNIQUE | Nombre de la categoría. |
| `description` | VARCHAR(255) | | Breve descripción para la interfaz. |
| `requires_size` | BOOLEAN | NOT NULL, DEFAULT FALSE| Indica si al vender obliga a elegir un tamaño. |
| `active` | BOOLEAN | NOT NULL, DEFAULT TRUE | Indica si la categoría está disponible. |
| `created_at` | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Fecha de creación. |
| `updated_at` | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP ON UPDATE | Fecha de última modificación. |

---

## 5. Tabla: `products`
**Descripción:** Almacena el catálogo de productos disponibles para la venta. Cada producto pertenece obligatoriamente a una categoría y tiene un precio base.

| Columna | Tipo de Dato | Restricciones | Descripción |
| :--- | :--- | :--- | :--- |
| `id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Identificador único del producto. |
| `category_id` | BIGINT | NOT NULL, FK (categories.id)| Categoría a la que pertenece el producto. |
| `name` | VARCHAR(120) | NOT NULL | Nombre del producto. |
| `description` | VARCHAR(255) | | Descripción opcional del producto. |
| `base_price` | DECIMAL(10,2)| NOT NULL | Precio monetario base (sin multiplicar). |
| `available` | BOOLEAN | NOT NULL, DEFAULT TRUE | Indica si hay stock / está a la venta. |
| `created_at` | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Fecha de creación. |
| `updated_at` | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP ON UPDATE | Fecha de última modificación. |

---

## 6. Tabla: `orders`
**Descripción:** Es el núcleo transaccional. Registra el encabezado de un pedido, vinculando al cliente que compra, al empleado que atiende, el monto total y el estado actual del flujo.

| Columna | Tipo de Dato | Restricciones | Descripción |
| :--- | :--- | :--- | :--- |
| `id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Número o comprobante de la orden. |
| `customer_id` | BIGINT | NOT NULL, FK (users.id) | ID del cliente al que se le vende. |
| `employee_id` | BIGINT | FK (users.id) | ID del empleado que registró la orden. |
| `status` | ENUM | NOT NULL | Estado actual ('PENDING', 'PREPARING', 'READY', 'DELIVERED', 'CANCELLED'). |
| `total` | DECIMAL(10,2)| NOT NULL | Sumatoria monetaria de todos los ítems. |
| `created_at` | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Fecha y hora de registro de la venta. |
| `updated_at` | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP ON UPDATE | Fecha de última modificación. |

---

## 7. Tabla: `order_items`
**Descripción:** Almacena el detalle desglosado de una orden. Cada fila representa un producto específico (con su respectivo tamaño, cantidad y subtotal) dentro de un pedido mayor.

| Columna | Tipo de Dato | Restricciones | Descripción |
| :--- | :--- | :--- | :--- |
| `id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Identificador único del ítem. |
| `order_id` | BIGINT | NOT NULL, FK (orders.id) | Orden a la que pertenece este detalle. |
| `product_id` | BIGINT | NOT NULL, FK (products.id)| Producto específico comprado. |
| `size_id` | BIGINT | FK (sizes.id) | Tamaño seleccionado (NULL si no aplica). |
| `quantity` | INT | NOT NULL | Cantidad comprada de este ítem. |
| `unit_price` | DECIMAL(10,2)| NOT NULL | Precio unitario final cobrado en ese momento. |
| `subtotal` | DECIMAL(10,2)| NOT NULL | unit_price * quantity. |
| `created_at` | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Fecha de registro del ítem. |
| `updated_at` | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP ON UPDATE | Fecha de última modificación. |
