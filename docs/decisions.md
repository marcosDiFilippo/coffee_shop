# Justificación de las Decisiones de Diseño

Como parte de la ingeniería del sistema, se tomaron decisiones arquitectónicas priorizando robustez, mantenibilidad y escalabilidad del código.

## 1. Arquitectura MVC + DTOs + Services + DAOs
El sistema no inyecta lógica de base de datos en las vistas ni manipula botones en el acceso a datos. Se dividió estrictamente en:
- **Capa de Presentación (Views):** Lo que ve el usuario, el diseño de la interfaz. Básicamente se encarga de mostrar y recibir los datos.
- **Capa de Controladores (Controllers):** Intermediarios que capturan eventos de la UI, interpretan excepciones y las muestran en pantalla.
- **Capa de Negocio (Services):** Se maneja la logica de negocio junto con validaciones y orquestación de transacciones antes de persistir los datos.
- **Capa de Acceso a Datos (DAOs):** Dedicada exclusivamente a la comunicación con MySQL. Ejecuta las sentencias SQL puras y aísla la persistencia del resto del sistema.
- **Capa de Modelos (Models):** Entidades que representan estructuralmente las tablas de la base de datos.
- **Capa de Transferencia (DTOs):** Objetos usados para transferir datos mandados desde la UI hacia los Controladores, o desde los Controladores hacia la UI.
- **Capa de Excepciones (Exceptions):** Clases de error de negocio propias diseñadas para ser lanzadas en los Servicios y capturadas en los Controladores.
- **Capa de Configuración (Config):** Maneja la instanciación única (Singleton) de la conexión a la base de datos JDBC.
- **Capa de Interfaces (Contracts):** Interface `GetterDAO<K, E>` haciendo uso de genericos para obtener una Colección de Entities y obtener mediante una Key la Entity de la base de datos.
- **Capa de Enums (Enums):** Utilizado para definir los estados de las entidades.

## 2. Abstracción y Herencia (`BaseModel`)
Se implementó una clase abstracta `BaseModel` de la cual heredan las entidades. 
* **Justificación:** Todas las tablas de la base de datos requieren campos de auditoría (`id`, `created_at`, `updated_at`). En lugar de repetir estos atributos en cada modelo, se aislaron en una superclase.

## 3. Manejo de Errores Profesional (Excepciones Propias)
En lugar de propagar genéricamente `SQLException` hasta la interfaz gráfica (lo cual es un riesgo de seguridad y mala experiencia de usuario), se diseñó un paquete de excepciones de negocio:
* `InvalidDataException`
* `TransactionFailedException`
* `ProductUnavailableException`

* **Justificación:** Permite a la capa de Negocio abortar operaciones de forma segura y explicativa, informando a los controladores la razón exacta del fallo.

## 4. Transacciones con JDBC Manual
Operaciones críticas como confirmar un pedido o dar de baja una categoría con relaciones involucran la modificación de múltiples tablas.
* **Justificación:** Se deshabilitó el auto-commit (`conn.setAutoCommit(false)`) y se implementó un manejo exhaustivo con `try-catch-finally`. Si la inserción del cliente funciona, pero falla el detalle de la orden, el bloque `catch (Exception e)` captura el error y ejecuta un `.rollback()`. Esto garantiza que la base de datos nunca quede inconsistente.

## 5. Diseño Híbrido en Consultas de Estadísticas
Para el cálculo de "Producto más vendido por categoría", se optó por un enfoque híbrido. 
* **Justificación:** En lugar de forzar a la base de datos a hacer subconsultas anidadas excesivamente complejas para encontrar el "Top N por grupo", se delegó el ordenamiento masivo (`ORDER BY`) a SQL y se aplicó un filtro asintóticamente rápido usando un `HashSet` en Java. Esto balancea la carga de procesamiento entre la BD y Java.
