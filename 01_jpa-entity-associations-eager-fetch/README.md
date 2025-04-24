# 🏥 01_jpa-entity-associations-eager-fetch

![Java](https://img.shields.io/badge/Java-17-blue?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.3-brightgreen?logo=springboot)
![Maven](https://img.shields.io/badge/Maven-Build-blue?logo=apachemaven)

> 🏷️ Built with ❤️ under the **Hexvoid** banner for clean, secure, and modular backend architecture.

---

This project demonstrates the use of JPA entity associations with an emphasis on `FetchType.EAGER` fetching strategy in a healthcare domain. It models a system with **Doctor**, **Clinic**, and **Appointment** entities, showcasing how to manage bidirectional relationships, DTO-layer abstraction, and the implications of eager fetching in a Spring Boot application.

---

## 📑 Table of Contents
- [🔍 Overview](#-overview)  
- [🛠️ Technologies Used](#-technologies-used)  
- [🧭 Project Structure](#-project-structure)  
- [🧩 Entity Relationships](#-entity-relationships)  
- [⚙️ Key Annotations and Configurations](#-key-annotations-and-configurations)  
- [📦 DTO Structure](#-dto-structure)  
- [🧪 REST API Endpoints](#-rest-api-endpoints)  
- [⚠️ Considerations on Fetch Strategies](#-considerations-on-fetch-strategies)  
- [🚀 Running the Application](#-running-the-application)  
- [📚 References](#-references)  

---

## 🔍 Overview

The application illustrates:

- 🔁 **Bidirectional Relationships**: Between Doctor and Appointment, and between Doctor and Clinic.  
- ⚡ **Eager Fetching**: Demonstrates the use of `FetchType.EAGER` in `@OneToMany` associations.  
- 🔄 **Cascade Operations**: Handles automatic persistence of related entities.  
- 🔒 **JSON Serialization**: Manages serialization of nested entities without infinite recursion.
- 📦 **DTO Pattern**: Clean separation between entities and API request/response structures.

---

## 🛠️ Technologies Used

- Java 17  
- Spring Boot 3.x  
- Spring Data JPA  
- Hibernate  
- H2 In-Memory Database  
- Jackson (for JSON serialization)  
- Maven  
- Swagger/OpenAPI (via SpringDoc)

---

## 🧭 Project Structure

```
src
└── main
    ├── java
    │   └── com
    │       └── hexvoid
    │           └── nexusjpa
    │               ├── Application.java
    │               ├── controllers
    │               │   └── DoctorController.java
    │               ├── dao
    │               │   ├── DoctorDAO.java
    │               │   └── DoctorDAOImpl.java
    │               ├── dto
    │               │   ├── AppointmentRequestDto.java
    │               │   ├── AppointmentResponseDto.java
    │               │   ├── ClinicRequestDto.java
    │               │   ├── ClinicResponseDto.java
    │               │   ├── DoctorRequestDto.java
    │               │   └── DoctorResponseDto.java
    │               ├── dtomapper
    │               │   └── DoctorMapper.java
    │               └── entity
    │                   ├── Appointment.java
    │                   ├── Clinic.java
    │                   └── Doctor.java
    └── resources
        └── application.properties

test
└── java
    └── com.hexvoid.nexusjpa.test

```
---

## 🧩 Entity Relationships

- 🩺 **Doctor ↔ Clinic**: One-to-One bidirectional relationship.  
- 📅 **Doctor ↔ Appointment**: One-to-Many bidirectional relationship.

---

## ⚙️ Key Annotations and Configurations

### 🧑‍⚕️ Doctor Entity

```java
@OneToOne(cascade = CascadeType.ALL)
@JoinColumn(name = "clinic_id")
private Clinic clinic;

@OneToMany(mappedBy = "doctor", fetch = FetchType.EAGER,
    cascade = {CascadeType.DETACH, CascadeType.MERGE,
               CascadeType.PERSIST, CascadeType.REFRESH})
@JsonManagedReference
private List<Appointment> appointments;
```

- `CascadeType.ALL`: Propagates persistence operations to `Clinic`.  
- `FetchType.EAGER`: Loads appointments immediately with doctor.  
- `@JsonManagedReference`: Handles the forward part of the reference during JSON serialization.

### 📅 Appointment Entity

```java
@ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE,
                      CascadeType.PERSIST, CascadeType.REFRESH})
@JoinColumn(name = "doctor_id")
@JsonBackReference
private Doctor doctor;
```

- `@JsonBackReference`: Handles the back part of the reference during JSON serialization to prevent infinite recursion.

### 🏥 Clinic Entity

```java
@OneToOne(mappedBy = "clinic", cascade = {CascadeType.DETACH,
    CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
@JsonIgnore
private Doctor doctor;
```

- `@JsonIgnore`: Prevents serialization of the doctor to avoid circular references.

#### 📦 JSON Serialization Helpers

| Annotation              | Purpose                                                                 |
|-------------------------|-------------------------------------------------------------------------|
| `@JsonManagedReference` | Forward part of bidirectional relationship serialization                |
| `@JsonBackReference`    | Back part to prevent infinite recursion                                 |
| `@JsonIgnore`           | Completely skips serialization of the marked field                     |


---

## 📦 DTO Structure

DTOs (Data Transfer Objects) are used to decouple the internal entity models from the external API contract. This helps maintain separation of concerns, reduces tight coupling, and ensures clean serialization/deserialization of nested relationships.

---

### 🔄 Request DTOs (Package: `com.hexvoid.nexusjpa.dto`)

-`DoctorRequestDto.java` : Accepts Doctor information from the API including nested Clinic and Appointment details.

-`ClinicRequestDto.java` : Contains clinic-specific fields, used inside `DoctorRequestDto`.

-`AppointmentRequestDto.java` : Contains appointment-specific fields, used inside `DoctorRequestDto`.

---

### 📤 Response DTOs (Package: `com.hexvoid.nexusjpa.dto`)

-`DoctorResponseDto.java` : Sends (Doctor + Clinic + Appointment) complete doctor details in API response along with nested clinic and appointments.

-`ClinicResponseDto.java` : Holds clinic data included in `DoctorResponseDto`.

-`AppointmentResponseDto.java` : Holds appointment data included in `DoctorResponseDto`.

---

### 🛠️ DTO Mapper (Package: `com.hexvoid.nexusjpa.dtomapper`)

#### 🔁 `DoctorMapper.java`
Handles the conversion between Entity classes and DTOs (both request and response), including nested mappings.  
Helps reduce boilerplate and keeps controller code clean.

---

## 🧭 Mapping Diagram

```
   [DoctorRequestDto]         [DoctorResponseDto]
         │                            ▲
         ▼                            │
     [Doctor Entity] ◀────▶ [DoctorMapper.java]
         ▲                            ▼
         │                            ▲
[ClinicRequestDto]          [ClinicResponseDto]
[AppointmentRequestDto]     [AppointmentResponseDto]
         │                            ▲
         ▼                            │
   [Clinic Entity]           [Appointment Entity]
```

This diagram shows how request/response DTOs are converted to/from entity models using the `DoctorMapper` class.

---

## 🧪 REST API Endpoints

### 💾 Save Doctor with Clinic and Appointments

- **POST** `/my/oneui/hexapi/save/data/doctor`

```json
{
  "firstName": "Anuj",
  "lastName": "Sharma",
  "specialization": "Dermatologist",
  "clinic": {
    "clinicName": "Wellness Clinic",
    "location": "Sector 21, Noida"
  },
  "appointments": [
    {
      "patientName": "Aarav Mehta",
      "appointmentDate": "2025-04-15"
    },
    {
      "patientName": "Priya Joshi",
      "appointmentDate": "2025-04-16"
    }
  ]
}
```
### 💾 Save Doctor Only

- **POST** `/my/oneui/hexapi/save/doctor`

### 🔍 Get Doctor by ID (with Appointments)

- **GET** `/my/oneui/hexapi/get/doctor/appointment/byid/{id}`

---

## ⚠️ Considerations on Fetch Strategies

Using `FetchType.EAGER` may be convenient, but it comes with performance overhead when loading large or deeply nested relationships. Always evaluate your use case:

✅ Use EAGER for small datasets or must-have relations.  
❌ Avoid it in large or deeply nested entities — prefer `FetchType.LAZY`.

ℹ️ **Also consider JSON serialization concerns** when working with bidirectional relationships—use annotations like `@JsonManagedReference`, `@JsonBackReference`, and `@JsonIgnore` to avoid infinite loops during JSON serialization.

---

## 🚀 Running the Application

```bash
cd 01_jpa-entity-associations-eager-fetch
mvn clean install
mvn spring-boot:run
```

### 🌐 Access APIs:
- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)  
- H2 Console: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)  
  - JDBC URL: `jdbc:h2:mem:doctor_db`  
  - Username: `sa`  
  - Password: *(leave blank)*

---

## 📚 References

-📘[Jakarta Persistence Spec](https://jakarta.ee/specifications/persistence/3.0/jakarta-persistence-spec-3.0.html#fetching-strategies)  
-📗[JPA FetchType (Oracle Docs)](https://docs.oracle.com/javaee/7/api/javax/persistence/FetchType.html)  
-📙[Baeldung on JPA Relationships](https://www.baeldung.com/jpa-join-types)  
-📝[Thorben Janssen Blog](https://thorben-janssen.com/)  
-🚀[Vlad Mihalcea - Hibernate Performance Tips](https://vladmihalcea.com/tutorials/hibernate/)

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
