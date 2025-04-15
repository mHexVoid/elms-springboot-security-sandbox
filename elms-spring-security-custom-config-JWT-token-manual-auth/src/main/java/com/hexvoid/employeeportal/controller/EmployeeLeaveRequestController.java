package com.hexvoid.employeeportal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hexvoid.employeeportal.entity.Employees;
import com.hexvoid.employeeportal.entity.LeaveRequests;
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
	 * Adds a new Employee to the system.
	 *
	 * Detailed Flow:
	 * 1. Maps to an HTTP POST request at the path /api/employee.
	 * 2. The @RequestBody annotation takes the incoming JSON payload and deserializes it into an object.
	 * 3. Checks if the Employee object already has an ID; new employees should not provide an ID since it is managed by the system.
	 * 4. If an ID exists, a ResponseEntity is returned with HTTP 400 (BAD REQUEST) along with an appropriate message.
	 * 5. If valid, the employeeService adds the new Employee.
	 * 6. Returns a ResponseEntity with HTTP 201 (CREATED) signifying successful creation.
	 *
	 * Using ResponseEntity Methods:
	 *   - `ResponseEntity.status(HttpStatus.BAD_REQUEST).body("...")`
	 *       • Sets the HTTP status to 400 and attaches a string message to the body.
	 *   - `new ResponseEntity<>("Employee Added Successfully", HttpStatus.CREATED)`
	 *       • Constructs a new ResponseEntity with the provided message and status 201.
	 *
	 * @param newEmployee Employee details to be added.
	 * @return ResponseEntity with success message and the corresponding HTTP status.
	 */
	@PostMapping(value = "/employee")
	@ResponseBody // Although redundant under @RestController, it clarifies that the return value is written to the response body.
	public ResponseEntity<String> addEmployee(@RequestBody Employees newEmployee) {
		// Prevent clients from specifying an employee ID for a new record.
		if (newEmployee.getId() != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Employee ID should not be provided for a new employee.");
		}
		employeeService.save(newEmployee);
		return new ResponseEntity<>("Employee Added Successfully", HttpStatus.CREATED);
	}

	/**
	 * Retrieves all Employees from the system.
	 *
	 * Detailed Flow:
	 * 1. Maps to an HTTP GET request at /api/employee.
	 * 2. employeeService.findAllEmployees() retrieves a list of all Employee objects.
	 * 3. The list is wrapped using ResponseEntity.ok(), which is shorthand for creating a response with HTTP 200 (OK).
	 *
	 * ResponseEntity Internals:
	 *   - `.ok()` sets the status to 200 and simplifies the response construction.
	 *
	 * @return ResponseEntity containing a list of Employees with HTTP 200 status.
	 */
	@GetMapping(value = "/employee")
	public ResponseEntity<List<Employees>> getAllEmployees() {
		return ResponseEntity.ok(employeeService.findAll());
	}

	/**
	 * Retrieves an Employee by ID.
	 *
	 * Detailed Flow:
	 * 1. Maps to an HTTP GET request at /api/employee/{id}.
	 * 2. The @PathVariable indicates that the {id} part of the URL is bound to the method parameter.
	 * 3. employeeService.findEmployeeByID(id) fetches the corresponding Employee.
	 * 4. ResponseEntity.status(HttpStatus.OK).body() is used to build a response with HTTP 200.
	 *
	 * @param id The Employee ID to fetch.
	 * @return ResponseEntity containing the Employee data and HTTP status 200.
	 */
	@GetMapping(value = "/employee/{id}")
	public ResponseEntity<Employees> getEmployeeById(@PathVariable int id) {
		return ResponseEntity.status(HttpStatus.OK).body(employeeService.findById(id));
	}

	/**
	 * Updates the details of an existing Employee.
	 *
	 * Detailed Flow:
	 * 1. Maps to an HTTP PUT request at /api/employee/{id}.
	 * 2. Uses @RequestBody to bind the updated Employee data from the incoming JSON payload.
	 * 3. Uses @PathVariable to capture the Employee ID from the URL.
	 * 4. employeeService.updateEmployeeByID(updatedEmployee, id) updates and returns the altered Employee object.
	 * 5. The updated object is wrapped in a ResponseEntity with HTTP 200 (OK).
	 *
	 * Best Practices:
	 *   - Ensure input validation before processing the update if not done in the service.
	 *
	 * @param updatedEmployee The updated Employee details.
	 * @param id              The Employee ID corresponding to the record to be updated.
	 * @return ResponseEntity containing the updated Employee with HTTP 200 status.
	 */
	@PutMapping(value = "/employee/{id}")
	public ResponseEntity<Employees> updateEmployee(@RequestBody Employees updatedEmployee, @PathVariable int id) {
		return new ResponseEntity<>(employeeService.updateById(updatedEmployee, id), HttpStatus.OK);
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