package com.hexvoid.employeeportal.service;

import com.hexvoid.employeeportal.entity.EmployeeCredentials;

/**
 * Service interface for managing secure employee login credentials.
 *
 * <p>Abstracts the persistence and retrieval logic for credential-related operations.</p>
 */
public interface EmployeeSecretsService {

    /**
     * Finds an employee credentials record by email.
     *
     * @param email the email to search by
     * @return the corresponding credentials entity
     */
    EmployeeCredentials findByEmail(String email);

    /**
     * Saves the given employee credentials.
     *
     * @param employeeCredentials the credentials to persist
     * @return the saved entity
     */
    EmployeeCredentials save(EmployeeCredentials employeeCredentials);
}
