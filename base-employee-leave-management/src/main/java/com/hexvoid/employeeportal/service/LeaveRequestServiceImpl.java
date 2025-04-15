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
 * <p>This class provides the business logic for leave request operations
 * and interacts with the DAO layer to perform database transactions.
 * It ensures validations and business rules are applied before persisting
 * or modifying leave request records.</p>
 * 
 * <p><b>Key Responsibilities:</b></p>
 * <ul>
 *   <li>Handles the business logic and interacts with the DAO layer for database transactions.</li>
 *   <li>Validates input data such as employee existence and leave request data.</li>
 *   <li>Applies business rules for leave requests before they are persisted or updated.</li>
 * </ul>
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
	 * <p><b>Advantages of Setter Injection:</b></p>
	 * <ul>
	 *   <li>Allows for optional dependencies, making it flexible.</li>
	 *   <li>Useful when we want to change the dependency implementation dynamically.</li>
	 * </ul>
	 * 
	 * <p><b>Disadvantages of Setter Injection:</b></p>
	 * <ul>
	 *   <li>Makes the object mutable, which can lead to unexpected behavior.</li>
	 *   <li>Not ideal for mandatory dependencies since the dependency may be unset.</li>
	 *   <li>Can introduce a <b>Cyclic Dependency Issue</b> if two beans depend on each other.</li>
	 * </ul>
	 * 
	 * <p><b>Cyclic Dependency Issue Explained:</b></p>
	 * <ul>
	 *   <li>Occurs when two or more beans depend on each other, forming an infinite loop.</li>
	 *   <li>Example:
	 *     <pre>
	 *     
	 *     @Service
	 *     public class EmployeeService {
	 *         @Autowired
	 *         private DepartmentService departmentService;
	 *     }
	 *
	 *     @Service
	 *     public class DepartmentService {
	 *         @Autowired
	 *         private EmployeeService employeeService;
	 *     }
	 *     
	 *     </pre>
	 *   </li>
	 *   <li>This leads to a failure in bean initialization because Spring cannot resolve the dependency cycle.</li>
	 * </ul>
	 * 
	 * <p><b>Solutions to Avoid Cyclic Dependency:</b></p>
	 * <ol>
	 *   <li>Use Constructor Injection (Recommended) - Preferred for mandatory dependencies.</li>
	 *   <li>Use {@code @Lazy} Annotation - Delays the initialization of one of the beans until required.</li>
	 *   <li>Refactor the Code Using an Interface - Introduce an intermediary layer to break the direct dependency.</li>
	 * </ol>
	 * 
	 * <p><b>Note:</b> Setter injection requires {@code @Autowired} explicitly because Spring 
	 * does not automatically inject dependencies into setter methods.</p>
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
	 * <p><b>Process:</b></p>
	 * <ul>
	 *   <li>Validates that the leave request does not contain an ID (since it's a new leave request).</li>
	 *   <li>Checks if the employee associated with the leave request exists.</li>
	 *   <li>Saves the leave request in the database.</li>
	 * </ul>
	 *
	 * @param theLeaveRequest The leave request entity containing details.
	 * @throws MyCustomExceptionClass If an ID is provided for a new leave request or the employee is not found.
	 */
	@Override
	@Transactional
	public void save(LeaveRequests theLeaveRequest) {

		// Ensure no ID is provided for a new leave request
		if (theLeaveRequest.getId() != null) {
			throw new MyCustomExceptionClass("Leave ID should not be provided for a new Leave Request.");
		}

		// Validate if employee ID is valid
		int employeeID = theLeaveRequest.getEmployee().getId().intValue();
		Employees employeeById = theEmpDao.findById(employeeID);
		if (employeeById == null) {
			throw new MyCustomExceptionClass("Not a valid Employee Id " + employeeID);
		}

		// Save the leave request in the DAO
		theLeaveRequestDao.save(theLeaveRequest);
	}

	/**
	 * Fetches all leave requests from the database.
	 * 
	 * <p><b>Process:</b></p>
	 * <ul>
	 *   <li>Retrieves a list of all leave requests from the database.</li>
	 * </ul>
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
	 * <p><b>Process:</b></p>
	 * <ul>
	 *   <li>Retrieves the leave request based on the provided ID.</li>
	 * </ul>
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
	 * <p>This method performs the following validations and updates:</p>
	 * <ul>
	 *   <li>Ensures that the leave request ID in the request body matches the ID in the URL.</li>
	 *   <li>Checks if the leave request exists in the database before proceeding.</li>
	 *   <li>Validates whether the associated employee exists based on both name and ID.</li>
	 *   <li>Updates only non-null fields in the existing leave request.</li>
	 * </ul>
	 *
	 * @param theLeaveRequest The updated leave request details received from the client.
	 * @param id The unique identifier of the leave request to be updated.
	 * @return The updated leave request object after persistence.
	 * @throws MyCustomExceptionClass If the provided leave request ID does not match the URL ID or employee is not found.
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
		int employeeId = theLeaveRequest.getEmployee().getId().intValue();

		if (!theEmpDao.findByName(employeeName).getName().equals(employeeName)) {
			throw new MyCustomExceptionClass("Employee with name '" + employeeName + "' does not exist.");
		}

		if (theEmpDao.findById(employeeId) == null) {
			throw new MyCustomExceptionClass("Invalid Employee ID: " + employeeId);
		}

		// Update non-null fields
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

		// Persist the updated leave request
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
