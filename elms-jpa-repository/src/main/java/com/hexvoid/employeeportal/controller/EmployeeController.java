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

import com.hexvoid.employeeportal.entity.Employees;
import com.hexvoid.employeeportal.service.EmployeeService;

/**
 * ===========================================
 * **EmployeeController - A REST API Controller**
 * ===========================================
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
@RestController // Marks this class as a RESTful API controller (combines @Controller + @ResponseBody)
@RequestMapping(value = "/api") // Defines the base URL for all endpoints in this controller
public class EmployeeController {

    // Logger for this class
    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    // Service layer dependency for handling business logic
    private final EmployeeService theEmployeeServiceMethod;

    /**
     * Constructor-based dependency injection of EmployeeService.
     *
     * **Why Constructor Injection?**
     * - Prevents **circular dependencies**.
     * - Improves **testability**.
     * - Encourages **immutability** (best practice in Spring applications).
     *
     * @param theServiceMethods The EmployeeService instance.
     */
    public EmployeeController(EmployeeService theServiceMethods) {
        this.theEmployeeServiceMethod = theServiceMethods;
    }

    /**
     * **POST API - Add a New Employee**
     * Endpoint: `/api/employees`
     *
     * **@PostMapping**
     * - Maps this method to handle HTTP **POST** requests.
     * - Used for **creating new resources** (employees in this case).
     *
     * **What is @RequestBody?**
     * - `@RequestBody` is used to **map the JSON payload** from the request body to a Java object.
     * - Spring **automatically deserializes JSON** into a Java object.
     *
     * **Example Request Body (JSON):**
     * ```json
     * {
     *   "name": "John Doe",
     *   "email": "john.doe@example.com",
     *   "department": "IT"
     * }
     * ```
     *
     * @param theEmptoadd The Employee object containing new employee details.
     */
    @PostMapping(value = "/employees")
    public void addEmployee(@RequestBody Employees theEmptoadd) {
        logger.info("Adding new employee: {}", theEmptoadd); // Logs the incoming request.
        theEmployeeServiceMethod.save(theEmptoadd);
    }

    /**
     * **GET API - Fetch All Employees**
     * Endpoint: `/api/employees`
     *
     * **@GetMapping**
     * - Maps this method to handle HTTP **GET** requests.
     * - Used to **retrieve resources** (list of employees).
     * - Returns a **List<Employees>** as a response.
     *
     * **Best Practice:**
     * - For larger datasets, implement pagination to improve performance.
     *
     * @return A list of all employees currently in the system.
     */
    @GetMapping(value = "/employees")
    public List<Employees> getEmployee() {
        logger.info("Fetching all employees...");
        return theEmployeeServiceMethod.findAll();
    }

    /**
     * **GET API - Fetch Employee by ID**
     * Endpoint: `/api/employees/{id}`
     *
     * **What is @PathVariable?**
     * - Extracts **dynamic values** from the **URL path**.
     * - Maps the value in `{id}` from the request URL to the method parameter.
     *
     * **Example Request:**  
     * `GET /api/employees/5`  
     * - The value `5` is mapped to `@PathVariable int id`.
     *
     * **Best Practice:**
     * - Always handle the case when an employee is not found (e.g., throw a `ResourceNotFoundException`).
     *
     * @param id The ID of the employee to retrieve.
     * @return The employee details corresponding to the given ID.
     */
    @GetMapping(value = "/employees/{id}")
    public Employees getEmployeeById(@PathVariable Long id) {
        logger.info("Fetching employee with ID: {}", id);
        return theEmployeeServiceMethod.findById(id);
    }

    /**
     * **PUT API - Update Employee by ID**
     * Endpoint: `/api/employees/{id}`
     *
     * **@PutMapping**
     * - Maps this method to handle HTTP **PUT** requests.
     * - Used for **updating existing resources**.
     * - Requires both **request body (JSON) and path variable (ID)**.
     *
     * **Example Request Body (JSON):**
     * ```json
     * {
     *   "name": "Updated Name",
     *   "email": "updated.email@example.com",
     *   "department": "HR"
     * }
     * ```
     *
     * @param theEmptoUpdate The Employee object containing the updated details.
     * @param id The ID of the employee to update.
     * @return The updated employee details.
     */
    @PutMapping(value = "/employees/{id}")
    public Employees updateEmployeeById(@RequestBody Employees theEmptoUpdate, @PathVariable Long id) {
    	//check how you going to handle id ?
        logger.info("Updating employee with ID: {} | New Details: {}", id, theEmptoUpdate);
        return theEmployeeServiceMethod.save(theEmptoUpdate);
    }

    /**
     * **DELETE API - Remove Employee by ID**
     * Endpoint: `/api/employees/{id}`
     *
     * **@DeleteMapping**
     * - Maps this method to handle HTTP **DELETE** requests.
     * - Used for **removing a resource permanently**.
     *
     * **Example Request:**  
     * `DELETE /api/employees/5`
     *
     * @param id The ID of the employee to delete.
     */
    @DeleteMapping(value = "/employees/{id}")
    public void deleteEmployeeById(@PathVariable Long id) {
        logger.info("Deleting employee with ID: {}", id);
        theEmployeeServiceMethod.deleteById(id);
    }
}
