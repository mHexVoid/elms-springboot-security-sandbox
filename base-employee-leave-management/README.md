
# 📝 Employee Leave Management System

![Java](https://img.shields.io/badge/Java-17-blue?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.3-green?logo=spring-boot)
![Spring Security](https://img.shields.io/badge/Spring_Security-6.0+-green?logo=spring-security&link=https://spring.io/projects/spring-security)
![Maven](https://img.shields.io/badge/Maven-Build-blue?logo=apachemaven)


> 🏷️ Built with ❤️ under the **Hexvoid** banner for clean, secure, and modular backend architecture.

---

This is a Spring Boot-based **Employee Leave Management System** designed with best practices like layered architecture, loose coupling, and modular components.
 It handles leave requests from employees and includes role-based access using **Spring Security with In-Memory Authentication**.

The project **does not use Spring Data JPA repositories**. Instead, it uses **EntityManager** for full control over data operations.

---

## 📑 Table of Contents

- [🔍 Overview](#-overview)
- [⚙️ Tech Stack](#-tech-stack)
- [📂 Project Structure Overview](#-project-structure-overview)
- [🧱 Layered Architecture Breakdown](#-Layered-Architecture-Breakdown)
- [🔐 Spring Security Configuration](#-Spring-Security-In-Memory-Authentication)
- [🗃️ Entities](#-entities)
- [🚀 How to Run](#-how-to-run)
- [🔗 Access](#-access)
- [📚 References](#-references)

---

## 🔍 Overview

This project is designed to simulate a real-world **Employee Leave Management** workflow:

- Employees can apply for different types of leaves.
- Admins can view and manage requests.
- Role-based access control with `ADMIN` and `USER` roles.
- Manual data handling using **EntityManager** (No `JpaRepository`).

---

## ⚙️ Tech Stack

- Java 17
- Spring Boot
- Spring MVC
- Spring Security (In-Memory UserDetailsManager)
- EntityManager (No JpaRepository)
- H2 In-Memory Database
- Maven
- Swagger (SpringDoc for API documentation)

---

## 📂 Project Structure Overview

```plaintext
src/main/java/com.hexvoid.employeeportal

├── controller         → REST APIs (Employee & Leave)
├── service            → Business logic layer
├── dao                → Database interaction via EntityManager
├── entity             → Domain models (Employees, LeaveRequests, etc.)
├── exceptionhandler   → Global and custom exception handling
└── security           → Spring Security configuration (In-Memory auth)
```

---

## 🧱 Layered Architecture Breakdown

### 🔹 Controller Layer
- Exposes REST APIs for managing employees and leave requests.
- Delegates calls to the service layer — no business logic here.

### 🔹 Service Layer
- Contains all **business logic**.
- Interfaces like `EmployeeService` and `LeaveRequestService` ensure **loose coupling**.
- Implementations handle validation and call DAO methods.

### 🔹 DAO Layer
- Direct database interaction using **EntityManager**.
- Interfaces like `EmployeeDAO` and `LeaveRequestDAO` abstract the persistence logic.
- No Spring Data JPA — everything is managed manually with native/JPQL queries.

---

## 🔐 Spring Security: In-Memory Authentication

Security is configured using **in-memory user definitions** via `UserDetailsManager` in `SecurityConfig.java`.

### ✅ Features:
- No external DB or user service needed.
- Quick setup for role-based access.
- Easily extensible for custom roles like `ADMIN`, `USER`, etc.

```java
@Bean
public InMemoryUserDetailsManager userDetailsService() {
    UserDetails admin = User.withUsername("admin")
        .password(passwordEncoder().encode("admin123"))
        .roles("ADMIN")
        .build();

    UserDetails user = User.withUsername("user")
        .password(passwordEncoder().encode("user123"))
        .roles("USER")
        .build();

    return new InMemoryUserDetailsManager(admin, user);
}
```

---

## 🔄 Loose Coupling vs Tight Coupling

| Loose Coupling                             | Tight Coupling                            |
|-------------------------------------------|-------------------------------------------|
| Components use interfaces                  | Direct object-to-object interaction        |
| Easy to maintain, test, extend             | Difficult to scale or test independently  |
| Used in DAO ↔ Service ↔ Controller layers  | Avoided in this project                   |

This project strictly follows **loose coupling**, ensuring:
- `Controller → Service` (via interfaces)
- `Service → DAO` (via interfaces)

---

## 🗃️ Entities

- `Employees.java` : Stores basic employee info (ID, name, email, etc.)
- `LeaveRequests.java` : Represents leave records including type, duration, and status.
- `LeaveType.java` : Enum for types of leaves: SICK, VACATION, EMERGENCY, etc.
- `LeaveStatus.java` : Enum to track status: PENDING, APPROVED, REJECTED

All mapped to DB tables and used by EntityManager manually.

---

## 🚀 How to Run

1. Clone the repository
2. Configure DB settings in `application.properties`
3. Create DB tables manually or via SQL script
4. Run `EmployeeLeaveManagementSystemApplication.java`
5. Access APIs via Swagger or Postman
6. Use in-memory users to login

---

## 🔗 Access

- 🔸 **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- 🔸 **H2 Console**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

### 🧾 Credentials

- 🛢️ **JDBC URL**: `jdbc:h2:mem:testdb`  
- 👤 **Username**: `pola`  
- 🔑 **Password**: `pola`

---

## 📚 References

- 📘 [Spring Boot Docs](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- 🔐 [Spring Security In-Memory Auth](https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/in-memory.html)
- 🧾 [JPA EntityManager Guide (Baeldung)](https://www.baeldung.com/jpa-entitymanager)
- 🔗 [Loose Coupling Explained](https://stackify.com/loose-coupling/)

---

## 👨‍💻 Developed by Hexvoid Devs  
Built with clean architecture, clean code, and strong boundaries. 🚀  
**Feel free to fork, contribute, or raise issues.**

---

## 👤 Author

**Gaurav Mishra**  
- 🧑‍💻 GitHub: [@mHexVoid](https://github.com/mHexVoid)  
- 🌐 Project Repo: [ELMS - Spring Security Sandbox](https://github.com/mHexVoid/elms-springboot-security-sandbox.git)  
- 💼 LinkedIn: [Gaurav Mishra](https://www.linkedin.com/in/gaurav-mishra-401a8a149/)

---

<p align="center">
  🚀 Built with ❤️ by <strong><a href="https://github.com/mHexVoid">Hexvoid</a></strong> — Part of the ✨ <strong>Hexvoid Initiative</strong> ✨
</p>
