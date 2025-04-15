package com.hexvoid.employeeportal.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Represents an Employee in the system.
 * 
 * This entity maps to the `employees` table in the database.
 * It stores employee details like name, email, and department.
 * It uses Hibernate and JPA for ORM (Object-Relational Mapping).
 */
@Entity // Marks this class as a JPA entity (table in DB)
@Table(name = "employees") // Maps this entity to the 'employees' table
public class Employees {

    /**
     * PRIMARY KEY
     * - `@Id`: Marks this field as the primary key.
     * - `@GeneratedValue(strategy = GenerationType.IDENTITY)`: Uses AUTO_INCREMENT in MySQL.
     * - `@Column(name = "id", updatable = false, nullable = false)`: Ensures `id` cannot be updated or null.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID (Primary Key)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    /**
     * EMPLOYEE NAME
     * - `@Column(name = "name", nullable = false, length = 255)`: Maps to the 'name' column.
     * - `nullable = false`: Name cannot be null.
     * - `length = 255`: Limits name length to 255 characters.
     */
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    /**
     * EMPLOYEE EMAIL
     * - `@Column(name = "email", nullable = false, unique = true, length = 255)`: 
     *   - Ensures each employee has a unique email.
     *   - Email cannot be null.
     */
    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    /**
     * DEPARTMENT NAME
     * - `@Column(name = "department", length = 100)`: Stores department name.
     * - `length = 100`: Limits department name to 100 characters.
     */
    @Column(name = "department", length = 100)
    private String department;

    /**
     * CREATED AT TIMESTAMP (AUTOMATIC)
     * - `@CreationTimestamp`: Automatically stores the timestamp when a record is inserted.
     * - `updatable = false`: This field will never be updated once set.
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    // ✅ Default Constructor (JPA requires a no-argument constructor)
    public Employees() {}

    // ✅ Getters and Setters (For accessing private fields)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Employees{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
