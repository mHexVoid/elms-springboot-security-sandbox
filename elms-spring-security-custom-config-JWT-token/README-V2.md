
# 🚀 Employee Portal - Secure API with Spring Boot & JWT

![Java](https://img.shields.io/badge/Java-17-blue?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.3-brightgreen?logo=springboot)
![JWT](https://img.shields.io/badge/JWT-Security-orange?logo=jsonwebtokens)
![Maven](https://img.shields.io/badge/Maven-Build-blue?logo=apachemaven)

A secure RESTful Employee Management Portal built using Spring Boot and Spring Security with custom JWT authentication and filter chain. Ideal for microservice-based architectures, stateless communication, and secure role-based access control.

---

## 📋 Table of Contents
- [🧠 Project Highlights](#-project-highlights)
- [🗂️ Project Structure](#-project-structure)
- [🔑 Authentication Flow (JWT Based)](#-authentication-flow-jwt-based)
- [📦 Tech Stack](#-tech-stack)
- [✨ Features](#-features)
- [🔍 Key Endpoints](#-key-endpoints)
- [📥 API Request/Response Examples](#-api-requestresponse-examples)
- [🛡️ Security Highlights](#-security-highlights)
- [🧰 Custom Filters Used](#-custom-filters-used)
- [🎯 Special Methods Explained](#-special-methods-explained)
- [🧪 How to Run Locally](#-how-to-run-locally)
- [🧠 Want to Extend?](#-want-to-extend)
- [📝 References](#-references)
  
---

## 🧠 Project Highlights

- 🔐 **JWT-based Authentication**
- 🔁 **Stateless Session Management**
- 🧰 **Custom Spring Security Filters**
- 🔎 **User Registration and Retrieval**
- 🛡️ **Role-based Endpoint Access**
- ⚙️ **Extensible Security Config**

---

## 🗂️ Project Structure

<!-- ![Project Explorer Screenshot](./A_README_documentation_image_generated_in_markdown.png)
-->

```
com.hexvoid.employeeportal
├── controller
│   └── UserController.java
├── entity
│   └── EmployeeCredentials, EmployeeAuthorities
├── filter
│   └── JWTTokenGeneratorFilter.java
│   └── JWTTokenValidatorFilter.java
├── security
│   └── SpringSecurityConfig.java
└── service
    └── EmployeeSecretServiceImpl.java
```

---

## 🔑 Authentication Flow (JWT Based)
<!--
![JWT Flow Diagram](./A_README_document_presents_an_"Employee_Portal"_de.png)
-->
1. **Login/Register**
   - New user registers at `/register/user`.
   - On successful login, a **JWT token is generated** and sent back.

2. **Token Generation**
   - `JWTTokenGeneratorFilter` creates a signed JWT token.
   - It includes user email and authorities.

3. **Token Validation**
   - `JWTTokenValidatorFilter` extracts the token on every request.
   - It validates signature and parses claims.
   - Authenticated user is set in `SecurityContext`.

---

## 📦 Tech Stack

| Layer        | Technology                 |
|--------------|-----------------------------|
| Backend      | Spring Boot 3.x             |
| Security     | Spring Security + JWT       |
| Build Tool   | Maven                       |
| DB Support   | MySQL / H2 (configurable)   |
| Auth System  | Custom Filters + JWT        |

---

## ✨ Features

- ✅ Custom JWT Token Generator & Validator filters
- ✅ Fine-grained access control with roles (VIEW, UPDATE, DELETE)
- ✅ Secure registration with password encryption
- ✅ Swagger support ready for dev/testing
- ✅ Clean layered architecture (Controller, Service, Filter)

---

## 🔍 Key Endpoints

| Endpoint                          | Method | Access Level      | Description                                |
|----------------------------------|--------|-------------------|--------------------------------------------|
| `/register/user`                 | POST   | Public            | Register a new user                        |
| `/user/details`                  | GET    | Authenticated     | Get logged-in user details                 |
| `/registered/user/findByEmail`  | GET    | Public            | Search user by email                       |

---

## 📥 API Request/Response Examples

### 🔐 Register New User

**POST** `/register/user`

```json
{
  "email": "john.doe@example.com",
  "password": "strongPassword123",
  "employeeAuthorities": [
    { "role": "VIEW" },
    { "role": "UPDATE" }
  ]
}
```

---

### 📤 Get Current User Details

**GET** `/user/details`  
Headers:
```
Authorization: Bearer <JWT Token>
```

Response:
```json
{
  "id": 2,
  "email": "john.doe@example.com",
  "employeeAuthorities": [
    { "role": "VIEW" },
    { "role": "UPDATE" }
  ]
}
```

---

## 🛡️ Security Highlights

### ✅ `SpringSecurityConfig`
- Stateless session with granular endpoint security.
- Custom filters used:
  - `RequestValidationBeforeFilter`
  - `AuthoritiesLoggingAtFilter`
  - `AuthoritiesLoggingAfterFilter`
  - `JWTTokenGeneratorFilter`
  - `JWTTokenValidatorFilter`
- CSRF disabled (suitable for APIs).
- Fine-grained access rules with `.hasRole()` / `.hasAuthority()` matchers.

---

## 🧰 Custom Filters Used

| Filter Name                     | Position in Chain       | Purpose |
|---------------------------------|--------------------------|---------|
| `RequestValidationBeforeFilter` | **Before** BasicAuth     | Validate request headers or custom constraints |
| `AuthoritiesLoggingAtFilter`    | **At** BasicAuth         | Log authorities before auth |
| `AuthoritiesLoggingAfterFilter` | **After** BasicAuth      | Log authorities post-authentication |
| `JWTTokenGeneratorFilter`       | **After** BasicAuth      | Generate JWT on successful login |
| `JWTTokenValidatorFilter`       | **Before** BasicAuth     | Validate JWT on each request |

---

## 🎯 Special Methods Explained

```java
/**
 * 🔐 Securely fetches details of the currently authenticated user.
 *
 * ▶️ Usage:
 *    - Called after successful login via Basic Auth.
 *    - A JWT is generated and returned in the response.
 *    - On subsequent API calls, the JWT is passed in the Authorization header.
 *
 * ✅ This endpoint uses the JWT-authenticated context (Authentication object)
 *    to retrieve the logged-in user's data from the database.
 *
 * 🧾 Typically used to:
 *    - Load user info into frontend dashboards.
 *    - Maintain user session context client-side.
 */
@RequestMapping("/user/details")
public EmployeeCredentials getEmployeesDetailsAfterLogin(Authentication authentication) {
    EmployeeCredentials user = employeeSecretService.findByEmail(authentication.getName());
    return user != null ? user : null;
}
```


## 🧪 How to Run Locally

```bash
git clone https://github.com/mHexVoid/elms-springboot-security-sandbox.git
cd elms-springboot-security-sandbox
./mvnw spring-boot:run
```

Visit:
- Swagger UI: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## 🧠 Want to Extend?

- Add refresh token mechanism
- Use RSA Public/Private Key Pair for signing JWT
- Plug into a database (PostgreSQL)
- Add email verification step in registration

---

## 📝 References

- 📘 [Spring Security Docs](https://docs.spring.io/spring-security/site/docs/current/reference/html5/)
- 🚀 [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- 🔒 [JWT Authentication Tutorial](https://www.baeldung.com/spring-security/jwt)
- ⚙️ [Spring Security Custom Authentication](https://www.baeldung.com/spring-security/authentication-with-a-database)
- 🧪 [Spring Swagger (SpringDoc)](https://springdoc.org/)
- 🧰 [Spring Data JPA Guide](https://spring.io/projects/spring-data-jpa)

---

## 👨‍💻 Author

**Gaurav Mishra** 
- 🧑‍💻 GitHub: [@mHexVoid](https://github.com/mHexVoid)  
- 🌐 Project Repo: [ELMS - Spring Security Sandbox](https://github.com/mHexVoid/elms-springboot-security-sandbox.git)  
- 💼 LinkedIn: [Gaurav Mishra](https://www.linkedin.com/in/gaurav-mishra-401a8a149/)
---
> 💡 Feel free to fork, extend, or raise a PR. Collaboration welcome!
---
<p align="center">
  🚀 Built with ❤️ by <strong><a href="https://github.com/mHexVoid">Hexvoid</a></strong> — Part of the ✨ <strong>Hexvoid Initiative</strong> ✨
</p>
