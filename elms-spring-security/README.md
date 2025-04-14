
# 📝 Employee Leave Management System (Spring Security Version)


![Java](https://img.shields.io/badge/Java-17-blue?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.3-green?logo=spring-boot)
![Spring Security](https://img.shields.io/badge/Spring_Security-6.0+-green?logo=spring-security&link=https://spring.io/projects/spring-security)
![Maven](https://img.shields.io/badge/Maven-Build-blue?logo=apachemaven)

> 🏷️ Built with ❤️ under the **Hexvoid** banner for clean, secure, and modular backend architecture.

---

A **Spring Boot-based Employee Leave Management System** with secure login using a **case-sensitive in-memory authentication mechanism**.  
This version uses `EntityManager` (no Spring Data JPA) for full control over database interactions and a fully decoupled architecture. This project is built for clean architecture, study, and interview prep — backed by Swagger API docs and an in-memory H2 database.

---

## 📑 Table of Contents
- [🔍 Overview](#-overview)  
- [🛠️ Technologies Used](#-technologies-used)  
- [📂 Project Structure](#-project-structure)  
- [🧱 Layered Architecture Breakdown](#-layered-architecture-breakdown)  
- [🔐 Spring Security (Case-Sensitive Login)](#-spring-security-case-sensitive-login)  
- [📦 Entity Models](#-entity-models)  
- [❗ Exception Handling](#-exception-handling)  
- [🧪 REST API & Swagger](#-rest-api--swagger)  
- [🚀 Running the Application](#-running-the-application)  
- [💡 Future Improvements](#-future-improvements)  
- [📚 References](#-references)  

---

## 🔍 Overview

This system handles employee leave requests and approvals in a secure and modular way. Highlights include:

- ✅ **In-Memory Authentication** with **case-sensitive user matching**
- 💼 **Leave management** using JPA Entities and DTOs
- 🧩 Custom **DAO layer using EntityManager**
- 🔒 Global exception handling strategy
- 🌐 **Swagger-enabled API Documentation**
- 🧪 **H2 Console** for live DB queries during dev

---

## 🛠️ Technologies Used

- Java 17  
- Spring Boot 3.x  
- Spring MVC  
- Spring Security (Case-Sensitive In-Memory Auth)  
- Hibernate (JPA) via EntityManager  
- H2 Database (In-Memory)  
- Maven  
- Swagger / SpringDoc OpenAPI

---

## 📂 Project Structure

```plaintext
src/main/java/com.hexvoid.employeeportal

├── controller         → REST APIs for employees & leave requests
├── service            → Interfaces and business logic implementation
├── dao                → Custom DAO layer using EntityManager
├── entity             → JPA Entities (Employee, LeaveRequests, etc.)
├── exceptionhandler   → Global & custom exception handling
└── security           → In-memory Spring Security (case-sensitive login)
```

---

## 🧱 Layered Architecture Breakdown

### 🔹 Controller Layer
- Handles incoming HTTP requests and exposes RESTful endpoints.
- Delegates processing to the service layer.

### 🔹 Service Layer
- Business logic goes here.
- Interfaces like `EmployeeService` and `LeaveRequestService` ensure loose coupling.

### 🔹 DAO Layer
- Uses **EntityManager** instead of Spring Data JPA.
- All CRUD and custom DB logic written manually in:
  - `EmployeeDaoImpl.java`
  - `LeaveRequestDAOImpl.java`

### 🔹 Entity Layer
- Models for `Employees`, `LeaveRequests`, `LeaveType`, `LeaveStatus`.
- Used for DB mapping with annotations like `@Entity`, `@Table`.

---

## 🔐 Spring Security (Case-Sensitive Login)

Spring Security by default does **case-insensitive** username checks.  
This project overrides that using a **custom `UserDetailsManager`**:

```java
public class CaseSensitiveUserDetailsManager implements UserDetailsManager {
    // Custom logic to enforce case-sensitive matching
}
```

Configured in `SpringSecurityConfig.java` to replace default manager.

### ✅ Why?
- Prevents `admin` and `Admin` from being treated the same  
- Adds stricter control & fine-grained security

---

## 🔄 Loose Coupling vs Tight Coupling

| Loose Coupling                             | Tight Coupling                            |
|-------------------------------------------|-------------------------------------------|
| Controller → Service via Interface         | Direct class access                       |
| Service → DAO via Interface                | No abstraction                            |
| Easy to test/mock/extend                   | Hard to scale                             |

> This project strictly follows **loose coupling** between all layers.

---

## 📦 Entity Models

### 🧑 `Employees.java`
- Stores employee details
- Connected to leave records

### 📄 `LeaveRequests.java`
- Each leave record for an employee
- Includes date, reason, type, and status

### 📌 `LeaveStatus.java` & `LeaveType.java`
- Enum-style static tables
- Mapped manually

Each mapped to corresponding tables manually and managed via **EntityManager**.

---

## ❗ Exception Handling

Centralized(Handled globally) and consistent exception flow with:

- `ExceptionHandlerGlobal.java` : Captures runtime & HTTP issues
- `MyCustomExceptionClass.java` : App-specific exception types
- `ExceptionHandlerEntity.java`: Wraps structured API responses

> Clean and consistent error messages for both frontend & debugging.

---

## 🧪 REST API & Swagger

This project includes Swagger via **SpringDoc** for exploring all REST APIs interactively.

### 🌐 API Explorer (Swagger UI)

- URL: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### 🔍 H2 Console

- URL: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)  
- JDBC URL: `jdbc:h2:mem:testdb`  
- Username: `pola`  
- Password: `pola`

---

## 🚀 Running the Application

### 🔧 Prerequisites
- Java 17
- Maven installed

### 📦 Build & Run

```bash
git clone <repo-url>
cd employee-leave-management-system
mvn clean install
mvn spring-boot:run
```

---

## 💡 Future Improvements

- 🔐 JWT or OAuth2 based token authentication  
- 🌐 Swagger endpoint grouping & examples  
- 📊 Admin dashboard UI  
- 📈 Analytics on leave types/status trends  
- ✉️ Email notifications for leave events

---

## 📚 References

- 📘 [Spring Security Docs](https://docs.spring.io/spring-security/reference/index.html)  
- 📗 [Spring Boot + EntityManager (Baeldung)](https://www.baeldung.com/the-persistence-layer-with-spring-and-jpa)  
- 📙 [SpringDoc OpenAPI](https://springdoc.org/)  
- 🛠️ [Hibernate ORM Docs](https://hibernate.org/orm/documentation/)  
- 📋 [Case-Sensitive Auth Discussion](https://stackoverflow.com/questions/30823723/case-sensitive-usernames-in-spring-security)  
- 🧪 [H2 Console Usage](https://www.h2database.com/html/main.html)

---

## 👨‍💻 Developed by Hexvoid

Built with 🧼 clean architecture, clean code, and strong modular boundaries.  
**Case-sensitive AF.** 🚀

---

## 👤 Author

**Gaurav Mishra**  
- 🧑‍💻 GitHub: [@mHexVoid](https://github.com/mHexVoid)  
- 🌐 Project Repo: [ELMS - Spring Security Sandbox](https://github.com/mHexVoid/elms-springboot-security-sandbox.git)  
- 💼 LinkedIn: [Gaurav Mishra](https://www.linkedin.com/in/gaurav-mishra-401a8a149/)

---

> 💡 Contributions, feedback, and pull requests are welcome!

---

<p align="center">
  🚀 Built with ❤️ by <strong><a href="https://github.com/mHexVoid">Hexvoid</a></strong> — Part of the ✨ <strong>Hexvoid Initiative</strong> ✨
</p>
