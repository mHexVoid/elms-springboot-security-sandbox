package com.hexvoid.employeeportal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hexvoid.employeeportal.dao.EmployeeDAO;
import com.hexvoid.employeeportal.dao.LeaveRequestDao;
import com.hexvoid.employeeportal.entity.Employees;
import com.hexvoid.employeeportal.entity.LeaveRequests;
import com.hexvoid.employeeportal.exceptionhandler.MyCustomExceptionClass;

import jakarta.transaction.Transactional;

/**
 * Service Implementation for managing leave requests.
 * 
 * This class provides the business logic for leave request operations
 * and interacts with the DAO layer to perform database transactions.
 * It ensures validations and business rules are applied before
 * persisting or modifying leave request records.
 */
@Service
public class LeaveRequestServiceImpl implements LeaveRequestService {

	// DAO instance for Leave Requests
	private LeaveRequestDao theLeaveRequestDao;

	// DAO instance for Employee data access
	private EmployeeDAO theEmpDao;

	/**
	 * Setter Injection for LeaveRequestDao and EmployeeDao.
	 * 
	 *  Advantages of Setter Injection:
	 *    - Allows for optional dependencies, making it flexible.
	 *    - Useful when we want to change the dependency implementation dynamically.
	 * 
	 *   Disadvantages of Setter Injection:
	 *    - Makes the object mutable, which can lead to unexpected behavior.
	 *    - Not ideal for mandatory dependencies since the dependency may be unset.
	 *    - Can introduce a **Cyclic Dependency Issue** if two beans depend on each other. 
	 * 
	 *   **Cyclic Dependency Issue Explained:**
	 *    - Occurs when two or more beans depend on each other, forming an infinite loop.
	 *    - Example:
	 *        ```
	 *        @Service
	 *        public class EmployeeService {
	 *            @Autowired
	 *            private DepartmentService departmentService;
	 *        }
	 *
	 *        @Service
	 *        public class DepartmentService {
	 *            @Autowired
	 *            private EmployeeService employeeService;
	 *        }
	 *        ```
	 *    - This leads to a failure in bean initialization because Spring cannot resolve the dependency cycle.
	 * 
	 *  **Solutions to Avoid Cyclic Dependency:**
	 *    1. **Use Constructor Injection (Recommended)**
	 *       - Preferred for mandatory dependencies.
	 *       - Helps enforce immutability and ensures the dependency is always set.
	 *    2. **Use `@Lazy` Annotation**  
	 *       - Delays the initialization of one of the beans until required.
	 *    3. **Refactor the Code Using an Interface**  
	 *       - Introduce an intermediary layer to break the direct dependency.
	 * 
	 * Note: Setter injection requires `@Autowired` explicitly because Spring 
	 * does not automatically inject dependencies into setter methods.
	 * 
	 * @param theLeaveRequestDao DAO for managing leave requests.
	 * @param theEmpDao DAO for managing employee data.
	 */

	@Autowired
	public void setLeaveRequestDao(LeaveRequestDao theLeaveRequestDao, EmployeeDAO theEmpDao) {
		this.theLeaveRequestDao = theLeaveRequestDao;
		this.theEmpDao = theEmpDao;
	}

	/**
	 * Saves a new leave request.
	 * 
	 * @param theLeaveRequest The leave request entity containing details.
	 * @throws MyCustomExceptionClass If an ID is provided for a new leave request.
	 */
	@Override
	@Transactional
	public void save(LeaveRequests theLeaveRequest) {

		if (theLeaveRequest.getId() != null) {
			throw new MyCustomExceptionClass("Leave ID should not be provided for a new Leave Request.");
		}

		//Before persisting the leave find out if employee ID is valid
		int employeeID = theLeaveRequest.getEmployee().getId().intValue();
		Employees employeeById = theEmpDao.findById(employeeID);
		if(employeeById.equals(null)) {
			throw new MyCustomExceptionClass("Not a valid Employee Id "+ employeeID);
		}


		theLeaveRequestDao.save(theLeaveRequest);
	}

	/**
	 * Fetches all leave requests from the database.
	 * 
	 * @return List of all leave requests.
	 */
	@Override
	public List<LeaveRequests> findAll() {
		return theLeaveRequestDao.findAll();
	}

	/**
	 * Fetches a specific leave request by ID.
	 * 
	 * @param theID The ID of the leave request.
	 * @return The leave request entity.
	 */
	@Override
	public LeaveRequests findById(int theID) {
		return theLeaveRequestDao.findById(theID);
	}

	/**
	 * Updates an existing leave request based on the provided ID.
	 * 
	 * This method performs the following validations and updates:
	 * - Ensures that the leave request ID in the request body matches the ID in the URL.
	 * - Checks if the leave request exists in the database before proceeding.
	 * - Validates whether the associated employee exists based on both name and ID.
	 * - Updates only non-null fields in the existing leave request.
	 * 
	 * @param theLeaveRequest The updated leave request details received from the client.
	 * @param id The unique identifier of the leave request to be updated.
	 * @return The updated leave request object after persistence.
	 * @throws MyCustomExceptionClass If the provided leave request ID does not match the URL ID.
	 */
	@Override
	@Transactional
	public LeaveRequests updateById(LeaveRequests theLeaveRequest, int id) {

		// Validate if the ID in the request body matches the ID in the URL path
		if (theLeaveRequest.getId() != id) {
			throw new MyCustomExceptionClass("The Requested Leave ID " + id + 
					" does not match the Leave ID in the request body: " + theLeaveRequest.getId());
		}

		// Retrieve the existing leave request from the database
		// If not found, findById() will throw an exception handled by Controller Advice
		LeaveRequests existingLeaveRequest = theLeaveRequestDao.findById(id);

		// Extract employee details from the request body for validation
		String employeeName = theLeaveRequest.getEmployee().getName();

		// Convert the employee ID from Long (wrapper type) to primitive int
		int employeeId = theLeaveRequest.getEmployee().getId().intValue();

		// Validate if the employee exists in the system
		// findByName() will throw an exception if the employee is not found
		if (!theEmpDao.findByName(employeeName).getName().equals(employeeName)) {
			throw new MyCustomExceptionClass("Employee with name '" + employeeName + "' does not exist.");
		}

		// Validate if the employee ID exists in the system
		// findById() will throw an exception if the employee ID is invalid
		if (theEmpDao.findById(employeeId) == null) {
			throw new MyCustomExceptionClass("Invalid Employee ID: " + employeeId);
		}

		// Update only the fields that are not null in the request object
		if (theLeaveRequest.getLeaveType() != null) {
			existingLeaveRequest.setLeaveType(theLeaveRequest.getLeaveType());
		}
		if (theLeaveRequest.getStartDate() != null) {
			existingLeaveRequest.setStartDate(theLeaveRequest.getStartDate());
		}
		if (theLeaveRequest.getEndDate() != null) {
			existingLeaveRequest.setEndDate(theLeaveRequest.getEndDate());
		}
		if (theLeaveRequest.getLeaveStatus() != null) {
			existingLeaveRequest.setLeaveStatus(theLeaveRequest.getLeaveStatus());
		}

		// Persist the updated leave request in the database and return the updated entity
		return theLeaveRequestDao.updateById(existingLeaveRequest, id);
	}

	/**
	 * Deletes a leave request by ID.
	 * 
	 * @param id The ID of the leave request to delete.
	 */
	@Override
	@Transactional
	public void deleteById(int id) {
		theLeaveRequestDao.deleteById(id);
	}
}
