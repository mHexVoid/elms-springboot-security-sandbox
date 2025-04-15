package com.hexvoid.employeeportal.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hexvoid.employeeportal.entity.Employees;
import com.hexvoid.employeeportal.exceptionhandler.MyCustomExceptionClass;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

/**
 * Implementation class for EmployeeDAO.
 * This class provides concrete implementations of CRUD operations
 * using JPA's EntityManager for database interactions.
 *
 * @Repository - Marks this class as a DAO (Data Access Object) component.
 *               It allows Spring to recognize this class as a bean and handle
 *               transaction management automatically.
 *               Lifecycle: Created once at application startup and managed by Spring.
 */
@Repository
public class EmployeeDaoImpl implements EmployeeDAO {

	/**
	 * EntityManager is responsible for handling database operations in JPA.
	 * It allows us to perform CRUD (Create, Read, Update, Delete) operations.
	 *
	 * @Autowired - Injects the EntityManager bean automatically when this class is initialized.
	 *              Lifecycle: It is created at startup and injected into this class.
	 *
	 * Important: EntityManager is **not thread-safe**, so it's recommended to use it within transactions.
	 */
	private final EntityManager entityManager;

	/**
	 * Constructor-based Dependency Injection for EntityManager.
	 * Spring Boot automatically injects the required bean at runtime.
	 *
	 * @param entityManager The JPA EntityManager that interacts with the database.
	 */
	@Autowired
	public EmployeeDaoImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/**
	 * Saves a new employee record to the database.
	 *
	 * persist() - This method makes the given entity managed and inserts it into the database.
	 *             - It does NOT return the entity because the entity is managed by JPA after persistence.
	 *             - If the entity already exists (has an ID), an exception will be thrown.
	 *
	 * @param theEmployee The Employee entity containing details to be saved.
	 */
	@Override
	public void save(Employees theEmployee) {
		entityManager.persist(theEmployee);  // Inserts the employee object into the database
	}

	/**
	 * Retrieves all employees from the database.
	 *
	 * Uses a JPQL query (Java Persistence Query Language) to fetch all employee records.
	 * - 'from Employees' is equivalent to 'SELECT * FROM employees' in SQL.
	 * - TypedQuery ensures type safety when querying the database.
	 *
	 * @return List of Employee objects.
	 * @throws MyCustomExceptionClass if no employees are found in the database.
	 */
	@Override
	public List<Employees> findAll() {
		TypedQuery<Employees> query = entityManager.createQuery("from Employees", Employees.class);
		List<Employees> employeesList = query.getResultList();

		if (employeesList.isEmpty()) {
			throw new MyCustomExceptionClass("No employees found in the database.");
		}
		return employeesList;
	}

	/**
	 * Finds an employee based on the given ID.
	 *
	 * find() - Retrieves an entity from the database using its primary key.
	 *          - Returns null if the entity does not exist.
	 *
	 * @param id The unique Employee ID to search.
	 * @return Employee entity if found.
	 * @throws MyCustomExceptionClass if the employee does not exist.
	 */
	@Override
	public Employees findById(int id) {
		Employees employee = entityManager.find(Employees.class, id);
		if (employee == null) {
			throw new MyCustomExceptionClass("Employee with ID " + id + " not found.");
		}
		return employee;
	}

	/**
	 * Updates an existing employee's details.
	 *
	 * merge() - Synchronizes the given detached entity with the database.
	 *           - If the entity does not exist, it creates a new one.
	 *           - Returns a managed instance of the updated entity.
	 *
	 * @param theEmployee Employee object with updated values.
	 * @param id Employee ID to update.
	 * @return Updated Employee entity.
	 */
	@Override
	public Employees updateById(Employees theEmployee, int id) {
		return entityManager.merge(theEmployee);
	}

	/**
	 * Deletes an employee by ID.
	 *
	 * remove() - Deletes the entity from the database.
	 *           - The entity must be managed before calling remove().
	 *
	 * @param id The Employee ID to delete.
	 * @throws MyCustomExceptionClass if the employee is not found.
	 */
	@Override
	public void deleteById(int id) {
		Employees employeeToDelete = entityManager.find(Employees.class, id);
		if (employeeToDelete == null) {
			throw new MyCustomExceptionClass("Cannot delete. Employee with ID " + id + " not found.");
		}
		entityManager.remove(employeeToDelete);
	}

	/**
	 * Finds an employee by their name.
	 *
	 * Uses a JPQL query to search for employees by name.
	 * - `getSingleResult()` is used because we expect a unique employee.
	 * - If no result is found, a `NoResultException` is thrown and handled appropriately.
	 *
	 * Spring Security & Exception Handling:
	 * - This method’s endpoint is configured with `permitAll()`, making it accessible without authentication.
	 * - However, Spring Security might still return a `401 Unauthorized` if an exception occurs, 
	 *   as security filters apply before reaching `@ControllerAdvice`.
	 * - To ensure that the proper error response is returned, we explicitly catch `NoResultException` 
	 *   and throw a custom exception (`MyCustomExceptionClass`).
	 *
	 * Key Considerations:
	 * - `@ControllerAdvice` correctly catches the exception and prevents the default `401 Unauthorized` response.
	 * - Custom exception handling ensures that the intended error message is returned.
	 * - If Spring Security still overrides the response, review `AccessDeniedHandler` and `AuthenticationEntryPoint`.
	 *
	 * @param name The Employee name to search for.
	 * @return Employee entity if found.
	 * @throws MyCustomExceptionClass if no employee with the given name is found.
	 */
	@Override
	public Employees findByName(String name) {
		TypedQuery<Employees> empByName = entityManager.createQuery(
				"select r from Employees r where r.name =:name", Employees.class
				);
		empByName.setParameter("name", name);
		Employees employeeName;

		try {
			// `getSingleResult()` retrieves a single record from the database.
			// If no record is found, it throws `NoResultException`, which we handle in the catch block.
			// In contrast, `getResultList()` would return an empty list instead of throwing an exception.
			employeeName = empByName.getSingleResult();
		} catch (NoResultException e) {
			// Custom exception handling ensures that a meaningful error response is returned
			// instead of a potential Spring Security `401 Unauthorized`.
			throw new MyCustomExceptionClass("Please Enter Valid Employee Name");
		}

		// Debugging statement to print the found employee (optional, should be removed in production).
		System.out.println(employeeName);

		return employeeName;
	}

}