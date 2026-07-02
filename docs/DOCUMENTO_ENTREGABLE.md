<div style="page-break-after: always;"></div>

# UNIVERSIDAD TECNOLÓGICA NACIONAL

### Carrera: Tecnicatura Universitaria en Programación a Distancia

### Materia: Programación 3

---
<br>
# FOOD STORE

### Sistema de comercio electrónico con API REST y frontend web

---

<br>

HILLAR SAUL

**Fecha de entrega:** 7 de Julio de 2026

---

<div style="page-break-after: always;"></div>

# Índice

> **Nota para la exportación a PDF:** Al convertir este documento a PDF (Word, Google Docs o Pandoc), activar la numeración automática de páginas y actualizar los números del índice según el resultado final.

| Sección | Página |
|---------|--------|
| 1. Introducción | 3 |
| 2. Marco Teórico | 4 |
| &nbsp;&nbsp;&nbsp;2.1. Tecnologías del backend | 4 |
| &nbsp;&nbsp;&nbsp;2.2. Tecnologías del frontend | 5 |
| &nbsp;&nbsp;&nbsp;2.3. Conceptos clave aplicados | 6 |
| 3. Decisiones Técnicas y Arquitectura | 8 |
| &nbsp;&nbsp;&nbsp;3.1. Visión general del sistema | 8 |
| &nbsp;&nbsp;&nbsp;3.2. Arquitectura del backend | 9 |
| &nbsp;&nbsp;&nbsp;3.3. Arquitectura del frontend | 11 |
| &nbsp;&nbsp;&nbsp;3.4. Modelo de datos | 12 |
| &nbsp;&nbsp;&nbsp;3.5. Interfaces principales | 14 |
| 4. Dificultades y Soluciones | 18 |
| 5. Bibliografía y Webgrafía | 20 |
| Anexo A — Endpoints de la API | 21 |

---

<div style="page-break-after: always;"></div>

# 1. Introducción

**Food Store** es una aplicación web de comercio electrónico desarrollada como Trabajo Práctico Integrador de la materia Programación 3 de la UTN. El sistema permite a los usuarios registrados explorar un catálogo de productos organizados por categorías, agregar artículos a un carrito de compras y generar pedidos. Por otro lado, los administradores disponen de un panel para gestionar usuarios, categorías, productos y el estado de los pedidos.

El proyecto se implementó bajo una arquitectura **cliente-servidor** desacoplada:

- **Backend:** API REST desarrollada con **Spring Boot** y **Java**, que expone los recursos del dominio y persiste la información en una base de datos **H2**.
- **Frontend:** Aplicación web multipágina construida con **Vite** y **TypeScript**, que consume la API mediante peticiones HTTP (`fetch`).

Esta separación permite que cada capa evolucione de forma independiente, facilita las pruebas unitarias del backend y constituye un patrón ampliamente adoptado en la industria del desarrollo de software.

### Objetivos del proyecto

1. Diseñar e implementar una API REST siguiendo buenas prácticas de arquitectura en capas.
2. Aplicar el patrón **DTO** para desacoplar el modelo de persistencia de la capa de presentación.
3. Desarrollar una interfaz de usuario funcional con distinción de roles (`ADMIN` y `USER`).
4. Integrar frontend y backend resolviendo desafíos de comunicación cross-origin (CORS).
5. Documentar el sistema de forma clara para su evaluación académica y futuro mantenimiento.

---

<div style="page-break-after: always;"></div>

# 2. Marco Teórico

## 2.1. Tecnologías del backend

### Java

Lenguaje de programación orientado a objetos, multiplataforma y ampliamente utilizado en entornos empresariales. En este proyecto se emplea **Java 17 o superior** como base del servidor de aplicaciones.

### Spring Boot

**Spring Boot** es un framework de Java que simplifica la creación de aplicaciones autónomas y listas para producción. Proporciona configuración automática (*auto-configuration*), un servidor embebido y un ecosistema de módulos (*starters*) que reducen el código repetitivo.

En Food Store se utilizan los siguientes módulos:

| Módulo | Propósito |
|--------|-----------|
| `spring-boot-starter-webmvc` | Exposición de endpoints REST |
| `spring-boot-starter-data-jpa` | Persistencia con JPA/Hibernate |
| `spring-boot-starter-validation` | Validación de datos de entrada |
| `spring-boot-devtools` | Recarga automática en desarrollo |

### Spring Data JPA y Hibernate

**JPA** (*Java Persistence API*) es la especificación estándar de Java para el mapeo objeto-relacional (ORM). **Hibernate** es su implementación de referencia. Mediante anotaciones como `@Entity`, `@OneToMany` y `@ManyToOne`, las clases Java se mapean a tablas de base de datos sin escribir SQL manualmente.

Los repositorios extienden `JpaRepository<Entity, Long>`, lo que provee operaciones CRUD (`save`, `findById`, `findAll`, `delete`) sin implementación adicional.

### H2 Database

**H2** es un motor de base de datos relacional escrito en Java. En este proyecto se utiliza en **modo en memoria** (`jdbc:h2:mem:foodstore`), lo que elimina la necesidad de instalar un servidor de base de datos externo durante el desarrollo y las demostraciones. Hibernate crea y actualiza el esquema automáticamente con `spring.jpa.hibernate.ddl-auto=update`.

### Lombok

Biblioteca que genera código repetitivo en tiempo de compilación mediante anotaciones como `@Getter`, `@Setter`, `@Builder` y `@RequiredArgsConstructor`, reduciendo el boilerplate en entidades, DTOs y servicios.

### SpringDoc OpenAPI

Genera documentación interactiva de la API REST accesible desde **Swagger UI** (`/swagger-ui.html`), permitiendo explorar y probar los endpoints sin herramientas externas.

---

## 2.2. Tecnologías del frontend

### Vite

**Vite** es una herramienta de build y servidor de desarrollo para aplicaciones web modernas. Ofrece arranque instantáneo del servidor de desarrollo y recarga en caliente (*Hot Module Replacement*). En Food Store se configuró como aplicación **multipágina** (MPA), con entradas HTML independientes para login, catálogo, panel admin, carrito, etc.

### TypeScript

Superset tipado de JavaScript que permite detectar errores en tiempo de compilación y mejorar la mantenibilidad del código. Se utiliza para definir interfaces (`IUser`, `Product`, `ICategory`) y tipar los servicios que consumen la API.

### Fetch API

API nativa del navegador para realizar peticiones HTTP. Cada módulo en `frontend/src/services/` encapsula las llamadas a un recurso REST del backend (`productService`, `orderService`, `categoryService`, etc.).

### localStorage

API de almacenamiento del navegador utilizada para:
- Persistir la sesión del usuario logueado.
- Mantener el carrito de compras entre recargas de página.

> **Importante:** El almacenamiento en `localStorage` es adecuado para prototipos educativos, pero **no constituye un mecanismo de seguridad** en producción, ya que los datos pueden ser modificados desde las herramientas de desarrollador del navegador.

---

## 2.3. Conceptos clave aplicados

### Arquitectura en capas

El backend sigue una organización en capas bien definida:

```
Controller  →  Service  →  Repository  →  Base de datos
   ↑              ↑
  DTO           Entity
```

Cada capa tiene una responsabilidad única: los **controladores** reciben peticiones HTTP, los **servicios** contienen la lógica de negocio, y los **repositorios** acceden a los datos.

### Patrón DTO (Data Transfer Object)

Los **DTOs** son objetos planos que transportan datos entre capas sin exponer la estructura interna de las entidades JPA. En el proyecto existen tres variantes por recurso:

| Variante | Uso |
|----------|-----|
| `*Create` | Datos necesarios para crear un recurso |
| `*Edit` | Datos para actualizar un recurso existente |
| `*Dto` | Datos devueltos al cliente (sin contraseña, por ejemplo) |

**Beneficios:** Ocultar campos sensibles (como `password`), evitar referencias circulares en JSON y desacoplar el contrato de la API del modelo de persistencia.

### Manejo global de excepciones

La clase `GlobalHandlerException` utiliza `@ControllerAdvice` para interceptar excepciones en todos los controladores y devolver respuestas HTTP uniformes mediante `ErrorDto`:

- `MethodArgumentNotValidException` → HTTP 400 con detalle de campos inválidos.
- `IllegalArgumentException` → HTTP 400 con mensaje descriptivo.
- `Exception` genérica → HTTP 500.

Esto evita respuestas inconsistentes y mejora la experiencia del consumidor de la API.

### Soft Delete (borrado lógico)

La clase base `Base` incluye un campo `deleted: Boolean`. En lugar de eliminar registros físicamente de la base de datos, se marca el registro como eliminado. Esto preserva la integridad referencial y permite auditoría futura.

### CORS (Cross-Origin Resource Sharing)

Dado que el frontend (`http://localhost:5173`) y el backend (`http://localhost:8080`) corren en orígenes distintos, los controladores declaran `@CrossOrigin(origins = "http://localhost:5173")` para autorizar peticiones del navegador.

### Roles y autorización

El enum `Rol` define dos perfiles:

| Rol | Permisos |
|-----|----------|
| `ADMIN` | Acceso al panel de administración (ABM de productos, categorías, usuarios y pedidos) |
| `USER` | Acceso al catálogo, carrito y pedidos propios |

La verificación de rol en el frontend se realiza con la función `checkAuhtUser()` en `auth.ts`. La seguridad real debería implementarse adicionalmente en el backend con Spring Security en un entorno productivo.

---

<div style="page-break-after: always;"></div>

# 3. Decisiones Técnicas y Arquitectura

## 3.1. Visión general del sistema

Food Store implementa una arquitectura **cliente-servidor REST** con persistencia relacional. El flujo principal de un pedido es el siguiente:

```
┌─────────────┐     HTTP/JSON      ┌─────────────┐     JPA/Hibernate     ┌──────────┐
│   Frontend  │ ◄───────────────► │   Backend   │ ◄──────────────────► │  H2 DB   │
│  (Vite+TS)  │   localhost:5173   │ (Spring Boot)│   localhost:8080     │ (memoria)│
└─────────────┘                    └─────────────┘                       └──────────┘
```

**Flujo de un pedido:**

1. El usuario navega el catálogo y agrega productos al carrito (almacenado en `localStorage`).
2. Desde el carrito, confirma la compra seleccionando un método de pago.
3. El frontend envía `POST /api/orders/user/{userId}` con los ítems del carrito.
4. El backend valida stock, descuenta unidades, crea la orden con estado `PENDING` y persiste los detalles.
5. El administrador puede consultar y actualizar el estado del pedido desde el panel admin.

---

## 3.2. Arquitectura del backend

### Estructura de paquetes

```
com.example.tp_integrador/
├── controllers/       # Capa REST — 5 controladores
│   ├── AuthController.java
│   ├── UserController.java
│   ├── CategoryController.java
│   ├── ProductController.java
│   └── OrderController.java
├── services/          # Interfaces de lógica de negocio
│   └── impl/          # Implementaciones concretas
├── repositories/      # Spring Data JPA
├── entities/          # Modelo de dominio JPA
├── dtos/              # Objetos de transferencia
├── enums/             # Rol, Status, Payment
└── exceptions/        # GlobalHandlerException
```

### Decisión: separación Service / ServiceImpl

Cada servicio expone una **interfaz** (`ProductService`, `OrderService`, etc.) con su implementación en el paquete `impl/`. Esto facilita:

- **Testabilidad:** Los tests unitarios pueden mockear la interfaz sin depender de la implementación concreta.
- **Extensibilidad:** Permite cambiar la implementación sin modificar los controladores.

### Decisión: validación en la capa de controlador

Los DTOs de entrada utilizan anotaciones de Jakarta Validation (`@NotBlank`, `@Email`, `@Min`, etc.) y el controlador los valida con `@Valid`. Los errores son capturados por `GlobalHandlerException` y devueltos como HTTP 400 con detalle por campo.

**Ejemplo — registro de usuario:**

```java
@PostMapping("/register")
public ResponseEntity<UserDto> register(@Valid @RequestBody RegisterRequestDTO dto) {
    UserDto userDto = authService.register(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
}
```

### Decisión: lógica de negocio en la entidad Order

La entidad `Order` implementa la interfaz `Calculable` y contiene métodos de dominio como `addDetail()`, `deleteDetailByProduct()` y `calcularTotal()`. Centralizar esta lógica en la entidad evita duplicación en el servicio y respeta el principio de **Rich Domain Model**.

### Decisión: control de stock en OrderServiceImpl

Al crear un pedido, el servicio:

1. Verifica que exista stock suficiente para cada producto.
2. Descuenta las unidades del stock del producto.
3. Agrega los detalles a la orden con el subtotal calculado.

Si el stock es insuficiente, lanza `IllegalArgumentException`, que el manejador global convierte en HTTP 400.

---

## 3.3. Arquitectura del frontend

### Estructura de carpetas

```
frontend/src/
├── pages/
│   ├── admin/          # Panel administrador
│   ├── auth/           # Login y registro
│   ├── client/         # Vistas del cliente
│   └── store/          # Catálogo, carrito, detalle
├── services/           # Capa de acceso a la API
├── types/              # Interfaces TypeScript
└── utils/              # auth.ts, localStorage.ts, navigate.ts
```

### Decisión: multipágina (MPA) en lugar de SPA

Se optó por una arquitectura **multipágina** con Vite en lugar de un Single Page Application (React/Vue router). Motivos:

- **Simplicidad educativa:** Cada pantalla es un par HTML + TypeScript independiente, más fácil de comprender para el nivel de la materia.
- **Protección de rutas explícita:** Cada página ejecuta `checkAuhtUser()` al cargar, verificando sesión y rol antes de renderizar contenido.

### Decisión: carrito en localStorage

El carrito no se persiste en el backend hasta que el usuario confirma la compra. Se almacena en `localStorage` mediante `cartService.ts`. Esto simplifica la experiencia de usuario (el carrito sobrevive recargas de página) sin requerir endpoints adicionales de "carrito temporal".

### Servicios HTTP

Cada recurso del backend tiene un servicio TypeScript dedicado que centraliza la URL base y las operaciones CRUD:

| Servicio | URL base |
|----------|----------|
| `loginService` / `registerService` | `/api/auth` |
| `userService` | `/api/users` |
| `categoryService` | `/api/categories` |
| `productService` | `/api/products` |
| `orderService` | `/api/orders` |

---

## 3.4. Modelo de datos

### Diagrama entidad-relación

```
┌──────────────┐       ┌──────────────┐       ┌──────────────┐
│    User      │       │    Order     │       │ OrderDetail  │
├──────────────┤       ├──────────────┤       ├──────────────┤
│ id           │──1:N─►│ id           │──1:N─►│ id           │
│ firstName    │       │ date         │       │ quantity     │
│ lastName     │       │ status       │       │ subtotal     │
│ email        │       │ payment      │       │ product_id   │──N:1──┐
│ phone        │       │ user_id      │       │ order_id     │       │
│ password     │       └──────────────┘       └──────────────┘       │
│ role         │                                                     │
└──────────────┘                                                     │
                                                                     ▼
┌──────────────┐       ┌──────────────┐                    ┌──────────────┐
│   Category   │       │   Product    │◄───────────────────│   Product    │
├──────────────┤       ├──────────────┤                    └──────────────┘
│ id           │──1:N─►│ id           │
│ name         │       │ name         │
│ description  │       │ price        │
└──────────────┘       │ description  │
                       │ stock        │
                       │ image        │
                       │ category_id  │
                       └──────────────┘
```

### Enumeraciones del dominio

| Enum | Valores | Uso |
|------|---------|-----|
| `Rol` | `ADMIN`, `USER` | Perfil de acceso del usuario |
| `Status` | `PENDING`, `CONFIRMED`, `COMPLETED`, `CANCELED` | Estado del ciclo de vida de un pedido |
| `Payment` | `CARD`, `TRANSFER`, `CASH` | Método de pago seleccionado |

### Clase base `Base`

Todas las entidades heredan de `Base`, que provee:

- `id` — Clave primaria autogenerada (`GenerationType.IDENTITY`).
- `deleted` — Flag de borrado lógico (default `false`).
- `createdAt` — Timestamp de creación (default `LocalDateTime.now()`).

---

## 3.5. Interfaces principales del sistema

> **Instrucción:** Reemplazar cada bloque `[CAPTURA]` con una captura de pantalla real del sistema en ejecución. Se recomienda resolución mínima de 1280×720 px y recortar barras del navegador innecesarias.

---

### 3.5.1. Pantalla de inicio de sesión

**Descripción:** Punto de entrada de la aplicación. El usuario ingresa email y contraseña; el frontend envía `POST /api/auth/login` y, según el rol recibido, redirige al catálogo (`USER`) o al panel admin (`ADMIN`).

**[CAPTURA 1 — Login]**

*Insertar captura de: `http://localhost:5173/src/pages/auth/login/login.html`*

---

### 3.5.2. Pantalla de registro

**Descripción:** Permite crear una cuenta nueva. El backend asigna automáticamente el rol `USER` y valida que el email no esté registrado previamente.

**[CAPTURA 2 — Registro]**

*Insertar captura de: `http://localhost:5173/src/pages/auth/registro/registro.html`*

---

### 3.5.3. Catálogo de productos (vista cliente)

**Descripción:** Muestra los productos disponibles en formato de tarjetas, con filtro por categoría. El usuario puede agregar productos al carrito. Si el usuario logueado es `ADMIN`, se muestra un enlace al panel de administración.

**[CAPTURA 3 — Catálogo / Store Home]**

*Insertar captura de: `http://localhost:5173/src/pages/store/home/home.html`*

---

### 3.5.4. Carrito de compras

**Descripción:** Lista los productos agregados con cantidades editables. Permite seleccionar método de pago y confirmar el pedido, enviando los ítems al backend.

**[CAPTURA 4 — Carrito]**

*Insertar captura de: `http://localhost:5173/src/pages/store/cart/cart.html`*

---

### 3.5.5. Panel de administración — Productos

**Descripción:** CRUD completo de productos. El administrador puede crear, editar y eliminar productos, asignándolos a una categoría existente.

**[CAPTURA 5 — Admin Productos]**

*Insertar captura de: `http://localhost:5173/src/pages/admin/products/admin-products.html`*

---

### 3.5.6. Panel de administración — Categorías

**Descripción:** ABM de categorías que organizan el catálogo de productos.

**[CAPTURA 6 — Admin Categorías]**

*Insertar captura de: `http://localhost:5173/src/pages/admin/categories/admin-categories.html`*

---

### 3.5.7. Panel de administración — Pedidos

**Descripción:** Listado de pedidos con posibilidad de actualizar su estado (`PENDING` → `CONFIRMED` → `COMPLETED` o `CANCELED`).

**[CAPTURA 7 — Admin Pedidos]**

*Insertar captura de: `http://localhost:5173/src/pages/admin/orders/admin-orders.html`*

---

### 3.5.8. Documentación Swagger UI

**Descripción:** Interfaz generada automáticamente por SpringDoc OpenAPI. Permite explorar y probar todos los endpoints de la API REST.

**[CAPTURA 8 — Swagger UI]**

*Insertar captura de: `http://localhost:8080/swagger-ui.html`*

---

<div style="page-break-after: always;"></div>

# 4. Dificultades y Soluciones

Durante los sprints de desarrollo del proyecto surgieron diversos obstáculos técnicos. A continuación se detallan los más relevantes y las estrategias adoptadas para resolverlos.

---

### 4.1. Comunicación cross-origin (CORS)

**Problema:** Al intentar consumir la API desde el frontend en `localhost:5173`, el navegador bloqueaba las peticiones con error de CORS, ya que el backend corría en `localhost:8080` (orígenes distintos).

**Solución:** Se agregó la anotación `@CrossOrigin(origins = "http://localhost:5173")` en cada controlador REST. Esto instruye al servidor a incluir los headers `Access-Control-Allow-Origin` necesarios en las respuestas.

**Aprendizaje:** CORS es una restricción de seguridad del navegador, no del servidor. En producción, la configuración debería centralizarse en una clase `WebMvcConfigurer` o mediante Spring Security.

---

### 4.2. Desacoplamiento entre entidades JPA y respuestas JSON

**Problema:** Exponer directamente las entidades JPA en los endpoints provocaba referencias circulares en la serialización JSON (por ejemplo, `User` → `Order` → `User`) y filtraba campos sensibles como la contraseña.

**Solución:** Se implementó el patrón **DTO** con clases dedicadas (`UserDto`, `ProductDto`, `OrderDto`, etc.) y métodos estáticos `toDto()` para la conversión. Los controladores nunca devuelven entidades directamente.

**Aprendizaje:** Los DTOs actúan como contrato estable de la API, independiente del modelo de persistencia.

---

### 4.3. Validación uniforme de errores

**Problema:** Sin un manejo centralizado, cada controlador devolvía errores en formatos distintos, dificultando el tratamiento en el frontend.

**Solución:** Se creó `GlobalHandlerException` con `@ControllerAdvice`, que intercepta excepciones de validación (`MethodArgumentNotValidException`), argumentos inválidos (`IllegalArgumentException`) y errores genéricos, devolviendo siempre un `ErrorDto` estructurado con timestamp, código HTTP, mensaje y lista de detalles.

**Aprendizaje:** Un manejador global de excepciones mejora la consistencia de la API y reduce código repetitivo en los controladores.

---

### 4.4. Control de stock al crear pedidos

**Problema:** Era necesario garantizar que no se vendieran más unidades de las disponibles en stock, especialmente en escenarios con múltiples ítems en un mismo pedido.

**Solución:** En `OrderServiceImpl.createOrder()`, antes de agregar cada detalle, se verifica `product.getStock() >= item.quantity()`. Si no hay stock suficiente, se lanza `IllegalArgumentException` y el pedido completo no se persiste. Si la validación pasa, se descuenta el stock inmediatamente.

**Aprendizaje:** La lógica de negocio crítica debe residir en el backend; el frontend solo refleja el estado, pero no es autoridad sobre el stock.

---

### 4.5. Protección de rutas solo en el cliente

**Problema:** Restringir el acceso a páginas admin desde el frontend usando `localStorage` es fácilmente evadible: un usuario puede modificar su rol en las herramientas de desarrollador.

**Solución adoptada (prototipo educativo):** Se implementó `checkAuhtUser()` que verifica sesión y rol al cargar cada página protegida, redirigiendo al login o al home correspondiente si no cumple los requisitos.

**Limitación reconocida:** Esta protección es **presentacional**, no de seguridad. En un entorno productivo se requeriría **Spring Security** con JWT o sesiones server-side, validando el rol en cada endpoint del backend.

**Aprendizaje:** La seguridad nunca debe depender exclusivamente del cliente. El frontend protege la experiencia de usuario; el backend protege los datos.

---

### 4.6. Sincronización del carrito con el backend

**Problema:** El carrito vive en `localStorage` del navegador, pero el pedido se persiste en el backend. Era necesario transformar la estructura local del carrito al formato `OrderDetailCreate` que espera la API.

**Solución:** Al confirmar la compra, el frontend mapea cada ítem del carrito a `{ productId, quantity }` y envía un `OrderCreateDto` con el método de pago y la lista de ítems. Tras una respuesta exitosa, se ejecuta `clearCart()` para vaciar el almacenamiento local.

**Aprendizaje:** Mantener estado local simplifica la UX, pero requiere una capa de transformación clara en el momento de persistir.

---

### 4.7. Configuración mínima de base de datos

**Problema:** El archivo `application.properties` inicial solo contenía el nombre de la aplicación, lo que dificultaba la comprensión de la configuración de H2 para evaluadores externos.

**Solución:** Se documentó y completó la configuración con URL JDBC, credenciales, dialecto Hibernate, consola H2 y ruta de Swagger UI, tanto en el archivo de propiedades como en el `README.md` del repositorio.

---

<div style="page-break-after: always;"></div>

# 5. Bibliografía y Webgrafía

## Documentación oficial

1. **Spring Boot Reference Documentation**  
   https://docs.spring.io/spring-boot/docs/current/reference/html/  
   Documentación oficial del framework utilizado para el backend.

2. **Spring Data JPA Reference**  
   https://docs.spring.io/spring-data/jpa/reference/  
   Referencia de repositorios, consultas y configuración JPA.

3. **Spring Framework — REST Clients / Web MVC**  
   https://docs.spring.io/spring-framework/reference/web/webmvc.html  
   Guía de desarrollo de aplicaciones web con Spring MVC.

4. **Jakarta Bean Validation Specification**  
   https://jakarta.ee/specifications/bean-validation/  
   Especificación de anotaciones de validación (`@NotBlank`, `@Email`, etc.).

5. **H2 Database Engine**  
   https://www.h2database.com/html/main.html  
   Documentación del motor de base de datos en memoria utilizado.

6. **Vite — Guide**  
   https://vite.dev/guide/  
   Documentación del bundler y servidor de desarrollo del frontend.

7. **TypeScript Handbook**  
   https://www.typescriptlang.org/docs/handbook/  
   Guía oficial del lenguaje TypeScript.

8. **SpringDoc OpenAPI**  
   https://springdoc.org/  
   Integración de OpenAPI 3 / Swagger UI con Spring Boot.

9. **MDN Web Docs — Fetch API**  
   https://developer.mozilla.org/es/docs/Web/API/Fetch_API  
   Referencia de la API de peticiones HTTP del navegador.

10. **MDN Web Docs — Window.localStorage**  
    https://developer.mozilla.org/es/docs/Web/API/Window/localStorage  
    Documentación del almacenamiento local del navegador.

## Recursos complementarios

11. **Lombok Project**  
    https://projectlombok.org/features/  
    Documentación de anotaciones Lombok utilizadas en entidades y servicios.

12. **Baeldung — Spring Boot Tutorials**  
    https://www.baeldung.com/spring-boot  
    Tutoriales prácticos sobre Spring Boot, JPA, validación y excepciones.

13. **Gradle User Manual**  
    https://docs.gradle.org/current/userguide/userguide.html  
    Guía del sistema de build utilizado en el backend.

14. **Repositorio del proyecto (GitHub)**  
    [Completar URL del repositorio]  
    Código fuente completo del frontend y backend.

---

<div style="page-break-after: always;"></div>

# Anexo A — Endpoints de la API REST

Base URL: `http://localhost:8080`

## Autenticación — `/api/auth`

| Método | Ruta | Descripción | Body |
|--------|------|-------------|------|
| POST | `/login` | Iniciar sesión | `{ email, password }` |
| POST | `/register` | Registrar usuario | `{ firstName, lastName, email, phone, password }` |

## Usuarios — `/api/users`

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/` | Listar todos los usuarios |
| GET | `/{id}` | Obtener usuario por ID |
| POST | `/` | Crear usuario |
| PUT | `/{id}` | Actualizar usuario |
| DELETE | `/{id}` | Eliminar usuario (soft delete) |

## Categorías — `/api/categories`

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/` | Listar categorías |
| GET | `/{id}` | Obtener categoría por ID |
| POST | `/` | Crear categoría |
| PUT | `/{id}` | Actualizar categoría |
| DELETE | `/{id}` | Eliminar categoría |

## Productos — `/api/products`

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/` | Listar productos |
| GET | `/{id}` | Obtener producto por ID |
| POST | `/` | Crear producto |
| PUT | `/{id}` | Actualizar producto |
| DELETE | `/{id}` | Eliminar producto |

## Pedidos — `/api/orders`

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/` | Listar pedidos |
| GET | `/{id}` | Obtener pedido por ID |
| POST | `/user/{userId}` | Crear pedido para un usuario |
| PUT | `/{id}` | Actualizar pedido (estado, pago) |
| DELETE | `/{id}` | Eliminar pedido (soft delete) |

---

*Fin del documento.*
