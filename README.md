# Marked API

## Vision

This project is a backend API for a custom webshop. The system allows customers to browse standard products, design their own custom products (e.g., T-shirts by choosing colors and materials), manage a shopping cart, and complete purchases. Administrators can manage the product catalog, inventory, and order statuses.

---

## Links

https://marked.rozh.dk


Source code repository:  
https://github.com/Nali1234-lab/Marked

Portfolio website:  
https://nali1234-lab.github.io/portfolio-na26

---

# Architecture

## System Overview

This project is built as a layered backend architecture using:

- Controller layer (REST endpoints)
- Service layer (business logic)
- DAO layer (database access)

Technologies used:

- Java 25
- Javalin
- JPA / Hibernate
- PostgreSQL
- Maven
- JWT authentication
- BCrypt (password hashing)
- Testcontainers & JUnit 5

---

## Architecture Diagram

Client (Browser / api.http) → Javalin Middleware (JWT check) → Controllers → Services → DAOs → PostgreSQL Database

---

## Key Design Decisions

**Authentication**
Authentication is implemented using JWT tokens. Users log in and receive a token which must be included in subsequent API requests via the `Authorization: Bearer <token>` header.
Passwords are securely hashed using BCrypt directly in the `Customer` entity constructor to prevent plain-text storage.

**Polymorphism**
The `Product` entity uses JPA Inheritance (`JOINED`) to separate `StandardProduct` (fixed price) and `CustomProduct` (calculated price).
This allows the system to return a unified list of products while handling their logic independently.

**Error Handling**
Custom `ApiException` is mapped in `ApplicationConfig` to return consistent JSON error messages (e.g., 400 Bad Request, 404 Not Found) instead of generic 500 errors.



# Data Model

## ERD



## Important Entities

### Customer

Represents a registered user in the system (both customers and admins).

Fields:
- id
- name
- email
- password
- role (CUSTOMER, ADMIN)

### Product (Abstract)

Represents a catalog item.

Fields:
- id
- name
- category
- *StandardProduct:* price
- *CustomProduct:* laborCost, hiddenMargin

### Variant

Represents a specific option for a product (e.g., size, color, material).

Fields:
- id
- name
- price
- stock
- designSlot (COLOR, MATERIAL)

### ProductConfiguration

Represents a saved custom design by a customer.

Fields:
- id
- product (CustomProduct)
- selectedOptions (List of Variants)
- finalPrice
- quantity

### Order

Represents a completed purchase.

Fields:
- id
- orderDate
- status (PENDING, PAID, DONE)
- deliveryStreet, deliveryZipCode, deliveryCity
- customer


# User Stories

## Customer (Kunde)
- As a customer, I want to register an account so I can save my details and view my orders later.
- As a customer, I want to log in so I can access my account and cart.
- As a customer, I want to browse products by category so I can easily find what I'm looking for.
- As a customer, I want to see items on sale so I can find good prices.
- As a customer, I want to add a variant to my cart so I can gather items before paying.
- As a customer, I want to complete a payment so my order is created (with status PENDING).
- As a customer, I want to see the status of my order so I know if it's paid/shipped.

## Admin
- As an admin, I want to log in with an admin role so I get access to functions regular customers don't have.
- As an admin, I want to create a new category so the catalog can be expanded.
- As an admin, I want to add a new product to an existing category so new designs can be sold.
- As an admin, I want to change the price of a variant so prices can be updated without code changes.
- As an admin, I want to update the stock count of a variant so customers see correct availability.
- As an admin, I want to put a variant on sale (discounted price) so it can be marketed as a campaign.
- As an admin, I want to see all paid orders so I know which ones to pack and deliver to the post office.
- As an admin, I want to update an order's status (e.g., to "shipped") after I've delivered the package.


# Development Notes

During the development of this project, we encountered and solved several key technical challenges:

**Handling Authentication and Security**
Implementing JWT-based authentication required careful planning. We created a custom `JwtUtil` class to generate and verify tokens.
To secure the API, we implemented a middleware in `ApplicationConfig` that intercepts requests, verifies the token,
and checks the user's role before allowing access to protected endpoints.
To ensure security, we moved password hashing (BCrypt) directly into the `Customer` entity constructor,
making it impossible to accidentally save a plain-text password.

**Handling `LazyInitializationException`**
A major challenge was fetching carts and orders that contained lazy-loaded collections (like `CartItems` or `OrderItems`).
Hibernate threw `LazyInitializationException` because the collections were accessed after the `EntityManager` was closed.
We solved this by explicitly initializing the required relationships (e.g., calling `.size()` or fetching names)
inside the DAO layer *before* returning the entity to the service/controller.

**Structuring the Service and DAO Layers**
Separating the business logic into a `Service` layer (e.g., `OrderService`, `CartService`) and database access into a `DAO` layer kept our controllers clean.
This separation made it much easier to implement complex logic, such as calculating the total price of a custom design or moving items from a cart to an order, without cluttering the REST endpoints.

**Testing with Testcontainers**
For integration testing, we wanted to test the DAOs against a real PostgreSQL database without affecting our local development data.
We used **Testcontainers** to automatically spin up an isolated PostgreSQL Docker container for the test suite. This ensured our tests were reliable and independent of the local environment.