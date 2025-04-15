package com.hexvoid.employeeportal.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hexvoid.employeeportal.dao.EmployeeDAO;
import com.hexvoid.employeeportal.entity.Employees;
import com.hexvoid.employeeportal.exceptionhandler.MyCustomExceptionClass;

/**
 * EmployeeLeaveRequestServiceImpl serves as the service layer for handling Employee and LeaveRequest operations.
 * It implements both EmployeeService and LeaveRequestService interfaces to manage employees and their leave requests.
 * 
 * ✅ Uses @Service annotation to mark this as a Spring-managed service component.
 * ✅ Implements both EmployeeService and LeaveRequestService to handle respective functionalities.
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    /**
     * DAO dependencies for Employee and LeaveRequest operations.
     */
    private final EmployeeDAO employeeDao; // Constructor-injected
    

    /**
     * Constructor Injection for EmployeeDAO.
     * 
     * ✅ Constructor injection is recommended over setter injection because:
     *    - It makes dependencies immutable (good for security and reliability).
     *    - Ensures required dependencies are provided at object creation.
     *    - Helps with unit testing by allowing dependency injection via constructor.
     *    
     * ⚠️ Disadvantage: If there are many dependencies, the constructor may become too long.
     */
    @Autowired
    public EmployeeServiceImpl(EmployeeDAO employeeDao) {
        this.employeeDao = employeeDao;
    }

   
    // ============================ Employee Operations ============================

    /**
     * Adds a new employee.
     * @param theEmployee Employee entity to be added.
     */
    @Override
	public
    Employees save(Employees theEmployee) {
        return employeeDao.save(theEmployee);
    }

    /**
     * Retrieves all employees.
     * @return List of all employees.
     */
    @Override
    public List<Employees> findAll() {
        return employeeDao.findAll();
    }

    /**
     * Finds an employee by ID.
     * Throws an exception if not found.
     * 
     * @param id Employee ID.
     * @return Employee entity.
     */
    @Override
    public Employees findById(Long id) {
         Optional<Employees> byId = employeeDao.findById(id);
         return byId.orElseThrow(()-> new MyCustomExceptionClass("Employee ID not found - " + id));
    }

  

    /**
     * Deletes an employee by ID.
     * @param id Employee ID.
     */
    @Override
    public void deleteById(Long id) {
        employeeDao.deleteById(id);
    }


	
}
