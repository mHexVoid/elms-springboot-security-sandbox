# 🔐 ELMS - Spring Security with Custom UserDetailsService


![Java](https://img.shields.io/badge/Java-17-blue?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.3-green?logo=spring-boot)
![Spring Security](https://img.shields.io/badge/Spring_Security-6.0+-green?logo=spring-security&link=https://spring.io/projects/spring-security)
![Maven](https://img.shields.io/badge/Maven-Build-blue?logo=apachemaven)

> 🏷️ Built with ❤️ under the **Hexvoid** banner for clean, secure, and modular backend architecture.

---

This is an advanced Spring Boot–based **Employee Leave Management System (ELMS)** built by **Gaurav Mishra**.  
It uses **custom Spring Security configuration**, overriding the default behavior with a fully customized `UserDetailsService` implementation.  
Instead of in-memory or JDBC authentication, user data is fetched from the database dynamically.

---


## 📚 Table of Contents

- [🚀 Stack Overview](#-stack-overview)
- [🗂️ Project Structure](#-Project-Structure)
- [🔐 Authentication Flow](#-authentication-flow)
- [🧱 DAO Layer (Hybrid Style)](#-dao-layer-hybrid-style)
- [📜 JPA Entities](#-jpa-entities)
- [🌐 API Overview](#-api-overview)
- [⚙️ How to Run the Application](#-how-to-run-the-application)
- [✅ Features](#-features)
- [👨‍💻 Author](#-author)
- [📦 Optional Add-ons](#-optional-add-ons)
- [📚 References](#-references)

---

## 🚀 Stack Overview

- **Java 17**
- **Spring Boot**
- **Spring Security (Custom Authentication)**
- **JPA + Hibernate**
- **RESTful APIs**
- **Hybrid DAO (JpaRepository + EntityManager)**

---

## 🗂️ Project Structure

```plaintext
src/
└── main/
    └── java/
        └── com/
            └── hexvoid/
                └── employeeportal/
                    ├── controller         # REST API endpoints
                    │   ├── CreateUser.java
                    │   ├── EmployeeController.java
                    │   ├── EmployeeLeaveRequestController.java
                    │   └── LeaveRequestController.java
                    │
                    ├── dao                # Hybrid DAO layer (JPA + EntityManager)
                    │   ├── EmployeeCredentialsDao.java
                    │   ├── EmployeeDAO.java
                    │   ├── EmployeeDaoImpl.java
                    │   ├── LeaveRequestDao.java
                    │   └── LeaveRequestDAOImpl.java
                    │
                    ├── entity             # JPA Entities
                    │   ├── EmployeeCredentials.java
                    │   ├── Employees.java
                    │   ├── LeaveRequests.java
                    │   ├── LeaveStatus.java
                    │   └── LeaveType.java
                    │
                    ├── exceptionhandler   # Global Exception Handling
                    │   ├── ExceptionHandlerEntity.java
                    │   ├── ExceptionHandlerGlobal.java
                    │   └── MyCustomExceptionClass.java
                    │
                    ├── security           # Spring Security Custom Config
                    │   ├── EmployeeUserDetailsService.java
                    │   ├── SecurityConfig.java
                    │   └── SpringSecurityConfig.java
                    │
                    └── service            # Business Logic Layer
                        ├── EmployeeSecretServiceImpl.java
                        ├── EmployeeSecretsService.java
                        ├── EmployeeService.java
                        ├── EmployeeServiceImpl.java
                        ├── LeaveRequestService.java
                        └── LeaveRequestServiceImpl.java
```

---

## 🔐 Authentication Flow

### ✅ Using Custom `UserDetailsService`

- `EmployeeUserDetailsService.java` implements `UserDetailsService`
- Fetches user data from DB using `EmployeeSecretServiceImpl`
- Converts DB records into Spring Security `UserDetails` object

```java
EmployeeCredentials emp = employeeSecretService.findByEmail(username);
return new User(emp.getEmail(), emp.getPassword(), authorities);
```

This overrides the default `JdbcUserDetailsManager`.

### 🤁 Optional: JDBC Auth (Commented Out)
You can optionally switch to JDBC-based authentication by uncommenting the following bean in `SpringSecurityConfig.java`:

```java
@Bean
JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
    JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
    manager.setUsersByUsernameQuery("SELECT email, pwd, true FROM employee_credentials WHERE email = ?");
    manager.setAuthoritiesByUsernameQuery("SELECT email, role FROM employee_credentials WHERE email = ?");
    return manager;
}
```

---

## 🧱 DAO Layer (Hybrid Style)

| DAO Class              | Type           | Usage                                 |
|------------------------|----------------|---------------------------------------|
| `EmployeeCredentialsDao` | JpaRepository  | Auto CRUD + custom method (`findByEmail`) |
| `EmployeeDAO`            | EntityManager  | Manual DB operations for employees     |
| `LeaveRequestDAO`        | EntityManager  | Manual DB operations for leave module  |

---

## 📜 JPA Entities

- `EmployeeCredentials.java`
- `Employees.java`
- `LeaveRequests.java`
- `LeaveStatus.java`
- `LeaveType.java`

> Fully annotated for database interaction using Hibernate (JPA).

---

## 🌐 API Overview

### 🟢 Public Endpoints

| Method | Endpoint                    | Access |
|--------|-----------------------------|--------|
| POST   | `/api/register/user`        | Open   |
| GET    | `/api/noauth/employees/**`  | Open   |
| CRUD   | `/api/noauth/leaves/**`     | Open   |

### 🔐 Secured Endpoints (Role-Based)

| Method | Endpoint              | Required Role(s)     |
|--------|-----------------------|------------------------|
| POST   | `/api/employee`       | EMPLOYEE              |
| PUT    | `/api/employee/**`    | EMPLOYEE, ADMIN       |
| DELETE | `/api/employee/**`    | ADMIN                 |
| GET    | `/api/employee`       | ADMIN                 |
| GET    | `/api/employee/**`    | ADMIN, EMPLOYEE       |
| POST   | `/api/leave`          | ADMIN, EMPLOYEE       |
| GET    | `/api/leave`          | ADMIN                 |
| PUT    | `/api/leave/**`       | ADMIN                 |
| DELETE | `/api/leave/**`       | ADMIN, EMPLOYEE       |

> All access rules are defined using Spring Security’s fluent API in `SpringSecurityConfig.java`.

---

## ⚙️ How to Run the Application

**Clone the repo**

```bash
git clone https://github.com/mHexVoid/elms-springboot-security-sandbox.git
cd elms-spring-security-custom-config
```

**Configure your database** in

```bash
 application.properties
```

*(Optional)* Add users in the

```bash
  employee_credentials table manually,
  or register via REST API Endpoint: /api/register/user
```

**Run the app**

```bash
mvn spring-boot:run
```

---

### 🧪 Access REST API endpoints using (Swagger) or Postman
This project includes Swagger via **SpringDoc** for exploring all REST APIs interactively.

#### 🌐 API Explorer (Swagger UI)

- URL: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## ✅ Features

- 🔐 Custom User Authentication using DB credentials
- 🔄 Optional JDBC Auth setup (toggle on/off)
- 🧠 Clean DAO design (Hybrid: JPA + EntityManager)
- 💥 Centralized Exception Handling
- 🧩 Role-based access control on all sensitive endpoints

---

## 👨‍💻 Author

**Gaurav Mishra**  
Crafting secure & scalable Spring Boot apps ☕💻   
[🔗 LinkedIn](https://www.linkedin.com/in/gaurav-mishra-401a8a149/) &nbsp;&nbsp;|&nbsp;&nbsp; [💻 GitHub](https://github.com/mHexVoid)
&nbsp;&nbsp;|&nbsp;&nbsp; [🌐 Project Repo](https://github.com/mHexVoid/elms-springboot-security-sandbox.git)
> Got ideas, feedback, or looking to collaborate?  
> Open an issue or drop a PR — let’s build something awesome together.

---

## 📦 Optional Add-ons

Want to level up the project?

- 📭 Postman collection export  
- 🐳 Dockerfile or Docker Compose support  
- 🧩 Environment-specific configs (`application-dev.properties`, etc.)

---

## 📚 References

- 🔐 [Spring Security Docs](https://docs.spring.io/spring-security/reference/index.html)
- 🚀 [Spring Boot Docs](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- 🛠️ [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- 📄 [SpringDoc / Swagger](https://springdoc.org/)
- 🗂️ [MySQL JDBC URL Guide](https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-url-format.html)


---
> 💡 Feel free to fork, extend, or raise a PR. Collaboration welcome!


---

<p align="center">
  🚀 Built with ❤️ by <strong><a href="https://github.com/mHexVoid">Hexvoid</a></strong> — Part of the ✨ <strong>Hexvoid Initiative</strong> ✨
</p>