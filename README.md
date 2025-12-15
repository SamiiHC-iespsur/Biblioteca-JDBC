# Biblioteca-JDBC

## Realizado por Samii de las Heras Cózar - IES Polígono Sur; 2º DAM

### Tecnologías utilizadas
- Java
- MySQL
- JDBC API
- Maven

### Patrones de diseño utilizados
- DAO (Data Access Object)
- MVC (Model-View-Controller)

### Descripción del proyecto
Este proyecto es un sistema de gestión de biblioteca desarrollado en Java que utiliza JDBC para interactuar con una base de datos MySQL. Implementa patrones de diseño como DAO y MVC para organizar el código y facilitar el mantenimiento.

### Estructura del proyecto
- `model`: Contiene las clases que representan las entidades de la base de datos (por ejemplo, Libro, Usuario).
    - `dao`: Contiene las interfaces y clases DAO que manejan las operaciones CRUD en la base de datos.
- `controller`: Contiene las clases que gestionan la lógica de negocio y la interacción entre el modelo y la vista.
- `vista`: Contiene las clases relacionadas con la interfaz de usuario.
    - `imgs`: Contiene las imágenes utilizadas en la interfaz de usuario.
- `principal`: Contiene la clase principal que inicia la aplicación.

### Cómo desplegar la aplicación
0. Asegúrate de tener instalado Java SE y MySQL en tu sistema.
1. Clona este repositorio en tu máquina local.
2. Configura la base de datos MySQL utilizando el script SQL proporcionado en la carpeta `database`.
3. Actualiza las credenciales de la base de datos en la clase `Conexion` ubicada en el paquete `model.dao`.
4. Compila y ejecuta la aplicación desde tu entorno de desarrollo o utilizando Maven.
