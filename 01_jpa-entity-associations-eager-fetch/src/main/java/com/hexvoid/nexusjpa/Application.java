package com.hexvoid.nexusjpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point of the Spring Boot application.
 * <p>
 * This class bootstraps the application by initializing the Spring context.
 * Ideal starting point for understanding Spring Boot-based JPA projects.
 *
 * <p><b>@SpringBootApplication:</b> This is a convenience annotation that combines:
 * <ul>
 *     <li>{@code @Configuration} – Marks this class as a source of bean definitions.</li>
 *     <li>{@code @EnableAutoConfiguration} – Enables Spring Boot’s auto-configuration mechanism.</li>
 *     <li>{@code @ComponentScan} – Automatically scans and registers beans from the current package and sub-packages.</li>
 * </ul>
 * This annotation simplifies configuration and is typically placed on the main class.
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
