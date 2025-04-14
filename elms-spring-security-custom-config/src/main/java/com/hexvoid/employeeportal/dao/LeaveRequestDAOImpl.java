package com.hexvoid.employeeportal.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hexvoid.employeeportal.entity.LeaveRequests;
import com.hexvoid.employeeportal.exceptionhandler.MyCustomExceptionClass;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

/**
 * LeaveRequestDAOImpl is the concrete implementation of the LeaveRequestDao interface.
 * 
 * This class provides the actual database interaction logic using JPA's EntityManager.
 * It follows the DAO pattern and is marked as a Spring Repository component.
 */
@Repository  // Marks this class as a Spring repository, making it eligible for component scanning and exception translation.
public class LeaveRequestDAOImpl implements LeaveRequestDao {

    private final EntityManager entityManagerForLeaveRequest;

    /**
     * Constructor-based dependency injection for EntityManager.
     * 
     * @param theEntityManager EntityManager instance provided by Spring Boot.
     */
    @Autowired  // Enables automatic injection of the EntityManager dependency.
    public LeaveRequestDAOImpl(EntityManager theEntityManager) {
        this.entityManagerForLeaveRequest = theEntityManager;
    }

    /**
     * Persists a new leave request entity in the database.
     * 
     * @param theLeaveRequest The LeaveRequests entity to be saved.
     */
    @Override
    public void save(LeaveRequests theLeaveRequest) {
        entityManagerForLeaveRequest.persist(theLeaveRequest); // Adds the entity to the persistence context.
    }

    /**
     * Retrieves all leave requests from the database.
     * 
     * @return A list of LeaveRequests entities.
     * @throws MyCustomExceptionClass If no leave requests are found.
     */
    @Override
    public List<LeaveRequests> findAll() {
        TypedQuery<LeaveRequests> allLeaveRequest = entityManagerForLeaveRequest
                .createQuery("from LeaveRequests", LeaveRequests.class);

        List<LeaveRequests> leaveRequestList = allLeaveRequest.getResultList();

        if (!leaveRequestList.isEmpty()) {
            return leaveRequestList;
        } else {
            throw new MyCustomExceptionClass("There are no leave requests available.");
        }
    }

    /**
     * Retrieves a leave request from the database using its unique ID.
     * If the request is not found, an exception is thrown.
     * 
     * @param theID The unique identifier of the leave request.
     * @return The corresponding LeaveRequests entity.
     * @throws MyCustomExceptionClass If the leave request is not found.
     */
    @Override
    public LeaveRequests findById(int theID) {
        // Fetch the leave request entity by its ID.
        LeaveRequests theSingleLeaveRequest = entityManagerForLeaveRequest.find(LeaveRequests.class, theID);

        // If the leave request does not exist, throw an exception.
        if (theSingleLeaveRequest == null) {
            throw new MyCustomExceptionClass("Leave Request ID: " + theID + " Not Found");
        }

        // Validate that the associated employee's name exists.
        String employeeName = (theSingleLeaveRequest.getEmployee() != null) 
                ? theSingleLeaveRequest.getEmployee().getName() 
                : null;

        if (employeeName == null || employeeName.isBlank()) {
            throw new MyCustomExceptionClass("For Leave Request ID: " + theID + ", Employee Name Not Found");
        }

        return theSingleLeaveRequest;
    }

    /**
     * Updates an existing leave request in the database.
     * The EntityManager merges the updated entity with the existing one.
     * 
     * @param theLeaveRequest The updated LeaveRequests entity.
     * @param id The ID of the leave request to update.
     * @return The updated LeaveRequests entity.
     */
    @Override
    public LeaveRequests updateById(LeaveRequests theLeaveRequest, int id) {
        return entityManagerForLeaveRequest.merge(theLeaveRequest);
    }

    /**
     * Deletes a leave request by its ID.
     * If the leave request is not found, an exception is thrown.
     * 
     * @param id The ID of the leave request to delete.
     * @throws MyCustomExceptionClass If the leave request does not exist.
     */
    @Override
    public void deleteById(int id) {
        LeaveRequests theLeaveRequestToDelete = entityManagerForLeaveRequest.find(LeaveRequests.class, id);

        if (theLeaveRequestToDelete != null) {
            entityManagerForLeaveRequest.remove(theLeaveRequestToDelete);
        } else {
            throw new MyCustomExceptionClass("Leave request with ID: " + id + " does not exist for deletion.");
        }
    }
}
