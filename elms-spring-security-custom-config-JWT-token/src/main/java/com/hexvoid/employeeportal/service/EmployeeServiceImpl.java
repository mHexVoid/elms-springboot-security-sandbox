package com.hexvoid.employeeportal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hexvoid.employeeportal.dao.EmployeeDAO;
import com.hexvoid.employeeportal.entity.Employees;
import com.hexvoid.employeeportal.exceptionhandler.MyCustomExceptionClass;

import jakarta.transaction.Transactional;

/**
 * Implementation of EmployeeService interface.
 * This class contains business logic for managing employee records.
 * 
 * Lifecycle of @Service:
 * - Spring automatically detects this class as a service component.
 * - It is instantiated as a singleton and managed by the Spring IoC container.
 * - This ensures business logic is consistently applied across the application.
 */
@Service
class EmployeeServiceImpl implements EmployeeService {

	// Dependency on EmployeeDAO for database operations.
	private final EmployeeDAO theEmployeeDao;

	/**
	 * Constructor-based dependency injection.
	 * - This method injects EmployeeDAO into EmployeeServiceImpl.
	 * - @Autowired is not required for a single constructor, as Spring Boot automatically wires it.
	 *
	 * @param theEmployeeDao DAO instance for Employee database operations.
	 */
	public EmployeeServiceImpl(EmployeeDAO theEmployeeDao) {
		this.theEmployeeDao = theEmployeeDao;
	}

	/**
	 * Saves a new employee record in the system.
	 * - Throws an exception if an employee ID is provided, ensuring proper creation logic.
	 * - Calls DAO layer to persist the employee in the database.
	 * 
	 * @param theEmployee Employee entity to be added.
	 * @throws MyCustomExceptionClass if an ID is provided in the request.
	 */
	@Override
	@Transactional // Ensures atomicity of the database operation.
	public void save(Employees theEmployee) {
		if (theEmployee.getId() != null) {
			throw new MyCustomExceptionClass("Employee ID should not be provided for a new employee.");
		}
		theEmployeeDao.save(theEmployee);
	}

	/**
	 * Retrieves all employee records from the database.
	 * - Calls DAO layer to fetch the data.
	 * - Returns a list of all employees.
	 * 
	 * @return List of Employee entities.
	 */
	@Override
	public List<Employees> findAll() {
		return theEmployeeDao.findAll();
	}

	/**
	 * Finds an employee record by its unique ID.
	 * - Calls DAO layer to fetch the employee details.
	 * - If the employee is not found, an exception is thrown.
	 * 
	 * @param id Employee ID to search for.
	 * @return Employee entity if found.
	 * @throws MyCustomExceptionClass if the employee does not exist.
	 */
	@Override
	public Employees findById(int id) {
		Employees employee = theEmployeeDao.findById(id);
		if (employee == null) {
			throw new MyCustomExceptionClass("Employee with ID: " + id + " not found.");
		}
		return employee;
	}

	/**
	 * Updates an existing employee record using its ID.
	 * - Ensures the ID in the request matches the ID in the database.
	 * - Updates only non-null fields to prevent data loss.
	 * - Calls DAO layer to update the data.
	 * 
	 * @param employee Employee entity containing updated details.
	 * @param id Employee ID to update.
	 * @return Updated Employee entity.
	 * @throws MyCustomExceptionClass if ID mismatch occurs.
	 */
	@Override
	@Transactional
	public Employees updateById(Employees employee, int id) {
		if (employee.getId() != id) {
			throw new MyCustomExceptionClass("Requested ID " + id + " and Employee ID " + employee.getId() + " do not match.");
		}

		// Fetch existing employee record
		Employees existingEmployee = theEmployeeDao.findById(id);
		if (existingEmployee == null) {
			throw new MyCustomExceptionClass("Employee with ID " + id + " not found.");
		}

		// Updating only non-null fields
		if (employee.getDepartment() != null) {
			existingEmployee.setDepartment(employee.getDepartment());
		}
		if (employee.getEmail() != null) {
			existingEmployee.setEmail(employee.getEmail());
		}
		if (employee.getName() != null) {
			existingEmployee.setName(employee.getName());
		}

		// Save updated employee details
		return theEmployeeDao.updateById(existingEmployee, id);
	}

	/**
	 * Deletes an employee record by ID.
	 * - Calls DAO layer to remove the employee from the database.
	 * - Throws an exception if the employee is not found.
	 * 
	 * @param id Employee ID to delete.
	 * @throws MyCustomExceptionClass if the employee does not exist.
	 */
	@Override
	@Transactional
	public void deleteById(int id) {
		Employees employee = theEmployeeDao.findById(id);
		if (employee == null) {
			throw new MyCustomExceptionClass("Employee with ID " + id + " does not exist for deletion.");
		}
		theEmployeeDao.deleteById(id);
	}
}
