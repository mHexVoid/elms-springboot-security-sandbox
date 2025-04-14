
# 🔐 🛡️ Employee Leave Management System (ELMS) – Spring Security + JWT (Custom Manual Auth)

![Java](https://img.shields.io/badge/Java-17-blue?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.3-brightgreen?logo=springboot)
![JWT](https://img.shields.io/badge/JWT-Security-orange?logo=jsonwebtokens)
![Maven](https://img.shields.io/badge/Maven-Build-blue?logo=apachemaven)


> 🏷️ Built with ❤️ under the **Hexvoid** banner for clean, secure, and modular backend architecture.

---

## 🧠 Project Overview

Welcome to the **Employee Leave Management System** – a backend system built using **Spring Boot**, **Spring Security**, and **JWT** with **custom manual authentication**.

This project is a secure, role-based portal for managing employee leave requests, tailored with custom login handling using JWT, without relying on Spring Security’s default login.

---

## 📚 Table of Contents

- [🎯 Purpose](#-purpose)
- [📁 Project Structure](#-project-structure)
- [🔐 Security and Authentication](#-security-and-authentication)
- [🧪 Custom Login Flow Diagram](#-Custom-Login-Flow)
- [🧠 Custom Manual Login Flow](#-custom-manual-login-flow)
- [🧩 Key Components](#-key-components)
- [✅ Record Classes Explained](#-record-classes-explained)
- [🪄 Beans and Config](#-beans-and-config)
- [📦 Technologies Used](#-technologies-used)
- [🚀 How to Run](#-how-to-run)
- [🧪 Swagger UI & API Testing](#-swagger-ui--api-testing)
- [📬 API Endpoints Summary](#-api-endpoints-summary)
- [📚 References](#-references)
- [🙌 Acknowledgements](#-acknowledgements)
- [👨‍💻 About Me](#-about-me)

---

## 🎯 Purpose

This project enables:
- Employees to **request**, **view**, **update**, or **cancel** leave requests.
- Admins/Managers to **approve/reject** requests.
- Role-based access using **JWT** and **Spring Security**.

---

## 📁 Project Structure

> Package: `com.hexvoid.employeeportal`

- **controller**: Handles REST APIs (e.g., `EmployeeController`, `LeaveRequestController`)
- **dao**: Interfaces for DB interaction.
- **entity**: JPA entities + enums + record classes.
- **events**: Event handling.
- **exceptionhandler**: Global exception management.
- **filter**: Custom filters like JWT processing.
- **security**: Core Spring Security logic + configs.
- **service**: Business logic.

---

## 🔐 Security and Authentication

This project **does not** rely on Spring Boot's default login page.

Instead, it uses:
- Custom manual authentication via `/api/login`.
- JWT-based stateless sessions.
- Role-based access using Spring Security with JWT and custom database-backed authentication.

### 🔁 JWT Flow:
1. User sends credentials to `/api/login`.
2. On success, receives a JWT token.
3. Token is used in headers for further API access.
4. JWT is validated via a filter on every request.

---

## 🧪 Custom Login Flow

### 🔁 Flow Diagram

```mermaid
sequenceDiagram
  participant Client
  participant LoginController
  participant AuthProvider
  participant JWTGenerator
  participant ProtectedAPI

  Client->>LoginController: POST /api/login (username, password)
  LoginController->>AuthProvider: Authenticate()
  AuthProvider-->>LoginController: Auth Success
  LoginController->>JWTGenerator: Generate Token
  JWTGenerator-->>Client: Return JWT
  Client->>ProtectedAPI: Request with JWT in Authorization
  ProtectedAPI->>JWTValidator: Validate Token
  JWTValidator-->>ProtectedAPI: Grant Access
  ```

---

## 🧠 Custom Manual Login Flow

**Endpoint**: `POST /api/login`

- Authenticates user using custom provider `EmployeeUserNamePwdAuthenticationProvider`.
- If credentials match, generates JWT and returns it in the `LoginResponse`.
- Skips Spring Security’s default login mechanism.

**Benefit**: Full control over how login works – ideal for APIs.

---

## 🧩 Key Components

- **`EmployeeCredentials`**: Stores user details.
- **`EmployeeAuthorities`**: Role-based permissions.
- **`LeaveRequests`**: Leave request data.
- **`EmployeeUserDetailsService`**: Integrates custom auth with Spring Security.
- **`EmployeeUserNamePwdAuthenticationProvider`**: Validates login manually.
- **`SecurityConfig`, `SpringSecurityConfig`**: Full Spring Security configurations.

---

## ✅ Record Classes Explained

We use **Java record classes** for request/response payloads to reduce boilerplate.

- **`LoginRequest`**:
  ```java
  public record LoginRequest(String username, String password) {}
  ```
  Used to capture login credentials.

- **`LoginResponse`**:
  ```java
  public record LoginResponse(String jwtToken, String username, List<String> roles) {}
  ```
  Returned to client after successful login.

These improve readability and maintainability.

---

## 🪄 Beans and Config

Manually declared beans in `SpringSecurityConfig.java`:

- `AuthenticationManager`
- `PasswordEncoder`
- `EmployeeUserDetailsService`

Each is annotated with `@Bean` for Spring to auto-inject them.

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

---

## 📦 Technologies Used

- Java 17
- Spring Boot 3.x
- Spring Security
- JWT (io.jsonwebtoken)
- Maven
- Postman (for testing)

---

## 🚀 How to Run

```bash
git clone https://github.com/mHexVoid/elms-springboot-security-sandbox.git
cd elms-springboot-security-sandbox
./mvnw spring-boot:run
```

---

## 🧪 Swagger UI & API Testing

> 📍 Base URL: `http://localhost:8080`

- 🧪 Swagger UI: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- 🛠️ Test APIs using Postman or Swagger

---

## 📬 API Endpoints Summary

### 🔑 Auth
- `POST /api/login`: Manual login, returns JWT

### 👥 Employee
- `POST /api/employees`
- `GET /api/employees`
- `GET /api/employees/{id}`
- `PUT /api/employees/{id}`
- `DELETE /api/employees/{id}`

### 📝 Leave Requests
- `POST /api/leaves`
- `GET /api/leaves`
- `GET /api/leaves/{id}`
- `PUT /api/leaves/{id}`
- `DELETE /api/leaves/{id}`

---

## 📚 References

- 📘 [Spring Boot Official Docs](https://spring.io/projects/spring-boot)
- 🔐 [Spring Security](https://spring.io/projects/spring-security)
- 🔑 [JWT.io](https://jwt.io/)
- 🧾 [JWT.IO Intro](https://jwt.io/introduction)
- 🧪 [OpenAPI Swagger Docs](https://swagger.io/tools/swagger-ui/)
- 🛠️ [Spring Boot + JWT Tutorial](https://www.baeldung.com/spring-security-oauth-jwt)

---

## 🙌 Acknowledgements

- Inspired by real-world security architecture and modular design principles.

---

## 👨‍💻 About Me

- 🧑‍💻 GitHub: [@mHexVoid](https://github.com/mHexVoid)
- 🌐 Project Repo: [elms-springboot-security-sandbox](https://github.com/mHexVoid/elms-springboot-security-sandbox.git)
- 💼 LinkedIn: [Gaurav Mishra](https://www.linkedin.com/in/gaurav-mishra-401a8a149/)

---

<p align="center">🚀 Built with ❤️ by <strong><a href="https://github.com/mHexVoid">Hexvoid</a></strong> — Part of the ✨ <strong>Hexvoid Initiative</strong> ✨</p>