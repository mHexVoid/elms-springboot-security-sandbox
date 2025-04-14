package com.hexvoid.employeeportal.service;

import com.hexvoid.employeeportal.entity.EmployeeCredentials;

/**
 * Service interface defining methods related to managing employee login credentials.
 * <p>
 * Intended to abstract DAO interaction and enable business logic handling around
 * employee authentication data (e.g., passwords, roles).
 */
public interface EmployeeSecretsService {

	/**
	 * Finds employee credentials by email.
	 *
	 * @param email the employee's email
	 * @return EmployeeCredentials if found; null otherwise
	 */
	EmployeeCredentials findByEmail(String email);

	/**
	 * Saves the employee credentials to the database.
	 *
	 * @param employeeCredentials credentials to save
	 * @return the saved EmployeeCredentials
	 */
	EmployeeCredentials save(EmployeeCredentials employeeCredentials);
}
