# Explicación de los Patrones de Diseño Utilizados

Durante el desarrollo del sistema se utilizaron y adaptaron múltiples patrones de diseño (estructurales y de comportamiento) dictados por las mejores prácticas de la industria.

## 1. Patrón DAO (Data Access Object)
Todo el acceso a la base de datos MySQL está centralizado en clases específicas sufijadas con DAO (`UserDAO`, `ProductDAO`, etc.).
- **Polimorfismo e Interfaz:** Se creó una interfaz genérica `GetterDAO<K, E>` que exige la implementación de métodos base como `findAll()` y `findById(K key)`. Cada DAO concreto implementa esta interfaz parametrizando su clave primaria y su entidad, asegurando una firma uniforme para operaciones comunes.

## 2. Patrón DTO (Data Transfer Object)
Implementado ampliamente en el paquete `dtos` (ej. `UserDTO`, `OrderItemDTO`, `LoginDTO`, `CategoryTopProductDTO`).
- **¿Por qué se usó?** Evita exponer la estructura de los modelos de base de datos a la vista. En el módulo de estadísticas, un DTO es la única forma elegante de transportar una "fila" de datos que mezcla información calculada y concatenada de 4 tablas distintas, las cuales no encajan en ningún modelo puro.

## 3. Patrón Singleton
Utilizado en la clase `config/DatabaseConnection`.
- **Explicación:** Garantiza que la aplicación instancie una única conexión a la base de datos de forma global. Esto previene fugas de memoria y errores de "demasiadas conexiones concurrentes", optimizando el tiempo de respuesta del JDBC.
