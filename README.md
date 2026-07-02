# Food Store

Proyecto integrador final de **Programación 3 — UTN**.  
Aplicación web de comercio electrónico (e-commerce) con catálogo de productos, carrito de compras, gestión de pedidos y panel de administración.

El repositorio está organizado en dos módulos:

| Módulo    | Tecnología                         | Descripción                          |
|-----------|------------------------------------|--------------------------------------|
| `backend` | Spring Boot, Java, Gradle, H2      | API REST con persistencia JPA        |
| `frontend`| Vite, TypeScript, HTML             | Interfaz de usuario multi-página     |

---

## Requisitos previos

Antes de clonar y ejecutar el proyecto, verificá tener instalado:

| Herramienta | Versión recomendada | Verificación          |
|-------------|---------------------|-----------------------|
| **Java JDK**| 17 o superior       | `java -version`       |
| **Node.js** | 18 o superior       | `node -version`       |
| **pnpm**    | Última estable      | `pnpm -version`       |
| **Git**     | Cualquier versión    | `git --version`       |

> **Nota:** Si no tenés `pnpm`, podés instalarlo con `npm install -g pnpm` o usar `npm` en su lugar dentro de la carpeta `frontend`.

---

## Clonación del repositorio

```bash
git clone <URL_DEL_REPOSITORIO>
cd FoodStore
```

---

## Configuración de la base de datos

El backend utiliza **H2 Database** en modo **memoria** (`jdbc:h2:mem:foodstore`). No es necesario instalar un motor de base de datos externo: al iniciar la aplicación Spring Boot, Hibernate crea y actualiza las tablas automáticamente.

La configuración se encuentra en `backend/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:h2:mem:foodstore
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

### Consola H2

Con el backend en ejecución, podés inspeccionar la base de datos desde el navegador:

1. Abrí [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
2. Completá los datos de conexión:
   - **JDBC URL:** `jdbc:h2:mem:foodstore`
   - **User Name:** `sa`
   - **Password:** *(dejar vacío)*
3. Hacé clic en **Connect**

> Los datos se pierden al detener el servidor, ya que la base es en memoria. Para persistencia entre reinicios, se puede cambiar la URL a `jdbc:h2:file:./data/foodstore`.

---

## Instalación y ejecución

El proyecto requiere **dos terminales**: una para el backend y otra para el frontend.

### 1. Backend (API REST)

```bash
cd backend
chmod +x gradlew
./gradlew bootRun
```

En Windows:

```bash
cd backend
gradlew.bat bootRun
```

El servidor quedará disponible en **http://localhost:8080**.

#### Documentación interactiva de la API

SpringDoc OpenAPI expone la documentación Swagger en:

- **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

#### Ejecutar tests del backend

```bash
cd backend
./gradlew test
```

### 2. Frontend (interfaz de usuario)

```bash
cd frontend
pnpm install
pnpm dev
```

Con npm:

```bash
cd frontend
npm install
npm run dev
```

La aplicación frontend quedará disponible en **http://localhost:5173**.  
Al abrirla, se redirige automáticamente a la pantalla de login.

#### Compilar frontend para producción

```bash
cd frontend
pnpm build
pnpm preview
```

---

## Roles y funcionalidades

El sistema distingue dos roles de usuario:

| Rol     | Acceso                                                                 |
|---------|------------------------------------------------------------------------|
| `USER`  | Registro, login, catálogo de productos, carrito y pedidos propios      |
| `ADMIN` | Panel de administración: usuarios, categorías, productos y pedidos     |

### Pantallas principales

| Ruta (desarrollo)                              | Descripción                    |
|------------------------------------------------|--------------------------------|
| `/src/pages/auth/login/login.html`             | Inicio de sesión               |
| `/src/pages/auth/registro/registro.html`       | Registro de nuevos usuarios    |
| `/src/pages/store/home/home.html`              | Catálogo de productos (cliente)|
| `/src/pages/store/cart/cart.html`              | Carrito de compras             |
| `/src/pages/admin/home/admin-home.html`        | Panel principal del admin      |
| `/src/pages/admin/products/admin-products.html`| ABM de productos               |
| `/src/pages/admin/categories/admin-categories.html` | ABM de categorías         |
| `/src/pages/admin/orders/admin-orders.html`    | Gestión de pedidos             |

---

## Endpoints de la API

Base URL: `http://localhost:8080`

| Recurso      | Prefijo            | Operaciones principales                          |
|--------------|--------------------|--------------------------------------------------|
| Autenticación| `/api/auth`        | `POST /login`, `POST /register`                  |
| Usuarios     | `/api/users`       | CRUD completo                                    |
| Categorías   | `/api/categories`  | CRUD completo                                    |
| Productos    | `/api/products`    | CRUD completo                                    |
| Pedidos      | `/api/orders`      | CRUD + `POST /user/{userId}` para crear pedido   |

Todos los controladores habilitan CORS para `http://localhost:5173`, permitiendo la comunicación con el frontend en desarrollo.

---

## Arquitectura del proyecto

### Backend

```
backend/src/main/java/com/example/tp_integrador/
├── controllers/     # Capa REST (Auth, User, Category, Product, Order)
├── services/        # Lógica de negocio (interfaces + impl/)
├── repositories/    # Acceso a datos con Spring Data JPA
├── entities/        # Modelo de dominio (User, Product, Order, etc.)
├── dtos/            # Objetos de transferencia (Create, Edit, Dto)
├── enums/           # Rol, Status, Payment
└── exceptions/      # Manejo global de excepciones (@ControllerAdvice)
```

**Patrones y decisiones técnicas:**

- **DTOs:** Separación entre entidades JPA y datos expuestos/consumidos por la API.
- **Soft delete:** La entidad base (`Base`) incluye un campo `deleted` para borrado lógico.
- **Manejo global de excepciones:** `GlobalHandlerException` centraliza errores de validación y excepciones genéricas, devolviendo respuestas estructuradas con `ErrorDto`.
- **Validación:** Jakarta Bean Validation (`@Valid`) en los controladores.
- **Documentación:** SpringDoc OpenAPI para explorar y probar endpoints.

### Frontend

```
frontend/src/
├── pages/
│   ├── admin/       # Vistas del administrador
│   ├── auth/        # Login y registro
│   ├── client/      # Vistas del cliente
│   └── store/       # Catálogo, carrito y detalle de producto
├── services/        # Llamadas HTTP a la API REST
├── types/           # Interfaces TypeScript
└── utils/           # Autenticación, localStorage y navegación
```

**Protección de rutas (frontend):**  
La verificación de sesión y rol se realiza en el cliente mediante `localStorage` (`src/utils/auth.ts`). Este mecanismo es **educativo** y no reemplaza la seguridad del backend en un entorno productivo.

---

## Modelo de datos

```
User ──< Order ──< OrderDetail >── Product >── Category
```

| Entidad      | Campos relevantes                                      |
|--------------|--------------------------------------------------------|
| **User**     | firstName, lastName, email, phone, password, role      |
| **Category** | name, description                                      |
| **Product**  | name, price, description, stock, image, category       |
| **Order**    | date, status, payment, user, details                   |
| **OrderDetail** | quantity, subtotal, product                         |

**Enums:**

- `Rol`: `ADMIN`, `USER`
- `Status`: `PENDING`, `CONFIRMED`, `COMPLETED`, `CANCELED`
- `Payment`: `CARD`, `TRANSFER`, `CASH`

---

## Stack tecnológico

| Capa       | Tecnología                                              |
|------------|---------------------------------------------------------|
| Backend    | Java, Spring Boot 4.x, Spring Data JPA, Spring Web MVC|
| Base de datos | H2 Database (en memoria)                           |
| Build tool | Gradle 8.x                                              |
| Frontend   | Vite 7.x, TypeScript 5.x, HTML                        |
| API Docs   | SpringDoc OpenAPI (Swagger UI)                          |
| Utilidades | Lombok, Jakarta Validation                              |

---

## Solución de problemas

| Problema | Posible causa | Solución |
|----------|---------------|----------|
| El frontend no carga datos | Backend detenido | Verificá que `bootRun` esté activo en el puerto 8080 |
| Error CORS | Origen distinto a `:5173` | El frontend debe correr en `http://localhost:5173` |
| `JAVA_HOME is not set` | JDK no configurado | Instalá JDK 17+ y configurá la variable de entorno |
| Puerto 8080 ocupado | Otra app usa el puerto | Cambiá `server.port` en `application.properties` |

---

## Equipo / Institución

- **Institución:** Universidad Tecnológica Nacional (UTN)
- **Materia:** Programación 3
- **Proyecto:** Food Store — Trabajo Práctico Integrador

---

## Licencia

Proyecto académico con fines educativos.
