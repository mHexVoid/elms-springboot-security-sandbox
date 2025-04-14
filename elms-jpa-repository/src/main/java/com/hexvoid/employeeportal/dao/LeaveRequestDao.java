package com.hexvoid.employeeportal.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hexvoid.employeeportal.entity.LeaveRequests;

/**
 * LeaveRequestDao interface defines the database operations 
 * related to leave requests.
 * This acts as the DAO (Data Access Object) layer to interact with the database.
 */
public interface LeaveRequestDao extends JpaRepository<LeaveRequests, Long>{

   
}
