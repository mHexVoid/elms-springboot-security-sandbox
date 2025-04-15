package com.hexvoid.employeeportal.service;

import java.util.List;

import com.hexvoid.employeeportal.entity.LeaveRequests;

/**
 * Service layer interface extending DAO for Leave Request operations.
 * Ensures proper separation of business logic and persistence layer.
 */
public interface LeaveRequestService  {
	
	LeaveRequests save(LeaveRequests theRequest);
	List<LeaveRequests> findAll();
	LeaveRequests findById(Long id);
	void deleteById(Long id) ;
	
}
