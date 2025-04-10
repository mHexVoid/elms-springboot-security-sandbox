# 🌐 ELMS Spring Boot Security Sandbox

🛡️ A secure, modular, and developer-friendly sandbox of Spring Boot projects focused on authentication, authorization, and security best practices — built around an **Employee Leave Management System (ELMS)**.

This repository showcases **progressive security implementations** — from basic Spring Security to custom filters, JWT-based authentication, and advanced configurations — organized module-wise for learning, experimentation, and backend mastery.

---



![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/SpringBoot-3.0-green)
![Security](https://img.shields.io/badge/Security-Custom%20JWT-red)


---

## 📚 Table of Contents

- [What’s Inside](#-whats-inside)
- [Technologies Used](#-technologies-used)
- [Key Concepts Covered](#-key-concepts-covered)
- [Repo Structure Philosophy](#-repo-structure-philosophy)
- [Getting Started](#-getting-started)
- [License](#-license)
- [Connect](#-connect)

---

## 📁 What’s Inside

### 🧪 Concept Module

| Project Folder | Description |
|----------------|-------------|
| `01_springboot_jpa_one_to_many_eager_couse_init` | A basic JPA setup demonstrating one-to-many eager fetch behavior. |


### 🏗️ Core Foundation Modules

| Project Folder | Description |
|----------------|-------------|
| `base-employee-leave-management` | Core ELMS project with CRUD APIs using Spring Boot and JPA. |
| `elms-jpa-repository` | Clean separation for repository pattern learning with Spring Data JPA. |


### 🔐 Security Modules

| Project Folder | Description |
|----------------|-------------|
| `elms-spring-security` | Initial Spring Security configuration over the ELMS project. |
| `elms-spring-security-custom-config` | Customized security config (without JWT) to override Spring Security defaults. |
| `elms-spring-security-custom-config-auth-provider` | Implementation of a custom `AuthenticationProvider`. |
| `elms-spring-security-custom-config-auth-provider-auth-event` | Security event handling during login (e.g. success/failure events). |
| `elms-spring-security-custom-config-auth-provider-custom-filter` | Custom authentication filter replacing default username-password filter. |
| `elms-spring-security-custom-config-JWT-token` | Stateless authentication using JWTs. |
| `elms-spring-security-custom-config-JWT-token-manual-auth` | Manual login endpoint returning JWT after authentication. |
| `elms-spring-security-custom-config-JWT-token-manual-auth-method-sec` | Method-level security using `@PreAuthorize`, `@PostFilter`, etc. with JWT. |

---

## 🚀 Technologies Used

- Java 17+  
- Spring Boot  
- Spring Security  
- Spring Data JPA (Hibernate)  
- JWT (JSON Web Token)  
- Maven  
- H2 / MySQL (configurable)  
- IntelliJ / Eclipse (IDE)  

---

## 🎯 Key Concepts Covered

- ✅ Spring Security fundamentals and architecture  
- 🔒 Custom `AuthenticationProvider` and filters  
- 🔑 JWT Token generation and validation  
- 🧠 Role-based access control (RBAC)  
- 🔄 Event listeners for login attempts  
- ⚙️ Method-level security using annotations  
- 📦 Layered architecture (Controller → Service → Repository)  
- 🌱 Hands-on with multiple small modules to experiment safely  

---

## 📂 Repo Structure Philosophy

Each folder is a self-contained project that builds upon the previous one:  
1. Start from CRUD basics  
2. Add default Spring Security  
3. Inject custom providers/filters  
4. Evolve into JWT-based secure APIs  
5. Use method-level annotations to fine-tune access  

---

## 📌 Getting Started

1. Clone this repo:
   ```bash
   git clone https://github.com/mHexVoid/elms-springboot-security-sandbox.git
   ```

2. Choose any subfolder (project module) to run. You can import it individually in your IDE (Eclipse/IntelliJ).

3. Run the project as a Spring Boot application.

Explore, break, build, learn — this sandbox is for you.

---

## 📜 License

This project is open-source and available for educational and portfolio purposes. If you use it or get inspired, feel free to give a ⭐ and credit 🙌

---

## 🤝 Connect

Let’s build together and explore deeper levels of Spring Security, Java, and backend architecture.

- GitHub: [mHexVoid](https://github.com/mHexVoid)  
- LinkedIn: [Gaurav Mishra](https://www.linkedin.com/in/gaurav-mishra-401a8a149/)  

---

💡 “Code fearlessly in your sandbox. That’s how you build castles in production.”
