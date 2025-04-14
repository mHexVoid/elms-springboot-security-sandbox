package com.hexvoid.employeeportal.dao;

import com.hexvoid.employeeportal.entity.EmployeeCredentials;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for performing CRUD operations on EmployeeCredentials entity.
 *
 * <p>Extends JpaRepository to leverage Spring Data JPA's built-in methods.
 * Provides a custom method to retrieve credentials by email.</p>
 */
public interface EmployeeCredentialsDao extends JpaRepository<EmployeeCredentials, Integer> {

    /**
     * Retrieves employee credentials using the email address.
     *
     * @param email the email to search by
     * @return EmployeeCredentials entity matching the email, or null if not found
     */
    EmployeeCredentials findByEmail(String email);

    // save() method is already inherited from JpaRepository,
    // redeclaring it here is optional but allowed for clarity or interface documentation.
    EmployeeCredentials save(EmployeeCredentials employeeCredentials);
}
