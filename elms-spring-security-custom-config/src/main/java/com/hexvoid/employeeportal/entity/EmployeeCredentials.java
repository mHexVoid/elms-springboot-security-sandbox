package com.hexvoid.employeeportal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entity class representing employee login credentials stored in the database.
 *
 * <p>This entity holds authentication details including email, encoded password,
 * and associated roles. Mapped to the 'employee_credentials' table.</p>
 */
@Entity
@Table(name="employee_credentials")
public class EmployeeCredentials {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @Column(name="email")
    private String email;

    @Column(name="pwd")
    private String password;

    @Column(name="role")
    private  String roles;

    @Override
    public String toString() {
        return "EmployeeCredentials{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", roles='" + roles + '\'' +
                '}';
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
}
