package com.hexvoid.employeeportal.service;

import java.util.List;
import com.hexvoid.employeeportal.entity.Employees;

/**
 * =====================================================================
 * EmployeeService Interface — Business Layer for Employee Operations
 * =====================================================================
 *
 * <p><b>Purpose:</b></p>
 * <ul>
 *     <li>Defines the core business logic related to employee operations.</li>
 *     <li>Acts as a bridge between the Controller and the DAO layer.</li>
 *     <li>Ensures separation of concerns and promotes cleaner architecture.</li>
 * </ul>
 *
 * <p><b>Why Service Layer?</b></p>
 * <ul>
 *     <li>Encapsulates and centralizes business rules.</li>
 *     <li>Reduces tight coupling between controllers and data access logic.</li>
 *     <li>Makes the application easier to test and maintain.</li>
 * </ul>
 */
public interface EmployeeService {

    /**
     * Persists a new employee into the database.
     *
     * @param theEmployee The {@link Employees} entity containing employee details to be saved.
     */
    void save(Employees theEmployee);

    /**
     * Retrieves all employees from the system.
     *
     * @return A list of all {@link Employees} records.
     */
    List<Employees> findAll();

    /**
     * Retrieves a specific employee by their unique ID.
     *
     * @param id The ID of the employee to be retrieved.
     * @return The corresponding {@link Employees} entity if found; otherwise, null or exception.
     */
    Employees findById(int id);

    /**
     * Updates the information of an existing employee.
     *
     * @param employee The updated {@link Employees} entity.
     * @param id The ID of the employee to update.
     * @return The updated {@link Employees} entity.
     */
    Employees updateById(Employees employee, int id);

    /**
     * Deletes an employee from the system based on their ID.
     *
     * @param id The ID of the employee to delete.
     */
    void deleteById(int id);
}
