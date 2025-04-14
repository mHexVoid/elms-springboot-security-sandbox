package com.hexvoid.employeeportal.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hexvoid.employeeportal.dao.LeaveRequestDao;
import com.hexvoid.employeeportal.entity.LeaveRequests;
import com.hexvoid.employeeportal.exceptionhandler.MyCustomExceptionClass;

@Service
public class LeaveRequestServiceImpl implements LeaveRequestService{
	
	private LeaveRequestDao leaveRequestDao; // Setter-injected
	
	 /**
     * Setter Injection for LeaveRequestDao.
     * 
     *  Setter injection allows for optional dependencies, making it flexible.
     *  Useful if we want to change dependency implementation dynamically.
     * 
     *  Disadvantage: 
     *    - Makes the object mutable, which can lead to unexpected behavior.
     *    - Not ideal for mandatory dependencies.
     *    - Can cause a **Cyclic Dependency Issue** (Explained below).
     */
    @Autowired
    public void setLeaveRequestDao(LeaveRequestDao leaveRequestDao) {
        this.leaveRequestDao = leaveRequestDao;
    }

    /**
     *  **Cyclic Dependency Issue**
     * - A cyclic dependency occurs when two or more beans depend on each other in a circular fashion.
     * - Example: If EmployeeLeaveRequestServiceImpl depends on LeaveRequestDao, and LeaveRequestDao depends on EmployeeLeaveRequestServiceImpl, 
     *   Spring fails to resolve the dependency tree.
     * 
     *  **Solution:**
     * - **Break the Cycle** by using constructor injection for at least one of the dependencies.
     * - **Use @Lazy Annotation** on one of the beans to delay initialization until needed.
     * - **Refactor Code** to introduce an intermediary layer.
     */

    // ============================ Leave Request Operations ============================

    /**
     * Adds a new leave request.
     * @param theLeaveRequest Leave request entity to be added.
     */
    @Override
    public LeaveRequests save(LeaveRequests theLeaveRequest) {
        return leaveRequestDao.save(theLeaveRequest);
    }

    /**
     * Retrieves all leave requests from the database.
     * @return List of leave requests.
     */
    @Override
    public List<LeaveRequests> findAll() {
        return leaveRequestDao.findAll();
    }

    /**
     * Finds a leave request by its unique ID.
     * Throws an exception if not found.
     * 
     * @param theID ID of the leave request.
     * @return Leave request entity.
     */
    @Override
    public LeaveRequests findById (Long theID) {
        Optional<LeaveRequests> theleaveReq = leaveRequestDao.findById(theID);
        return theleaveReq.orElseThrow(()-> new MyCustomExceptionClass("Leave Request Id Not Found "+theID));
    }

   
    /**
     * Deletes a leave request by ID.
     * @param id ID of the leave request.
     */
    @Override
    public void deleteById(Long id) {
        leaveRequestDao.deleteById(id);
    }

	


}
