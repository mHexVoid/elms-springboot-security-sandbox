package com.hexvoid.employeeportal.service;

import java.util.List;
import com.hexvoid.employeeportal.entity.LeaveRequests;

/**
 * ==================================================================================================
 * LeaveRequestService — Service Layer Interface for Managing Leave Requests
 * ==================================================================================================
 *
 * <p><b>Purpose:</b> This interface acts as a bridge between the controller and the data access
 * layer (DAO), ensuring that business logic is applied before interacting with the persistence layer.
 *
 * <p><b>Key Responsibilities:</b></p>
 * <ul>
 *   <li>Abstracts the DAO operations to provide a clean service layer.</li>
 *   <li>Ensures proper encapsulation of business logic.</li>
 *   <li>Facilitates easy testing and scalability.</li>
 * </ul>
 *
 * <p><b>Design Considerations:</b></p>
 * <ul>
 *   <li>Interfaces promote loose coupling, making it easier to replace implementations.</li>
 *   <li>Implementation classes can use {@code @Service} annotation for service layer identification.</li>
 *   <li>Transactions should be handled at the service layer using {@code @Transactional} annotation.</li>
 * </ul>
 */
public interface LeaveRequestService {

    /**
     * Adds a new leave request to the system.
     *
     * <p><b>Process:</b></p>
     * <ul>
     *   <li>Validates the leave request object.</li>
     *   <li>Ensures business rules such as date validation (startDate &lt; endDate).</li>
     *   <li>Calls the DAO layer to persist the data.</li>
     *   <li>Returns success/failure response to the caller.</li>
     * </ul>
     *
     * <p><b>Constraints:</b></p>
     * <ul>
     *   <li>Employee must exist before submitting a leave request.</li>
     *   <li>Leave type should be valid (Sick, Casual, Earned).</li>
     *   <li>Leave request should not overlap with existing approved leaves.</li>
     * </ul>
     *
     * @param theLeaveRequest The {@link LeaveRequests} entity containing leave details.
     * @throws IllegalArgumentException If input validation fails.
     * @throws DataIntegrityViolationException If there is a database constraint violation.
     */
    void save(LeaveRequests theLeaveRequest);

    /**
     * Retrieves all leave requests from the system.
     *
     * <p><b>Process:</b></p>
     * <ul>
     *   <li>Fetches all leave requests from the database.</li>
     *   <li>Supports pagination and filtering (to be implemented in DAO layer).</li>
     * </ul>
     *
     * <p><b>Performance Considerations:</b></p>
     * <ul>
     *   <li>For large datasets, pagination should be used to avoid excessive memory consumption.</li>
     *   <li>Use caching mechanisms if required to optimize frequent queries.</li>
     * </ul>
     *
     * @return A list of all leave requests available in the database.
     */
    List<LeaveRequests> findAll();

    /**
     * Fetches a specific leave request based on its unique identifier.
     *
     * <p><b>Process:</b></p>
     * <ul>
     *   <li>Checks if the leave request exists in the database.</li>
     *   <li>Returns the {@link LeaveRequests} entity if found.</li>
     *   <li>Throws an exception if the ID is invalid.</li>
     * </ul>
     *
     * <p><b>Use Cases:</b></p>
     * <ul>
     *   <li>View leave details in employee dashboards.</li>
     *   <li>Fetch leave request for further processing (approval/rejection).</li>
     * </ul>
     *
     * @param theID The unique ID of the leave request.
     * @return The corresponding {@link LeaveRequests} entity if found.
     * @throws EntityNotFoundException If no record is found for the given ID.
     */
    LeaveRequests findById(int theID);

    /**
     * Updates an existing leave request identified by its ID.
     *
     * <p><b>Process:</b></p>
     * <ul>
     *   <li>Validates if the provided ID exists.</li>
     *   <li>Ensures only modifiable fields (leaveType, startDate, endDate, status) are updated.</li>
     *   <li>Calls DAO to persist changes.</li>
     * </ul>
     *
     * <p><b>Business Rules:</b></p>
     * <ul>
     *   <li>Employees can modify only their <b>own pending leave requests</b>.</li>
     *   <li>Admins/Managers can approve/reject leave requests.</li>
     *   <li>Date validation should be performed (startDate &lt; endDate).</li>
     *   <li>Status can only transition logically (PENDING → APPROVED/REJECTED).</li>
     * </ul>
     *
     * @param theLeaveRequest The updated leave request details.
     * @param id The ID of the leave request to be updated.
     * @return The updated {@link LeaveRequests} entity.
     * @throws IllegalArgumentException If business rules are violated.
     * @throws EntityNotFoundException If the provided leave request ID does not exist.
     */
    LeaveRequests updateById(LeaveRequests theLeaveRequest, int id);

    /**
     * Deletes a leave request from the system based on its ID.
     *
     * <p><b>Process:</b></p>
     * <ul>
     *   <li>Checks if the leave request exists.</li>
     *   <li>Ensures only pending leave requests can be deleted.</li>
     *   <li>Calls DAO layer to remove the record.</li>
     * </ul>
     *
     * <p><b>Constraints:</b></p>
     * <ul>
     *   <li>Approved/Rejected leave requests cannot be deleted, only canceled.</li>
     *   <li>Admins may have permissions to delete in exceptional cases.</li>
     *   <li>Deletion should be <b>logged for audit purposes</b>.</li>
     * </ul>
     *
     * @param id The unique identifier of the leave request to be removed.
     * @throws UnsupportedOperationException If deletion is not allowed.
     * @throws EntityNotFoundException If the leave request does not exist.
     */
    void deleteById(int id);
}
