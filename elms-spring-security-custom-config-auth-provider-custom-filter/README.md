
# 🛡️ ELMS - Employee Leave Management System (Spring Security Custom Filter Integration)
![Java](https://img.shields.io/badge/Java-17-blue?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.3-green?logo=spring-boot)
![Spring Security](https://img.shields.io/badge/Spring_Security-6.0+-green?logo=spring-security&link=https://spring.io/projects/spring-security)
![Maven](https://img.shields.io/badge/Maven-Build-blue?logo=apachemaven)

> 🏷️ Built with ❤️ under the **Hexvoid** banner for clean, secure, and modular backend architecture.

---

An enhanced version of the **Employee Leave Management System (ELMS)** focused on **custom security filters** within the Spring Security filter chain. This module demonstrates the strategic placement of filters **before**, **at**, and **after** authentication to provide fine-grained control over request validation, logging, and security flows.

---

### 📋 Table of Contents
- [🔐 Key Features](#-key-features)
- [🏗️ Project Structure](#-project-structure)
- [🔸 Custom Filters Overview](#-custom-filters-overview)
  - [✅ RequestValidationBeforeFilter](#-1-requestvalidationbeforefilter)
  - [✅ AuthoritiesLoggingAtFilter](#-2-authoritiesloggingatfilter)
  - [✅ AuthoritiesLoggingAfterFilter](#-3-authoritiesloggingafterfilter)
- [⚙️ Security Configuration](#-security-configuration)
- [▶️ How to Run](#-how-to-run)
- [🧪 Test Credentials](#-test-credentials)
- [📌 Why Custom Filters?](#-why-custom-filters)
- [🔁 Example Request & Response](#-example-request--response)
- [🚀 Future Enhancements](#-future-enhancements)
- [📝 References](#-references)

---

## 🔐 Key Features

- ✅ Pre-authentication validation via custom filter
- ✅ Authentication phase logging
- ✅ Post-authentication role/authority inspection
- ✅ Modular security configuration using `SecurityFilterChain`
- ✅ Clean and extensible project structure

---

## 🏗️ Project Structure

```
src/main/java/com/hexvoid/employeeportal
├── controller                  # REST endpoints
├── dao                         # Data access layer
├── entity                      # JPA entities
├── events                      # (From previous version) auth/authorization logs
├── exceptionhandler            # Global exception handling
├── filter                      # ✅ Custom filters (Before, At, After)
├── security                    # Spring Security config & auth
├── service                     # Business logic
└── EmployeeLeaveManagementSystemApplication.java
```

---

## 🔸 Custom Filters Overview

### ✅ 1. RequestValidationBeforeFilter

**Position**: Before `BasicAuthenticationFilter`  
**Purpose**: Validates `Authorization` header and blocks invalid usernames (e.g., ones containing `"test"`).  

```java
http.addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class);
```

🧪 **Behavior**:
- Decodes Base64 credentials from `Authorization` header
- Rejects requests with username containing `test` (400 Bad Request)

---

### ✅ 2. AuthoritiesLoggingAtFilter

**Position**: At `BasicAuthenticationFilter`  
**Purpose**: Logs incoming authentication attempts.  

```java
http.addFilterAt(new AuthoritiesLoggingAtFilter(), BasicAuthenticationFilter.class);
```

🧪 **Behavior**:
- Logs a message each time an authentication request is made

---

### ✅ 3. AuthoritiesLoggingAfterFilter

**Position**: After `BasicAuthenticationFilter`  
**Purpose**: Logs user details and authorities post-authentication.  

```java
http.addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class);
```

🧪 **Behavior**:
- Logs authenticated user's username and all assigned roles/permissions

---

### 📋 Custom Filter Summary

| Filter Class                     | Position                | Purpose                                  |
|----------------------------------|--------------------------|-------------------------------------------|
| `RequestValidationBeforeFilter`  | Before `BasicAuthenticationFilter` | Blocks invalid usernames in Authorization header |
| `AuthoritiesLoggingAtFilter`     | At `BasicAuthenticationFilter`     | Logs raw authentication attempts         |
| `AuthoritiesLoggingAfterFilter`  | After `BasicAuthenticationFilter`  | Logs authenticated user's roles & details |


---

## ⚙️ Security Configuration

Defined in: `SpringSecurityConfig.java`

🔧 **Highlights**:
- Integrates all custom filters into the security filter chain
- Configures open vs. protected endpoints via `.requestMatchers()`
- Disables CSRF for stateless REST APIs
- Uses `PasswordEncoderFactories.createDelegatingPasswordEncoder()` for password hashing
- Supports Basic Auth for easier testing with tools like Postman/curl

---

## ▶️ How to Run

🧰 **Prerequisites**:
- Java 17+
- Maven

🧪 **Steps**:

```bash
git clone https://github.com/mHexVoid/elms-springboot-security-sandbox.git
cd elms-security-custom-filter
mvn clean install
mvn spring-boot:run
```

🌐 **Access Swagger UI**:  
Once the app is running, open  
[🔗 http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)  
in your browser to explore available APIs.

---

## 🧪 Test Credentials

```plaintext
Username: alice
Password: securepass
```

Or register a new user via `/create-user`.

---



## 📌 Why Custom Filters?

Spring Security offers a robust filter chain, but sometimes you need:

- Pre-validating headers before authentication
- Logging attempts precisely at authentication phase
- Capturing post-auth details for audit or debugging

By inserting filters at strategic points, you gain full visibility and control over the authentication lifecycle.

---

## 🔁 Example Request & Response

### 🔐 Login Request

**Request**:

```http
POST /login HTTP/1.1
Authorization: Basic YWxpY2U6c2VjdXJlcGFzcw==
```

**Response**:

```json
{
  "status": "success",
  "username": "alice",
  "authorities": ["ROLE_EMPLOYEE"]
}
```

### ❌ Rejected Username

**Request**:

```http
POST /login HTTP/1.1
Authorization: Basic dGVzdHVzZXI6cGFzcw==
```

**Response**:

```json
{
  "error": "Username containing 'test' is not allowed."
}
```

---

## 🚀 Future Enhancements

- 🧠 Metrics for filter performance
- 🔄 Custom responses based on filter outcome
- 🕹️ Toggle filters dynamically via application properties

---

## 📝 References

- 📘 [Spring Security Docs](https://docs.spring.io/spring-security/site/docs/current/reference/html5/)  
  Official documentation for securing Java applications using Spring Security.

- 🧑‍💻 [Spring Security Custom Filters](https://www.baeldung.com/spring-security/custom-filters)  
  A practical guide on creating and using custom filters in Spring Security. This directly applies to the custom filters you created in your project (e.g., `RequestValidationBeforeFilter`, `AuthoritiesLoggingAtFilter`, `AuthoritiesLoggingAfterFilter`).

- 🚀 [Spring Boot Security](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-security)  
  The official documentation for integrating Spring Security with Spring Boot, which is key for your project’s setup and configuration.

- 📊 [Spring Security Event Publishing & Listeners](https://www.baeldung.com/spring-security-events)  
  Since your project involves logging authentication events (like logging successful logins and authorization failures), this tutorial would be extremely relevant to your event logging and listener setup.

- 🧰 [SpringDoc OpenAPI](https://springdoc.org/)  
  You’re using Swagger UI for API documentation, so this is relevant for setting up and customizing your Swagger integration within the Spring Boot application.

- 🔐 [Spring Security JWT Authentication](https://www.baeldung.com/spring-security-jwt)  
  This resource would be useful if you want to further enhance security with JWT-based authentication, as you’re already working on authentication-related logic.

- 🧪 [Spring Data JPA Guide](https://spring.io/projects/spring-data-jpa)  
  If your custom filters or security logic involves JPA repositories or entities, this guide would provide additional insights into working with data persistence, which is often tightly coupled with user authentication.

- ⚙️ [Spring Security: Custom AuthenticationProvider](https://www.baeldung.com/spring-security-authentication-provider)  
  You might not be using a custom `AuthenticationProvider` yet, but if you plan to enhance authentication in the future, this reference would be useful.


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