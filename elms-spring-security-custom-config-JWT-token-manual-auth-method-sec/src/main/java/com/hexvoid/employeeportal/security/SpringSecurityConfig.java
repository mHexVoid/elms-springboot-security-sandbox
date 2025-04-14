package com.hexvoid.employeeportal.security;


import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.hexvoid.employeeportal.filter.AuthoritiesLoggingAfterFilter;
import com.hexvoid.employeeportal.filter.AuthoritiesLoggingAtFilter;
import com.hexvoid.employeeportal.filter.JWTTokenGeneratorFilter;
import com.hexvoid.employeeportal.filter.JWTTokenValidatorFilter;
import com.hexvoid.employeeportal.filter.RequestValidationBeforeFilter;

@Configuration
public class SpringSecurityConfig {


	/**
	 * Configures a {@link JdbcUserDetailsManager} for JDBC-based authentication.
	 * <p>
	 * This setup allows Spring Security to authenticate users by executing custom SQL queries
	 * against the <code>employee_credentials</code> table. It is useful when you want to manage
	 * users and their authorities directly from the database using Spring's default JDBC support.
	 *
	 * <p><b>Responsibilities:</b></p>
	 * <ul>
	 *     <li>Fetch user credentials (email and password) during login.</li>
	 *     <li>Fetch user roles or authorities linked to the email (used for authorization).</li>
	 *     <li>Enable JDBC authentication without writing a custom <code>UserDetailsService</code>.</li>
	 * </ul>
	 *
	 * <p><b>Important Notes:</b></p>
	 * <ul>
	 *     <li>If a custom implementation of <code>UserDetailsService</code> (e.g., <code>EmployeeUserDetailsService</code>) 
	 *         is annotated with <code>@Service</code>, Spring will prioritize it over this configuration.</li>
	 *     <li>The provided SQL queries must match your database schema and return expected columns:
	 *         <ul>
	 *             <li><code>setUsersByUsernameQuery</code>: Must return email, password, and a boolean (enabled).</li>
	 *             <li><code>setAuthoritiesByUsernameQuery</code>: Must return email and authority (role).</li>
	 *         </ul>
	 *     </li>
	 * </ul>
	 *
	 * @param dataSource The configured {@link DataSource} used to execute authentication queries.
	 * @return A configured instance of {@link JdbcUserDetailsManager}.
	 */
	//	 @Bean
	//	 public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
	//	     JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
	//	
	//	     jdbcUserDetailsManager.setUsersByUsernameQuery(
	//	         "SELECT email, pwd, true FROM employee_credentials WHERE email = ?");
	//	
	//	     jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
	//	         "SELECT email, role FROM employee_credentials WHERE email = ?");
	//	
	//	     return jdbcUserDetailsManager;
	//	 }



	/**
	 * Creates a {@link PasswordEncoder} bean using Spring Security's {@code DelegatingPasswordEncoder}.
	 * <p>
	 * This encoder supports multiple encoding formats and automatically chooses the appropriate one
	 * based on a prefix in the stored password (e.g., {bcrypt}).
	 *
	 * <p>It is a secure, flexible approach to password encoding and is the recommended strategy in Spring Security.
	 *
	 * @return a delegating password encoder supporting various encoding schemes
	 */
	@Bean
	PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}



	/**
	 * Defines a custom {@link AuthenticationManager} bean responsible for initiating the
	 * authentication process.
	 *
	 * <p>This implementation wires a custom {@link EmployeeUserNamePwdAuthenticationProvider}
	 * using the injected {@link EmployeeUserDetailsService} (custom implementation of {@link org.springframework.security.core.userdetails.UserDetailsService})
	 * and the {@link PasswordEncoder} bean.
	 *
	 * <p>The {@code ProviderManager} is the default Spring implementation of {@code AuthenticationManager}.
	 * Credentials are preserved after authentication by disabling {@code eraseCredentialsAfterAuthentication}.
	 *
	 * @param employeeUserDetailsService the custom user details service for loading user data
	 * @param passwordEncoder the encoder used to verify passwords securely
	 * @return a fully configured {@code AuthenticationManager} bean
	 */
	@Bean
	AuthenticationManager authenticationManager(EmployeeUserDetailsService employeeUserDetailsService,
			PasswordEncoder passwordEncoder)
	{
		EmployeeUserNamePwdAuthenticationProvider authProvider =
				new EmployeeUserNamePwdAuthenticationProvider(employeeUserDetailsService, passwordEncoder);

		ProviderManager providerManager = new ProviderManager(authProvider);
		//default value is true in spring implementation
		providerManager.setEraseCredentialsAfterAuthentication(false);

		return providerManager;
	}



	/**
	 * Security Configuration:
	 * -----------------------
	 * Defines the security filter chain for handling authentication and authorization in the application.
	 *
	 * Key Responsibilities:
	 * - Configure public and protected endpoints.
	 * - Enable role-based access control.
	 * - Register custom security filters.
	 * - Configure session management and disable CSRF for stateless APIs.
	 */

	@Bean
	SecurityFilterChain chain(HttpSecurity http) throws Exception {


		http.authorizeHttpRequests(customizer ->customizer

				/** ------------------ Public Endpoints (No Authentication Required) ------------------ **/


				// Login and Registration endpoints
				.requestMatchers("/api/login").permitAll()
				.requestMatchers("/user/details").authenticated()
				.requestMatchers("/register/user").permitAll()
				.requestMatchers("/registered/user/findByEmail/**").permitAll()
				// Allows access to error endpoints to prevent Spring Security from intercepting internal error responses.
				.requestMatchers("/error", "/error/**").permitAll()
				/*
				 * Different ways to define request matchers:
				 * 
				 * Way 1:
				 * Matches all HTTP methods (GET, POST, PUT, DELETE) for the given endpoints.
				 */
				.requestMatchers("/api/noauth/employees", "/api/noauth/employees/**").permitAll()

				// Way 2: Define request matchers for specific HTTP methods explicitly.
				.requestMatchers(HttpMethod.POST, "/api/noauth/leaves").permitAll()
				.requestMatchers(HttpMethod.GET, "/api/noauth/leaves").permitAll()
				.requestMatchers(HttpMethod.GET, "/api/noauth/leaves/**").permitAll()
				.requestMatchers(HttpMethod.PUT, "/api/noauth/leaves/**").permitAll()
				.requestMatchers(HttpMethod.DELETE, "/api/noauth/leaves/**").permitAll()

				// Swagger UI and OpenAPI documentation
				.requestMatchers(HttpMethod.GET, "/swagger-ui/**", "/v3/api-docs/**").permitAll()

				// H2 Console (if enabled during development)
				// .requestMatchers(HttpMethod.GET, "/h2-console").permitAll()
				// .requestMatchers("/h2-console", "/h2-console/**").permitAll() //For Post API and get both while providing credentials to UI


				// Authenticated Endpoints

				// Employee APIs
				.requestMatchers(HttpMethod.POST, "/api/employee").hasAuthority("UPDATE") // Requires UPDATE authority

				// These endpoints are authenticated to support method-level security 
				// using @PreAuthorize/@PostAuthorize annotations defined in EmployeeDAO.
				.requestMatchers(HttpMethod.GET, "/api/employee").authenticated()
				.requestMatchers(HttpMethod.GET, "/api/employee/**").authenticated()


				.requestMatchers(HttpMethod.PUT, "/api/employee/**").hasAnyAuthority("UPDATE", "DELETE")
				.requestMatchers(HttpMethod.DELETE, "/api/employee/**").hasAuthority("DELETE")

				// Leave APIs
				.requestMatchers(HttpMethod.POST, "/api/leave").hasAnyRole("ADMIN", "EMPLOYEE")
				.requestMatchers(HttpMethod.GET, "/api/leave").hasRole("ADMIN")
				.requestMatchers(HttpMethod.GET, "/api/leave/**").hasAnyRole("ADMIN", "EMPLOYEE")
				.requestMatchers(HttpMethod.PUT, "/api/leave/**").hasRole("ADMIN")
				.requestMatchers(HttpMethod.DELETE, "/api/leave/**").hasAnyRole("ADMIN", "EMPLOYEE")



				);

		// Uncomment the below block if using a UI client (like React, Angular) that requires CSRF protection.
		// The configuration allows CSRF tokens to be handled via cookies and ignores specific endpoints like Swagger UI.
		/*
		    CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();
		    http.csrf(csrfConfig -> csrfConfig
		            .csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
		            .ignoringRequestMatchers("/error/**", "/swagger-ui/**")
		            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		    );
		 */


		/**
		 * Add custom filters to the security filter chain.
		 * <p>
		 * Filters are added to enforce specific behaviors in the authentication and authorization process:
		 * </p>
		 * <ul>
		 *   <li><b>RequestValidationBeforeFilter:</b> Validates the incoming request before proceeding with authentication.</li>
		 *   <li><b>AuthoritiesLoggingAfterFilter:</b> Logs user authorities after authentication has occurred.</li>
		 *   <li><b>AuthoritiesLoggingAtFilter:</b> Logs user authorities at a different point in the filter chain.</li>
		 *   <li><b>JWTTokenGeneratorFilter:</b> Generates a new JWT token after successful authentication and includes it in the response header.</li>
		 *   <li><b>JWTTokenValidatorFilter:</b> Validates the JWT token sent by the client in the request header.</li>
		 * </ul>
		 */

		// Registering custom filters in the Spring Security filter chain.
		http.addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class);
		http.addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class);
		http.addFilterAt(new AuthoritiesLoggingAtFilter(), BasicAuthenticationFilter.class);
		http.addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class);
		http.addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class);

		// Disable session creation to enforce stateless authentication (JWT-based).
		// If set to ALWAYS, Spring would create a JSESSIONID for every request.
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		// Disable CSRF, form login, and use basic HTTP authentication for API requests.
		http.csrf(csrf -> csrf.disable());
		http.formLogin(form -> form.disable());
		http.httpBasic(Customizer.withDefaults());

		//http.headers(t -> t.frameOptions(v ->v.sameOrigin()));

		// Return the configured security filter chain.
		return http.build();

	}

}
