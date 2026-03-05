# Práctica 3 - API REST con Persistencia y Seguridad
### Programación de Aplicaciones Telemáticas - 3ºB GITT+BA
#### José Manuel Fernández Freige

En este documento se expone la memoria de la práctica 3 de la asignatura, detallando los pasos seguidos y resumiendo las nuevas funcionalidades implementadas respecto a la versión anterior de la API.

El objetivo principal de esta práctica ha sido evolucionar el servicio Web REST creado en la Práctica 2. Se ha sustituido el almacenamiento temporal en memoria por una **persistencia real en base de datos** utilizando Spring Data JPA y Hibernate. Además, se ha introducido un sistema de **Autenticación y Autorización** mediante Spring Security, y se ha refinado la arquitectura del software separando responsabilidades en capas (Controladores, Servicios y Repositorios).

El dominio del sistema también ha evolucionado para modelar un e-commerce más completo, incluyendo ahora la gestión de **Usuarios**, **Carritos** y **Líneas de Carrito** (productos dentro de un carrito).

## Arquitectura del Proyecto

Para esta iteración, se ha seguido estrictamente el patrón arquitectónico multicapa:

1.  **Capa de Presentación (Controladores):** Recibe las peticiones HTTP HTTP, valida la entrada y devuelve las respuestas JSON. Ej: `UsuarioControlador`, `CarritoControlador`.
2.  **Capa de Lógica de Negocio (Servicios):** Centraliza las reglas de negocio, cálculos (ej. coste total del carrito) y comprobaciones de seguridad. Ej: `ServicioUsuarios`, `ServicioCarritos`.
3.  **Capa de Acceso a Datos (Repositorios):** Interfaces de Spring Data JPA que abstraen las consultas a la base de datos. Ej: `RepoUsuario`, `RepoCarrito`, `RepoLineadeCarrito`.
4.  **Capa de Dominio (Entidades):** Clases Java mapeadas a tablas de la base de datos relacional. Ej: `Usuario`, `Carrito`, `LineadeCarrito`, `Rol`.

## Modelo de Datos (Entidades JPA)

La información se persiste en una base de datos relacional (H2) configurada para crearse automáticamente en cada arranque. Se han definido las siguientes entidades principales con sus respectivas relaciones:

* **Usuario:** Contiene credenciales (`email` único, `contraseña`) y está vinculado a un `Rol` (relación Many-To-One).
* **Rol:** Define los permisos del usuario en el sistema (`USER` o `ADMIN`).
* **Carrito:** Representa la cesta de la compra de un usuario específico (relación Many-To-One con Usuario). Calcula su `precioTotal` dinámicamente.
* **LineadeCarrito:** Representa un artículo añadido a un carrito. Contiene el `idArticulo`, `unidades`, `precioUnitario` y `costeLinea`. Pertenece a un único Carrito (relación Many-To-One).

### Validaciones y Restricciones (Bean Validation y JPA)

Se ha implementado una doble capa de validación para garantizar la integridad de los datos:
* **Validaciones API (@Valid):** Se rechazan peticiones mal formadas antes de procesarlas (ej. `@Email` para el correo, `@Size` para contraseñas, `@Positive` para unidades o precios).
* **Validaciones Base de Datos:** Se han definido restricciones a nivel de tabla mediante JPA (ej. `@Column(nullable = false, unique = true)` para evitar correos duplicados).

## Seguridad (Spring Security)

Se ha integrado **Spring Security** utilizando autenticación **Basic Auth** para proteger los recursos de la API. Las reglas de acceso implementadas son:

* **Rutas Públicas (`permitAll`):** * `/api/auth/register` (Registro de nuevos usuarios).
    * Rutas de error globales (`/error`).
* **Rutas Protegidas (Usuarios autenticados):** * La mayoría de los endpoints (crear carrito, añadir líneas, modificar perfil) requieren estar logueado. 
    * La lógica de negocio (`ServicioCarritos` y `ServicioUsuarios`) asegura que un usuario **solo pueda acceder o modificar sus propios recursos** (su perfil, sus carritos). Cualquier intento de acceder a un carrito ajeno devuelve un error `403 Forbidden`.
* **Rutas de Administración (`hasRole('ADMIN')`):** * Endpoints específicos, como obtener la lista de *todos* los usuarios o *todos* los carritos del sistema, están restringidos exclusivamente a usuarios con el rol `ADMIN`.

## Gestión Global de Errores (@RestControllerAdvice)

Se ha implementado un controlador global de excepciones (`ControladorGlobalErrores`) que intercepta cualquier fallo del sistema y devuelve una respuesta estructurada y uniforme al cliente en formato JSON (clase `ModeloError`). Este sistema captura:

1.  **ResponseStatusException:** Errores de lógica de negocio lanzados intencionadamente desde los servicios (ej. "El carrito no existe").
2.  **MethodArgumentNotValidException:** Errores de validación de los datos de entrada (ej. contraseña demasiado corta).
3.  **DataIntegrityViolationException:** Errores de la base de datos (ej. intento de registro con un email ya existente).
4.  **Excepciones Genéricas (500):** Captura fallos inesperados del servidor para evitar exponer la traza de la pila al cliente.

## Inicialización de Datos (data.sql)

Para facilitar las pruebas, el proyecto está configurado para ejecutar un script `data.sql` al arrancar. Este script puebla la base de datos recién creada con los roles básicos del sistema (`USER` y `ADMIN`), evitando la necesidad de crearlos manualmente antes de registrar el primer usuario.

## Endpoints Implementados

## Tabla Completa de Endpoints de la API

La API cuenta con los siguientes endpoints, protegidos mediante Spring Security y validados con `jakarta.validation`:

### Gestión de Usuarios

| Método | Ruta | Autorización | Cuerpo (Body) | Descripción | Códigos de Respuesta |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Pública | JSON Usuario | Registra un nuevo usuario en el sistema. Crea el rol si no existe. | `201`, `400`, `409` |
| `GET` | `/api/usuarios/me` | Autenticado | N/A | Obtiene los datos del perfil del usuario logueado. | `200`, `401`, `404` |
| `PUT` | `/api/usuarios/me` | Autenticado | JSON Usuario | Modifica el email y/o contraseña del usuario logueado. | `200`, `400`, `401`, `404` |
| `DELETE` | `/api/usuarios/me` | Autenticado | N/A | Elimina por completo la cuenta del usuario logueado. | `204`, `401`, `404` |
| `GET` | `/api/usuarios` | ADMIN | N/A | Obtiene la lista completa de todos los usuarios registrados. | `200`, `401`, `403` |

### Gestión de Carritos

| Método | Ruta | Autorización | Cuerpo (Body) | Descripción | Códigos de Respuesta |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `POST` | `/api/carritos` | Autenticado | JSON Carrito | Crea un nuevo carrito vacío asociado automáticamente al usuario logueado. | `201`, `400`, `401` |
| `GET` | `/api/carritos/{idCarrito}` | Propietario | N/A | Devuelve la información de un carrito específico si pertenece al usuario. | `200`, `401`, `403`, `404` |
| `PUT` | `/api/carritos/{idCarrito}` | Propietario | JSON Carrito | Actualiza la información a nivel de cabecera del carrito. | `200`, `400`, `401`, `403`, `404` |
| `DELETE` | `/api/carritos/{idCarrito}` | Propietario | N/A | Elimina un carrito y, en cascada, todas sus líneas asociadas. | `204`, `401`, `403`, `404` |
| `GET` | `/api/carritos` | ADMIN | N/A | Devuelve la lista de todos los carritos de la base de datos (para auditoría). | `200`, `401`, `403` |

### Gestión de Líneas de Carrito (Artículos)

| Método | Ruta | Autorización | Cuerpo (Body) | Descripción | Códigos de Respuesta |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `POST` | `/api/carritos/{id}/lineas` | Propietario | JSON LineadeCarrito | Añade un artículo al carrito. Si ya existe, suma las unidades y actualiza el coste total del carrito. | `201`, `400`, `401`, `403`, `404` |
| `DELETE` | `/api/carritos/{id}/lineas/{idLinea}`| Propietario | N/A | Borra una línea específica y resta su coste del total del carrito padre. | `204`, `401`, `403`, `404` |

> **Nota sobre Códigos de Error Frecuentes:**
> * `400 Bad Request`: JSON mal formado o datos que no superan las validaciones `@Valid` (ej: precio negativo).
> * `401 Unauthorized`: El usuario no ha enviado credenciales válidas en la petición.
> * `403 Forbidden`: El usuario logueado intenta acceder o modificar un carrito que pertenece a otro usuario, o no tiene permisos de `ADMIN`.
> * `404 Not Found`: El recurso solicitado (usuario, carrito o línea) no existe en la base de datos.
> * `409 Conflict`: Intento de registrar un usuario con un email que ya existe en la base de datos.
## Pruebas Realizadas

Se han realizado pruebas funcionales exhaustivas utilizando **Postman** (configurado con *Basic Auth*), verificando tanto los "caminos felices" (creación de usuarios, gestión de carritos completos) como los casos de error (intentos de acceso no autorizado, validaciones de formato, correos duplicados).
