package com.hexvoid.employeeportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * EmployeeLeaveManagementSystemApplication
 * -----------------------------------------
 * This is the entry point for the Spring Boot application. It initializes the application context 
 * and bootstraps all the Spring-managed components (e.g., Controllers, Services, Repositories).
 *
 * **What is a Controller?**
 * -------------------------
 * - A controller is a Spring-managed component responsible for handling HTTP requests.
 * - Controllers are annotated with `@RestController` or `@Controller` and serve as the gateway 
 *   between the client-side (frontend or API consumers) and the backend business logic.
 * 
 * **How Does a Controller Work Internally in Spring Boot?**
 * - Spring scans for controllers during the startup process (via component scanning) and maps HTTP 
 *   requests (e.g., GET, POST) to specific methods based on their annotations (`@GetMapping`, `@PostMapping`).
 * - It uses **reflection** to bind parameters (e.g., JSON payloads, path variables) to method arguments 
 *   and then serializes the response (e.g., converting Java objects to JSON/XML) automatically using 
 *   HttpMessageConverters.
 * 
 * **Why Are Controllers Essential?**
 * - They encapsulate the routing logic and link client-side requests to backend services, enabling 
 *   clean separation of concerns.
 * - They enforce REST standards, manage HTTP status codes, and ensure proper serialization/deserialization.
 */

/**
 * @SpringBootApplication
 * -----------------------
 * **Purpose:** Marks this class as the main entry point for the Spring Boot application. It is 
 * a convenience annotation that combines:
 * - `@Configuration`: Indicates that this class contains Spring configuration.
 * - `@EnableAutoConfiguration`: Enables Spring Boot's auto-configuration mechanism to set up the 
 *   application context.
 * - `@ComponentScan`: Instructs Spring to scan the package (and subpackages) for components such 
 *   as Controllers, Services, and Repositories.
 * 
 * **Internal Functioning:**
 * - During runtime, Spring Boot's auto-configuration scans the classpath, identifies beans, and 
 *   configures them automatically (based on dependencies like `spring-boot-starter-web`).
 * - It reduces boilerplate code by automatically wiring components such as web servers (e.g., Tomcat) 
 *   and dependency injection.
 * 
 * **Best Practices:**
 * - Ensure that the `@SpringBootApplication` annotation is placed in a package that acts as the 
 *   root for all subpackages containing Spring components.
 * - Avoid placing heavy logic or business code within the main application class.
 */
@SpringBootApplication
@EnableWebSecurity(debug=true)
public class EmployeeLeaveManagementSystemApplication {

	/**
	 * **Main Method:**
	 * ----------------
	 * The entry point for the Java application. The SpringApplication.run() method:
	 * - **Bootstraps the Spring application:** It initializes the Spring application context.
	 * - **Performs Component Scanning:** Scans and identifies all Spring-managed components within 
	 *   the specified package and its subpackages.
	 * - **Configures Auto-wired Dependencies:** Automatically injects dependencies into beans using 
	 *   Spring's Dependency Injection (DI) mechanism.
	 * - **Starts the Embedded Server:** For web applications, it starts the embedded servlet 
	 *   container (Tomcat/Jetty/Undertow).
	 *
	 * **Best Practices:**
	 * - Keep this method concise and minimal.
	 * - Use a logging framework (e.g., SLF4J) to log application startup details instead of using 
	 *   System.out.println().
	 *
	 * @param args Command-line arguments for custom configurations.
	 */
	public static void main(String[] args) {
		SpringApplication.run(EmployeeLeaveManagementSystemApplication.class, args);
	}
}
