package com.hexvoid.employeeportal.dao;

import java.util.List;

import com.hexvoid.employeeportal.entity.Employees;

/**
 * EmployeeDAO interface defines the contract for CRUD operations 
 * related to Employee entities.
 *
 * Key Responsibilities:
 * - Declares methods for saving, retrieving, updating, and deleting employees.
 * - Acts as an abstraction layer between the service and database implementation.
 * - Ensures consistency across different data access implementations.
 */
public interface EmployeeDAO {

	/**
	 * Saves a new employee record to the database.
	 *
	 * Important Notes:
	 * - Uses `persist()`, which inserts a new record but does **not** return the entity.
	 * - The entity must be managed by the persistence context to reflect changes.
	 *
	 * @param theEmployee Employee entity containing details to be saved.
	 */
	void save(Employees theEmployee);

	/**
	 * Retrieves all employees from the database.
	 * 
	 * @return List of all Employee entities.
	 */
	List<Employees> findAll();

	/**
	 * Finds an employee by their unique ID.
	 *
	 * @param id Employee ID to search for.
	 * @return Employee entity if found, otherwise `null`.
	 */
	Employees findById(int id);

	/**
	 * Updates an employee's details using their ID.
	 *
	 * Important Notes:
	 * - Uses `merge()`, which updates the entity **if it exists** or inserts a new one if not.
	 * - Unlike `persist()`, `merge()` returns the updated entity.
	 *
	 * @param employee Employee entity with updated details.
	 * @param id Employee ID to update.
	 * @return Updated Employee entity.
	 */
	Employees updateById(Employees employee, int id);

	/**
	 * Deletes an employee record based on the given ID.
	 *
	 * @param id Employee ID to delete.
	 */
	void deleteById(int id);

	/**
	 * Finds an employee by their name.
	 *
	 * @param name Employee name to search for.
	 * @return Employee entity if found, otherwise `null`.
	 */
	Employees findByName(String name);
}