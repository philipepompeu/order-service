# Order Service

**Order Service** is a microservice developed for educational purposes, demonstrating how to design, build, and test a robust and scalable Java backend. The project is part of a larger microservices-based architecture and focuses on best practices, design patterns, and clean code principles such as SOLID.

## 🚀 Technologies Used

- **Spring Boot** – Java framework for rapid backend development.
- **Spring Data JPA** – ORM for seamless database interaction.
- **PostgreSQL** – Relational database, used via JPA.
- **Spring Security + JWT** – Stateless authentication with role-based authorization.
- **RabbitMQ + Spring Cloud Stream** – Asynchronous messaging for inter-service communication.
- **Spring Retry + @CircuitBreaker** – Adds fault tolerance and recovery mechanisms.
- **Redis** – Used for message persistence in case of messaging failure.
- **Testcontainers (WIP)** – Planned for future containerized integration tests.
- **RestAssured** – End-to-end testing of all RESTful endpoints.
- **Spring Cache** – Caching with Redis support.
- **Spring Actuator** – Application health and metrics exposure.
- **GitHub Actions** – CI pipeline with automated test execution on every commit.
- **Docker & Docker Compose** – Containers for PostgreSQL, RabbitMQ, Redis, and test infrastructure.

## ✨ Highlights & Patterns

- **Soft Delete Strategy**: Implemented globally using `@PreRemove`, `@SQLRestriction`, and `revokedAt` timestamp.
- **Base Entity**: Inherits common fields like `id`, `createdAt`, `updatedAt`, and `revokedAt`.
- **Observer Pattern**: For sales order events — messages are dispatched using observers rather than tight coupling.
- **Strategy Pattern**: Dynamic message creation based on the payment method.
- **Exception Handling**: Global exception handler using Spring’s `@ControllerAdvice`, enhanced with `ProblemDetail`.
- **Security Filters**: Custom JWT filter with full role-based access control.
- **Caching**: Selective use of `@Cacheable` with Redis for read-heavy queries (evaluated trade-offs for JPA).
- **Status Tracking**: `SalesOrder` maintains a full history of status changes with timestamps via `@ElementCollection`.

## 🔐 Authentication

The application uses JWT-based stateless authentication.

- Default user is created on application startup:
  - **Username**: `user`
  - **Password**: `123`
  - **Role**: `ROLE_USER`

Other roles like `ROLE_ADMIN` can be assigned as needed.

## 🧪 Test Coverage

All endpoints (`/products`, `/clients`, `/orders`) are tested using **RestAssured**, including:

- Creation, update, deletion (with soft delete)
- Pagination and filtering
- Token-based authentication
- Full end-to-end flow with RabbitMQ and Redis
- Role-based access control (`@PreAuthorize`)
- Business logic (e.g. total calculation for orders)

## 🔄 Message Recovery (RabbitMQ)

If RabbitMQ is temporarily unavailable:

- Messages are captured in **Redis**.
- A scheduled job (`@Scheduled`) retries dispatching them later.

This ensures asynchronous operations are not lost and adds resilience to the system.

## 📦 How to Run Locally

1. **Clone the repository**

```bash
git clone https://github.com/philipepompeu/order-service.git
cd order-service
```

2. **Start services with Docker Compose**

```bash
podman-compose up -d
# or
docker-compose up -d
```

3. **Run the application**

Use your preferred IDE or run:

```bash
./mvnw spring-boot:run
```

The application will start at `http://localhost:8080`.

## 🧪 Run Tests

```bash
./mvnw test
```

> All RestAssured tests will be executed, covering the entire API.

## 🗂️ Swagger UI

Available at:

```
http://localhost:8080/swagger-ui/index.html
```

## 📌 Future Enhancements

- [ ] Integrate TestContainers for Redis/RabbitMQ to isolate tests
- [ ] Implement the second microservice (e.g. `payment-service`) to consume the RabbitMQ queue
---

This project is a personal initiative for practicing backend architecture with Java & Spring, DevOps tools, and clean design principles.

