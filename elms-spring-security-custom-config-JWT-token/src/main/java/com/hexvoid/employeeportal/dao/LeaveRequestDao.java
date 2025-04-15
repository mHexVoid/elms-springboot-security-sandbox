package com.hexvoid.employeeportal.dao;

import java.util.List;

import com.hexvoid.employeeportal.entity.LeaveRequests;

/**
 * LeaveRequestDao interface defines the contract for database operations 
 * related to leave requests.
 * 
 * This interface follows the Data Access Object (DAO) design pattern, which 
 * is responsible for abstracting and encapsulating all access to the data source.
 * 
 * The implementing class will provide the actual database interaction logic.
 */
public interface LeaveRequestDao {

	/**
	 * Saves a new leave request in the database.
	 * 
	 * @param theLeaveRequest The LeaveRequests entity containing leave details.
	 */
	void save(LeaveRequests theLeaveRequest);

	/**
	 * Retrieves all leave requests from the database.
	 * 
	 * @return A list of LeaveRequests entities representing all leave records.
	 */
	List<LeaveRequests> findAll();

	/**
	 * Finds a specific leave request using its unique ID.
	 * 
	 * @param theID The unique identifier of the leave request.
	 * @return The LeaveRequests entity corresponding to the given ID.
	 */
	LeaveRequests findById(int theID);

	/**
	 * Updates a leave request based on its ID.
	 * 
	 * @param theLeaveRequest The updated LeaveRequests entity.
	 * @param id The ID of the leave request to update.
	 * @return The updated LeaveRequests entity.
	 */
	LeaveRequests updateById(LeaveRequests theLeaveRequest, int id);

	/**
	 * Deletes a leave request using its unique ID.
	 * 
	 * @param id The ID of the leave request to delete.
	 */
	void deleteById(int id);
}
