package com.hexvoid.employeeportal.service;

import java.util.List;

import com.hexvoid.employeeportal.entity.Employees;

/**
 * EmployeeService interface defines the business logic for Employee-related operations.
 * It acts as a service layer, ensuring a separation between controllers and the DAO layer.
 * 
 * Purpose of Service Layer:
 * - Provides abstraction over DAO.
 * - Contains business logic and validation.
 * - Reduces tight coupling between controller and database operations.
 */
public interface EmployeeService  {

    /**
     * Saves a new employee record in the system.
     * Calls DAO layer to persist employee data.
     *
     * @param theEmployee Employee entity containing details to be stored.
     */
    void save(Employees theEmployee);

    /**
     * Retrieves all employee records from the system.
     * Calls DAO layer to fetch the data.
     *
     * @return List of Employee entities.
     */
    List<Employees> findAll();

    /**
     * Finds a specific employee by their unique ID.
     * Calls DAO layer to retrieve the data.
     *
     * @param id Employee ID to search for.
     * @return Employee entity if found.
     */
    Employees findById(int id);

    /**
     * Updates an existing employee record using its ID.
     * Calls DAO layer to modify the data.
     *
     * @param employee Employee entity containing updated details.
     * @param id Employee ID to update.
     * @return Updated Employee entity.
     */
    Employees updateById(Employees employee, int id);

    /**
     * Deletes an employee record from the system based on ID.
     * Calls DAO layer to remove the data.
     *
     * @param id Employee ID to delete.
     */
    void deleteById(int id);
}
