package com.hexvoid.employeeportal.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 *  Spring Security Configuration
 * 
 *  Configures authentication and authorization rules
 *  Defines a security filter chain to restrict access to API endpoints
 *  Uses either JDBC-based authentication OR a custom UserDetailsService
 */
@Configuration
public class SpringSecurityConfig {

	/**
	 * ❗ Note: If `EmployeeUserDetailsService` is present, this `JdbcUserDetailsManager` will be ignored.
	 * 
	 *  This Bean provides JDBC-based authentication using the `employee_credentials` table.
	 *  It executes SQL queries to fetch user credentials and roles.
	 *  If `EmployeeUserDetailsService` is enabled, Spring will ignore this configuration.
	 */
	//	@Bean
	//	JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
	//		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
	//
	//		//  Query to fetch user credentials: Must return (username, password, enabled)
	//		jdbcUserDetailsManager.setUsersByUsernameQuery(
	//				"SELECT email, pwd, true FROM employee_credentials WHERE email = ?");
	//
	//		//  Query to fetch user roles: Must return (username, authority)
	//		jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
	//				"SELECT email, role FROM employee_credentials WHERE email = ?");
	//
	//		return jdbcUserDetailsManager;
	//	}

	/**
	 *  Password Encoder Bean
	 *  Uses a delegating password encoder, allowing Spring Security to handle password encoding.
	 *  Supports multiple encoding types (bcrypt, PBKDF2, etc.).
	 */
	@Bean
	PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	/**
	 *  Security Filter Chain: Defines authentication and authorization rules.
	 * 
	 * @param http - HttpSecurity object for defining security rules.
	 * @return SecurityFilterChain - Configured security filter chain.
	 * @throws Exception - If any security configuration fails.
	 * 
	 *  Configures access rules for different API endpoints based on roles and permissions.
	 *  Disables CSRF for stateless APIs.
	 *  Enables HTTP Basic authentication.
	 */
	@Bean
	SecurityFilterChain chain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(customizer ->

		customizer
		//  Public endpoints (No authentication required)
		.requestMatchers("/api/register/user").permitAll()
		.requestMatchers("/error", "/error/**").permitAll()

		/*
		 * Ways of defining request matchers
		 * Method 1: Multiple endpoints in a single request matcher
		 * By default, this matches all HTTP methods (GET, POST, PUT, DELETE)
		 */
		.requestMatchers("/api/noauth/employees", "/api/noauth/employees/**").permitAll()

		/*
		 * Method 2: Defining separate matchers for specific HTTP methods
		 */
		.requestMatchers(HttpMethod.POST, "/api/noauth/leaves").permitAll()
		.requestMatchers(HttpMethod.GET, "/api/noauth/leaves").permitAll()
		.requestMatchers(HttpMethod.GET, "/api/noauth/leaves/**").permitAll()
		.requestMatchers(HttpMethod.PUT, "/api/noauth/leaves/**").permitAll()
		.requestMatchers(HttpMethod.DELETE, "/api/noauth/leaves/**").permitAll()

		//  Allow access to Swagger UI, H2 if Applicable and API documentation
		.requestMatchers(HttpMethod.GET, "/swagger-ui/**", "/v3/api-docs/**").permitAll()
		//.requestMatchers(HttpMethod.GET,"/h2-console").permitAll()
		// .requestMatchers("/h2-console","/h2-console/**").permitAll()

		/*
		 *  Authenticated Endpoints: Require login and role-based access
		 * 
		 *  `hasRole("ROLE_NAME")` → Requires "ROLE_" prefix in the database
		 *  `hasAuthority("ROLE_NAME")` → Direct authority check (No "ROLE_" prefix required)
		 */

		//  Employee Endpoints (Access based on roles)
		.requestMatchers(HttpMethod.POST, "/api/employee").hasRole("EMPLOYEE")  // Only EMPLOYEE can create employee
		.requestMatchers(HttpMethod.PUT, "/api/employee/**").hasAnyRole("ADMIN", "EMPLOYEE")  // EMPLOYEE & ADMIN can update employee
		.requestMatchers(HttpMethod.DELETE, "/api/employee/**").hasRole("ADMIN")  // Only ADMIN can delete employee

		//  Role-based authority checks
		.requestMatchers(HttpMethod.GET, "/api/employee").hasAuthority("ADMIN")  // ADMIN only
		.requestMatchers(HttpMethod.GET, "/api/employee/**").hasAnyAuthority("ADMIN", "EMPLOYEE")  // Both ADMIN & EMPLOYEE

		//  Leave Management Endpoints (Access control)
		.requestMatchers(HttpMethod.POST, "/api/leave").hasAnyRole("ADMIN", "EMPLOYEE")  // Both ADMIN & EMPLOYEE can apply leave
		.requestMatchers(HttpMethod.GET, "/api/leave").hasRole("ADMIN")  // Only ADMIN can view all leave requests
		.requestMatchers(HttpMethod.GET, "/api/leave/**").hasAnyRole("ADMIN", "EMPLOYEE")  // Both can view leave details
		.requestMatchers(HttpMethod.PUT, "/api/leave/**").hasRole("ADMIN")  // Only ADMIN can update leave
		.requestMatchers(HttpMethod.DELETE, "/api/leave/**").hasAnyRole("ADMIN", "EMPLOYEE")  // Both can cancel leave
				);

		//  Disable CSRF (Not needed for stateless APIs)
		http.csrf(csrf -> csrf.disable());

		//  Disable default login form (Since we are using token-based authentication)
		http.formLogin(t -> t.disable());

		//  Enable HTTP Basic Authentication (username/password login via API requests)
		http.httpBasic(Customizer.withDefaults());

		//  If using H2 database, enable frame options for console access (Uncomment if needed)
		// http.headers(t -> t.frameOptions(v -> v.sameOrigin()));

		return http.build();
	}
}
