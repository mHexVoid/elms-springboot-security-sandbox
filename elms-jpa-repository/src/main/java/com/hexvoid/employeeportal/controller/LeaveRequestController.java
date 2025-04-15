package com.hexvoid.employeeportal.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hexvoid.employeeportal.entity.LeaveRequests;
import com.hexvoid.employeeportal.service.LeaveRequestService;

/**
 * ===================================================
 * **LeaveRequestController - Handles Leave Requests**
 * ===================================================
 * **What is a REST API?**
 * - REST (**Representational State Transfer**) is an **architectural style** for designing web services.
 * - It uses standard HTTP methods like **GET, POST, PUT, DELETE** to interact with resources.
 * - REST APIs return data in formats like **JSON**.
 *
 * **What is @RestController?**
 * - `@RestController` is a **Spring annotation** that combines `@Controller` and `@ResponseBody`.
 * - This means that **every method in this class returns a response body** (typically JSON).
 * - It **eliminates the need to use** `@ResponseBody` on every method.
 * - `@RestController` is specifically used to build **RESTful web services**.
 */
@RestController
@RequestMapping(value = "/api")
public class LeaveRequestController {

    // Logger for this class
    private static final Logger logger = LoggerFactory.getLogger(LeaveRequestController.class);

    // Service layer dependency for handling business logic
    private final LeaveRequestService theLeaveRequestService;

    /**
     * Constructor-based dependency injection of LeaveRequestService.
     *
     * **Why Constructor Injection?**
     * - Prevents **circular dependencies**.
     * - Improves **testability**.
     * - Encourages **immutability** (best practice in Spring applications).
     *
     * @param theLeaveRequestServiceMethod The LeaveRequestService instance.
     */
    public LeaveRequestController(LeaveRequestService theLeaveRequestServiceMethod) {
        this.theLeaveRequestService = theLeaveRequestServiceMethod;
    }

    /**
     * **POST API - Apply for a Leave**
     * Endpoint: `/api/leaves`
     *
     * **@PostMapping**
     * - Maps this method to handle HTTP **POST** requests.
     * - Used for **creating new leave requests**.
     *
     * **What is @RequestBody?**
     * - `@RequestBody` is used to **map the JSON payload** from the request body to a Java object.
     * - Spring **automatically deserializes JSON** into a Java object using `HttpMessageConverters`.
     *
     * **Example Request Body (JSON):**
     * ```json
     * {
     *   "employeeId": 123,
     *   "leaveType": "Sick Leave",
     *   "startDate": "2025-04-01",
     *   "endDate": "2025-04-05",
     *   "status": "Pending"
     * }
     * ```
     *
     * **Best Practices:**
     * - Validate the input data to ensure required fields are present.
     * - Log events like request submissions to aid in debugging and audits.
     *
     * @param theLeaveRequest The leave request object to be created, mapped from the request body.
     */
    @PostMapping(value = "/leaves")
    public void addLeaveRequest(@RequestBody LeaveRequests theLeaveRequest) {
        logger.info("Applying for leave: {}", theLeaveRequest); // Logging the incoming request.
        theLeaveRequestService.save(theLeaveRequest);
    }

    /**
     * **GET API - Fetch All Leave Requests**
     * Endpoint: `/api/leaves`
     *
     * **@GetMapping**
     * - Maps this method to handle HTTP **GET** requests.
     * - Used to **retrieve all leave requests**.
     * - Returns a **List<LeaveRequests>** as a response.
     *
     * **Best Practices:**
     * - For large datasets, implement pagination to improve performance.
     * - Return only necessary data fields instead of the entire object if the list is extensive.
     *
     * @return A list of all leave requests in the system.
     */
    @GetMapping(value = "/leaves")
    public List<LeaveRequests> getAllLeaveRequests() {
        logger.info("Fetching all leave requests...");
        return theLeaveRequestService.findAll();
    }

    /**
     * **GET API - Fetch Leave Request by ID**
     * Endpoint: `/api/leaves/{id}`
     *
     * **What is @PathVariable?**
     * - Extracts **dynamic values** from the **URL path**.
     * - Maps the value in `{id}` from the request URL to the method parameter.
     *
     * **Example Request:**  
     * `GET /api/leaves/10`  
     * - The value `10` is mapped to `@PathVariable int id`.
     *
     * **Best Practices:**
     * - Handle cases where the leave request ID is not found.
     * - Log every request attempt, even if it fails, for auditing purposes.
     *
     * @param id The unique identifier of the leave request to retrieve.
     * @return The leave request details corresponding to the provided ID.
     */
    @GetMapping(value = "/leaves/{id}")
    public LeaveRequests getLeaveRequestById(@PathVariable Long id) {
        logger.info("Fetching leave request with ID: {}", id);
        return theLeaveRequestService.findById(id);
    }

    /**
     * **PUT API - Update Leave Request by ID**
     * Endpoint: `/api/leaves/{id}`
     *
     * **@PutMapping**
     * - Maps this method to handle HTTP **PUT** requests.
     * - Used for **updating an existing leave request**.
     *
     * **Example Request Body (JSON):**
     * ```json
     * {
     *   "employeeId": 123,
     *   "leaveType": "Annual Leave",
     *   "startDate": "2025-04-15",
     *   "endDate": "2025-04-20",
     *   "status": "Approved"
     * }
     * ```
     *
     * **Best Practices:**
     * - Validate the input data to ensure it matches the system's expected schema.
     * - Check if the leave request exists before updating it.
     *
     * @param theRequest The updated leave request object, mapped from the request body.
     * @param id The unique identifier of the leave request to update.
     * @return The updated leave request details.
     */
    @PutMapping(value = "/leaves/{id}")
    public LeaveRequests updateLeaveRequestById(@RequestBody LeaveRequests theRequest, @PathVariable int id) {
        logger.info("Updating leave request with ID: {} | New Details: {}", id, theRequest);
        //Look how you are going to work on update ?
        return theLeaveRequestService.save(theRequest);
    }

    /**
     * **DELETE API - Cancel a Leave Request**
     * Endpoint: `/api/leaves/{id}`
     *
     * **@DeleteMapping**
     * - Maps this method to handle HTTP **DELETE** requests.
     * - Used for **cancelling a leave request**.
     *
     * **Example Request:**  
     * `DELETE /api/leaves/10`
     *
     * **Best Practices:**
     * - Return HTTP 204 (No Content) to indicate successful deletion.
     * - Log deletion actions for future reference and compliance.
     *
     * @param id The unique identifier of the leave request to delete.
     */
    @DeleteMapping(value = "/leaves/{id}")
    public void deleteLeaveRequestById(@PathVariable Long id) {
        logger.info("Cancelling leave request with ID: {}", id);
        theLeaveRequestService.deleteById(id);
    }
}
