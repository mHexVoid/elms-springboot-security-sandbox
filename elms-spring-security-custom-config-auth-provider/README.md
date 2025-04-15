
# 🛡️ ELMS - Employee Leave Management System (Spring Security Custom Auth)

![Java](https://img.shields.io/badge/Java-17-blue?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.3-green?logo=spring-boot)
![Spring Security](https://img.shields.io/badge/Spring_Security-6.0+-green?logo=spring-security&link=https://spring.io/projects/spring-security)
![Maven](https://img.shields.io/badge/Maven-Build-blue?logo=apachemaven)

> 🏷️ Built with ❤️ under the **Hexvoid** banner for clean, secure, and modular backend architecture.

---

A full-stack **Spring Boot** project for managing employee leave with a strong focus on **custom authentication**, a tailored **`UserDetailsService`**, and a **hybrid DAO layer**. Built to demonstrate deep customization of Spring Security, it uses a custom `AuthenticationProvider`, handles roles and permissions via JPA, and follows clean, scalable architecture principles.

---

## 📜 Table of Contents

1. [🔥Features ](#-features)
2. [🏗️Project Structure ](#-project-structure)
3. [🔐Security Architecture ](#-security-architecture)
   - [EmployeeUserNamePwdAuthenticationProvider](#-employeeusernamepwdauthenticationprovider)
   - [EmployeeUserDetailsService](#-employeeuserdetailsservice)
   - [EmployeeAuthorities Entity](#-employeeauthorities-entity)
4. [⚙️Authentication Flow ](#-authentication-flow)
5. [🧠Technologies Used ](#-technologies-used)
6. [📦Important Classes ](#-important-classes-cheat-sheet)
7. [🚀API Endpoints ](#-api-endpoints)
8. [ 🛠️Setup Instructions](#-setup-instructions)
9. [🧪Test Credentials ](#-test-credentials)
10. [🚧Future Enhancements ](#-future-enhancements)
11. [📝References](#-References)
---

## 🔥 Features

- ✅ Custom Authentication Provider (`EmployeeUserNamePwdAuthenticationProvider`)
- ✅ Custom `UserDetailsService` (`EmployeeUserDetailsService`)
- ✅ Role & Authority management (`EmployeeAuthorities`)
- ✅ Java-based Spring Security config (`SecurityConfig`, `SpringSecurityConfig`)
- ✅ DAO layer using both `EntityManager` and `JpaRepository`
- ✅ Leave request APIs & employee management
- ✅ Global exception handling
- ✅ Clean modular structure

---


## 🏗️ Project Structure

```plaintext
src/main/java/com/hexvoid/employeeportal
├── controller                   # REST endpoints for users & leave
├── dao                         # Hybrid DAO using JPA + EntityManager
├── entity                      # JPA entities (EmployeeCredentials, Authorities)
├── exceptionhandler            # Global exception handling logic
├── security                    # All custom security/authentication components
├── service                     # Business logic for auth & leave mgmt
└── EmployeeLeaveManagementSystemApplication.java
```

---

## 🔐 Security Architecture

### 🔸 EmployeeUserNamePwdAuthenticationProvider

A fully custom implementation of AuthenticationProvider.

- Validates login credentials using data from DB
- Injects and uses EmployeeUserDetailsService
- Throws exceptions if:
  - Username not found
  - Password mismatch
- On success, returns:
```java
new UsernamePasswordAuthenticationToken(userDetails, password, authorities);
```

### 🔸 EmployeeUserDetailsService

- Implements UserDetailsService
- Fetches user credentials `EmployeeCredentials` and roles/Authorities `EmployeeAuthorities` from the DB
- Converts user roles (EmployeeAuthorities) into Spring's GrantedAuthority
- Returns a Spring-compatible `UserDetails` object:
```java
return new User(username, password, grantedAuthorities);
```

### 🔸 EmployeeAuthorities Entity

- Represents roles (like ROLE_EMPLOYEE, ROLE_MANAGER)
- Stored in DB and linked to each user via JPA
- Used to control access using Spring’s hasRole() and hasAuthority()

---

## ⚙️ Authentication Flow

```plaintext
1. User hits POST /login **Login API Call** → `/login` (via Spring Security)
2. Spring Security invokes routes it to `UsernamePasswordAuthenticationFilter`
3. This forwards credentials to Custom `AuthenticationProvider` (`EmployeeUserNamePwdAuthenticationProvider`) 
4. Provider calls EmployeeUserDetailsService to load user
5. User verified → authorities fetched → token returned
6. SecurityContext holds authenticated user
```

---

## 🧠 Technologies Used

- Java 17+
- Spring Boot
- Spring Security
- JPA (Hibernate)
- MySQL / H2 (for dev)
- Maven

---

## 📦 Important Classes (Cheat Sheet)

| Layer / Package | Class Name | Responsibility |
|------------------|-------------|----------------|
| `security` | `EmployeeUserDetailsService` | Load user from DB |
| `security` | `EmployeeUserNamePwdAuthenticationProvider` | Custom login logic |
| `security` | `SecurityConfig`, `SpringSecurityConfig` | Security rules, filters, session config |
| `dao` | `EmployeeDAO`, `EmployeeCredentialsDao` | DB access using JPA + native queries |
| `entity` | `EmployeeCredentials`, `EmployeeAuthorities` | Auth-related JPA models |
| `controller` | `CreateUser`, `EmployeeController` | API endpoints |
| `exceptionhandler` | `ExceptionHandlerGlobal`, `ExceptionHandlerEntity` | Centralized error handling |

---

## 🚀 API Endpoints

| Endpoint | Method | Description | Auth Required |
|-----------|--------|-------------|----------------|
| `/create-user` | POST | Register new user | ❌ |
| `/login` | POST | Login using credentials | ❌ |
| `/employee/{id}` | GET | Fetch employee by ID | ✅ |
| `/leave-request/apply` | POST | Apply for leave | ✅ |

---

## 🛠️ Setup Instructions

```bash
# Clone and build the project
https://github.com/mHexVoid/elms-springboot-security-sandbox.git
cd current-project-directory
mvn clean install

# Run the application
./mvnw spring-boot:run
```

> Default port: `8080`

---

## 🧪 Test Credentials

```plaintext
Username: johndoe
Password: password123
```
You can also register via `/create-user`.

---

### 🧪 Access REST API endpoints using (Swagger) or Postman
This project includes Swagger via **SpringDoc** for exploring all REST APIs interactively.

#### 🌐 API Explorer (Swagger UI)

- URL: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 🚧 Future Enhancements

- 🔐 Switch to JWT token authentication
- 📧 Email notifications on leave approval
- 🧑‍💼 Manager/HR role with leave approval rights
- 📊 Admin dashboard for analytics

---

## 📝 References

- 📚 [Spring Security Docs](https://docs.spring.io/spring-security/site/docs/current/reference/html5/)
- 🚀 [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- 🗃️ [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- 🔐 [JWT Authentication in Spring Security](https://www.baeldung.com/security/jwt-authorization)
- 📖 [SpringDoc & Swagger UI Integration](https://springdoc.org/)
- 🧪 [H2 Database Docs](https://www.h2database.com/html/main.html)
- 🧰 [Maven Build Tool](https://maven.apache.org/guides/index.html)
- 💡 [Spring Security Custom AuthenticationProvider Guide](https://www.baeldung.com/spring-security-authentication-provider)

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