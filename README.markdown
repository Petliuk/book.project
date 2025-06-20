# 📚 Online Bookstore

Welcome to the  **Online Bookstore** — a simple and secure web app made for anyone who loves books.
This project was built to make it easy to browse, find, and buy books online, while giving admins the tools they need to manage everything smoothly — from updating inventory to tracking orders. It's designed to be helpful, reliable, and user-friendly for both readers and store managers.

---

## 🔎 What’s Inside

This documentation includes:
- a high-level overview of the project
- key technologies and libraries used
- main features and API capabilities
- a visual database schema and entity relationships
- detailed controller functionalities with endpoints
- setup and configuration instructions
- how to run and test the API locally

---

## Project Overview

The **Online Bookstore** is a RESTful API backend application that provides a secure and scalable solution for:
- 📖 Browsing and searching books by title, author, or category
- 🛒 Managing shopping carts and placing orders
- 🔐 Authenticating users and authorizing roles (USER & ADMIN)
- 🧰 Administering inventory, categories, and order statuses

---

## Technologies Used

| Technology | Description |
|-----------|-------------|
| ☕ Java 21 | Main programming language |
| 🚀 Spring Boot 3.4.0 | Core framework for rapid development |
| 🔐 Spring Security | JWT-based authentication and role-based access |
| 🗄️ Spring Data JPA | ORM with Hibernate |
| 🐬 MySQL / H2 | MySQL (prod), H2 (test) |
| 🌐 Swagger (OpenAPI) | API documentation and UI |
| 🧱 Liquibase | Database schema migrations |
| 🔄 MapStruct | DTO to entity mapping |
| 🧹 Lombok | Reduces boilerplate |
| ⚙️ Maven | Dependency and build management |
| 🔑 JJWT 0.11.5 | JWT handling |
| 🧪 JUnit & Mockito | Unit & integration testing |
| 📊 JaCoCo | Code coverage |
| 🧼 Checkstyle | Code style checks |
| ✅ Validation API | Data validation annotations |
| 🐳 Docker | Containerization and deployment |

---

## Features

- 🔐 **JWT Authentication** — Secure user registration and login
- 📘 **Book Management** — CRUD operations (admin only)
- 🏷️ **Category Management** — Create and organize book categories
- 🛒 **Shopping Cart** — Add/update/remove books from cart
- 📦 **Order Management** — Place/view orders and update status
- 🔍 **Search Books** — By title or author
- 📄 **Pagination** — On books, categories, and orders
- 🧽 **Soft Delete** — Logical deletion of records

---

## Database Schema

![Database Schema](diagram.png)

### Entities Overview

- **users** ↔ **roles** — many-to-many via `users_roles`
- **books** ↔ **categories** — many-to-many via `books_categories`
- **shopping_carts** — one-to-one with users
- **cart_items** — one-to-many with shopping carts
- **orders** — one-to-many with order items
- **order_items** — contains book reference and quantity

---

## Controller Functionalities

### 🔑 AuthController
- `POST /api/auth/registration` — Register a new user
- `POST /api/auth/login` — Authenticate user and obtain JWT token

### 📘 BookController
- `GET /api/books` — Get a paginated list of all books
- `GET /api/books/{id}` — Get details of a book by ID
- `GET /api/books/search` — Search books by title or author
- `POST /api/books` — Create a new book (Admin only)
- `PUT /api/books/{id}` — Update a book (Admin only)
- `DELETE /api/books/{id}` — Delete a book (Admin only)

### 🏷️ CategoryController
- `GET /api/categories` — Get a paginated list of categories
- `GET /api/categories/{id}` — Get category details by ID
- `GET /api/categories/{id}/books` — List books in a category
- `POST /api/categories` — Create a new category (Admin only)
- `PUT /api/categories/{id}` — Update a category (Admin only)
- `DELETE /api/categories/{id}` — Delete a category (Admin only)

### 📦 OrderController
- `POST /api/orders` — Place an order from the shopping cart
- `GET /api/orders` — View order history
- `PATCH /api/orders/{id}` — Update order status (Admin only)
- `GET /api/orders/{orderId}/items` — List all items in an order
- `GET /api/orders/{orderId}/{itemId}` — Get specific order item details

### 🛒 ShoppingCartController
- `GET /api/cart` — Get current shopping cart details
- `POST /api/cart` — Add a book to the cart
- `PUT /api/cart/cart-items/{cartItemId}` — Update quantity of a cart item
- `DELETE /api/cart/cart-items/{cartItemId}` — Remove an item from the cart

---

## Setup Instructions

### ✅ Prerequisites

- Java 21
- Maven 3.8+
- MySQL (for production) or H2 (for testing)
- Postman (optional)

### 📥 Clone the Repository

```bash
git clone <https://github.com/Petliuk/book.project.git>
```

### ⚙️ Configure the Database

#### For MySQL:

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/db?serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
```

#### For H2:

Use default in-memory settings (no changes needed).

### ▶️ Build and Run

```bash
mvn clean install
mvn spring-boot:run
```
---

## 🐳 Running with Docker

1. Copy `.env.template` to `.env` and update variables:
   Example `.env`:
   ```env
   MYSQLDB_ROOT_PASSWORD=your_root_password
   MYSQLDB_USERNAME=your_username
   MYSQLDB_DATABASE=your_database_name
   MYSQLDB_LOCAL_PORT=3307
   MYSQLDB_DOCKER_PORT=3306
   APP_LOCAL_PORT=8081
   APP_DOCKER_PORT=8080
   SERVER_CONTEXT_PATH=/api
   SPRING_PROFILES_ACTIVE=prod
   ```
2. Build and start containers:
   ```bash
   docker-compose up --build
   ```
   For detached mode:
   ```bash
   docker-compose up -d --build
   ```
3. Access services:
    - **Application**: `http://localhost:8081/api`
    - **Swagger UI**: `http://localhost:8081/api/swagger-ui/index.html`
    - **MySQL**:
        - Host: `localhost`
        - Port: `3307`
        - User: `your_username`
        - Password: `your_root_password`
        - Database: `your_database_name`

4. Stop containers:
   ```bash
   docker-compose down
   ```
### 🔌 Essential Docker Commands
| Command | Description |
| --- | --- |
| `docker-compose up --build` | Build and start containers |
| `docker-compose down` | Stop and remove containers |
| `docker-compose logs -f app` | View app logs |
| `docker-compose ps` | Check container status |

### ℹ️ Notes
- First run may take 3-5 minutes due to image downloads and database initialization (via Liquibase).
---
## 🧪 Test the API

### 📡 API Access
- **Endpoint**: `http://localhost:8081/api` (Docker) or `http://localhost:8080/api` (local)
- **Swagger UI**: `http://localhost:8081/api/swagger-ui/index.html` (Docker) or `http://localhost:8080/api/swagger-ui/index.html` (local)

### 🧪 Sample Requests

#### ✅ Register a new user

```http
POST /api/auth/registration
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123",
  "repeatPassword": "password123",
  "firstName": "user",
  "lastName": "user"
}
```

#### 🔐 Login to receive JWT

```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}
```

Use the returned token as:

```http
Authorization: Bearer <JWT_TOKEN>
```
### 📸 Swagger UI Screenshots
Below are screenshots of the Swagger UI showcasing key API endpoints:

#### Authentication APIs
![Auth Screenshot](auth_screenshot.png)

#### Book Management APIs
![Book Screenshot](book_screenshot.png)

#### Category Management APIs
![Category Screenshot](category_screenshot.png)

#### Order Management APIs
![Order Screenshot](order_screenshot.png)

#### Shopping Cart APIs
![Cart Screenshot](cart_screenshot.png)