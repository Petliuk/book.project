# Online Bookstore

Welcome to the **Online Bookstore**, a robust and secure web application designed to manage a virtual bookstore. This project was inspired by the need to create a user-friendly platform for book enthusiasts to browse, search, and purchase books while providing administrators with tools to manage inventory and orders efficiently. Built with modern Java technologies, this application showcases a full-stack solution for e-commerce needs.

## Table of Contents
- [Project Overview](#project-overview)
- [Technologies Used](#technologies-used)
- [Features](#features)
- [Database Schema](#database-schema)
- [Controller Functionalities](#controller-functionalities)
- [Setup Instructions](#setup-instructions)
- [Postman Collection](#postman-collection)
- [Demo Video](#demo-video)
- [Challenges and Solutions](#challenges-and-solutions)
- [Contributing](#contributing)

## Project Overview
The Online Bookstore is a RESTful API that facilitates book browsing, user authentication, shopping cart management, and order processing. It solves the problem of providing a scalable and secure platform for users to explore books by categories, add them to a cart, and place orders, while administrators can manage books, categories, and order statuses. The application emphasizes clean code, security, and a seamless user experience.

## Technologies Used
- **Spring Boot**: Framework for building the application with minimal configuration.
- **Spring Security**: Implements JWT-based authentication and role-based access control (USER and ADMIN roles).
- **Spring Data JPA**: Simplifies database operations with Hibernate as the ORM.
- **MySQL/H2 Database**: MySQL for production and H2 for testing.
- **Swagger (OpenAPI)**: Provides API documentation and testing via a user-friendly UI.
- **Liquibase**: Manages database schema migrations.
- **MapStruct**: Automates mapping between DTOs and entities.
- **Lombok**: Reduces boilerplate code for models and services.
- **Maven**: Dependency management and build tool.
- **JJWT**: Handles JSON Web Token generation and validation.
- **JUnit & Mockito**: For unit and integration testing.
- **JaCoCo**: Generates code coverage reports.
- **Checkstyle**: Enforces coding standards.

## Features
- **User Authentication**: Secure registration and login with JWT.
- **Book Management**: CRUD operations for books (admin-only for create, update, delete).
- **Category Management**: Organize books by categories with CRUD operations (admin-only for create, update, delete).
- **Shopping Cart**: Add, update, and remove books from the cart.
- **Order Management**: Place orders, view order history, and update order statuses (admin-only).
- **Search Functionality**: Search books by title or author.
- **Pagination**: Efficient data retrieval for books, categories, and orders.
- **Soft Delete**: Ensures data integrity by marking records as deleted instead of physical deletion.

## Database Schema
The database schema for the Online Bookstore is designed to support the application's functionality. Below is a visual representation:


### Schema Description
- **users**: Stores user details (email, password, name, etc.) with a many-to-many relationship to roles.
- **roles**: Defines user roles (USER, ADMIN).
- **books**: Contains book information (title, author, ISBN, price, etc.) with a many-to-many relationship to categories.
- **categories**: Manages book categories with a soft delete feature.
- **shopping_carts**: Links to users and contains cart items.
- **cart_items**: Represents items in a shopping cart with quantity.
- **orders**: Stores order details with a one-to-many relationship to order items.
- **order_items**: Details individual items within an order.
- **users_roles** and **books_categories**: Junction tables for many-to-many relationships.

## Controller Functionalities
The application includes five main controllers, each handling specific functionalities:

### AuthController
- **POST /api/auth/registration**: Registers a new user and assigns the USER role.
- **POST /api/auth/login**: Authenticates a user and returns a JWT token.

### BookController
- **GET /api/books**: Retrieves a paginated list of all books (USER role).
- **GET /api/books/{id}**: Fetches a book by ID (USER role).
- **GET /api/books/search**: Searches books by title or author (USER role).
- **POST /api/books**: Creates a new book (ADMIN role).
- **PUT /api/books/{id}**: Updates an existing book (ADMIN role).
- **DELETE /api/books/{id}**: Deletes a book by ID (ADMIN role).

### CategoryController
- **GET /api/categories**: Retrieves a paginated list of all categories (USER role).
- **GET /api/categories/{id}**: Fetches a category by ID (USER role).
- **GET /api/categories/{id}/books**: Lists books by category ID (USER role).
- **POST /api/categories**: Creates a new category (ADMIN role).
- **PUT /api/categories/{id}**: Updates an existing category (ADMIN role).
- **DELETE /api/categories/{id}**: Deletes a category by ID (ADMIN role).

### OrderController
- **POST /api/orders**: Places an order from the user's shopping cart (USER role).
- **GET /api/orders**: Retrieves the user's order history (USER role).
- **PATCH /api/orders/{id}**: Updates an order's status (ADMIN role).
- **GET /api/orders/{orderId}/items**: Lists all items in an order (USER role).
- **GET /api/orders/{orderId}/{itemId}**: Retrieves a specific order item (USER role).

### ShoppingCartController
- **GET /api/cart**: Retrieves the user's shopping cart details (USER role).
- **POST /api/cart**: Adds a book to the shopping cart (USER role).
- **PUT /api/cart/cart-items/{cartItemId}**: Updates the quantity of a cart item (USER role).
- **DELETE /api/cart/cart-items/{cartItemId}**: Removes an item from the cart (USER role).