# ToDo: Replace Test Infrastructure with Testcontainers

This document outlines how to replace real infrastructure (PostgreSQL and RabbitMQ) with [Testcontainers](https://www.testcontainers.org/) to run isolated, reproducible integration tests.

## ✅ Objectives

- Replace local PostgreSQL with Testcontainers.
- Replace RabbitMQ with Testcontainers.
- Keep compatibility with RestAssured-based tests.
- Run all services in isolated containers at test runtime.

---

## 🧱 1. PostgreSQL with Testcontainers

### 📦 Add Maven dependencies
```xml
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>postgresql</artifactId>
    <version>1.19.0</version> <!-- Use latest compatible -->
    <scope>test</scope>
</dependency>
```

### 🛠️ Create PostgreSQL container
```java
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword("pass");

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
}
```

### 🧪 Inherit from BaseIntegrationTest
```java
class ProductControllerTest extends BaseIntegrationTest {
    // RestAssured tests
}
```

---

## 📡 2. RabbitMQ with Testcontainers

### 📦 Add Maven dependency
```xml
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>rabbitmq</artifactId>
    <version>1.19.0</version>
    <scope>test</scope>
</dependency>
```

### 🛠️ Create RabbitMQ container
```java
@Container
static RabbitMQContainer rabbitmq = new RabbitMQContainer("rabbitmq:3-management")
        .withExposedPorts(5672, 15672);

@DynamicPropertySource
static void configureRabbit(DynamicPropertyRegistry registry) {
    registry.add("spring.rabbitmq.host", rabbitmq::getHost);
    registry.add("spring.rabbitmq.port", () -> rabbitmq.getMappedPort(5672));
    registry.add("spring.rabbitmq.username", rabbitmq::getAdminUsername);
    registry.add("spring.rabbitmq.password", rabbitmq::getAdminPassword);
}
```

---

## 🧪 3. Compatibility with RestAssured

RestAssured-based tests will continue working normally, as long as:
- You keep using `@SpringBootTest(webEnvironment = RANDOM_PORT)`
- Any dependencies (like datasource or RabbitMQ) are configured through `@DynamicPropertySource`

Make sure to annotate all integration tests with:
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
```

And use the parent `BaseIntegrationTest` class that initializes the containers.

---

## 📝 Notes
- Testcontainers will spin up containers **only during test runtime**.
- You'll no longer need Docker Compose for test execution.
- Local Docker (or Podman with compatibility layers) must be running.
- Redis can be added later with `GenericContainer` if needed.

---

Let me know when your Podman setup supports Testcontainers and we’ll implement it juntos 💪

