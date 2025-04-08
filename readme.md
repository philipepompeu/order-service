# Order Service

**Order Service** is a microservice developed for educational purposes, demonstrating the implementation of a scalable and secure order management system. The project leverages modern technologies and best practices to showcase proficiency in building robust backend services.

## Technologies Used

- **Spring Boot**: Framework for building Java-based applications.
- **Spring Data JPA**: Simplifies database interactions using Java Persistence API.
- **Spring Security with JWT**: Implements authentication and authorization using JSON Web Tokens.
- **Spring Cloud Stream with RabbitMQ**: Facilitates message-driven microservices architecture.
- **Spring Retry with @CircuitBreaker**: Provides resilience and fault tolerance.
- **RestAssured**: Enables automated testing of RESTful APIs.
- **GitHub Actions**: Automates workflows, including testing and deployment.
- **Docker & Docker Compose**: Containerizes the application for consistent deployment.

## Features

- **Secure Authentication**: Utilizes JWT for stateless authentication.
- **Message Queuing**: Sends messages to RabbitMQ for asynchronous processing by other microservices.
- **Resilience**: Implements circuit breaker pattern to handle failures gracefully.
- **Automated Testing**: Covers all endpoints with RestAssured tests.
- **Continuous Integration**: Integrates with GitHub Actions to run tests on each commit.
- **Containerization**: Provides a Docker image for easy deployment.
