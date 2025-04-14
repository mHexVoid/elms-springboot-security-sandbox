package com.hexvoid.employeeportal.dao;

import com.hexvoid.employeeportal.entity.EmployeeCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository interface for accessing EmployeeCredentials data from the database.
 * <p>
 * Extends JpaRepository to provide CRUD operations. Contains a custom query method
 * to fetch credentials along with associated authorities based on email.
 */
public interface EmployeeCredentialsDao extends JpaRepository<EmployeeCredentials, Integer> {

	/**
	 * Fetches EmployeeCredentials along with eagerly loaded authorities based on email.
	 * <p>
	 * The LEFT JOIN FETCH ensures authorities are loaded in the same query to avoid
	 * LazyInitializationException when accessed outside the persistence context.
	 *
	 * @param email the email of the employee
	 * @return the EmployeeCredentials entity with roles/authorities loaded
	 */

	@Query("SELECT ec FROM EmployeeCredentials ec LEFT JOIN FETCH ec.employeeAuthorities WHERE ec.email = :email")
	EmployeeCredentials findByEmail(@Param("email") String email);

	// save() is inherited from JpaRepository; explicitly redeclared for clarity or documentation.
	EmployeeCredentials save(EmployeeCredentials employeeCredentials);
}