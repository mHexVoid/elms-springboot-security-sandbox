package com.hexvoid.employeeportal.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hexvoid.employeeportal.entity.Employees;
import com.hexvoid.employeeportal.entity.LeaveRequests;
import com.hexvoid.employeeportal.exceptionhandler.MyCustomExceptionClass;
import com.hexvoid.employeeportal.service.EmployeeService;
import com.hexvoid.employeeportal.service.LeaveRequestService;

/*
 ===================================================================================================
 OVERVIEW:
 ===================================================================================================

 What is a Controller?
 ----------------------
 In Spring Boot, a Controller is a specialized component that handles HTTP requests and maps them
 to business logic. Marked with annotations like @RestController or @Controller, it acts as the entry
 point for requests coming into the system. Internally, the Spring Framework performs component scanning
 to detect these controllers, then uses reflection and dependency injection to wire together their
 components (such as services).

 The Controller’s Role in a Spring Boot Application:
   • It translates HTTP requests (e.g., GET, POST, PUT, DELETE) into service calls.
   • It handles request data by mapping JSON/XML inputs to domain objects (@RequestBody).
   • It produces responses by serializing domain objects back into JSON/XML (@ResponseBody, implicit
     within @RestController).
   • It facilitates a clear separation of concerns by delegating business logic to service layers.

 Why is it Essential in a Web Application?
   • Ensures modularity and ease of maintenance.
   • Simplifies request routing and error handling.
   • Empowers RESTful design by aligning HTTP methods and response semantics with business operations.

 ---------------------------------------------------------------------------------------------------

 Detailed Explanation of Each Annotation:
 -------------------------------------------
 @RestController:
   - **Purpose:** Indicates that the data returned by each method will be written straight 
     into the HTTP response body.
   - **Internal Functioning:** Combines @Controller and @ResponseBody. During runtime, Spring
     automatically converts the returned objects to JSON/XML using message converters.
   - **Best Practices:** Use for REST APIs. Avoid using it when you need to return views.
   - **Pitfalls:** Overusing it for non-REST endpoints can lead to inflexible designs.

 @RequestMapping("/api"):
   - **Purpose:** Specifies the base URL for all endpoints in this controller.
   - **Internal Functioning:** Maps incoming requests whose URL starts with /api to these endpoints.
   - **Best Practices:** Keep base paths consistent for versioning or grouping of APIs.
   - **Pitfalls:** Avoid setting overly broad mappings that can intercept unrelated requests.

 @Autowired:
   - **Purpose:** Enables dependency injection to automatically assign service beans.
   - **Internal Functioning:** Instructs Spring’s IoC container to resolve and inject the appropriate bean.
   - **Best Practices:** Prefer constructor injection (as shown) for better testability and immutability.
   - **Common Pitfalls:** Field injection can lead to issues with immutability and is considered less
     test-friendly.

 @PostMapping, @GetMapping, @PutMapping, @DeleteMapping:
   - **Purpose:** Specialized annotations to map HTTP POST, GET, PUT, DELETE requests respectively.
   - **Internal Functioning:** They internally resolve to @RequestMapping(method = RequestMethod.*) with the
     given HTTP method.
   - **Best Practices:** Use specific annotations for clarity.
   - **Pitfalls:** Using the wrong mapping for an endpoint might violate REST semantics.

 @ResponseBody:
   - **Purpose:** Tells Spring MVC to write the method return value directly into the HTTP response as a JSON/XML payload.
   - **Internal Functioning:** Even though it is redundant under @RestController, it makes the behavior explicit.
   - **Best Practices:** In a REST API, using @RestController eliminates the need for explicit @ResponseBody.
   - **Pitfalls:** Misusing @ResponseBody can lead to unexpected serialization results.

 @PathVariable:
   - **Purpose:** Binds a URI template variable (a path parameter) to a method parameter.
   - **Internal Functioning:** Spring extracts the value from the URI and passes it to the controller.
   - **Best Practices:** Validate your inputs. Ensure the variable names are consistent between the
     URL and the method parameter.
   - **Pitfalls:** Mismatches between the variable names or types can lead to runtime errors.

 @RequestBody:
   - **Purpose:** Signals to Spring to bind the incoming HTTP request body to a method parameter.
   - **Internal Functioning:** Uses HttpMessageConverters to deserialize the request payload (JSON/XML)
     into an object.
   - **Best Practices:** Validate input objects (consider using @Valid for bean validation).
   - **Pitfalls:** Incorrect mapping of JSON structure to object properties can lead to errors or null fields.

 ---------------------------------------------------------------------------------------------------

 What is a REST API?
 --------------------
 A REST (Representational State Transfer) API uses standard HTTP protocols to allow clients 
 (browsers, mobile apps, etc.) to interact with the system. Its core principles are:
   • **Statelessness:** Each client request must contain all necessary information.
   • **Client-Server Architecture:** The client and server are separate, allowing them to evolve independently.
   • **Cacheability:** Responses should define themselves as cacheable or non-cacheable.
   • **Uniform Interface:** Simplified, standardized methods (GET, POST, PUT, DELETE) to perform operations.
   • **Layered System:** The API can be composed of multiple layers to improve scalability.

 This controller follows REST principles by:
   • Mapping URLs to resources (e.g., /employee, /leave).
   • Using HTTP methods that match the operation (GET for retrieval, POST for creation, etc.).
   • Returning standard HTTP status codes through ResponseEntity.

 ---------------------------------------------------------------------------------------------------

 Deep Explanation of ResponseEntity:
 --------------------------------------
 `ResponseEntity` is a wrapper for the entire HTTP response. It provides a way to build responses
 with full control over:
   • **HTTP Status Code:** Indicates the outcome of the request (e.g., 200 OK, 201 CREATED, 400 BAD REQUEST).
   • **Response Headers:** Metadata like content-type, caching, etc.
   • **Response Body:** The actual data being returned.

 **Why is it used in this code?**
   • Provides explicit control over the HTTP status, ensuring that the client receives an accurate
     indication of what happened.

 **Benefits:**
   • Makes the API more expressive.
   • Facilitates adding custom headers and status codes.
   • Enhances error handling with proper status communication.

 **Processing Internals:**
   • `.ok()`: A static method that returns a builder with HTTP status 200. When you append `.body()`,
     it mounts the provided object as the response payload.
   • `.status(HttpStatus)`: Sets a custom HTTP status. Chaining `.body()` attaches the payload.
   • `new ResponseEntity<>(body, status)`: Direct constructor for pairing the response body with a status.

 **What problems does it solve?**
   • Eliminates ambiguity about the result of HTTP operations.
   • Helps in communicating both success and failure in a standardized manner.

 ---------------------------------------------------------------------------------------------------

 Detailed Explanation of HttpStatus:
 --------------------------------------
 HttpStatus is an enumeration that represents standard HTTP status codes. It is essential because:
   • It ensures responses use semantically correct and standardized codes (e.g., 404 for Not Found).
   • It encourages consistency across endpoints.

 **Mapping Best Practices:**
   • Map creation operations to 201 (CREATED).
   • Use 200 (OK) for successful operations (retrieval, updates, deletions).
   • Use 400 (BAD REQUEST) for client errors (e.g., invalid input) and 404 (NOT FOUND) where applicable.

 **Factors to Consider:**
   • The operation’s intent and the client’s expected result.
   • Clear communication of error details when the request fails.
   • Adherence to HTTP/REST best practices for standardization.

 ===================================================================================================
 */

@RestController // Combines @Controller and @ResponseBody; simplifies API development by returning data directly.
@RequestMapping("/api") // Base path for this controller; each endpoint here will be under the "/api" URL.
public class EmployeeLeaveRequestController {

	// Service responsible for employee business operations.
	private final EmployeeService employeeService;
	// Service responsible for leave request operations.
	private final LeaveRequestService leaveRequestService;

	/**
	 * Constructor-based dependency injection for the required services.
	 * 
	 * @param theEmpService      Instance of EmployeeService.
	 * @param theLeaveRequest    Instance of LeaveRequestService.
	 *
	 * This approach (constructor injection) is preferred because:
	 *   • It allows the fields to be declared as final (ensuring immutability).
	 *   • Helps with testing, as mocked dependencies can be easily injected.
	 */
	@Autowired
	public EmployeeLeaveRequestController(EmployeeService theEmpService, LeaveRequestService theLeaveRequest) {
		this.employeeService = theEmpService;
		this.leaveRequestService = theLeaveRequest;
	}

	//===================================================================================
	// Employee Controller Methods
	//===================================================================================

	/**
	 * Adds a list of new Employees to the system with input-level filtering using @PreFilter.
	 *
	 *  PreFilter Explanation:
	 *   - Evaluates the condition *before* the method is executed.
	 *   - Ensures that only those Employees from the input list with:
	 *       → Non-null email,
	 *       → Containing "@",
	 *       → Ending with ".com",
	 *     are passed to the method.
	 *   -  Safe for DB updates because invalid data is filtered out before processing.
	 *
	 * @param newEmployees List of Employee objects sent in request.
	 * @return ResponseEntity with success message or error.
	 */
	@PostMapping(value = "/employee")
	@PreFilter("filterObject.email != null and"
			+ " filterObject.email.contains('@') and"
			+ " filterObject.email.endsWith('.com')")
	public ResponseEntity<String> addEmployee(@RequestBody List<Employees> newEmployees) {
		if (newEmployees.isEmpty()) {
			throw new MyCustomExceptionClass("Please Provide valid Mail ID");
		}
		Employees requestedEmployees = newEmployees.get(0);
		if (requestedEmployees.getId() != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Employee ID should not be provided for a new employee.");
		}
		employeeService.save(requestedEmployees);
		return new ResponseEntity<>("Employee Added Successfully", HttpStatus.CREATED);
	}

	/**
	 * Retrieves all employees from the system.
	 *
	 * Security:
	 * - Secured with @PreAuthorize("hasAuthority('VIEW')") at DAO level.
	 * - The method will only execute if the authenticated user has the 'VIEW' authority.
	 *
	 * Flow:
	 * 1. Maps to HTTP GET at /api/employee.
	 * 2. Calls employeeService.findAll() to fetch the list of all employees.
	 * 3. Returns the result with HTTP 200 using ResponseEntity.ok().
	 *
	 * Note:
	 * - If the user lacks the required authority, Spring Security prevents method execution altogether.
	 *
	 * @return List of Employees wrapped in ResponseEntity with HTTP 200 status.
	 */
	@GetMapping(value = "/employee")
	public ResponseEntity<List<Employees>> getAllEmployees() {

		System.out.println("Using @PreAuthorize – if the user doesn't have VIEW access, this DAO method won't be executed at all.");

		return ResponseEntity.ok(employeeService.findAll());
	}

	/**
	 * Retrieves an Employee by their ID.
	 *
	 * Security:
	 * - Secured with @PostAuthorize("hasAuthority('VIEW')") at DAO level.
	 * - Authorization is applied **after** the method executes and the return value is available.
	 *
	 * Flow:
	 * 1. Maps to HTTP GET at /api/employee/{id}.
	 * 2. The path variable {id} is bound to the method parameter.
	 * 3. employeeService.findById(id) fetches the Employee entity from the database.
	 * 4. The result is returned in a ResponseEntity with HTTP 200 status.
	 *
	 * PostAuthorize Behavior:
	 * 🔸 If the employee ID doesn't exist in the database, the method returns null.
	 *     ➤ PostAuthorize doesn't block it since there's no object to evaluate.
	 * 🔸 If the employee exists and the user lacks VIEW authority,
	 *     ➤ Spring throws AccessDeniedException after evaluating the returned object.
	 *
	 * @param id The ID of the employee to retrieve.
	 * @return ResponseEntity containing the Employee data with HTTP 200 status.
	 */
	@GetMapping(value = "/employee/{id}")
	public ResponseEntity<Employees> getEmployeeById(@PathVariable int id) {

		System.out.println("PostAuthorize check: If employee ID " + id + " doesn't exist, method returns null and PostAuthorize doesn't block it.");
		System.out.println("But if the employee exists and the user lacks VIEW access, Spring throws AccessDeniedException after evaluating the result.");

		return ResponseEntity.status(HttpStatus.OK).body(employeeService.findById(id));
	}


	/**
	 * Updates an Employee record based on the provided ID and request body.
	 *
	 * <p><strong>Security and @PostFilter Usage:</strong></p>
	 * <ul>
	 *   <li>This method updates the employee record before the {@code @PostFilter} is applied.</li>
	 *   <li>The returned list is filtered based on the following conditions:
	 *       <ul>
	 *           <li>Email must not be {@code null}</li>
	 *           <li>Email must contain '@'</li>
	 *           <li>Email must end with ".com"</li>
	 *       </ul>
	 *   </li>
	 *   <li>{@code @PostFilter} filters only the response, not the execution logic.</li>
	 * </ul>
	 *
	 * <p><strong>Important Notes:</strong></p>
	 * <ul>
	 *   <li>{@code @PostFilter} requires the return type to be a {@code Collection} (e.g., {@code List}, {@code Set}).</li>
	 *   <li>It does not work with wrapped responses like {@code ResponseEntity} or {@code Optional}.</li>
	 *   <li>This method performs a write operation before filtering. As such, invalid or unauthorized updates
	 *       can still persist in the database even if they are excluded from the response.</li>
	 *   <li>For update/delete operations, use with caution and preferably with additional validations or {@code @PreAuthorize}.</li>
	 *   <li>Only the first element in the input list is processed for update.</li>
	 * </ul>
	 *
	 * @param updatedEmployee a list containing employee details to update (only the first element is considered)
	 * @param id the ID of the employee to be updated
	 * @return a list containing the updated employee, if it satisfies the filter conditions
	 */

	@PutMapping(value = "/employee/{id}")
	@PostFilter("filterObject.email != null and"
			+ " filterObject.email.contains('@') and"
			+ " filterObject.email.endsWith('.com')")
	public List<Employees> updateEmployee(@RequestBody List<Employees> updatedEmployee, @PathVariable int id) {
		List<Employees> theEmployees = new ArrayList<>();
		if (!updatedEmployee.isEmpty()) {
			Employees updateById = employeeService.updateById(updatedEmployee.get(0), id);
			theEmployees.add(updateById);
		}
		return theEmployees;
	}

	/**
	 * Deletes an Employee by ID.
	 *
	 * Detailed Flow:
	 * 1. Maps to an HTTP DELETE request at /api/employee/{id}.
	 * 2. The @PathVariable extracts the Employee ID from the URL.
	 * 3. employeeService.deleteEmployeeByID(id) performs the deletion process.
	 * 4. After deletion, ResponseEntity.ok() returns a successful response with HTTP 200.
	 *
	 * @param id The Employee ID to be deleted.
	 * @return ResponseEntity with a confirmation message and HTTP 200 status.
	 */
	@DeleteMapping(value = "/employee/{id}")
	public ResponseEntity<String> deleteEmployeeById(@PathVariable int id) {
		employeeService.deleteById(id);
		return ResponseEntity.ok().body("Deleted Employee Id " + id);
	}

	//===================================================================================
	// Controller Methods for Handling Leave Requests
	//===================================================================================

	/**
	 * Adds a new Leave Request to the system.
	 *
	 * Detailed Flow:
	 * 1. Maps to an HTTP POST request at /api/leave.
	 * 2. The @RequestBody annotation converts the request payload into a LeaveRequests object.
	 * 3. leaveRequestService.addLeaveRequest(newLeaveRequest) persists the new leave request.
	 * 4. The response, with HTTP 201 (CREATED), is built using the ResponseEntity constructor.
	 *
	 * @param newLeaveRequest Leave request details to be added.
	 * @return ResponseEntity with a success message and corresponding HTTP status.
	 */
	@PostMapping("/leave")
	public ResponseEntity<String> addLeaveDetails(@RequestBody LeaveRequests newLeaveRequest) {
		leaveRequestService.save(newLeaveRequest);
		return new ResponseEntity<>("Leave request added successfully.", HttpStatus.CREATED);
	}

	/**
	 * Retrieves all Leave Requests.
	 *
	 * Detailed Flow:
	 * 1. Maps to an HTTP GET request at /api/leave.
	 * 2. leaveRequestService.findAllLeaveRequest() returns a list of all leave requests.
	 * 3. The response is built using ResponseEntity.status(HttpStatus.OK).body(), ensuring
	 *    explicit declaration of HTTP 200 status.
	 *
	 * @return ResponseEntity containing a list of leave requests and HTTP 200 status.
	 */
	@GetMapping("/leave")
	public ResponseEntity<List<LeaveRequests>> getAllLeaveRequests() {
		return ResponseEntity.status(HttpStatus.OK).body(leaveRequestService.findAll());
	}

	/**
	 * Retrieves a Leave Request by its ID.
	 *
	 * Detailed Flow:
	 * 1. Maps to an HTTP GET request at /api/leave/{id}.
	 * 2. Uses @PathVariable to extract the leave request ID from the URL.
	 * 3. leaveRequestService.findLeaveRequestByID(id) fetches the corresponding leave request.
	 * 4. A new ResponseEntity is constructed with the found object and HTTP 200 status.
	 *
	 * @param id The Leave Request ID to be fetched.
	 * @return ResponseEntity containing the leave request details and HTTP status 200.
	 */
	@GetMapping("/leave/{id}")
	public ResponseEntity<LeaveRequests> getLeaveRequestById(@PathVariable int id) {
		//LeaveRequests is not required over here as generic because from java 7+ we don't have to specify  explicitly
		return new ResponseEntity<LeaveRequests>(leaveRequestService.findById(id), HttpStatus.OK);
	}

	/**
	 * Updates an existing Leave Request.
	 *
	 * Detailed Flow:
	 * 1. Maps to an HTTP PUT request at /api/leave/{id}.
	 * 2. Uses @RequestBody to bind the updated leave request data from the HTTP request.
	 * 3. Uses @PathVariable to capture the ID of the leave request to update.
	 * 4. leaveRequestService.updateLeaveRequestByID(updatedLeaveRequest, id) processes the update.
	 * 5. ResponseEntity.status(HttpStatus.OK).body() constructs the response with the updated leave 
	 *    request and explicit HTTP 200 status.
	 *
	 * @param updatedLeaveRequest The updated details for the leave request.
	 * @param id                  The Leave Request ID corresponding to the record to be updated.
	 * @return ResponseEntity containing the updated leave request with HTTP 200 status.
	 */
	@PutMapping("/leave/{id}")
	public ResponseEntity<LeaveRequests> updateLeaveRequestById(
			@RequestBody LeaveRequests updatedLeaveRequest, @PathVariable int id) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(leaveRequestService.updateById(updatedLeaveRequest, id));
	}

	/**
	 * Deletes a Leave Request by its ID.
	 *
	 * Detailed Flow:
	 * 1. Maps to an HTTP DELETE request at /api/leave/{id}.
	 * 2. Uses @PathVariable to retrieve the leave request ID from the URL.
	 * 3. leaveRequestService.deleteLeaveRequestById(id) deletes the specified leave request.
	 * 4. A new ResponseEntity is constructed containing a deletion confirmation message
	 *    along with HTTP 200 (OK) status.
	 *
	 * @param id The Leave Request ID to be deleted.
	 * @return ResponseEntity with a confirmation message and HTTP status 200.
	 */
	@DeleteMapping("/leave/{id}")
	public ResponseEntity<String> deleteLeaveRequestById(@PathVariable int id) {
		leaveRequestService.deleteById(id);
		return new ResponseEntity<>("Deleted Leave Request with ID: " + id, HttpStatus.OK);
	}
}