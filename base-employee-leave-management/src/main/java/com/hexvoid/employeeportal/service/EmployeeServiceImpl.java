package com.hexvoid.employeeportal.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.hexvoid.employeeportal.dao.EmployeeDAO;
import com.hexvoid.employeeportal.entity.Employees;
import com.hexvoid.employeeportal.exceptionhandler.MyCustomExceptionClass;
import jakarta.transaction.Transactional;

/**
 * =====================================================================
 * EmployeeServiceImpl — Implementation of EmployeeService
 * =====================================================================
 *
 * <p><b>Purpose:</b> Implements the business logic for managing employee data.</p>
 *
 * <p><b>Spring Lifecycle:</b></p>
 * <ul>
 *   <li>Marked with {@code @Service}, allowing Spring to detect and instantiate it as a singleton bean.</li>
 *   <li>Managed by the Spring IoC container for dependency injection and lifecycle management.</li>
 *   <li>This ensures business logic is consistently applied across the application.</li>
 *   <li>Acts as the bridge between the controller and the DAO layer.</li>
 * </ul>
 *
 * <p><b>Key Responsibilities:</b></p>
 * <ul>
 *   <li>Applies validation and business rules before DAO interaction.</li>
 *   <li>Handles business exceptions and provides a clean interface for controller usage.</li>
 *   <li>Uses {@code @Transactional} for data integrity and rollback on failure.</li>
 * </ul>
 */

@Service
class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeDAO theEmployeeDao;

    /**
     * Constructor-based dependency injection.
     * Spring auto-wires this DAO because there's only one constructor.
     *
     * @param theEmployeeDao The DAO responsible for data access operations.
     */
    public EmployeeServiceImpl(EmployeeDAO theEmployeeDao) {
        this.theEmployeeDao = theEmployeeDao;
    }

    /**
     * Saves a new employee to the database.
     * Throws an exception if an ID is provided to prevent overriding existing records.
     *
     * @param theEmployee The employee entity to persist.
     * @throws MyCustomExceptionClass if the ID is not null.
     */
    @Override
    @Transactional
    public void save(Employees theEmployee) {
        if (theEmployee.getId() != null) {
            throw new MyCustomExceptionClass("Employee ID should not be provided for a new employee.");
        }
        theEmployeeDao.save(theEmployee);
    }

    /**
     * Fetches all employee records from the database.
     *
     * @return A list of all employees.
     */
    @Override
    public List<Employees> findAll() {
        return theEmployeeDao.findAll();
    }

    /**
     * Fetches a single employee based on the provided ID.
     *
     * @param id The unique ID of the employee.
     * @return The employee entity if found.
     * @throws MyCustomExceptionClass if the employee is not found.
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
     * Updates an existing employee based on ID.
     * Performs null checks on fields to avoid overwriting existing data unintentionally.
     *
     * @param employee The updated employee object.
     * @param id The ID of the employee to update.
     * @return The updated employee.
     * @throws MyCustomExceptionClass if the ID mismatch occurs or the employee doesn't exist.
     */
    @Override
    @Transactional
    public Employees updateById(Employees employee, int id) {
        if (employee.getId() != id) {
            throw new MyCustomExceptionClass("Requested ID " + id + " and Employee ID " + employee.getId() + " do not match.");
        }

        Employees existingEmployee = theEmployeeDao.findById(id);
        if (existingEmployee == null) {
            throw new MyCustomExceptionClass("Employee with ID " + id + " not found.");
        }

        // Only update fields that are not null
        if (employee.getName() != null) {
            existingEmployee.setName(employee.getName());
        }
        if (employee.getEmail() != null) {
            existingEmployee.setEmail(employee.getEmail());
        }
        if (employee.getDepartment() != null) {
            existingEmployee.setDepartment(employee.getDepartment());
        }

        return theEmployeeDao.updateById(existingEmployee, id);
    }

    /**
     * Deletes an employee based on their ID.
     *
     * @param id The ID of the employee to delete.
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
