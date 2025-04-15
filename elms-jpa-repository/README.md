
# 🧾 Employee Leave Management System (ELMS)

![Java](https://img.shields.io/badge/Java-17-blue?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.3-green?logo=spring-boot)
![Spring Security](https://img.shields.io/badge/Spring_Security-6.0+-green?logo=spring-security&link=https://spring.io/projects/spring-security)
![Maven](https://img.shields.io/badge/Maven-Build-blue?logo=apachemaven)



> 🏷️ Built with ❤️ under the **Hexvoid** banner for clean, secure, and modular backend architecture.

---

A full-stack, modular **Employee Leave Management System** built using **Spring Boot** and **Spring Data JPA**.  
This project allows employees to apply for leaves and lets admins manage leave requests, statuses, and employee records.

It follows a **clean layered architecture** and uses **Spring Security (In-Memory Authentication)** for role-based access control.

---

# Table of Contents

- [🔍 Overview](#-overview)
- [⚙️ Tech Stack](#-tech-stack)
- [🧱 Architecture Overview](#-Architecture-Overview)
- [📂 Project Structure](#-project-structure)
- [🔐 Spring Security Configuration](#-spring-security-configuration)
- [🚫 Exception Handling](#-Exception-Handling)
- [🗃️ Key Classes & Files](#-Key-Classes--Files)
- [🚀 Getting Started](#-Getting-Started)
- [🔮 Future Enhancements](#-Future-Enhancements)
- [🔗 Access](#-access)
- [📚 References](#-references)


---

## ⚙️ Tech Stack

- Java 17
- Spring Boot
- Spring MVC (REST APIs)
- Spring Security (In-Memory)
- Spring Data JPA
- Maven

---

## 🧠 Architecture Overview

Follows a well-structured **3-layered architecture**:

### 📦 Controller Layer
- REST endpoints for managing employees and leave requests.
- No business logic — delegates everything to service layer.

### 🧩 Service Layer
- Business logic and validations.
- Interfaces like `EmployeeService` ensure loose coupling.
- Implementations like `EmployeeServiceImpl` handle orchestration.

### 🗃️ DAO Layer
- **Uses Spring Data JPA repositories** like `EmployeeDAO`, `LeaveRequestDAO`.
- No need to write boilerplate CRUD — handled by extending `JpaRepository`.

---

## 📁 Project Structure

```plaintext
com.hexvoid.employeeportal

├── controller            → REST APIs for employees and leave requests
├── service               → Interfaces & business logic implementations
├── dao                   → Spring Data JPA repositories
├── entity                → JPA entities (Employees, LeaveRequests, etc.)
├── exceptionhandler      → Global & custom exception handling
└── security              → Spring Security configuration (in-memory)
```

---

## 🔗 Entity Relationship

### ✅ One-to-Many: Employees → LeaveRequests

- One employee can have multiple leave requests.
- Implemented using JPA annotations.

```java
// In LeaveRequests.java
@ManyToOne
@JoinColumn(name = "employee_id", nullable = false)
private Employees employee;
```

---

## 🔐 Spring Security Configuration

Security is handled via **in-memory authentication** in `SecurityConfig.java`.

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

## 🚫 Exception Handling

Robust and centralized exception management:

- `ExceptionHandlerGlobal.java` → handles runtime & validation exceptions.
- `MyCustomExceptionClass.java` → custom app-level exceptions.
- `ExceptionHandlerEntity.java` → response wrapper for consistent error format.

---

## 📦 Key Classes & Files

### Entities:
- `Employees.java`
- `LeaveRequests.java`
- `LeaveStatus.java`
- `LeaveType.java`

### DAOs (Spring Data JPA):
- `EmployeeDAO.java`
- `LeaveRequestDAO.java`

### Services:
- `EmployeeService` & `EmployeeServiceImpl`
- `LeaveRequestService` & `LeaveRequestServiceImpl`

### Controllers:
- `EmployeeController.java`
- `LeaveRequestController.java`

---

## 🚀 Getting Started

1. **Clone the repo**  
   ```bash
   git clone <repo-url>
   ```

2. **Configure Database**  
   Update your DB config in `application.properties`

3. **Create Tables**  
   Use schema.sql or rely on `spring.jpa.hibernate.ddl-auto=update`

4. **Run the App**  
   Execute `EmployeeLeaveManagementSystemApplication.java`

5. **Access APIs**  
   Use Postman or any client  
   - Username: `admin` / `user`  
   - Password: `admin123` / `user123`

---

## ✅ Features

- Submit & manage leave requests
- View all leave requests per employee
- Admin-only actions protected by roles
- One-to-Many JPA mapping
- In-memory Spring Security setup
- Global exception handling

---

## 🔮 Future Enhancements

This project lays the foundation for a full-fledged leave management system. Here's what can be added going forward:

- 📄 Swagger/OpenAPI for live API documentation  
- 🔐 JWT Authentication for secure, token-based login  
- 📧 Email Notifications on leave approval/rejection  
- 📊 Admin Dashboard for managing users & requests (React/Angular)  
- 🔍 Pagination, Filtering & Sorting for large datasets  
- 📁 File Uploads for attaching documents with leave requests  
- 🕒 Leave History & Audit Logs  
- 🌍 Multi-role system (HR, Manager, Employee)  

---

## 🔗 Access

- 🔸 **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- 🔸 **H2 Console**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

### Credentials:

- 🛢️ **JDBC URL**: `jdbc:h2:mem:testdb`
- 👤 **Username**: `pola`
- 🔑 **Password**: `pola`

---

## 📚 References

- 📘 [Spring Boot Docs](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- 🔐 [Spring Security In-Memory Auth](https://docs.spring.io/spring-security/site/docs/current/reference/html5/#in-memory-authentication)
- 🧾 [JPA EntityManager Guide (Baeldung)](https://www.baeldung.com/learn-spring-course)
- 🔗 [Loose Coupling Explained](https://www.geeksforgeeks.org/loose-coupling-in-java/)


## 👨‍💻 Made with ☕ by Hexvoid Devs

Built with clean code principles, passion, and a dash of caffeine.  
For any suggestions or collaborations, feel free to reach out 🤝

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
