package com.hexvoid.employeeportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * -----------------------------------------------------------------------------
 * Class: EmployeeLeaveManagementSystemApplication
 * -----------------------------------------------------------------------------
 * This class serves as the **entry point** of the Spring Boot-based Employee
 * Leave Management System. It is responsible for initializing the Spring
 * Application Context and bootstrapping all application components.
 *
 * The class leverages several core Spring annotations to configure component
 * scanning, enable auto-configuration, and establish security settings.
 *
 * -----------------------------------------------------------------------------
 * Annotations and Their Purpose
 * -----------------------------------------------------------------------------
 * 
 * {@code @SpringBootApplication}
 * - A convenience annotation that combines:
 *   - {@code @Configuration}: Marks this class as a source of Spring bean definitions.
 *   - {@code @EnableAutoConfiguration}: Enables Spring Boot's auto-configuration
 *     mechanism to configure beans based on classpath settings and defined properties.
 *   - {@code @ComponentScan}: Automatically scans for Spring components
 *     (e.g., @Controller, @Service, @Repository) within the base package and subpackages.
 * 
 * {@code @EnableWebSecurity(debug = true)}
 * - Enables Spring Security’s web-based security mechanisms.
 * - The {@code debug=true} flag activates verbose logging for the security filter chain,
 *   which is useful for development and debugging purposes. This should be set to {@code false}
 *   in production environments to avoid unnecessary logging and potential security risks.
 * 
 * {@code @EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)}
 * - Enables method-level security annotations throughout the application:
 *   - {@code @Secured}: Allows role-based access control at the method level.
 *   - {@code @RolesAllowed}: Enables support for JSR-250 standard security annotations.
 *   - Also supports {@code @PreAuthorize}, {@code @PostAuthorize}, and filtering annotations
 *     like {@code @PreFilter}, {@code @PostFilter} if enabled.
 *
 * -----------------------------------------------------------------------------
 * Application Responsibilities
 * -----------------------------------------------------------------------------
 * - Initializes the Spring Boot runtime.
 * - Loads external configurations (application.properties or YAML).
 * - Automatically registers Spring-managed components.
 * - Starts the embedded servlet container (e.g., Tomcat) for web interactions.
 * - Configures application-level security.
 *
 * -----------------------------------------------------------------------------
 * Best Practices
 * -----------------------------------------------------------------------------
 * - Ensure that this main application class is located in the root package
 *   to allow complete component scanning across the application modules.
 * - Avoid embedding business logic in this class; it should strictly serve
 *   as the bootstrap class.
 * - Replace {@code System.out} statements with a structured logging framework
 *   such as SLF4J or Logback for production-grade logging.
 */
@SpringBootApplication
@EnableWebSecurity(debug = true)
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
public class EmployeeLeaveManagementSystemApplication {

    /**
     * -----------------------------------------------------------------------------
     * Method: main
     * -----------------------------------------------------------------------------
     * The main method serves as the starting point of the Java Virtual Machine (JVM).
     * It delegates the application startup process to Spring Boot's runtime via
     * {@code SpringApplication.run()}.
     *
     * Responsibilities:
     * - Initializes the Spring application context.
     * - Triggers component scanning and bean registration.
     * - Resolves and injects dependencies via Spring's IoC container.
     * - Launches the embedded web server (typically Tomcat) if it's a web application.
     *
     * Parameters:
     * @param args Command-line arguments passed to the application at runtime.
     *
     * Notes:
     * - This method should remain minimal and not contain any functional or business logic.
     * - Application-specific initialization logic should be handled via
     *   lifecycle hooks like {@code CommandLineRunner} or {@code ApplicationRunner}.
     */
    public static void main(String[] args) {
        SpringApplication.run(EmployeeLeaveManagementSystemApplication.class, args);
    }
}
