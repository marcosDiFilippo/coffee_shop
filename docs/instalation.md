# Instrucciones de Compilación y Ejecución

Sigue estos pasos para poner en marcha el sistema desde cero en un entorno local.

## Prerrequisitos del Entorno
- **Java Development Kit (JDK):** Versión 11 o superior.
- **Base de Datos:** MySQL Server versión 8.0 o superior.
- **IDE Recomendado:** Eclipse IDE.
- **Driver JDBC:** Archivo `.jar` del conector de MySQL (ej. `mysql-connector-j-x.x.xx.jar`). Ya debe estar referenciado en el Build Path / `.classpath` del proyecto.

## 1. Configuración de la Base de Datos
1. Inicia tu servidor MySQL (XAMPP, MySQL Workbench o CLI).
2. Localiza el archivo `script.sql` en el directorio raíz del proyecto.
3. Ejecuta el script completo en tu motor de base de datos. Este script se encargará de:
   - Crear la base de datos `coffee_shop`.
   - Generar las tablas con sus relaciones (llaves foráneas) y reglas `ON DELETE CASCADE`.
   - **Usuario administrador por defecto** (revisar el script para ver credenciales iniciales, usuario: `admin`, pass: `1111`).

## 2. Configuración de Credenciales de Conexión
Antes de compilar, es necesario indicar al programa cómo conectarse a tu MySQL local.
1. Abre el archivo: `src/config/DatabaseConnection.java`.
2. Busca las constantes de conexión:
   ```java
   private static final String URL = "jdbc:mysql://localhost:3306/coffee_shop_db";
   private static final String USER = "root"; // <-- Cambiar si tu user es otro
   private static final String PASSWORD = ""; // <-- Escribe aquí tu contraseña de MySQL
   ```
3. Guarda el archivo.

## 3. Compilación y Ejecución
**Si usas un IDE (Eclipse):**
1. Importa el proyecto como "Existing Java Project".
2. Localiza el paquete `views` dentro de `src/`.
3. Haz clic derecho sobre el archivo `Main.java` (o el que contenga el método `public static void main(String[] args)` inicial).
4. Selecciona **Run As > Java Application**.

**Si usas Terminal/Línea de Comandos:**
1. Navega hasta el directorio raíz del proyecto: `cd C:/ruta/al/proyecto/coffee_shop`
2. Compila los archivos (asegurándote de enlazar el conector JDBC):
   ```bash
   javac -cp ".;lib/mysql-connector-j-9.7.0.jar" src/**/*.java -d bin/
   ```
3. Ejecuta la clase principal:
   ```bash
   java -cp "bin;lib/mysql-connector-j-9.7.0.jar" views.Main
   ```

## 4. Uso del Sistema
- Al arrancar la interfaz gráfica, se mostrará la pantalla de Login.
- Utiliza las credenciales generadas por el `script.sql` (Perfil: MANAGER) para iniciar sesión.
- Una vez dentro del Dashboard, podrás acceder al menú lateral para cargar Categorías, Productos, Usuarios y gestionar Órdenes.
