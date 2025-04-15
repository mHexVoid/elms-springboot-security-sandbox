package com.hexvoid.employeeportal.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;

import com.hexvoid.employeeportal.entity.LeaveRequests;

import jakarta.persistence.EntityNotFoundException;

/**
 * Service layer interface for managing leave request operations.
 * 
 * This interface acts as a bridge between the controller and the data access layer (DAO),
 * ensuring that business logic is applied before interacting with the persistence layer.
 * 
 * Key Responsibilities:
 * - Abstracts the DAO operations to provide a clean service layer.
 * - Ensures proper encapsulation of business logic.
 * - Facilitates easy testing and scalability.
 * 
 * Design Considerations:
 * - Interfaces promote loose coupling, making it easier to replace implementations.
 * - Implementation classes can use @Service annotation for service layer identification.
 * - Transactions should be handled at the service layer using @Transactional annotation.
 */
public interface LeaveRequestService {

    /**
     * Adds a new leave request to the system.
     * 
     * Process:
     * - Validates the leave request object.
     * - Ensures business rules such as date validation (startDate < endDate).
     * - Calls the DAO layer to persist the data.
     * - Returns success/failure response to the caller.
     * 
     * Constraints:
     * - Employee must exist before submitting a leave request.
     * - Leave type should be valid (Sick, Casual, Earned).
     * - Leave request should not overlap with existing approved leaves.
     * 
     * @param theLeaveRequest The LeaveRequests entity containing leave details.
     * @throws IllegalArgumentException If input validation fails.
     * @throws DataIntegrityViolationException If there is a database constraint violation.
     */
    void save(LeaveRequests theLeaveRequest);

    /**
     * Retrieves all leave requests from the system.
     * 
     * Process:
     * - Fetches all leave requests from the database.
     * - Supports pagination and filtering (to be implemented in DAO layer).
     * 
     * Performance Considerations:
     * - For large datasets, pagination should be used to avoid excessive memory consumption.
     * - Use caching mechanisms if required to optimize frequent queries.
     * 
     * @return A list of all leave requests available in the database.
     */
    List<LeaveRequests> findAll();

    /**
     * Fetches a specific leave request based on its unique identifier.
     * 
     * Process:
     * - Checks if the leave request exists in the database.
     * - Returns the LeaveRequests entity if found.
     * - Throws an exception if the ID is invalid.
     * 
     * Use Cases:
     * - View leave details in employee dashboards.
     * - Fetch leave request for further processing (approval/rejection).
     * 
     * @param theID The unique ID of the leave request.
     * @return The corresponding LeaveRequests entity if found.
     * @throws EntityNotFoundException If no record is found for the given ID.
     */
    LeaveRequests findById(int theID);

    /**
     * Updates an existing leave request identified by its ID.
     * 
     * Process:
     * - Validates if the provided ID exists.
     * - Ensures only modifiable fields (leaveType, startDate, endDate, status) are updated.
     * - Calls DAO to persist changes.
     * 
     * Business Rules:
     * - Employees can modify only their **own pending leave requests**.
     * - Admins/Managers can approve/reject leave requests.
     * - Date validation should be performed (startDate < endDate).
     * - Status can only transition logically (PENDING → APPROVED/REJECTED).
     * 
     * @param theLeaveRequest The updated leave request details.
     * @param id The ID of the leave request to be updated.
     * @return The updated LeaveRequests entity.
     * @throws IllegalArgumentException If business rules are violated.
     * @throws EntityNotFoundException If the provided leave request ID does not exist.
     */
    LeaveRequests updateById(LeaveRequests theLeaveRequest, int id);

    /**
     * Deletes a leave request from the system based on its ID.
     * 
     * Process:
     * - Checks if the leave request exists.
     * - Ensures only pending leave requests can be deleted.
     * - Calls DAO layer to remove the record.
     * 
     * Constraints:
     * - Approved/Rejected leave requests cannot be deleted, only canceled.
     * - Admins may have permissions to delete in exceptional cases.
     * - Deletion should be **logged for audit purposes**.
     * 
     * @param id The unique identifier of the leave request to be removed.
     * @throws UnsupportedOperationException If deletion is not allowed.
     * @throws EntityNotFoundException If the leave request does not exist.
     */
    void deleteById(int id);
}
