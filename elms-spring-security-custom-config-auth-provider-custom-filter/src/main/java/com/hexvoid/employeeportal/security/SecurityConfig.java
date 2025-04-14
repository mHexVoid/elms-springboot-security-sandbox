//package com.hexvoid.employeeportal.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
///**
// * =====================================================================================
// * **Spring Security Configuration for Employee Portal**
// * =====================================================================================
// * **Purpose:**
// * - Handles **authentication**: Verifying username and password for login.
// * - Manages **authorization**: Controlling access to endpoints based on roles.
// * - Defines a **security filter chain** to process incoming HTTP requests.
// *
// * **Key Concepts Explained:**
// * - **SecurityFilterChain**: Defines access rules for API endpoints.
// * - **InMemoryUserDetailsManager**: Stores user credentials in memory (for development only).
// * - **Basic Authentication**: Enables authentication using username/password.
// * - **Role-Based Access Control (RBAC)**: Restricts access based on roles (`hasRole()`).
// * - **Common Errors:**
// *   - `401 Unauthorized`: Missing/invalid credentials.
// *   - `403 Forbidden`: Authenticated user lacks required role.
// * - What is `SecurityFilterChain`, and how does it handle authentication/authorization?  
// * - Lambda Expressions: Role in configuring security rules and why they are preferred over method chaining.  
// */
//
//
///**
// * Annotation: @Configuration
// * 
// * **Purpose:**
// * - Marks this class as a **Spring Configuration Class**.
// * - Allows Spring to **automatically detect and register** it as a Bean.
// *
// * **Lifecycle:**
// * - Detected at **Application Startup**.
// * - Instantiated **only once** in the Spring container.
// * - All @Bean methods inside it are processed and registered in the Spring Context.
// */
//@Configuration // Tells Spring that this class defines beans and configuration.
//public class SecurityConfig {
//
//	/**
//	 * =====================================================================================
//	 * **User Management: InMemoryUserDetailsManager**
//	 * =====================================================================================
//	 * **Purpose:**
//	 * - Stores credentials (`username`, `password`, and roles) **in memory**.
//	 * - Implements the `UserDetailsService` interface to fetch user information during authentication.
//	 *
//	 * **Internal Workflow:**
//	 * 1. When a user tries to log in, Spring calls `loadUserByUsername()` to fetch the user details.
//	 * 2. If the user exists, their password and roles are validated.
//	 * 3. If valid, authentication succeeds; otherwise, access is denied.
//	 *
//	 * **Why Use This?**
//	 * - Ideal for development/testing environments.
//	 * - Not recommended for production (credentials reset on restart).
//	 * - For production, use **JdbcUserDetailsManager** (Database-based storage).
//	 *
//	 * **Production Note:**  
//	 * - Replace this with a database-backed implementation for dynamic and secure credential management.
//	 */
//
//	//**************************************************************************
//
//	/**
//	 * **Annotation: @Bean** 
//	 * 
//	 * **Purpose:**
//	 * - Registers `InMemoryUserDetailsManager` as a Spring Bean.
//	 * - This Bean manages user authentication in-memory (for development/testing only).
//	 *
//	 * **Lifecycle:**
//	 * - Created at **Application Startup** when Spring scans @Configuration classes.
//	 * - Returns a **singleton** instance unless explicitly set otherwise.
//	 */
//	@Bean
//	public InMemoryUserDetailsManager userDetailsManager() {
//
//		// **UserDetails: Represents an authenticated user in Spring Security** 
//		/**
//		 * **Internal Working of UserDetails**
//		 * - `username`: Used for authentication.
//		 * - `password`: Stored securely (in real-world applications, use password hashing).
//		 * - `roles`: Defines user permissions.
//		 * - The `User.builder()` method creates an immutable user object.
//		 */
//
//		// **Admin User (Has Role: ADMIN)**
//		UserDetails adminUser = User.builder()
//				.username("adminUser") // Username for login
//				.password("{noop}admin123") // {noop} disables password encoding (for testing only)
//				.roles("ADMIN") // Assigns ADMIN role for access control
//				.build();
//
//		// **Employee User (Has Role: EMPLOYEE)**
//		UserDetails employeeUser = User.builder()
//				.username("employeeUser") // Username for login
//				.password("{noop}employee123") // Plain password (use a PasswordEncoder like BCrypt for production)
//				.roles("EMPLOYEE") // Assigns the "EMPLOYEE" role to this user
//				.build();
//
//		/*
//		 * How Users Are Stored:
//		 * - These user objects are stored in a concurrent map inside InMemoryUserDetailsManager.
//		 * - On login, Spring fetches the user via the `loadUserByUsername()` method.
//		 */
//
//		// **Returning the InMemoryUserDetailsManager Instance**
//		/**
//		 * **Purpose:**
//		 * - Stores `UserDetails` in memory.
//		 * - Fetches user details when authentication is required.
//		 *
//		 * **Internal Methods Used:**
//		 * - `loadUserByUsername(String username)`: Fetches a user by username during authentication.
//		 * - `createUser(UserDetails user)`: Adds a new user (not used here, but available in `UserDetailsManager`).
//		 */
//		return new InMemoryUserDetailsManager(adminUser, employeeUser);
//	}
//
//	/**
//	 * =====================================================================================
//	 * **Security Filter Chain: Authorization & Request Handling**
//	 * =====================================================================================
//	 * **Purpose:**
//	 * - Defines security rules for each API endpoint based on roles and authentication requirements.
//	 * - Allows public access for some endpoints while restricting others to specific roles.
//	 * - Configures authentication mechanisms such as Basic Authentication.
//	 *
//	 * **Internal Workflow:**
//	 * 1. The filter chain intercepts every HTTP request sent to the server.
//	 * 2. Requests are matched against configured rules (e.g., `permitAll()`, `hasRole()`).
//	 * 3. Requests that fail the rules are rejected with appropriate error codes (401/403).
//	 * 4. Successful requests are forwarded to the respective controller.
//	 *
//	 * ************************************
//	 * SAME COMMENTS BELOW SIMPLIFIED
//	 * ************************************
//	 *
//	 * **Purpose:**
//	 * - Configures **which endpoints require authentication**.
//	 * - Implements **role-based access control (RBAC)**.
//	 * - Enables **Basic Authentication**.
//	 * - Disables CSRF for stateless API calls.
//	 *
//	 * **Spring Security Request Flow:**
//	 * 1️ **Every request passes through the SecurityFilterChain.**
//	 * 2️ Spring checks authentication status:
//	 *     - If authentication is required → User must provide credentials.
//	 * 3️ If authenticated, authorization rules are applied (`hasRole()`, `permitAll()`).
//	 * 4️ If access is granted → Request proceeds to the controller.
//	 * 5️ If access is denied → Returns **401 Unauthorized** or **403 Forbidden**.
//	 */
//
//
//	/**
//	 * **Annotation: @Bean**
//	 * 
//	 * **Purpose:**
//	 * - Registers `SecurityFilterChain` to define authentication rules.
//	 * - Controls which users can access specific API endpoints.
//	 *
//	 * **Lifecycle:**
//	 * - Created once at **Application Startup**.
//	 * - All security filters are registered in the `FilterChainProxy`.
//	 * - Runs **before** any controller method execution.
//	 */
//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//		/*
//		 * ==========================================================================================================
//		 *                                       SecurityFilterChain Overview
//		 * ==========================================================================================================
//		 * 
//		 * - `SecurityFilterChain` is a collection of filters applied to incoming HTTP requests.
//		 * - These filters enforce authentication and authorization based on configured security rules.
//		 * - The chain includes various security filters such as:
//		 *   - Authentication checks
//		 *   - CSRF (Cross-Site Request Forgery) protection
//		 *   - Session management
//		 *   - Request validation and access control
//		 * 
//		 * - Spring Security applies these authentication and authorization rules to all incoming requests.
//		 * - Configurations are typically defined using:
//		 *   - `authorizeHttpRequests()` → Defines access rules for different endpoints.
//		 *   - `httpBasic()` → Enables HTTP Basic authentication.
//		 *   - `csrf()` → Configures CSRF protection settings.
//		 * 
//		 * ==========================================================================================================
//		 *                            How `authorizeHttpRequests()` Works Internally
//		 * ==========================================================================================================
//		 * 
//		 * - The method `authorizeHttpRequests()` is used to configure endpoint-specific security rules.
//		 * - Instead of traditional method chaining, lambda expressions are used for better readability and flexibility.
//		 * 
//		 * **Key Functional Interface:**
//		 * - `authorizeHttpRequests()` accepts an argument of type `Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>>`.
//		 * - `Customizer<T>` is a functional interface with a single method:
//		 * 
//		 *   ```java
//		 *   @FunctionalInterface
//		 *   public interface Customizer<T> {
//		 *       void customize(T t);
//		 *   }
//		 *   ```
//		 * 
//		 * - Since `Customizer<T>` has a single abstract method, it allows the use of lambda expressions.
//		 * - The lambda expression `(configurer -> configurer.requestMatchers(...).permitAll())` is a shorthand for:
//		 * 
//		 *   ```java
//		 *   http.authorizeHttpRequests(new Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>>() {
//		 *       @Override
//		 *       public void customize(AuthorizeHttpRequestsConfigurer<HttpSecurity> configurer) {
//		 *           configurer.requestMatchers(HttpMethod.GET, "/api/noauth/employees").permitAll();
//		 *       }
//		 *   });
//		 *   ```
//		 * 
//		 * ==========================================================================================================
//		 *                      Step-by-Step Execution of `authorizeHttpRequests()`
//		 * ==========================================================================================================
//		 * 
//		 * 1. Spring Security invokes the `authorizeHttpRequests()` method.
//		 * 2. The lambda expression is passed as an instance of `Customizer<T>`, triggering the `customize()` method.
//		 * 3. The `requestMatchers()` method defines endpoint-specific access control rules.
//		 * 4. The `permitAll()` method allows unrestricted access to the specified endpoints.
//		 * 5. These configurations are then registered within the `SecurityFilterChain`.
//		 * 6. At runtime, incoming HTTP requests pass through the filter chain, where:
//		 *    - Spring Security evaluates the configured rules.
//		 *    - Authentication and authorization checks are applied.
//		 *    - Requests are either granted access or denied based on the defined policies.
//		 * 
//		 * **Key Takeaways:**
//		 * - The lambda expression simplifies configuration but internally works via the `Customizer<T>` interface.
//		 * - Security rules are stored and enforced dynamically as part of the `SecurityFilterChain`.
//		 * - This mechanism ensures fine-grained access control while keeping the configuration concise and readable.
//		 */
//
//		http.authorizeHttpRequests(configurer -> configurer
//				
//				/*
//				 * NOTE: The following configuration ensures that error handling endpoints (`/error` and `/error/**`) 
//				 * are accessible without authentication. 
//				 * Without this, Spring Security may enforce authentication even if other paths are permitted. 
//				 *
//				 * Where does `/error` come from?
//				 * - Spring Boot provides a built-in global error handling mechanism through `BasicErrorController` 
//				 *   (from `spring-boot-starter-web`).
//				 * - When an unhandled exception occurs (e.g., database failure, 404 not found, or invalid request),
//				 *   Spring Boot internally forwards the request to `/error` instead of returning a raw stack trace.
//				 * - This forwarding happens inside `DefaultErrorWebExceptionHandler` (for WebFlux) or 
//				 *   `BasicErrorController` (for MVC).
//				 *
//				 * How does it work behind the scenes?
//				 * 1. A request is received and processed through Spring Security filters.
//				 * 2. If an exception occurs inside a controller/service, Spring Boot catches it.
//				 * 3. Instead of sending an HTML error page or JSON response immediately, Spring internally forwards 
//				 *    the request to `/error`.
//				 * 4. The `/error` endpoint is handled by `BasicErrorController`, which generates a default error response.
//				 * 5. Since this is a new internal request, Spring Security applies security rules again.
//				 * 6. If `/error` is not explicitly permitted, Spring Security may block it, causing authentication prompts 
//				 *    or an unexpected 403 error.
//				 *
//				 * To prevent this, we explicitly allow `/error` so that error responses are correctly returned.
//				 */
//				 
//				 .requestMatchers("/error", "/error/**").permitAll()
//
//
//				// =====================================================================================
//				// **PUBLIC ENDPOINTS (Accessible Without Authentication)**
//				// =====================================================================================
//				.requestMatchers(HttpMethod.GET, "/swagger-ui/**", "/v3/api-docs/**").permitAll()  
//				/*
//				 * permitAll():
//				 * - Allows unrestricted access to these endpoints.
//				 * - These are typically used for publicly accessible resources, like Swagger API docs.
//				 */
//
//				// =====================================================================================
//				// **H2 DATABASE
//				// =====================================================================================
//
//				/*
//				 * Enable debugging for Spring Security to troubleshoot authentication/authorization issues.
//				 * This helps identify why requests are blocked or permitted.
//				 * Initially, I was getting an authentication popup even after allowing access to H2 Console.
//				 * To debug this, I enabled **Spring Security debug mode** by adding:
//				 * ---logging.level.org.springframework.security=DEBUG  in application.properties
//				 * Running the application in debug mode **helped me identify that POST requests were being blocked**,
//				 *  ******** which led me to explicitly permit them below. ********
//				 */
//
//				// Allowing GET requests to H2 Console
//				/*
//				 *  Initially, I didn't know why I couldn't access H2 Console.
//				 *  Debugging logs showed that **Spring Security was blocking GET requests**.
//				 *  GET is used to access URLs (e.g., opening the H2 console UI in a browser).
//				 *   Without this, accessing `http://localhost:8080/h2-console/` would be blocked.
//				 *   After allowing GET requests, the login page loaded but authentication still failed.
//				 */
//				.requestMatchers(HttpMethod.GET, "/h2-console/**").permitAll() 
//
//				// Allowing POST requests to H2 Console
//				/*
//				 * After debugging, I realized that even after allowing GET requests, **login was still failing**.
//				 * The logs showed that when I entered credentials, the **form was making a POST request** to `/h2-console/login.do`.
//				 *  Since POST requests were blocked by default, the login couldn't proceed.
//				 *  Solution: I explicitly allowed **POST requests** for `/h2-console/**`, which finally resolved the issue.
//				 */
//				.requestMatchers(HttpMethod.POST, "/h2-console/**").permitAll()
//
//				// =====================================================================================
//				// **EmployeeController Class Endpoints** (No Authentication Needed)
//				// =====================================================================================
//				.requestMatchers(HttpMethod.POST, "/api/noauth/employees").permitAll()
//				.requestMatchers(HttpMethod.GET, "/api/noauth/employees").permitAll()
//				.requestMatchers(HttpMethod.GET, "/api/noauth/employees/**").permitAll()
//				.requestMatchers(HttpMethod.PUT, "/api/noauth/employees/**").permitAll()
//				.requestMatchers(HttpMethod.DELETE, "/api/noauth/employees/**").permitAll()
//				
//
//				/*
//				 * Question: Is `/api/noauth/employees` an endpoint?
//				 * - Yes, `/api/employees` is a valid REST endpoint defined in the `EmployeeController` class.
//				 * - It is explicitly permitted using `.permitAll()`, meaning **no authentication is required**.
//				 * - **Username and password do NOT impact access** because this endpoint is public.
//				 */
//
//				// =====================================================================================
//				// **LeaveRequestController Class Endpoints** (No Authentication Needed)
//				// =====================================================================================
//				.requestMatchers(HttpMethod.POST, "/api/noauth/leaves").permitAll()
//				.requestMatchers(HttpMethod.GET, "/api/noauth/leaves").permitAll()
//				.requestMatchers(HttpMethod.GET, "/api/noauth/leaves/**").permitAll()
//				.requestMatchers(HttpMethod.PUT, "/api/noauth/leaves/**").permitAll()
//				.requestMatchers(HttpMethod.DELETE, "/api/noauth/leaves/**").permitAll()
//				/*
//				 * Question: Is `/api/noauth/leaves` an endpoint?
//				 * - Yes, `/api/leaves` is a valid REST endpoint defined in `LeaveRequestController`.
//				 * - It is also public (`.permitAll()`), so **authentication is not needed**.
//				 * - **Username and password do NOT impact access** for this endpoint.
//				 */
//
//				// =====================================================================================
//				// **EmployeeLeaveRequestController** (PROTECTED ENDPOINTS - Authentication & Roles Required)
//				// =====================================================================================
//				.requestMatchers(HttpMethod.POST, "/api/employee").hasRole("ADMIN")
//				.requestMatchers(HttpMethod.GET, "/api/employee").hasRole("ADMIN")
//				.requestMatchers(HttpMethod.GET, "/api/employee/**").hasRole("ADMIN")
//				.requestMatchers(HttpMethod.PUT, "/api/employee/**").hasRole("ADMIN")
//				.requestMatchers(HttpMethod.DELETE, "/api/employee/**").hasRole("ADMIN")
//
//				.requestMatchers(HttpMethod.POST, "/api/leave").hasRole("ADMIN")
//				.requestMatchers(HttpMethod.GET, "/api/leave").hasRole("ADMIN")
//				.requestMatchers(HttpMethod.GET, "/api/leave/**").hasRole("ADMIN")
//				.requestMatchers(HttpMethod.PUT, "/api/leave/**").hasRole("ADMIN")
//				.requestMatchers(HttpMethod.DELETE, "/api/leave/**").hasRole("ADMIN")
//
//				/*
//				 * How does authorization work for `EmployeeLeaveRequestController`?
//				 * - These endpoints **require authentication** because they use `.hasRole("ADMIN")`.
//				 * - **If no authentication is provided → 401 Unauthorized.**
//				 * - **If the user is authenticated but lacks the ADMIN role → 403 Forbidden.**
//				 */
//				);
//
//		// =====================================================================================
//		// **SPRING SECURITY CONFIGURATION: BASIC AUTH, CSRF, AND FRAME OPTIONS**
//		// =====================================================================================
//
//		/*
//		 * Enabling Basic Authentication
//		 * -----------------------------------------------------------------------------
//		 * **What is Basic Authentication?**
//		 * - A simple authentication mechanism where users send `username:password` as a Base64-encoded string.
//		 * - Suitable for **internal** or **testing** environments.
//		 * - Not recommended for public APIs without HTTPS due to security risks.
//		 */
//		http.httpBasic(Customizer.withDefaults());
//
//		/*
//		 * Disabling CSRF Protection
//		 * -----------------------------------------------------------------------------
//		 * **Why disable CSRF?**
//		 * - CSRF (Cross-Site Request Forgery) protection is **important for stateful (user-session-based) applications**.
//		 * - However, the H2 Console is a **stateless in-memory database UI**, mainly used for development.
//		 * - The H2 console **does not send CSRF tokens**, so Spring Security blocks requests by default.
//		 * - Disabling CSRF allows H2 Console POST requests (login) to work without CSRF errors.
//		 */
//		http.csrf(csrf -> csrf.disable());
//
//		/*
//		 * Alternative Approaches (Other Working Solutions)
//		 * -----------------------------------------------------------------------------
//		 * These lines also worked, but why are they needed? Let's break it down:
//		 *
//		 * 1️ `http.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**").disable());`
//		 *    - Instead of **disabling CSRF globally**, this disables it **only for H2 Console**.
//		 *    - More **secure** than `csrf.disable()` because other API endpoints still have CSRF protection.
//		 *
//		 * 2️ `http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));`
//		 *    - **Why is this needed?**
//		 *    - By default, browsers **block loading pages in frames** to prevent clickjacking attacks.
//		 *    - The H2 Console loads inside a `<frame>` in the browser, so this setting allows it to load.
//		 *
//		 * 3️ `http.formLogin(Customizer.withDefaults());`
//		 *    - **Why is this needed?**
//		 *    - This enables Spring Security’s **default login form**, which is not needed for H2 Console.
//		 *    - If enabled, users will be redirected to a Spring Security login page before reaching H2 Console.
//		 */
//
//		/*
//		 *  Allowing H2 Console to Load Inside Frames (Clickjacking Prevention Override)
//		 * -----------------------------------------------------------------------------
//		 * **Why do we need this?**
//		 * - By default, `frameOptions().deny()` prevents pages from being embedded in iframes (for security).
//		 * - Since H2 Console runs **inside an iframe**, it must be explicitly allowed.
//		 * - `sameOrigin()` means: 
//		 *   - Only **pages from the same domain** (`localhost:8080`) can embed this page in an iframe.
//		 *   - This is **more secure than disabling frame options completely**.
//		 */
//		http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));
//
//
//
//		return http.build(); // Returns the fully configured SecurityFilterChain.
//	}
//
//	/**
//	 * =====================================================================================
//	 * **Understanding Error Responses: 401 vs. 403**
//	 * =====================================================================================
//	 * 401 Unauthorized:
//	 * - Occurs when the client fails to authenticate (invalid/missing credentials).
//	 * - Common causes:
//	 *   1. Missing or invalid "Authorization" header.
//	 *   2. Incorrect username or password.
//	 * - **Fix**: Ensure the client provides valid credentials.
//	 *
//	 * 403 Forbidden:
//	 * - Occurs when the user is authenticated but lacks the required permissions (authorization failure).
//	 * - Common causes:
//	 *   1. User lacks the role required by `hasRole()` rules.
//	 * - **Fix**: Assign appropriate roles or adjust the security configuration for the endpoint.
//	 */
//}