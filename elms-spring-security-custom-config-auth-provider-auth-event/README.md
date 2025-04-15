
# 🛡️ ELMS - Employee Leave Management System (Spring Security Custom Auth & Events)

![Java](https://img.shields.io/badge/Java-17-blue?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.3-green?logo=spring-boot)
![Spring Security](https://img.shields.io/badge/Spring_Security-6.0+-green?logo=spring-security&link=https://spring.io/projects/spring-security)
![Maven](https://img.shields.io/badge/Maven-Build-blue?logo=apachemaven)

> 🏷️ Built with ❤️ under the **Hexvoid** banner for clean, secure, and modular backend architecture.

---

A full-stack **Spring Boot** project for managing employee leave with a strong focus on **custom authentication**, **authentication/authorization event logging**, and a tailored **UserDetailsService**. Built to demonstrate advanced customization of Spring Security, it uses a custom `AuthenticationProvider`, handles roles and permissions via JPA, and now also logs all authentication and authorization activities for better auditing and security monitoring.

---

## 📚 Table of Contents

- 📝 [Overview](#-overview)
- 🔥 [Features](#-features)
- 🏗️ [Project Structure](#-project-structure)
- 🔐 [Security Architecture](#-security-architecture)
- ⚙️ [Authentication Flow](#-authentication-flow)
- 🧠 [Technologies Used](#-technologies-used)
- 🚀 [API Endpoints](#-api-endpoints)
- 🛠️ [Setup Instructions](#-setup-instructions)
- 🧪 [Test Credentials](#-test-credentials)
- 💡 [Thought Process](#-thought-process)
- 📚 [References](#-references)

---

## 📝 Overview

This project showcases a secure leave management system for employees with login authentication, role-based access, and real-time security event logging. It leverages Spring Security’s extensibility to monitor login attempts and access denials.

---

## 🔥 Features

- ✅ Custom Authentication Provider (`EmployeeUserNamePwdAuthenticationProvider`)
- ✅ Custom `UserDetailsService` (`EmployeeUserDetailsService`)
- ✅ Role & Authority management (`EmployeeAuthorities`)
- ✅ Java-based Spring Security config (`SecurityConfig`, `SpringSecurityConfig`)
- ✅ DAO layer using both `EntityManager` and `JpaRepository`
- ✅ Authentication Success & Failure Logging
- ✅ Authorization Denied Event Logging
- ✅ Leave request APIs & employee management
- ✅ Global exception handling
- ✅ Clean modular structure

---

## 🏗️ Project Structure

```
src/main/java/com/hexvoid/employeeportal
├── controller                   # REST endpoints
├── dao                         # DAO using JPA + EntityManager
├── entity                      # JPA entities
├── events                      # Event listeners for auth success/failure
├── exceptionhandler            # Global exception handling
├── security                    # Custom security/auth components 
├── service                     # Business logic
└── EmployeeLeaveManagementSystemApplication.java
```

---

## 🔐 Security Architecture

### 🔸 Custom AuthenticationProvider

- Validates login using DB data
- Injects `EmployeeUserDetailsService`
- Handles exceptions on bad credentials
- On success, returns an authenticated token

### 🔸 Custom UserDetailsService

- Loads user credentials and roles from DB
- Converts them into `UserDetails` for Spring Security

### 🔸 AuthenticationEvents (NEW)

Logs every successful and failed login attempt using Spring's event system:

```java
@EventListener
public void onSuccess(AuthenticationSuccessEvent successEvent) {
    logger.info("Login successful for: " + successEvent.getAuthentication().getName());
}

@EventListener
public void onFailure(AbstractAuthenticationFailureEvent failureEvent) {
    logger.error("Login failed for: " + failureEvent.getAuthentication().getName());
}
```

### 🔸 AuthorizationEvents (NEW)

Captures denied authorization attempts for protected endpoints:

```java
@EventListener
public void onFailure(AuthorizationDeniedEvent deniedEvent) {
    logger.error("Authorization failed for: " + deniedEvent.getAuthentication().get().getName());
}
```

---

## ⚙️ Authentication Flow

```
1. User POSTs credentials to /login
2. Spring forwards to custom AuthenticationProvider
3. Provider validates with DB via UserDetailsService
4. On success: returns token → SecurityContext
5. Events fired: success/failure logged
```

---

## 🧠 Technologies Used

- Java 17+
- Spring Boot
- Spring Security
- Spring Security Events API
- JPA (Hibernate)
- MySQL / H2 (for dev)
- Maven

---

## 🚀 API Endpoints

| Endpoint                    | Method | Description              | Auth |
|----------------------------|--------|--------------------------|------|
| `/create-user`             | POST   | Register new user        | ❌    |
| `/login`                   | POST   | Login                    | ❌    |
| `/employee/{id}`           | GET    | Fetch employee by ID     | ✅    |
| `/leave-request/apply`     | POST   | Apply for leave          | ✅    |

---

## 🛠️ Setup Instructions

```bash
git clone https://github.com/mHexVoid/elms-springboot-security-sandbox
cd local-project-directory
mvn clean install
./mvnw spring-boot:run
```

Default port: `8080`

---

## 🧪 Test Credentials

```plaintext
Username: johndoe
Password: password123
```
Or create your own using `/create-user`.

---

### 🧪 Access REST API endpoints using (Swagger) or Postman
This project includes Swagger via **SpringDoc** for exploring all REST APIs interactively.

#### 🌐 API Explorer (Swagger UI)

- URL: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---



## 💡 Thought Process

This implementation was designed with real-world security practices:
- Custom authentication logic decoupled from Spring's default behavior
- Modular structure to allow role-based access management
- Login/authorization monitoring using Spring’s native event system
- Auditing via logs (for future integration with ELK or audit trails)

---

## 📝 References

- 📘 [Spring Security Docs](https://docs.spring.io/spring-security/site/docs/current/reference/html5/)  
  Official documentation for securing Java applications using Spring Security.

- 🚀 [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)  
  Covers auto-configuration, application properties, and startup behavior.

- 🔍 [Understanding `UserDetailsService` in Spring Security](https://www.baeldung.com/spring-security-authentication-with-a-database)  
  Guide to implementing custom `UserDetailsService` with database-backed authentication.

- ⚙️ [Spring Security: Custom AuthenticationProvider](https://www.baeldung.com/spring-security-authentication-provider)  
  Step-by-step for creating and plugging in a custom `AuthenticationProvider`.

- 📊 [Spring Security Events API](https://docs.spring.io/spring-security/reference/servlet/authentication/events.html)  
  Official documentation on event publishing/listening in Spring Security.

- 🔄 [Spring Security Event Publishing & Listeners](https://www.baeldung.com/spring-security-events)  
  Practical example of logging login success/failure events using listeners.

- 🚫 [Authorization Denied Handling with Events](https://www.baeldung.com/spring-security-access-denied-handler)  
  Learn how to capture and respond to authorization denial events.

- 🧰 [Spring Data JPA Guide](https://spring.io/projects/spring-data-jpa)  
  Core guide to working with repositories and data persistence in Spring.

- 📘 [Spring Boot Swagger Integration (`springdoc-openapi`)](https://springdoc.org/)  
  Everything you need to enable and customize Swagger UI in Spring Boot.

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